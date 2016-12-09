<%@ tag body-content="scriptless" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%-- массив с объектами, внутри которых есть методы для получения свойств с именами
    лежащими в itemLabel и itemValue. если не написать тип, то при связывании параметров произойдет toString() --%>
<%@ attribute name="options" required="false"  type="java.lang.Object[]" %>
<%@ attribute name="itemLabel" required="false" %>
<%@ attribute name="itemValue" required="false" %>

<ff:row path="${path}" label="${label}">
    <form:select path="${path}" htmlEscape="true" cssErrorClass="errorInput" cssClass="blue">
        <form:option value="" label="" />
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
</ff:row>