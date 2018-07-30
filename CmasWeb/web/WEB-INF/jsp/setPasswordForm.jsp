<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.recovery.PasswordChangeFormObject"/>

<my:nonsecurepage title="cmas.face.registration.setPassword.page.title"
                  customScripts="js/controller/set_password_controller.js,js/controller/registration_flow_controller.js"
>
    <script type="application/javascript">
        $(document).ready(function () {
            set_password_controller.init({
                hasOldPassword: false,
                setPasswordAction: function (form) {
                    window.location = "/setPasswordSubmit.html?code=" + form.code +
                        "&password=" + form.password + "&checkPassword=" + form.checkPassword;
                }
            });
        });
    </script>

    <my:setPassword headerCode="cmas.face.registration.setPassword.header"
                    command="${command}" hasCode="true" hasSuccessBlock="false"/>

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
        <img id="setPasswordImageBackground" alt="set password background" style="display: none">
    </picture>

</my:nonsecurepage>
