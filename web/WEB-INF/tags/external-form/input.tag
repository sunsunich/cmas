<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="false" %>
<%@ attribute name="id" required="false" %>
<%--<%@ attribute name="size" required="false" %>--%>
<%@ attribute name="maxLen" required="false" %>
<%@ attribute name="required" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ef:row path="${path}" label="${label}"  >
    <form:input maxlength="${maxLen}" path="${path}" htmlEscape="true" id="${id}"/>
</ef:row>

