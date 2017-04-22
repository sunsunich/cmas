<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%-- код сообщения из ui.messages, которое надо показать в качестве альта на изображении сабмита. --%>
<%@ attribute name="submitText" required="true" %>
<%@ attribute name="submitImg" required="true" %>
<%@ attribute name="submitImgWidth" required="true" %>
<%@ attribute name="submitImgHeight" required="true" %>
<%@ attribute name="submitPosition" required="false" %>
<%@ attribute name="submitPositionTop" required="false" %>
<%@ attribute name="submitPositionLeft" required="false" %>
<%@ attribute name="noRequiredText" required="false" %>
<%@ attribute name="cssClass" required="false" %>

<%@ attribute name="action" required="false" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="method" required="false" type="java.lang.String"%>
<%@ attribute name="commandName" required="false" %>
<%@ attribute name="onsubmit" required="false" %>
<c:set var="command">${(not empty commandName)?(commandName):('command')}</c:set>

<c:if test="${empty method}">
    <c:set var="method" value="POST"/>
</c:if>
<c:set var="formClass" >b-form<c:if test="${not empty cssClass}"> b-form-${cssClass}</c:if></c:set>

<form:form htmlEscape="true" action="${action}" commandName="${command}" method="${method}" cssClass="${formClass}" id="${id}" onsubmit="${onsubmit}">
    <spring:hasBindErrors name="${command}">
        <ul>
           <c:forEach items="${errors.globalErrors}" var="error">
                <li><span class="error"><spring:message message="${error}"/></span></li>
           </c:forEach>
        </ul>
    </spring:hasBindErrors>
    <jsp:doBody/>
    <c:if test="${not noRequiredText}">
    <div class="zvezd"><s:message code="cmas.face.extForm.requiredText_1"/> <span class="zvezdsym">*</span> <s:message code="cmas.face.extForm.requiredText_2"/></div>
    </c:if>
    <div class="b-form-row"><label>&nbsp;</label>
        <input type="image" src="${submitImg}"
               alt="<spring:message code="${submitText}"/>" id="submitButton" style="width: ${submitImgWidth}px; height: ${submitImgHeight}px; position: ${submitPosition}; top: ${submitPositionTop}px; left: ${submitPositionLeft}px;" /></div>
</form:form>