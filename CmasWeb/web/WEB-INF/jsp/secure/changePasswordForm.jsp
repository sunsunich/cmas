<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.user.PasswordEditFormObject"/>

<my:securepage title="cmas.face.changePasswd.form.page.title"
                  customScripts="js/model/profile_model.js,js/controller/registration_flow_controller.js,js/controller/set_password_controller.js"
>
    <script type="application/javascript">
        $(document).ready(function () {
            set_password_controller.init({
                hasOldPassword: true,
                setPasswordAction: function (form) {
                    profile_model.changePassword(
                        form
                        , function (/*json*/) {
                            registration_flow_controller.toggleMode('setPasswordSuccess');
                        }
                        , function (json) {
                            set_password_controller.validationController.showErrors(json);
                        }
                    );
                }
            });
        });
    </script>

    <my:setPassword headerCode="cmas.face.changePasswd.form.header"
                    command="${command}" hasCode="false" hasSuccessBlock="true"/>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/requestImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/requestImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/requestImage.png 1x, /i/requestImage@2x.png 2x, /i/requestImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/requestImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/requestImageMob.png 1x, /i/requestImageMob@2x.png 2x, /i/requestImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="setPasswordImageBackground" alt="change password background" style="display: none">
    </picture>
    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/changePasswordSuccessImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/changePasswordSuccessImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/changePasswordSuccessImage.png 1x, /i/changePasswordSuccessImage@2x.png 2x, /i/changePasswordSuccessImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/changePasswordSuccessImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/changePasswordSuccessImageMob.png 1x, /i/changePasswordSuccessImageMob@2x.png 2x, /i/changePasswordSuccessImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="setPasswordSuccessImageBackground" alt="change password success background" style="display: none">
    </picture>

</my:securepage>
