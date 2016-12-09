<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<jsp:useBean id="user" scope="request" type="org.cmas.entities.User"/>

<my:basePage title="cmas.face.lostPasswd.success.title" indexpage="false">
    <div class="content" id="Content">
        <div class="form-logo">
            <a href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/i/logo.png">
            </a>
        </div>
        <div class="text">
            <s:message code="cmas.face.lostPasswd.success.message"/>:
            <div class="header">${user.email}</div>
        </div>
        <div class="pass_link">
            <a class="link" href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.link.back.text"/>
            </a>
        </div>
    </div>
</my:basePage>