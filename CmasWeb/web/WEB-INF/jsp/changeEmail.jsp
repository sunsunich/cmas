<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<my:basePage title="cmas.face.changeEmail.success.title" doNotDoAuth="true">
    <div class="content" id="Content">
        <div class="form-logo">
            <a href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/i/logo.png">
            </a>
        </div>
        <div class="text">
            <s:message code="cmas.face.changeEmail.success.message"/>:
            <div class="header">${diver.email}</div>
        </div>
        <div class="pass_link">
            <a class="link" href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.link.back.text"/>
            </a>
        </div>
    </div>
</my:basePage>
