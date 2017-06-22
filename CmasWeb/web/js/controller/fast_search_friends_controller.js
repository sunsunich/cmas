var fast_search_friends_controller = {

    init: function () {
        var self = this;
        $('#searchFriendInput').keyup(function () {
            self.search();
        });
        $('#searchFriendsButton').click(function () {
            self.search();
        });
        $("#friendRequestSuccessDialogOk").click(function () {
            $('#friendRequestSuccessDialog').hide();
        });
        $("#friendRequestSuccessDialogClose").click(function () {
            $('#friendRequestSuccessDialog').hide();
        });
    },

    search: function () {
        var input = $('#searchFriendInput').val();
        if (!input || input.length < 3) {
            this.showError("validation.diver.fast.search.tooSmall");
        } else {
            this.hideError();
            var self = this;
            fast_search_friends_model.fastSearchFriends(
                input,
                function (json) {
                    self.hideError();
                    self.renderFriends(json);
                }
                , function (json) {
                    self.showError(json.message);
                }
            );
        }
    },

    showError: function (code) {
        $('#foundFriendList').hide();
        $('#searchFriends_error_input').html(error_codes[code]);
    },

    hideError: function () {
        $('#searchFriends_error_input').empty();
    },

    renderFriends: function (divers) {
        var self = this;
        $('#foundFriendList').show();
        if (divers && divers.length > 0) {
            $('#noDiversFoundMessage').hide();
            $('#foundFriendListContent').html(
                new EJS({url: '/js/templates/foundFriendList.ejs?v=' + webVersion}).render({
                    "divers": divers,
                    "webVersion": webVersion
                })
            ).show();
            var i;
            for (i = 0; i < divers.length; i++) {
                $('#' + divers[i].id + '_foundFriend').click(function () {
                    self.sendFriendRequest($(this)[0].id);
                });
            }
        } else {
            $('#foundFriendListContent').empty();
            $('#noDiversFoundMessage').show();
        }
    },

    sendFriendRequest: function (elemId) {
        var diverId = elemId.split('_')[0];
        fast_search_friends_model.sendFriendRequest(
            diverId
            , function (json) {
                if (isStringTrimmedEmpty(json.message)) {
                    $('#friendRequestSuccessDialog').show();
                }
                else {
                    error_dialog_controller.showErrorDialog(
                        labels["cmas.face.friendRequest.failure"] + ' ' + error_codes[json.message]
                    );
                }
            }
            , function (json) {
                error_dialog_controller.showErrorDialog(
                    labels["cmas.face.friendRequest.failure"] + ' ' + error_codes[json.message]
                );
            });
    }
};

$(document).ready(function () {
    fast_search_friends_controller.init();
}).click(function (event) {
    if (!$(event.target).closest('#foundFriendList').length) {
        if ($('#foundFriendList').is(":visible")) {
            $('#foundFriendList').hide();
        }
    }
});