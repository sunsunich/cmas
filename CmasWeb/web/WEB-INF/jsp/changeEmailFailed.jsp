<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<my:nonsecurepage title="cmas.face.changeEmail.success.title" doNotDoAuth="true"
                  customScripts="js/controller/registration_flow_controller.js"
>
    <p>

    </p>
    <script type="application/javascript">
        $(document).ready(function () {
            registration_flow_controller.init('simple', {backgroundImageId: 'changeEmailFailImageBackground'});
        });
    </script>
    <div class="content" id="Content">
        <div id="formImage" class="formImage">
        </div>
        <div class="formWrapper" id="formWrapper">
            <div id="changeEmailBlock">
                <div class="header1-text">
                    <s:message code="cmas.face.changeEmail.success.title"/>
                </div>
                <div class="form-description">
                    <s:message code="cmas.face.changeEmail.fail.sorryMsg"/> ${diver.newMail}
                    <s:message code="cmas.face.changeEmail.fail.alreadyUsedMsg"/>
                </div>
                <button class="positive-button form-item-right form-button-smaller" onclick="window.location='/'">
                    <s:message code="cmas.face.link.back.home.text"/>
                </button>
            </div>
        </div>
    </div>

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
        <img id="changeEmailFailImageBackground" alt="change email success background" style="display: none">
    </picture>
</my:nonsecurepage>
