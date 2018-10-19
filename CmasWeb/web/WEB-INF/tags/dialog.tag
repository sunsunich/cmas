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
    <div class="dialog-title">
        <span id="${id}Title"><s:message code="${title}"/></span>
        <div id="${id}Close" class="dialogClose"></div>
    </div>
    <div class="dialog-content" id="${id}Content">
        <jsp:doBody/>
    </div>

    <div class="button-container">
        <c:if test="${buttonText}">
            <button class="positive-button dialog-button" id="${id}Ok">
                <s:message code="${buttonText}"/>
            </button>
        </c:if>
    </div>
</div>