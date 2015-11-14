<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.UserClient"/>

<my:securepage title="face.changeEmail.sent.title">
    <p >
    <s:message code="face.changeEmail.sent.message"/> ${user.newMail}
    </p>
</my:securepage>
