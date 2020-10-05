<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.user.EmailEditFormObject"/>

<my:fed_adminpage title="cmas.face.changeEmail.form.page.title"
               customScripts="js/model/profile_model.js,js/controller/change_email_controller.js"
>
    <h2><s:message code="cmas.face.changeEmail.form.header"/></h2>
    <ff:form submitText="cmas.face.changeEmail.form.submitText" action="/fed/processEditEmail.html" method="POST"
             noRequiredText="true">
        <ff:password path="password" label="cmas.face.changeEmail.form.label.password"/>
        <ff:input path="email" label="cmas.face.changeEmail.form.label.newEmail"/>

    </ff:form>

</my:fed_adminpage>
