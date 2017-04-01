<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:basePage title="cmas.face.index.header" intrenal="true" indexpage="false" doNotDoAuth="true" >

    <div class="content" id="Content">
        <div class="form-logo">
            <img src="${pageContext.request.contextPath}/i/logo.png?v=${webVersion}">
        </div>
        <div class="welcome-text">
            <c:choose>
                <c:when test="${isSuccess}">
                    <s:message code="cmas.face.welcome.payment.success"/>
                </c:when>
                <c:otherwise>
                    <s:message code="cmas.face.welcome.payment.failed"/>
                </c:otherwise>
            </c:choose>

        </div>
        <div class="button-container">
            <a href="${pageContext.request.contextPath}">
                <button class="form-button enter-button">
                    <s:message code="cmas.face.welcome.continue.button"/>
                </button>
            </a>
        </div>
    </div>

</my:basePage>
