<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="comment" required="false" %>
<%@ attribute name="size" required="false" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="maxLen" required="false" %>
<%@ attribute name="required" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ff:row path="${path}" label="${label}" comment="${comment}" required="${required}">
    <c:if test="${empty size}">
        <c:set var="size" value="20"/>
    </c:if>
    <%-- первый шаг на дороге в ад --%>
    <c:if test="${empty id}">
        <form:input maxlength="${maxLen}" path="${path}" htmlEscape="true"
                    cssErrorClass="errorInput" cssClass="blue" cssStyle="width:${size}em" />
    </c:if>
    <c:if test="${not empty id}">
        <form:input maxlength="${maxLen}" id="${id}" path="${path}" htmlEscape="true"
                    cssErrorClass="errorInput" cssClass="blue" cssStyle="width:${size}em"/>
    </c:if>

</ff:row>

