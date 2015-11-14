<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<my:securepage title="face.changeEmail.form.page.title">
	<ef:form submitText="face.changeEmail.form.submitText" action="/secure/processEditEmail.html" id="emailChangeForm"
             submitImg="/i/gray/button.png" submitImgHeight="33" submitImgWidth="60" submitPosition="relative"
            >
		<ef:password path="password" label="face.changeEmail.form.label.password" required="true"/>
		<ef:input path="email" label="face.changeEmail.form.label.newEmail" required="true"/>
	</ef:form>
</my:securepage>