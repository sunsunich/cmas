var spots_controller = {

    init: function () {
        var self = this;
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
            }, function () {
                self.handleLocationError(true, infoWindow, map.getCenter());
            });
        } else {
            // Browser doesn't support Geolocation
            self.handleLocationError(false, infoWindow, map.getCenter());
        }

    },

    refreshSpots: function () {
        var self = this;
        var mapBounds = spots_model.map.getBounds();
        var bounds = {};
        bounds.swLatitude = mapBounds.getSouthWest().lat();
        bounds.swLongitude = mapBounds.getSouthWest().lng();
        bounds.neLatitude = mapBounds.getNorthEast().lat();
        bounds.neLongitude = mapBounds.getNorthEast().lng();
        spots_model.getSpots(
            bounds,
            function (json) {
                self.showFoundSpots(json);
            },
            function () {
                $('#spots').hide();
            }
        );
    },

    showFoundSpots: function (spots) {
        var self = this;
        if (spots.length > 0) {
            $('#noSpotsText').hide();
            $('#foundSpots').html(
                new EJS({url: '/js/templates/spots.ejs'}).render({"spots": spots})
            ).show();
            for (var i = 0; i < spots.length; i++) {
                var spot = spots[i];
                $("#" + spot.id + "_chooseSpot").click(function () {
                    self.showLogbookEntryCreationDialog($(this)[0].id);
                });
                var latLng = {lat: spot.latitude, lng: spot.longitude};
                var isSpotOnMap = false;
                for (var j = 0; j < spots_model.spots.length; j++) {
                    isSpotOnMap = spots_model.spots[j].latitude == latLng.lat && spots_model.spots[j].longitude == latLng.lng;
                    if (isSpotOnMap) {
                        break;
                    }
                }
                if (!isSpotOnMap) {
                    spots_model.spots.push(spot);
                    const marker = new google.maps.Marker({
                        position: latLng,
                        map: spots_model.map
                    });
                    const infoWindow = new google.maps.InfoWindow({
                        content: '<span id=' + spot + '_infoChooseSpot' + '>' + spot.name + '</span>'
                    });

                    marker.addListener('click', function () {
                        if (spots_model.map.lastOpenInfoWindow) {
                            spots_model.map.lastOpenInfoWindow.close();
                        }
                        infoWindow.open(spots_model.map, marker);
                        spots_model.map.lastOpenInfoWindow = infoWindow;
                        $("#" + spot.id + "_infoChooseSpot").click(function () {
                            self.showLogbookEntryCreationDialog($(this)[0].id);
                        });
                    });
                }
            }
        }
        else {
            $('#foundSpots').empty().hide();
            $('#noSpotsText').show();
        }
    },

    showLogbookEntryCreationDialog: function (elemId) {
        spots_model.spotId = elemId.split('_')[0];
        alert(spots_model.spotId);
    },

    handleLocationError: function (browserHasGeolocation, pos) {
        var infoWindow = new google.maps.InfoWindow({map: map});
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
                error_codes["error.geolocation.service.failed"] :
                error_codes["error.geolocation.support"]
        );
    },

    setListeners: function () {

    }
};

$(document).ready(function () {
    spots_controller.init();
});
