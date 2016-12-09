<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="comment" required="false" %>
<%@ attribute name="size" required="false" %>
<%@ attribute name="required" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ff:row path="${path}" label="${label}" comment="${comment}" required="${required}">
    <c:if test="${empty size}">
        <c:set var="size" value="20"/>
    </c:if>
    <form:password path="${path}" htmlEscape="true" cssErrorClass="errorInput" cssClass="blue" cssStyle="width:${size}em"/>
</ff:row>