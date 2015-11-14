<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="comment" required="false" %>
<%@ attribute name="cols" required="false" %>
<%@ attribute name="rows" required="false" %>
<%@ attribute name="required" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>

<ef:row path="${path}" label="${label}">
    <form:textarea path="${path}" cols="${cols}" rows="${rows}" htmlEscape="true" cssErrorClass="errorInput" cssClass="field text small"/>
</ef:row>