<%@ tag body-content="scriptless" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="required" required="false" %>
<%@ attribute name="prefix" required="false" %>
<%@ attribute name="cssClass" required="false" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty prefix}">
    <c:set var="prefix" value="${prefix}_"/>
</c:if>
<c:if test="${empty cssClass}">
    <c:set var="cssClass" value="input-fed-admin"/>
</c:if>

<div>
    <label class="${cssClass}">
        <c:if test="${required}"><span class="reqMark">* </span></c:if>${label}
    </label>
    <jsp:doBody/>
    <span id="${prefix}error_${name}" class="error"></span>
</div>

