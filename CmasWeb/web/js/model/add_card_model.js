var add_card_model = {

    diverLevels: [],

    uploadCardRequest: function (formData, files, successHandler, unSuccessHandler) {
        loader_controller.startwait();
        var form = new FormData();
        form.append('diverLevel', formData.diverLevel);
        form.append('countryCode', formData.countryCode);

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