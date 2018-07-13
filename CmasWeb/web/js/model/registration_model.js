var registration_model = {

    areas: [],
    regForm: null,
    chosenDiverId: null,

    reset: function () {
        this.regForm = null;
        this.chosenDiverId = null;
    },

    registerFirstStep: function (regForm, successHandler, unSuccessHandler) {
        this.regForm = regForm;
        basicClient.sendPostRequestCommonCase(
            "/diver-registration-submit.html",
            regForm,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },

    chooseDiver: function (email, areaOfInterest, successHandler, unSuccessHandler) {
        this.regForm.email = email;
        this.regForm.areaOfInterest = areaOfInterest;
        basicClient.sendGetRequestCommonCase(
            "/diver-registration-choose.html",
            {
                diverId: this.chosenDiverId,
                email: email,
                areaOfInterest: areaOfInterest

            },
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },

    createNewRegistration: function (email, areaOfInterest, successHandler, unSuccessHandler) {
        this.regForm.email = email;
        this.regForm.areaOfInterest = areaOfInterest;
        basicClient.sendGetRequestCommonCase(
            "/diver-registration-create.html",
            this.regForm,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },
};