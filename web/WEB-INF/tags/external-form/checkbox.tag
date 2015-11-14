<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="value" required="false" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="cssClass" required="false" %>
<%@ attribute name="required" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>

<ef:row path="${path}" label="${label}" required="${required}" id="${id}" cssClass="${cssClass}">
    <form:checkbox path="${path}" htmlEscape="true" cssErrorClass="errorInput" value="${value}"/>
</ef:row>