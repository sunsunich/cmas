var fast_search_friends_controller = {

    prefix: "searchFriends",
    fastSearchUrl: "/secure/social/searchFriendsFast.html",
    listElemEjs: "foundFriendList",
    setupFoundElem: null, /*function*/
    emptyInputCallback: null, /*function*/

    init: function () {
        var self = this;
        $('#' + this.prefix + '_input').keyup(function () {
            self.search();
        });
        $('#' + this.prefix + '_button').click(function () {
            self.search();
        });
    },

    search: function () {
        var input = $('#' + this.prefix + '_input').val();
        if (input) {
            if (input.length < 3) {
                this.showError("validation.diver.fast.search.tooSmall");
            } else {
                this.hideError();
                var self = this;
                fast_search_friends_model.fastSearch(
                    this.fastSearchUrl,
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
        } else {
            if (this.emptyInputCallback) {
                this.emptyInputCallback();
            } else {
                this.showError("validation.diver.fast.search.tooSmall");
            }
        }
    },

    showError: function (code) {
        $('#' + this.prefix + '_container').hide();
        $('#' + this.prefix + '_error_input').html(error_codes[code]);
    },

    hideError: function () {
        $('#' + this.prefix + '_error_input').empty();
    },

    renderFriends: function (divers) {
        $('#' + this.prefix + '_container').show();
        if (divers && divers.length > 0) {
            $('#' + this.prefix + '_notFound').hide();
            $('#' + this.prefix + '_list').html(
                new EJS({url: '/js/templates/' + this.listElemEjs + '.ejs?v=' + webVersion}).render({
                    "divers": divers,
                    "webVersion": webVersion,
                    "imagesData": imagesData
                })
            ).show();
            var i;
            for (i = 0; i < divers.length; i++) {
                this.setupFoundElem(divers[i].id);
            }
        } else {
            $('#' + this.prefix + '_list').empty();
            $('#' + this.prefix + '_notFound').show();
        }
    }

    // sendFriendRequest: function (elemId) {
    //     var diverId = elemId.split('_')[0];
    //     fast_search_friends_model.sendFriendRequest(
    //         diverId
    //         , function (json) {
    //             if (isStringTrimmedEmpty(json.message)) {
    //                 $('#friendRequestSuccessDialog').show();
    //             }
    //             else {
    //                 error_dialog_controller.showErrorDialog(
    //                     labels["cmas.face.friendRequest.failure"] + ' ' + error_codes[json.message]
    //                 );
    //             }
    //         }
    //         , function (json) {
    //             error_dialog_controller.showErrorDialog(
    //                 labels["cmas.face.friendRequest.failure"] + ' ' + error_codes[json.message]
    //             );
    //         });
    // }
};

// $(document).ready(function () {
//     fast_search_friends_controller.init();
// });
// .click(function (event) {
//     if (!$(event.target).closest('#foundFriendList').length) {
//         if ($('#foundFriendList').is(":visible")) {
//             $('#foundFriendList').hide();
//         }
//     }
// });