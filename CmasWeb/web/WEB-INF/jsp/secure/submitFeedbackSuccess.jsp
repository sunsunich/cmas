<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="siteEmail" scope="request" type="java.lang.String"/>
<my:securepage title="cmas.face.changeEmail.success.title"
               customScripts="js/controller/registration_flow_controller.js"
>
    <script type="application/javascript">
        $(document).ready(function () {
            registration_flow_controller.init('simple', {backgroundImageId: 'sendFeedbackSuccessImageBackground'});
        });
    </script>
    <div class="content" id="Content">
        <div id="formImage" class="formImage">
        </div>
        <div class="formWrapper" id="formWrapper">
            <div id="sendFeedbackSuccessBlock">
                <div class="header1-text">
                    <s:message code="cmas.face.feedback.success.title"/>
                </div>
                <div class="form-description">
                    <s:message code="cmas.face.feedback.success.message"/>:
                    <div class="header3-text">${siteEmail}</div>
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
        <source srcset="/i/changePasswordSuccessImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/changePasswordSuccessImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/changePasswordSuccessImage.png 1x, /i/changePasswordSuccessImage@2x.png 2x, /i/changePasswordSuccessImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/changePasswordSuccessImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/changePasswordSuccessImageMob.png 1x, /i/changePasswordSuccessImageMob@2x.png 2x, /i/changePasswordSuccessImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="sendFeedbackSuccessImageBackground" alt="change email success background" style="display: none">
    </picture>
</my:securepage>
