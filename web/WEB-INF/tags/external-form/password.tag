<%@ tag body-content="empty" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="false" %>
<%@ attribute name="id" required="false" %>


<ef:row path="${path}" label="${label}">
    <form:password path="${path}" htmlEscape="true" id="${id}"/>
</ef:row>