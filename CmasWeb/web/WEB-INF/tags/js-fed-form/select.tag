<%@ tag body-content="scriptless" %>

<%@ taglib prefix="fed" tagdir="/WEB-INF/tags/js-fed-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="required" required="false" %>
<%@ attribute name="prefix" required="false" %>
<%@ attribute name="cssClass" required="false" %>

<%@ attribute name="options" required="false" type="java.lang.Object[]" %>
<%@ attribute name="value" required="false" %>

<c:if test="${not empty prefix}">
    <c:set var="prefixWithSep" value="${prefix}_"/>
</c:if>

<fed:row name="${name}" label="${label}" prefix="${prefix}" required="${required}" cssClass="${cssClass}">
    <select id="${prefixWithSep}${name}" name="${prefixWithSep}${name}">
        <option value=""></option>
        <c:if test="${not empty options}">
            <c:forEach items="${options}" var="option">
                <c:choose>
                    <c:when test="${option == value}">
                        <option value='${option}' selected="selected">${option}</option>
                    </c:when>
                    <c:otherwise>
                        <option value='${option}'>${option}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:if>
        <c:if test="${empty options}">
            <jsp:doBody/>
        </c:if>
    </select>
</fed:row>