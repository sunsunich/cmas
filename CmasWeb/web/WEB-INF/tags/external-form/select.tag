<%@ tag body-content="scriptless" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="options" required="false" type="java.lang.Object[]" %>
<%@ attribute name="itemLabel" required="false" %>
<%@ attribute name="comment" required="false" %>
<%@ attribute name="itemValue" required="false" %>

<ef:row path="${path}" label="${label}" >
    <form:select path="${path}" htmlEscape="true" cssErrorClass="errorInput" cssClass="field text small">
        <c:if test="${not empty options}">
            <c:choose>
                <c:when test="${empty itemValue}">
                    <form:options items="${options}" itemLabel="${itemLabel}"/>
                </c:when>
                <c:otherwise>
                    <form:options items="${options}" itemLabel="${itemLabel}" itemValue="${itemValue}" />
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:if test="${empty options}">
            <jsp:doBody/>
        </c:if>
    </form:select>
</ef:row>