<%@ tag body-content="scriptless" %>

<%@ taglib prefix="fed" tagdir="/WEB-INF/tags/js-fed-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="required" required="false" %>
<%@ attribute name="prefix" required="false" %>
<%@ attribute name="type" required="false" %>
<%@ attribute name="value" required="false" %>
<%@ attribute name="cssClass" required="false" %>

<c:if test="${not empty prefix}">
    <c:set var="prefixWithSep" value="${prefix}_"/>
</c:if>

<c:if test="${empty type}">
    <c:set var="type" value="text"/>
</c:if>

<fed:row name="${name}" label="${label}" prefix="${prefix}" required="${required}" cssClass="${cssClass}">
    <input id="${prefixWithSep}${name}" name="${prefixWithSep}${name}" type="${type}" value="${value}"/>
</fed:row>