<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.user.UserFormObject"/>
<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.UserClient"/>

<my:securepage title="face.client.editProfile.title">

	<div class="b-rblock b-rblock_fix">
		<div class="b-rblock_inner personal">
			<h1><s:message code="face.client.editProfile.form.title"/></h1>

			<ef:form submitText="face.client.editProfile.form.submitText"
					 action="/secure/profile/processEditUser.html" id="userEditChangeForm" noRequiredText="true"
                     submitImg="/i/gray/button.png" submitImgHeight="33" submitImgWidth="60" submitPosition="relative">

				<ef:input path="shopName" label="face.client.editProfile.form.label.shopName"/>

			</ef:form>

		</div>
        <%----%>
		<c:if test="${isSuccess}">
			<div>
				<s:message code="face.client.editProfile.success"/>
			</div>
		</c:if>
	</div>
</my:securepage>


