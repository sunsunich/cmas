<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<my:securepage title="cmas.face.index.header"
               activeMenuItem="personal"
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
                        <div class="userpic">
                            <div class="centered" id="userpic" style='background : url("${pageContext.request.contextPath}${userpicRoot}${diver.userpicUrl}") center no-repeat; background-size: cover'>
                            </div>
                        </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="error" id="selectUserpic_error_file"></div>
                </div>
                <form id="userpicFileForm" style="visibility: hidden">
                    <input id="userpicFileInput" name="userpicFileInput" type="file" accept="image/*">
                </form>
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
                    <div class="card-container" id="noCard">
                        <div>
                <span class="header2-text">
                    <s:message code="cmas.face.client.profile.noCard"/>
                </span>
                        </div>
                        <button class="positive-button" id="cardReload">
                            <s:message code="cmas.face.client.profile.cardReload"/>
                        </button>
                    </div>
                    <div class="card-container" id="card">
                        <img id="cardImg"/>
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
        <div class="panel panel-header">
            <span class="header2-text"><s:message code="cmas.face.client.profile.mylogbook"/></span>
            <b id="addLogbookEntryButton" onclick="window.location='/secure/createLogbookRecordForm.html'">
                <div class="add-button logbook-button-right"></div>
                <label class="logbook-form-checkbox-label logbook-button-right">
                    <s:message code="cmas.face.logbook.addLogbookEntry"/>
                </label>
            </b>
        </div>
        <div id="accountFeed"></div>
    </div>

    <my:dialog id="showDiver"
               title="cmas.face.showDiver.title">
    </my:dialog>

    <my:dialog id="recordDeleteDialog"
               title="cmas.face.logbook.delete.title"
               buttonText="cmas.face.logbook.delete.ok">
        <div class="dialog-form-row"><s:message code="cmas.face.logbook.delete.question"/></div>
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

    <%--<my:dialog id="selectUserpic"--%>
    <%--title="cmas.face.client.profile.selectUserpic"--%>
    <%--buttonText="cmas.face.client.profile.dialog.submitText">--%>
    <%--<div class="dialog-form-row">--%>
    <%--<form id="userpicFileForm">--%>
    <%--<input id="userpicFileInput" name="userpicFileInput" type="file" accept="image/*">--%>
    <%--</form>--%>
    <%--<img id="userpicPreview" class="userpicPreview"--%>
    <%--src="${pageContext.request.contextPath}/i/no_img.png?v=${webVersion}"/>--%>
    <%--</div>--%>
    <%--<div class="error" id="selectUserpic_error_file"></div>--%>
    <%--<div class="dialog-form-row" id="cameraSelect">--%>
    <%--<img src="${pageContext.request.contextPath}/i/photo_ico_gray.png?v=${webVersion}"/>--%>
    <%--<a href="#">--%>
    <%--<s:message code="cmas.face.client.profile.selectUserpic.camera"/>--%>
    <%--</a>--%>
    <%--</div>--%>
    <%--<div class="dialog-form-row" id="fileFromDiscSelect">--%>
    <%--<img src="${pageContext.request.contextPath}/i/mobile_ico_gray.png?v=${webVersion}"/>--%>
    <%--<a href="#">--%>
    <%--<s:message code="cmas.face.client.profile.selectUserpic.fromDisc"/>--%>
    <%--</a>--%>
    <%--</div>--%>
    <%--</my:dialog>--%>

</my:securepage>


