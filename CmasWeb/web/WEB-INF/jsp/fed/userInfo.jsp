<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fed" tagdir="/WEB-INF/tags/js-fed-form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="diverTypes" scope="request" type="org.cmas.entities.diver.DiverType[]"/>
<jsp:useBean id="diverLevels" scope="request" type="org.cmas.entities.diver.DiverLevel[]"/>
<jsp:useBean id="command" scope="request" type="org.cmas.entities.diver.Diver"/>

<%--
mmap.addAttribute("cardsJson", new Gson().toJson(diver.getCards()));
--%>
<%--<jsp:useBean id="natFedCard" scope="request" type="org.cmas.entities.PersonalCard"/>--%>
<%--<jsp:useBean id="natFedInstructorCard" scope="request" type="org.cmas.entities.PersonalCard"/>--%>
<jsp:useBean id="cardGroups" scope="request" type="java.util.Map<java.lang.String, org.cmas.entities.PersonalCard[]>"/>
<jsp:useBean id="cardsJson" scope="request" type="java.lang.String"/>

<my:fed_adminpage title="Diver Info"
                  customScripts="/js/model/fed_edit_diver_model.js,/js/controller/fed_edit_diver_controller.js">
    <script type="application/javascript">
        <c:choose>
        <c:when test="${command.dob == null}">
        var dob = "";
        </c:when>
        <c:otherwise>
        var dob = "<fmt:formatDate value="${command.dob}" pattern="dd/MM/yyyy"/>";
        </c:otherwise>
        </c:choose>

        var cards = JSON.parse('${cardsJson}');
    </script>

    <c:choose>
        <c:when test="${command.email == null}">
            <c:set var="submitText" value="Add Driver"/>
        </c:when>
        <c:otherwise>
            <c:set var="submitText" value="Edit Driver"/>
        </c:otherwise>
    </c:choose>

    <h2>Diver Info</h2>
    <fed:form id="addDiver" submitText="${submitText}" submitButtonClass="button-fed-admin" requiredText="true">

        <input id="diver_id" type="hidden" value="${command.id}"/>

        <fed:input prefix="diver" name="email" label="E-mail" value="${command.email}" required="true"/>
        <fed:input prefix="diver" name="firstName" label="First Name" value="${command.firstName}" required="true"/>
        <fed:input prefix="diver" name="lastName" label="Last Name" value="${command.lastName}" required="true"/>
        <fed:input prefix="diver" name="dob" label="Date of birth" value='' required="true"/>
        <fed:select prefix="diver" name="diverType" label="Diver type" options="${diverTypes}"
                    value="${command.diverType}"
                    required="true"/>
        <fed:select prefix="diver" name="diverLevel" label="Diver level" options="${diverLevels}"
                    value="${command.diverLevel}"
                    required="true"/>
        <c:if test="${command.primaryPersonalCard != null}">
            <div>
                <label class="input-fed-admin">CMAS card number</label>
                <span>${command.primaryPersonalCard.number}</span>
            </div>
        </c:if>
        <c:choose>
            <c:when test="${natFedCard != null}">
                <fed:input prefix="diver" name="natFedCardNumber" label="National Federation card number"
                           value="${natFedCard.number}"/>
            </c:when>
            <c:otherwise>
                <fed:input prefix="diver" name="natFedCardNumber" label="National Federation card number"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${natFedInstructorCard != null}">
                <fed:input prefix="diver" name="natFedInstructorCardNumber"
                           label="National Federation Instructor card number"
                           value="${natFedInstructorCard.number}"/>
            </c:when>
            <c:otherwise>
                <fed:input prefix="diver" name="natFedInstructorCardNumber"
                           label="National Federation Instructor card number"/>
            </c:otherwise>
        </c:choose>
        <div>
            <label class="input-fed-admin"><b>Certifications</b></label>
        </div>
        <div class="card-addition-block clearfix">
            <div class="card-addition">
                <div>
                    <label class="input-fed-admin-card"><span class="reqMark">* </span>Type of certificate</label>
                    <select id="card_cardType">
                        <c:forEach var="cardGroup" items="${cardGroups}">
                            <optgroup label='<s:message code="${cardGroup.key}"/>'>
                                <c:forEach var="cardType" items="${cardGroup.value}">
                                    <option value="${cardType}">${cardType}</option>
                                </c:forEach>
                            </optgroup>
                        </c:forEach>
                    </select>
                    <span id="card_error_cardType" cssclass="error"></span>
                </div>
                <fed:select name="diverType" prefix="card" label="Diver type" options="${diverTypes}" value=""
                            required="true" cssClass="input-fed-admin-card"/>
                <fed:select name="diverLevel" prefix="card" label="Diver level" options="${diverLevels}" value=""
                            cssClass="input-fed-admin-card"/>
                <fed:input name="number" prefix="card" label="Certificate number" value=""
                           cssClass="input-fed-admin-card"/>
                <button id="addCard" type="button" class="button-fed-admin" name="Add Certificate">Add Certificate
                </button>
            </div>
            <div class="card-addition">
                <label style="margin:0 10px 0 20px"><b>Added Certifications</b></label>
                <div id="cardsContainer">
                </div>
            </div>
        </div>
    </fed:form>

    <my:dialog id="diverSaveSuccess"
               title="cmas.face.fed.diver.saveTitle"
               buttonText="cmas.face.dialog.ok">
        <div><s:message code="cmas.face.fed.diver.saveText"/></div>
    </my:dialog>

</my:fed_adminpage>