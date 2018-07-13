<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<my:securepage title="cmas.face.index.header"
               customScripts="js/model/util_model.js,js/model/profile_model.js,js/model/logbook_feed_model.js,js/controller/util_controller.js,js/controller/logbook_feed_controller.js,js/controller/profile_controller.js,js/controller/userpic_controller.js"
>
    <c:set var="isCMAS"
           value="${diver.diverRegistrationStatus.name == 'CMAS_FULL' || diver.diverRegistrationStatus.name == 'CMAS_BASIC'}"/>

    <script type="application/javascript">
        <c:if test="${isCMAS}">
        var cmas_primaryCardId = "${diver.primaryPersonalCard.id}";
        </c:if>
        var diverUserpicUrl = "${diver.userpicUrl}";
    </script>

    <div class="content-left" id="mainContent">
        <div id="privateSettings">
            <div class="panel">
                <div class="userpic-selection" id="userpicSelectButton">
                    <c:choose>
                        <c:when test="${diver.userpicUrl == null}">
                            <div class="no-userpic">
                                <img id="userpic" src="/i/no-userpic-big.png?v=${webVersion}">
                                <span class="no-userpic-text"><s:message
                                        code="cmas.face.client.profile.selectUserpic"/></span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <img id="userpic" src="${pageContext.request.contextPath}${userpicRoot}${diver.userpicUrl}"
                                 class="userpic userpicPreview userpic-selection-left"/>
                        </c:otherwise>
                    </c:choose>

                </div>
                <div class="panel-row">
                    <div class="header1-text">${diver.firstName} ${diver.lastName}</div>
                </div>
                <div class="panel-row">
                    <div class="basic-text"><b><s:message code="cmas.face.client.profile.form.label.dob"/></b></div>
                    <div class="basic-text"><fmt:formatDate value="${diver.dob}" pattern="dd.MM.yyyy"/></div>
                </div>
            </div>
            <c:if test="${isCMAS}">
                <div class="panel">
                    <div class="button-container" id="noCard">
                        <div>
                <span class="panel-text">
                    <s:message code="cmas.face.client.profile.noCard"/>
                </span>
                        </div>
                        <button class="form-button reg-button" id="cardReload">
                            <s:message code="cmas.face.client.profile.cardReload"/>
                        </button>
                    </div>
                    <div class="button-container" id="card">
                        <img id="cardImg" width="90%"/>
                    </div>
                    <div class="pass_link">
                        <a class="panel-href link" href="${pageContext.request.contextPath}/secure/cards.html">
                            <s:message code="cmas.face.client.profile.showAllCards"/>
                        </a>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <div class="content-right content-large">
        <div class="panel-header">
            <span class="header2-text"><s:message code="cmas.face.client.profile.mylogbook"/></span>
        </div>
        <div id="accountFeed"></div>
    </div>

    <my:dialog id="showDiver"
               title="cmas.face.showDiver.title"
               buttonText="cmas.face.showDiver.submitText">
    </my:dialog>

    <div id="showLogbookEntry" class="logbookEntry" style="display: none">
        <img id="showLogbookEntryClose" src="${pageContext.request.contextPath}/i/close.png?v=${webVersion}"
             class="dialogClose"/>

        <div class="dialog-title" id="showLogbookEntryTitle"><s:message code="cmas.face.showLogbookEntry.title"/></div>

        <div class="dialog-content" id="showLogbookEntryContent">
        </div>

        <div class="button-container">
            <button class="form-button enter-button" id="showLogbookEntryOk">
                <s:message code="cmas.face.showLogbookEntry.submitText"/>
            </button>
        </div>
    </div>

    <my:dialog id="selectUserpic"
               title="cmas.face.client.profile.selectUserpic"
               buttonText="cmas.face.client.profile.dialog.submitText">
        <div class="dialog-form-row">
            <form id="userpicFileForm">
                <input id="userpicFileInput" name="userpicFileInput" type="file" accept="image/*">
            </form>
            <img id="userpicPreview" class="userpicPreview"
                 src="${pageContext.request.contextPath}/i/no_img.png?v=${webVersion}"/>
        </div>
        <div class="error" id="selectUserpic_error_file"></div>
        <div class="dialog-form-row" id="cameraSelect">
            <img src="${pageContext.request.contextPath}/i/photo_ico_gray.png?v=${webVersion}"/>
            <a href="#">
                <s:message code="cmas.face.client.profile.selectUserpic.camera"/>
            </a>
        </div>
        <div class="dialog-form-row" id="fileFromDiscSelect">
            <img src="${pageContext.request.contextPath}/i/mobile_ico_gray.png?v=${webVersion}"/>
            <a href="#">
                <s:message code="cmas.face.client.profile.selectUserpic.fromDisc"/>
            </a>
        </div>
    </my:dialog>

    <my:dialog id="cameraPreview"
               title="cmas.face.client.profile.selectUserpic.camera.preview.title"
               buttonText="cmas.face.dialog.ok">
        <video autoplay id="cameraPreviewWindow">
        </video>
        <img id="cameraPreviewImg" src="">
        <canvas style="display:none;"></canvas>
        <div class="button-container">
            <button class="form-button enter-button" id="takePicture">
                <s:message code="cmas.face.client.profile.selectUserpic.camera.preview.takePicture"/>
            </button>
            <button class="form-button enter-button" id="takePictureAgain">
                <s:message code="cmas.face.client.profile.selectUserpic.camera.preview.takePictureAgain"/>
            </button>
        </div>
    </my:dialog>

</my:securepage>


