<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<%@ attribute name="title" required="false" type="java.lang.String" %>
<%@ attribute name="buttonText" required="false" type="java.lang.String" %>
<%@ attribute name="id" required="false" type="java.lang.String" %>

<c:if test="${empty id}">
    <c:set var="id" value="dialog"/>
</c:if>

<div id="${id}" class="dialog" style="display: none">
    <img id="${id}Close" src="${pageContext.request.contextPath}/i/close.png" class="dialogClose"/>

    <p class="dialog-title" id="${id}Title"><s:message code="${title}"/></p>

    <div class="dialog-content" id="${id}Content">
        <jsp:doBody/>
    </div>

    <div class="button-container">
        <button class="form-button enter-button" id="${id}Ok">
            <s:message code="${buttonText}"/>
        </button>
    </div>
</div>