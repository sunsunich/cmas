<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="isFree" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="firstPayment" scope="request" type="java.lang.String"/>

<my:securepage title="cmas.face.index.header" hideMenu="true">

    <div class="content" id="Content">
    <div class="form-logo">
        <img src="${pageContext.request.contextPath}/i/logo.png">
    </div>
    <div class="welcome-header"><s:message code="cmas.face.welcome.title"/></div>
    <div class="welcome-text">
        <s:message code="cmas.face.welcome.text"/>
    </div>
    <div class="header">
        <c:choose>
            <c:when test="${isFree}">
                <s:message code="cmas.face.welcome.free.title"/>
            </c:when>
            <c:otherwise>
                <s:message code="cmas.face.welcome.payment.title"/>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="text">
        <c:choose>
            <c:when test="${isFree}">
                <s:message code="cmas.face.welcome.free.text"/>
            </c:when>
            <c:otherwise>
                <s:message code="cmas.face.welcome.payment.text"/>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="button-container">
        <c:choose>
            <c:when test="${isFree}">
                <a href="${pageContext.request.contextPath}/secure/welcome-continue.html">
                    <button class="form-button enter-button">
                        <s:message code="cmas.face.welcome.free.button"/>
                    </button>
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/secure/pay.html?amount=${firstPayment}">
                    <button class="form-button enter-button">
                        <s:message code="cmas.face.welcome.payment.button"/>
                    </button>
                </a>
                <a href="${pageContext.request.contextPath}/logout.html">
                    <button class="form-button reg-button">
                        <s:message code="cmas.face.welcome.cancel.button"/>
                    </button>
                </a>
            </c:otherwise>
        </c:choose>
    </div>

</my:securepage>


