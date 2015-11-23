var registration_model = {

    login: function (username, password, remember_me, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/j_spring_security_check",
            dataType: "json",
            data: {
                  j_username : username
                , j_password : password
                , _spring_security_remember_me : remember_me
            },
            success: function(json){
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json.message);
                }
                loader_controller.stopwait();
            },
            error: function (){
                window.location.reload();
            }
        });
    }
};