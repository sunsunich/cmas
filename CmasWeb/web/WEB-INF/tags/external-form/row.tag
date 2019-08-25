<%@ tag body-content="scriptless" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="false" %>

<%@ attribute name="id" required="false" %>
<%@ attribute name="cssClass" required="false" %>

<span>${label}</span>
<jsp:doBody/>
<spring:bind path="${path}">
    <c:if test="${status.error}">
        <form:errors path="${path}" htmlEscape="true" cssClass="error"/>
    </c:if>
</spring:bind>
<br/>
