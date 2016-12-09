<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.BackendUser"/>

<my:standardpage title="face.changeEmail.fail.title">
    <p >
    <s:message code="face.changeEmail.fail.sorryMsg"/> ${user.newMail} <s:message code="face.changeEmail.fail.alreadyUsedMsg"/>    
    </p>
</my:standardpage>
