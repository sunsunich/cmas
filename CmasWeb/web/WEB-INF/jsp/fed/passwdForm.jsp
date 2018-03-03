<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>

<my:fed_adminpage title="cmas.face.changePasswd.form.page.title">

    <h2><s:message code="cmas.face.changePasswd.form.description"/></h2>
    <ff:form submitText="cmas.face.changePasswd.form.submitText" action="/fed/processEditPasswd.html" method="POST"
             noRequiredText="true">
        <ff:password path="oldPassword" label="cmas.face.changePasswd.form.label.oldPassword"/>
        <ff:password path="password" label="cmas.face.changePasswd.form.label.password"/>
        <ff:password path="checkPassword" label="cmas.face.changePasswd.form.label.checkPassword"/>

    </ff:form>


    <!-- end of Content -->
</my:fed_adminpage>