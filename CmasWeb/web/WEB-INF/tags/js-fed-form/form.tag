<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<%@ attribute name="submitText" required="true" %>
<%@ attribute name="submitButtonClass" required="true" %>
<%@ attribute name="requiredText" required="false" %>

<%@ attribute name="action" required="false" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="method" required="false" type="java.lang.String"%>
<%@ attribute name="onsubmit" required="false" %>

<c:if test="${empty method}">
    <c:set var="method" value="POST"/>
</c:if>

<form action="${action}" method="${method}" class="fed-form" id="${id}" onsubmit="${onsubmit}">
    <jsp:doBody/>
    <c:if test="${requiredText}">
        <div class="zvezd"><s:message code="cmas.face.extForm.requiredText_1"/> <span class="zvezdsym">*</span> <s:message code="cmas.face.extForm.requiredText_2"/></div>
    </c:if>
    <div>
        <button id="submit_${id}"  type="button" class="${submitButtonClass}">${submitText}</button>
    </div>
</form>