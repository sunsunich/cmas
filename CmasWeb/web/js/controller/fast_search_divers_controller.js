var fast_search_divers_controller = {

    isInstructorSearch: false,

    selectedDiverIds: {},

    init: function () {
        var self = this;
        $('#searchFriendInput').keyup(function () {
            self.search();
        });
        $('#searchFriendsButton').click(function () {
            self.search();
        });
    },

    clean: function (isInstructorSearch) {
        this.isInstructorSearch = isInstructorSearch;
        this.selectedDiverIds = {};
        $('#searchFriendInput').val('');
        this.hideError();
        $('#foundFriendListContent').empty();
        $('#noDiversFoundMessage').show();
    },

    listSelectedDiverIds: function () {
        var result = [];
        for (var diverId in this.selectedDiverIds) {
            if (this.selectedDiverIds.hasOwnProperty(diverId)) {
                result.push(diverId);
            }
        }
        return result;
    },

    search: function () {
        var input = $('#searchFriendInput').val();
        if (!input || input.length < 3) {
            this.showError("validation.diver.fast.search.tooSmall");
        } else {
            this.hideError();
            var self = this;
            var diverType = this.isInstructorSearch ? "INSTRUCTOR" : "";
            fast_search_divers_model.searchDiversFast(
                input, diverType,
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
        $('#searchFriends_error_input').html(error_codes[code]);
    },

    hideError: function () {
        $('#searchFriends_error_input').empty();
    },

    renderFriends: function (divers) {
        var self = this;
        if (divers && divers.length > 0) {
            $('#noDiversFoundMessage').hide();
            $('#foundFriendListContent').html(
                new EJS({url: '/js/templates/foundDiversList.ejs?v=' + webVersion}).render({
                    "divers": divers,
                    "webVersion": webVersion,
                    "imagesData": imagesData
                })
            ).show();
            var i;
            for (i = 0; i < divers.length; i++) {
                $('#' + divers[i].id + '_foundFriend').click(function (e) {
                    self.toggleSelectDiver($(this)[0].id);
                    e.preventDefault();
                });
            }
        } else {
            $('#foundFriendListContent').empty();
            $('#noDiversFoundMessage').show();
        }
    },

    toggleSelectDiver: function (elemId) {
        var diverId = elemId.split('_')[0];
        if (this.selectedDiverIds.hasOwnProperty(diverId)) {
            delete this.selectedDiverIds[diverId];
            $('#' + diverId + '_foundFriendCheckbox').prop('checked', false);
        } else {
            if (this.isInstructorSearch) {
                this.selectedDiverIds = {};
                $('.foundFriendCheckbox').prop('checked', false);
            }
            $('#' + diverId + '_foundFriendCheckbox').prop('checked', true);
            this.selectedDiverIds[diverId] = true;
        }
    }
};

$(document).ready(function () {
    fast_search_divers_controller.init();
});