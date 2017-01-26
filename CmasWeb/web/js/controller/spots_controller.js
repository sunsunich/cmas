var spots_controller = {

    init: function () {
        var self = this;
        country_controller.inputId = "createSpotCountry";
        country_controller.drawIcon = false;
        country_controller.init();

        spots_model.map = new google.maps.Map(document.getElementById('map'), {
            center: {lat: 41.9, lng: 12.4833333},
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

        $('#createSpotOk').click(function () {
            self.submitSpot();
        });

        $('#createSpotClose').click(function () {
            $('#createSpot').hide();
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
        }
        else {
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

    deleteSpotFromMap: function (spotId) {
        var infoWindow = spots_model.spotMap[spotId].infoWindow;
        google.maps.event.clearInstanceListeners(infoWindow);  // just in case handlers continue to stick around
        infoWindow.close();
        spots_model.spotMap[spotId].marker.setMap(null);
        delete spots_model.spotMap[spotId];
    },

    showSpotOnMap: function (latitude, longitude, isAutoGeoLocation) {
        var self = this;
        var spot = {
            id: 'new',
            isApproved: false,
            latitude: latitude,
            longitude: longitude,
            isAutoGeoLocation: isAutoGeoLocation
        };
        var foundSpot = this.findSpotOnMap(spot);
        if (foundSpot == null) {
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
                    }
                    else {
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

    showEditSpotDialog: function (spot) {
        spots_model.currentEditSpot = spot;
        this.cleanEditSpotForm(spot);
        $('#createSpot').show();
    },

    submitSpot: function () {
        spots_model.currentEditSpot.name = $('#createSpotName').val();
        spots_model.currentEditSpot.toponymName = $('#createSpotToponymName').val();
        spots_model.currentEditSpot.countryCode = $('#createSpotCountry').val();
        spots_model.currentEditSpot.countryName = $('#createSpotCountry :selected').text();

        this.cleanEditSpotErrors();
        var formErrors = this.validateEditSpotForm(spots_model.currentEditSpot);
        if (formErrors.success) {
            $('#createSpot').hide();
            this.createSpotMarker(spots_model.currentEditSpot);
            this.updateFoundSpots();
            //  google.maps.event.trigger(spots_model.spotMap[spots_model.currentEditSpot.id].marker, 'click');
        }
        else {
            validation_controller.showErrors('createSpot', formErrors);
        }
    },

    validateEditSpotForm: function (form) {
        var result = {};
        result.fieldErrors = {};
        result.errors = [];
        if (isStringTrimmedEmpty(form.name)) {
            result.fieldErrors["name"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(form.countryCode)) {
            result.fieldErrors["countryCode"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(form.toponymName)) {
            result.fieldErrors["toponymName"] = 'validation.toponym';
        }
        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);
        return result;
    },

    cleanEditSpotForm: function (spot) {
        $('#createSpotName').val(spot && spot.name ? spot.name : "");
        $('#createSpotToponymName').val(spot && spot.toponymName ? spot.toponymName : "");
        $('#createSpotCountry').val(spot && spot.countryCode ? spot.countryCode : "").trigger("change");
        this.cleanEditSpotErrors();
    },

    cleanEditSpotErrors: function () {
        $("#createSpot_error").empty().hide();
        $('#createSpot_error_name').empty();
        $('#createSpot_error_toponymName').empty();
        $('#createSpot_error_countryCode').empty();
    },

    createSpotMarker: function (spot) {
        var self = this;
        var latLng = {lat: spot.latitude, lng: spot.longitude};
        const marker = new google.maps.Marker({
            position: latLng,
            map: spots_model.map
        });
        var content;
        if (spot.isApproved) {
            content = '<span>' + spot.name + '</span> <br />' +
                '<button id="' + spot.id + '_chooseSpotInfoWindow">' + labels['cmas.face.spots.choose'] + '</button>';
        } else {
            content = '<span>' + spot.name + '</span> <br />' +
                '<span>' + spot.toponymName + '</span> <br />' +
                '<span>' + spot.countryName + '</span> <br />' +
                '<button id="' + spot.id + '_chooseSpotInfoWindow">' + labels['cmas.face.spots.choose'] + '</button> <br />' +
                '<button id="' + spot.id + '_editSpotInfoWindow">' + labels['cmas.face.spots.edit'] + '</button>';

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
        if (!spotMapElem.isApproved) {
            $("body").off("click", '#' + spotId + '_editSpotInfoWindow');
            $('#' + spotId + '_editSpotInfoWindow').click(function () {
                    var spotId = $(this)[0].id.split('_')[0];
                    self.showEditSpotDialog(spots_model.spotMap[spotId].spot);
                }
            );
        }

        $("body").off("click", '#' + spotId + '_chooseSpotInfoWindow');
        $('#' + spotId + '_chooseSpotInfoWindow').click(function () {
                self.createLogbookEntry($(this)[0].id);
            }
        );

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
        this.updateFoundSpots();
    },

    updateFoundSpots: function () {
        var self = this;
        if (Object.keys(spots_model.spotMap).length > 0) {
            $('#noSpotsText').hide();
            $('#foundSpots').html(
                new EJS({url: '/js/templates/spots.ejs'}).render({"spotMap": spots_model.spotMap})
            ).show();
            for (var key in spots_model.spotMap) {
                if (spots_model.spotMap.hasOwnProperty(key)) {
                    var spot = spots_model.spotMap[key].spot;
                    $("#" + spot.id + "_chooseSpot").click(function () {
                        self.createLogbookEntry($(this)[0].id);
                    });
                    $("#" + spot.id + "_chooseSpotArrow").click(function () {
                        self.createLogbookEntry($(this)[0].id);
                    });
                    if (!spot.isApproved) {
                        $('#' + spot.id + '_editSpot').click(function (e) {
                                e.preventDefault();
                                var spotId = $(this)[0].id.split('_')[0];
                                self.showEditSpotDialog(spots_model.spotMap[spotId].spot);
                            }
                        );
                    }
                }
            }
        }
        else {
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

    createLogbookEntry: function (elemId) {
        var gotoUrl;
        if (spots_model.logbookEntryId) {
            gotoUrl = "/secure/editLogbookRecordForm.html?logbookEntryId=" + spots_model.logbookEntryId + '&spotId=';
        } else {
            gotoUrl = "/secure/createLogbookRecordForm.html?spotId=";
        }
        var spotId = elemId.split('_')[0];
        var spot = spots_model.spotMap[spotId].spot;
        if (!spot.isApproved) {
            spots_model.createSpot(
                spot,
                function (json) {
                    window.location = gotoUrl + json.id;
                },
                function () {
                    error_dialog_controller.showErrorDialog(labels["error.spot.creation"]);
                }
            );
        } else {
            window.location = gotoUrl + spotId;
        }
    },

    handleLocationError: function (browserHasGeolocation, pos) {
        var infoWindow = new google.maps.InfoWindow({map: spots_model.map});
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
                error_codes["error.geolocation.service.failed"] :
                error_codes["error.geolocation.support"]
        );
        /*
         error_dialog_controller.showErrorDialog(
         browserHasGeolocation ?
         error_codes["error.geolocation.service.failed"] :
         error_codes["error.geolocation.support"]
         )
         */
    }
};

$(document).ready(function () {
    spots_controller.init();
});
