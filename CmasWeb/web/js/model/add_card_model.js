var add_card_model = {

    diverLevels: [],
    diverTypes: [],
    cardGroups: {},

    mobileUploadCardRequest: function (formData, successHandler, unSuccessHandler) {
        loader_controller.startwait();
        $.ajax({
            type:  "POST",
            url: "/submitCertificateApprovalRequest.html",
            dataType: "json",
            data: {"requestJson": JSON.stringify(formData)},
            headers: {'CMAS_AUTH_TOKEN': formData.token},
            success: function (json) {
                if (json.success) {
                    successHandler(json);
                } else {
                    unSuccessHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function (e) {
                window.location.reload();
            }
        });
    },

    uploadCardRequest: function (formData, files, successHandler, unSuccessHandler) {
        loader_controller.startwait();
        var form = new FormData();
        form.append('diverLevel', formData.diverLevel == null ? "" : formData.diverLevel);
        form.append('diverType', formData.diverType == null ? "" : formData.diverType);
        form.append('validUntil', formData.validUntil);
        form.append('federationCardNumber', formData.federationCardNumber);
        form.append('federationId', formData.federationId);
        form.append('frontImage', files['1']);
        form.append('backImage', files['2']);

        $.ajax({
            type: "POST",
            url: "/secure/submitCardApprovalRequest.html",
            cache: false,
            contentType: false,
            enctype: 'multipart/form-data',
            processData: false,
            data: form,
            success: function (jsonStr) {
                var json = JSON.parse(jsonStr);
                if (json.success) {
                    successHandler(json);
                } else {
                    unSuccessHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                window.location.reload();
            }
        });
    }

};