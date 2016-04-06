<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="title" required="false" type="java.lang.String" %>
<%@ attribute name="buttonText" required="false" type="java.lang.String" %>

<div id="dialog" class="dialog" style="display: none">
    <img id="dialogClose" src="${pageContext.request.contextPath}/i/close.png"/>

    <p class="dialog-title" id="dialogTitle"><s:message code="${title}"/></p>

    <p class="dialog-content" id="dialogContent">
        <jsp:doBody/>
    </p>
    <button class="form-button enter-button" id="dialogOk">
        <s:message code="${buttonText}"/>
    </button>
</div>