<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fed" tagdir="/WEB-INF/tags/js-fed-form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="diverTypes" scope="request" type="org.cmas.entities.diver.DiverType[]"/>
<jsp:useBean id="diverLevels" scope="request" type="org.cmas.entities.diver.DiverLevel[]"/>
<jsp:useBean id="cardApprovalRequest" scope="request" type="org.cmas.entities.cards.CardApprovalRequest"/>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.cards.CardApprovalRequestEditFormObject"/>
<jsp:useBean id="cardGroups" scope="request"
             type="java.util.Map<java.lang.String, org.cmas.entities.cards.PersonalCard[]>"/>

<my:fed_adminpage title="Certificate approval request"
                  customScripts="/js/model/fed_card_approval_request_model.js,/js/controller/fed_card_approval_request_controller.js">
    <script type="application/javascript">
        <c:choose>
        <c:when test="${cardApprovalRequest.validUntil == null}">
        var validUntil = "";
        </c:when>
        <c:otherwise>
        var validUntil = "<fmt:formatDate value="${cardApprovalRequest.validUntil}" pattern="dd/MM/yyyy"/>";
        </c:otherwise>
        </c:choose>
        fed_card_approval_request_model.requestId = ${cardApprovalRequest.id};
        fed_card_approval_request_model.requestStatus = '${cardApprovalRequest.status.name}';
        <c:if test="${cardApprovalRequest.cardType == null}">
        fed_card_approval_request_model.cardType = '${cardApprovalRequest.cardType.name}';
        </c:if>
    </script>

    <h2>Validate Certificate approval request</h2>
    <table>
        <tr>
            <td>
                <img class="cardImage"
                     src="${pageContext.request.contextPath}${cardApprovalRequestImagesRoot}${cardApprovalRequest.frontImage.fileUrl}"/>
            </td>
            <td>
                <img class="cardImage"
                     src="${pageContext.request.contextPath}${cardApprovalRequestImagesRoot}${cardApprovalRequest.backImage.fileUrl}"/>
            </td>
        </tr>
    </table>
    <div>
        Diver info:<br/>
        Diver email: ${cardApprovalRequest.diver.email}<br/>
        First name: ${cardApprovalRequest.diver.firstName}<br/>
        Last name: ${cardApprovalRequest.diver.lastName}<br/>
        Diver date of birth: <fmt:formatDate value="${cardApprovalRequest.diver.dob}" pattern="dd/MM/yyyy"/><br/>
    </div>

    <fed:form id="addCard" submitText="Approve request" submitButtonClass="positive-button" requiredText="true"
              declineButtonClass="negative-button" declineText="Decline request"
    >
        <input id="card_requestId" type="hidden" value="${cardApprovalRequest.id}"/>

        <fed:input name="federationCardNumber" prefix="card" label="Certificate number"
                   value="${cardApprovalRequest.federationName}"
                   cssClass="input-fed-admin-card"
        />
        <fed:select name="diverType" prefix="card" label="Diver type" options="${diverTypes}"
                    value="${cardApprovalRequest.diverType.name}"
                    required="true" cssClass="input-fed-admin-card"/>
        <fed:select name="diverLevel" prefix="card" label="Diver level" options="${diverLevels}"
                    value="${cardApprovalRequest.diverLevel.name}"
                    required="true" cssClass="input-fed-admin-card"/>
        <div>
            <label class="input-fed-admin-card"><span class="reqMark">* </span>Type of certificate</label>
            <select id="card_cardType">
                <c:forEach var="cardGroup" items="${cardGroups}">
                    <optgroup label='<s:message code="${cardGroup.key}"/>'>
                        <c:forEach var="cardType" items="${cardGroup.value}">
                            <c:choose>
                                <c:when test="${cardType.name == cardApprovalRequest.cardType.name}">
                                    <option value='${cardType}' selected="selected">${cardType}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value='${cardType}'>${cardType}</option>
                                </c:otherwise>
                            </c:choose>
                            <option value="${cardType}">${cardType}</option>
                        </c:forEach>
                    </optgroup>
                </c:forEach>
            </select>
            <span id="card_error_cardType" class="error"></span>
        </div>
        <fed:input prefix="card" name="validUntil" label="Valid until" value='' cssClass="input-fed-admin-card"/>
        <my:dialog id="cardApprovalSuccess"
                   title="cmas.face.fed.cardApprovalRequest.approveTitle"
                   buttonText="cmas.face.dialog.ok">
            <div><s:message code="cmas.face.fed.cardApprovalRequest.approveText"/></div>
        </my:dialog>
    </fed:form>
</my:fed_adminpage>