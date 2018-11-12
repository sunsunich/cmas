<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="intrenal" required="false" %>
<%@ attribute name="doNotDoAuth" required="false" %>
<%@ attribute name="customScripts" required="false" %>
<%@ attribute name="customCSSFiles" required="false" %>
<%@ attribute name="showAdvertButton" required="false" %>

<c:if test="${empty showAdvertButton}">
    <c:set var="showAdvertButton" value="true"/>
</c:if>

<my:basePage bodyId="nonsecureBody" title="${title}"
             doNotDoAuth="${doNotDoAuth}" customCSSFiles="${customCSSFiles}"
             customScripts="${customScripts}">

    <my:header>
        <div class="menu-link menu-item-left menu-item menu-item-first" style="display: none">
            <a href="${pageContext.request.contextPath}/diver-verification.html">
                <s:message code="cmas.face.login.form.link.verify"/>
            </a>
        </div>
        <c:if test="${showAdvertButton}">
            <button class="positive-button menu-item-right menu-item menu-button" id="headerAdvertButton"
                    onclick="window.location='${pageContext.request.contextPath}/diver-registration.html'">
                <s:message code="cmas.face.login.form.link.freejoin"/>
            </button>
            <span class="menu-item-right menu-text menu-item basic-text" id="headerAdvertText">
                    <s:message code="cmas.face.login.header.advert.text"/>
                </span>
            <button class="positive-button menu-item-right menu-item menu-button" id="headerLoginButton" style="display: none"
                    onclick="window.location='${pageContext.request.contextPath}/login-form.html'">
                <s:message code="cmas.face.login.form.submitText"/>
            </button>
        </c:if>
    </my:header>

    <div id="Wrapper-content">
        <div id="loading" class="loader" title="Please wait..."></div>
        <jsp:doBody/>
        <my:dialog id="errorDialog"
                   title="cmas.face.error.title"
                   buttonText="cmas.face.error.submitText">
            <div class="dialog-form-row" id="errorDialogText"></div>
        </my:dialog>
    </div>

</my:basePage>
