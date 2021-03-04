<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<jsp:useBean id="federations" scope="request" type="java.util.List<org.cmas.entities.sport.NationalFederation>"/>
<jsp:useBean id="diverTypes" scope="request" type="org.cmas.entities.diver.DiverType[]"/>
<jsp:useBean id="diverLevels" scope="request" type="org.cmas.entities.diver.DiverLevel[]"/>

<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.cards.CardApprovalRequestFormObject"/>

<jsp:useBean id="cardGroups" scope="request"
             type="java.util.Map<java.lang.String, org.cmas.entities.cards.PersonalCard[]>"/>

<jsp:useBean id="heliCardGroups" scope="request"
             type="java.util.Map<java.lang.String, org.cmas.entities.cards.PersonalCard[]>"/>

<my:securepage title="cmas.face.index.header"
               activeMenuItem="cards"
               customScripts="js/model/add_card_model.js,js/controller/multiple_fileUpload_controller.js,js/controller/add_card_controller.js"
>
    <script type="application/javascript">
        <my:enumToJs enumItems="${diverLevels}" arrayVarName="add_card_model.diverLevels"/>
    </script>
    <script type="application/javascript">
        <my:enumToJs enumItems="${diverTypes}" arrayVarName="add_card_model.diverTypes"/>
    </script>

    <div class="content-center-wide" id="content">
        <div class="panel panel-header panel-add-card">
            <span class="header2-text"><s:message code="cmas.face.card.addCard.header"/></span>
            <c:if test="${diver.diverRegistrationStatus.name != 'CMAS_FULL' && diver.diverRegistrationStatus.name != 'CMAS_BASIC'}">
                <div class="basic-text"><s:message code="cmas.face.card.becomeCmas"/></div>
            </c:if>
            <div class="form-row"></div>
            <div class="logbookHeader header2-text">
                <label><s:message code="cmas.face.card.addCard.required"/></label>
            </div>
            <div class="form-row">
                <label class="form-label card-form-label" for="card_diverType"><s:message
                        code="cmas.face.card.diverType"/></label>
                <select name="diverType" id="card_diverType" size=1 onChange=""
                        style="width: 205px">
                </select>
                <label class="error" id="card_error_diverType"></label>
            </div>
            <div class="form-row">
                <label class="form-label card-form-label" for="card_federationId"><s:message
                        code="cmas.face.card.federation"/></label>
                <select name="federationId" id="card_federationId" size=1 onChange=""
                        style="width: 205px">
                    <c:forEach items="${federations}" var="federation">
                        <option value='${federation.id}'>${federation.name}</option>
                    </c:forEach>
                </select>
                <div class="error" id="card_error_federationId"></div>
            </div>
            <div class="logbookHeader header2-text">
                <label><s:message code="cmas.face.card.addCard.optional"/></label>
            </div>
            <div class="form-row">
                <label class="form-label card-form-label" for="card_diverLevel"><s:message
                        code="cmas.face.card.level"/></label>
                <select name="diverLevel" id="card_diverLevel" size=1 onChange=""
                        style="width: 205px">
                </select>
                <label class="error" id="card_error_diverLevel"></label>
            </div>
            <div class="form-row">
                <label class="form-label card-form-label"><s:message code="cmas.face.card.number"/></label>
                <input id="card_federationCardNumber" type="text"
                       placeholder="<s:message code="cmas.face.card.number"/>"
                />
                <label class="error" id="card_error_federationCardNumber"></label>
            </div>
            <div class="form-row">
                <label class="form-label card-form-label"><s:message code="cmas.face.card.addCard.validUntil"/></label>
                <div style="display: inline-block">
                    <input id="card_validUntil" type="text"
                           placeholder="<s:message code="cmas.face.card.addCard.validUntil"/>">
                    <img src="/i/ic_calendar.png" class="error-input-ico" id="card_ico_validUntil" style="position: relative; left: -36px; top: 7px">
                    <label class="error" id="card_error_validUntil"></label>
                </div>
            </div>
            <div class="form-row">
                <div class="clearfix">
                    <div class="logbookHeader header2-text"><s:message code="cmas.face.card.images"/></div>
                    <div id="addImage"
                         class="feedback-button-right positive-button logbook-button logbook-inline-button">
                        <img class="logbook-inline-button-icon"
                             alt="error icon"
                             src="${pageContext.request.contextPath}/i/ic_plus_white.png?v=${webVersion}"/>
                        <span class="logbook-inline-button-icon-text"><s:message code="cmas.face.card.addImage"/></span>
                    </div>
                </div>
                <div class="error" id="card_error_photo"></div>
            </div>
            <div id="photoListContainer" class="clearfix">
            </div>
            <div class="form-row">
                <div class="error" id="card_error"></div>
            </div>
            <div class="clearfix">
                <button class="white-button form-item-left" onclick="window.location='/secure/cards.html'">
                    <s:message code="cmas.face.button.cancel"/>
                </button>
                <button class="positive-button form-item-right" id="cardSubmit">
                    <s:message code="cmas.face.card.submit"/>
                </button>
            </div>
        </div>
    </div>

    <my:dialog id="cardAddedDialog"
               title="cmas.face.card.success.title"
               buttonText="">
        <div class="form-row"><s:message code="cmas.face.card.success.text"/></div>
        <div class="button-container">
            <button class="white-button dialog-button dialog-button-margin"
                    onclick="window.location='/secure/cards.html'">
                <s:message code="cmas.face.card.success.button.back"/>
            </button>
            <button class="positive-button dialog-button" onclick="window.location='/secure/addCard.html'">
                <s:message code="cmas.face.card.success.button.another"/>
            </button>
        </div>
    </my:dialog>

</my:securepage>


