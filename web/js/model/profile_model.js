var profile_model = {

    register: function (regForm, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/diver-registration-submit.html",
            dataType: "json",
            data: regForm,
            success: function(json){
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function (){
                window.location.reload();
            }
        });
    }
};