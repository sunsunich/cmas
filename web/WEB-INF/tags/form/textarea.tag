<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="comment" required="false" %>
<%@ attribute name="cols" required="false" %>
<%@ attribute name="rows" required="false" %>
<%@ attribute name="required" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>

<ff:row path="${path}" label="${label}" comment="${comment}" required="${required}">
    <form:textarea path="${path}" cols="${cols}" rows="${rows}" htmlEscape="true" cssErrorClass="errorInput" cssClass="blue"/>
</ff:row>