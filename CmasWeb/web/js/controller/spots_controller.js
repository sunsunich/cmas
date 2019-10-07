var spots_controller = {

    validationController: null,

    init: function () {
        var self = this;
        var wrapperElem = $('#Wrapper');
        wrapperElem.height("100%");
        $('#Wrapper-content').css('height', wrapperElem.height() - $('#header').height() - 5/*box shadow*/ - 3 /*map borders*/);
        country_controller.inputId = "createSpot_countryCode";
        country_controller.init();

        spots_model.map = new google.maps.Map(document.getElementById('map'), {
            center: {lat: 41.9, lng: 12.4833333}, // Rome
            zoom: 8
        });

        spots_model.map.addListener('bounds_changed', function () {
            self.refreshSpots();
        });

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                var pos = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude
                };
                spots_model.map.setCenter(pos);
                google.maps.event.addListenerOnce(spots_model.map, 'tilesloaded', function () {
                    self.showSpotOnMap(pos.lat, pos.lng, true);
                });
            }, function () {
                self.handleLocationError(true, spots_model.map.getCenter());
            });
        } else {
            // Browser doesn't support Geolocation
            self.handleLocationError(false, spots_model.map.getCenter());
        }

        google.maps.event.addListener(spots_model.map, 'click', function (event) {
            self.showSpotOnMap(event.latLng.lat(), event.latLng.lng(), false);
        });

        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'createSpot';
        this.validationController.fields = [
            {
                id: 'latinName',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyDivingSpotLatinName';
                    }
                    if (!isStringLatin(value)) {
                        return "validation.mismatchDivingSpotLatinName";
                    }
                }
            },
            {
                id: 'toponymName',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.toponym';
                    }
                }
            },
            {
                id: 'countryCode',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyCountry';
                    }
                }
            }
        ];
        this.validationController.submitButton = $('#createSpotOk');
        this.validationController.init();
        $('#createSpotOk').click(function () {
            self.submitSpot();
        });
        $('#createSpotClose').click(function () {
            $('#createSpot').hide();
        });

        $('#deleteSpotOk').click(function () {
            self.deleteSpot();
        });

        $('#deleteSpotClose').click(function () {
            $('#deleteSpot').hide();
        });
    },

    refreshSpots: function () {
        var self = this;
        var mapBounds = spots_model.map.getBounds();
        var bounds = {};
        bounds.swLatitude = mapBounds.getSouthWest().lat();
        bounds.swLongitude = mapBounds.getSouthWest().lng();
        bounds.neLatitude = mapBounds.getNorthEast().lat();
        bounds.neLongitude = mapBounds.getNorthEast().lng();
        var isSpotRefresh = false;
        if (spots_model.currentBounds) {
            if (Math.abs(spots_model.currentBounds.swLatitude - bounds.swLatitude) > spots_model.delta
                || Math.abs(spots_model.currentBounds.swLongitude - bounds.swLongitude) > spots_model.delta
                || Math.abs(spots_model.currentBounds.neLatitude - bounds.neLatitude) > spots_model.delta
                || Math.abs(spots_model.currentBounds.neLongitude - bounds.neLongitude) > spots_model.delta
            ) {
                isSpotRefresh = true;
            }
        } else {
            isSpotRefresh = true;
        }
        if (isSpotRefresh) {
            spots_model.currentBounds = bounds;
            spots_model.getSpots(
                bounds,
                function (json) {
                    self.showFoundSpots(json);
                },
                function () {
                    $('#spots').hide();
                }
            );
        }
    },

    showFoundSpots: function (spots) {
        for (var key in spots_model.spotMap) {
            if (spots_model.spotMap.hasOwnProperty(key)) {
                var spotElem = spots_model.spotMap[key].spot;
                if (spotElem.latitude < spots_model.currentBounds.swLatitude
                    || spotElem.latitude > spots_model.currentBounds.neLatitude
                    || spotElem.longitude < spots_model.currentBounds.swLongitude
                    || spotElem.longitude > spots_model.currentBounds.neLongitude
                ) {
                    this.deleteSpotFromMap(key);
                }
            }
        }

        for (var i = 0; i < spots.length; i++) {
            var spot = spots[i];
            spot.toponymName = spot.toponym ? spot.toponym.name : "";
            spot.countryCode = spot.country ? spot.country.code : "";
            spot.countryName = spot.country ? spot.country.name : "";

            if (this.findSpotOnMap(spot) == null) {
                this.createSpotMarker(spot);
            }
        }
        this.updateFoundSpotsMenu();
    },

    showSpotOnMap: function (latitude, longitude, isAutoGeoLocation) {
        var self = this;
        var spot = {
            id: 'new',
            editable: true,
            latitude: latitude,
            longitude: longitude,
            isAutoGeoLocation: isAutoGeoLocation
        };
        var foundSpot = this.findSpotOnMap(spot);
        if (foundSpot == null) {
            if (!isPaid) {
                return;
            }
            if (spot.id in spots_model.spotMap) {
                //handling new spot creation in other place
                self.deleteSpotFromMap(spot.id);
            }
            const newSpot = spot;
            spots_model.getSpotByCoords(
                latitude, longitude,
                function (json) {
                    json.toponymName = json.toponym ? json.toponym.name : "";
                    json.countryCode = json.country ? json.country.code : "";
                    json.countryName = json.country ? json.country.name : "";
                    if (json.id in spots_model.spotMap) {
                        //open existing marker for already approved spot
                        self.openSpotMarkerInfoWindow(json.id);
                    } else {
                        self.showEditSpotDialog(json);
                    }
                },
                function () {
                    //todo search in geoname.org
                    //no spot found on server using new spot data
                    self.showEditSpotDialog(newSpot);
                }
            );
        } else {
            //open existing marker
            this.openSpotMarkerInfoWindow(foundSpot.id);
        }
    },

    createSpotMarker: function (spot) {
        var self = this;
        var latLng = {lat: spot.latitude, lng: spot.longitude};
        var marker = new google.maps.Marker({
            position: latLng,
            map: spots_model.map
        });
        var content = '<span>' + spot.latinName + '</span> <br />';
        if (spot.name) {
            content += '<span>' + spot.name + '</span> <br />'
        }
        content += '<span>' + spot.toponymName + '</span> <br />' +
            '<span>' + spot.countryName + '</span> <br />' +
            '<button id="' + spot.id + '_chooseSpotInfoWindow">' + labels['cmas.face.spots.choose'] + '</button> <br />'
        ;
        if (spot.editable) {
            content += '<button id="' + spot.id + '_editSpotInfoWindow">' + labels['cmas.face.spots.edit'] + '</button>' +
                '<button id="' + spot.id + '_deleteSpotInfoWindow">' + labels['cmas.face.spots.delete'] + '</button>';

        }
        var infoWindow = new google.maps.InfoWindow({
            content: content
        });
        spots_model.spotMap[spot.id] = {'spot': spot, 'marker': marker, 'infoWindow': infoWindow};
        marker.addListener('click', function () {
            self.openSpotMarkerInfoWindow(spot.id);
        });
    },

    openSpotMarkerInfoWindow: function (spotId) {
        var self = this;
        if (spots_model.map.lastOpenInfoWindow) {
            spots_model.map.lastOpenInfoWindow.close();
        }
        var spotMapElem = spots_model.spotMap[spotId];
        var marker = spotMapElem.marker;
        var infoWindow = spotMapElem.infoWindow;

        infoWindow.open(spots_model.map, marker);
        spots_model.map.lastOpenInfoWindow = infoWindow;
        var mapElem = $("#map");
        if (spotMapElem.spot.editable) {
            mapElem
                .off("click", '#' + spotId + '_editSpotInfoWindow')
                .on("click", '#' + spotId + '_editSpotInfoWindow', function (e) {
                        e.stopPropagation();
                        var spotId = $(this)[0].id.split('_')[0];
                        self.showEditSpotDialog(spots_model.spotMap[spotId].spot);
                    }
                );
            mapElem
                .off("click", '#' + spotId + '_deleteSpotInfoWindow')
                .on("click", '#' + spotId + '_deleteSpotInfoWindow', function (e) {
                        e.stopPropagation();
                        var spotId = $(this)[0].id.split('_')[0];
                        self.showDeleteSpotDialog(spotId);
                    }
                );
        }
        mapElem
            .off("click", '#' + spotId + '_chooseSpotInfoWindow')
            .on("click", '#' + spotId + '_chooseSpotInfoWindow', function (e) {
                    e.stopPropagation();
                    self.createLogbookEntry($(this)[0].id);
                }
            );
    },

    updateFoundSpotsMenu: function () {
        var self = this;
        if (Object.keys(spots_model.spotMap).length > 0) {
            $('#noSpotsText').hide();
            $('#foundSpots').html(
                new EJS({url: '/js/templates/spots.ejs?v=' + webVersion}).render({
                    "spotMap": spots_model.spotMap,
                    "webVersion": webVersion,
                    "isPaid": isPaid
                })
            ).show();
            for (var key in spots_model.spotMap) {
                if (spots_model.spotMap.hasOwnProperty(key)) {
                    var spot = spots_model.spotMap[key].spot;
                    var chooseSpotElem = $("#" + spot.id + "_chooseSpot");
                    chooseSpotElem.click(function () {
                        self.createLogbookEntry($(this)[0].id);
                    });
                    $("#" + spot.id + "_chooseSpotArrow").click(function (e) {
                        e.stopPropagation();
                        self.createLogbookEntry($(this)[0].id);
                    });
                    chooseSpotElem.hover(
                        function () {
                            var spotId = $(this)[0].id.split('_')[0];
                            $("#" + spotId + "_spotButtons").show();
                        }, function () {
                            var spotId = $(this)[0].id.split('_')[0];
                            $("#" + spotId + "_spotButtons").hide();
                        }
                    );
                    if (spot.editable) {
                        $('#' + spot.id + '_editSpot').click(function (e) {
                                e.stopPropagation();
                                var spotId = $(this)[0].id.split('_')[0];
                                self.showEditSpotDialog(spots_model.spotMap[spotId].spot);
                            }
                        );
                        $('#' + spot.id + '_deleteSpot').click(function (e) {
                                e.stopPropagation();
                                var spotId = $(this)[0].id.split('_')[0];
                                self.showDeleteSpotDialog(spotId);
                            }
                        );
                    } else {
                        $('#' + spot.id + '_reportSpot').click(function (e) {
                                e.stopPropagation();
                                var spotId = $(this)[0].id.split('_')[0];
                                window.open('/secure/reportSpot.html?spotId=' + spotId, '_blank');
                            }
                        );
                    }
                }
            }
        } else {
            $('#foundSpots').empty().hide();
            $('#noSpotsText').show();
        }
    },

    findSpotOnMap: function (spot) {
        for (var key in spots_model.spotMap) {
            if (spots_model.spotMap.hasOwnProperty(key)) {
                var spotElem = spots_model.spotMap[key].spot;
                var isSpotOnMap = spotElem.latitude == spot.latitude && spotElem.longitude == spot.longitude;
                if (isSpotOnMap) {
                    return spotElem;
                }
            }
        }
        return null;
    },

    showEditSpotDialog: function (spot) {
        spots_model.currentEditSpot = spot;
        this.cleanEditSpotForm();
        $('#createSpot').show();
    },

    cleanEditSpotForm: function () {
        var spot = spots_model.currentEditSpot;
        $('#createSpot_latinName').val(spot && spot.latinName ? spot.latinName : "");
        $('#createSpot_name').val(spot && spot.name ? spot.name : "");
        $('#createSpot_toponymName').val(spot && spot.toponymName ? spot.toponymName : "");
        $('#createSpot_countryCode').val(spot && spot.countryCode ? spot.countryCode : "").trigger("change");
        this.validationController.cleanErrors();
        this.validationController.checkForm();
    },

    submitSpot: function () {
        var self = this;
        spots_model.currentEditSpot.name = $('#createSpot_name').val();
        spots_model.currentEditSpot.latinName = this.validationController.form.latinName;
        spots_model.currentEditSpot.toponymName = this.validationController.form.toponymName;
        spots_model.currentEditSpot.countryCode = this.validationController.form.countryCode;
        spots_model.currentEditSpot.countryName = $('#createSpot_countryCode :selected').text();
        if (this.validationController.validateForm()) {
            $('#createSpot').hide();
            if (spots_model.currentEditSpot.editable) {
                spots_model.createSpot(
                    spots_model.currentEditSpot,
                    function (json) {
                        spots_model.currentEditSpot.id = json.id;
                        self.createSpotMarker(spots_model.currentEditSpot);
                        self.updateFoundSpotsMenu();
                    },
                    function () {
                        error_dialog_controller.showErrorDialog(error_codes["error.spot.creation"]);
                    }
                );
            } else {
                this.createSpotMarker(spots_model.currentEditSpot);
                this.updateFoundSpotsMenu();
            }
            //  google.maps.event.trigger(spots_model.spotMap[spots_model.currentEditSpot.id].marker, 'click');
        }
    },

    showDeleteSpotDialog: function (spotId) {
        spots_model.currentSpotIdToDelete = spotId;
        $('#deleteSpot').show();
    },

    deleteSpot: function () {
        var self = this;
        if (spots_model.currentSpotIdToDelete == 'new') {
            self.successfulSpotDelete();
        } else {
            spots_model.deleteSpot(
                function (json) {
                    self.successfulSpotDelete();
                },
                function () {
                    spots_model.currentSpotIdToDelete = "";
                    $('#deleteSpot').hide();
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
            );
        }
    },

    successfulSpotDelete: function () {
        this.deleteSpotFromMap(spots_model.currentSpotIdToDelete);
        this.updateFoundSpotsMenu();
        spots_model.currentSpotIdToDelete = "";
        $('#deleteSpot').hide();
    },

    deleteSpotFromMap: function (spotId) {
        var infoWindow = spots_model.spotMap[spotId].infoWindow;
        google.maps.event.clearInstanceListeners(infoWindow);  // just in case handlers continue to stick around
        infoWindow.close();
        spots_model.spotMap[spotId].marker.setMap(null);
        delete spots_model.spotMap[spotId];
    },

    createLogbookEntry: function (elemId) {
        var gotoUrl;
        if (spots_model.logbookEntryId) {
            gotoUrl = "/secure/editLogbookRecordForm.html?logbookEntryId=" + spots_model.logbookEntryId + '&spotId=';
        } else {
            gotoUrl = "/secure/createLogbookRecordForm.html?spotId=";
        }
        var spotId = elemId.split('_')[0];
        var spot = spots_model.spotMap[spotId].spot;
        if (spot.editable) {
            spots_model.createSpot(
                spot,
                function (json) {
                    window.location = gotoUrl + json.id;
                },
                function () {
                    error_dialog_controller.showErrorDialog(error_codes["error.spot.creation"]);
                }
            );
        } else {
            window.location = gotoUrl + spotId;
        }
    },

    handleLocationError: function (browserHasGeolocation, pos) {
        // var infoWindow = new google.maps.InfoWindow({map: spots_model.map});
        // infoWindow.setPosition(pos);
        // infoWindow.setContent(browserHasGeolocation ?
        //     error_codes["error.geolocation.service.failed"] :
        //     error_codes["error.geolocation.support"]
        // );
    }
};

$(document).ready(function () {
    spots_controller.init();
});
