<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<jsp:useBean id="user" scope="request" type="org.cmas.entities.User"/>

<my:nonsecurepage title="cmas.face.lostPasswd.success.title"
                  customScripts="js/controller/registration_flow_controller.js">
    <script type="application/javascript">
        $(document).ready(function () {
            registration_flow_controller.init('simple', {backgroundImageId: 'lostPasswordImageBackground'});
        });
    </script>
    <div class="content" id="Content">
        <div id="formImage" class="formImage">

        </div>
        <div class="formWrapper" id="formWrapper">
            <div id="lostPasswordBlock">
                <div class="form-description">
                    <s:message code="cmas.face.lostPasswd.success.message"/>:
                    <div class="header3-text">${user.email}</div>
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
        <img id="lostPasswordImageBackground" alt="lost password background" style="display: none">
    </picture>
</my:nonsecurepage>