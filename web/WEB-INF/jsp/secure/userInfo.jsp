<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="command" scope="request" type="org.cmas.entities.User"/>
<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.BackendUser"/>

<my:securepage title="face.client.editProfile.title">

    <div class="b-rblock b-rblock_fix">
        <div class="b-rblock_inner personal">
            <h1><s:message code="face.client.editProfile.form.title"/></h1>

            <ef:form submitText="face.client.editProfile.form.submitText"
                     action="/secure/profile/processEditUser.html" id="userEditChangeForm" noRequiredText="true"
                     submitImg="/i/gray/button.png" submitImgHeight="33" submitImgWidth="60" submitPosition="relative">

                <ef:input path="dateReg" label="face.client.editProfile.form.label.dateReg"/>
                <ef:input path="locale" label="face.client.editProfile.form.label.locale"/>

                <ef:input path="email" label="face.client.editProfile.form.label.email"/>
                <ef:input path="country.name" label="face.client.editProfile.form.label.country"/>

                <ef:input path="firstName" label="face.client.editProfile.form.label.firstName"/>
                <ef:input path="lastName" label="face.client.editProfile.form.label.lastName"/>
                <ef:input path="dob" label="face.client.editProfile.form.label.dob"/>


            </ef:form>

            <c:if test="${command.role eq 'ROLE_SPORTSMAN'}">
                <c:if test="${command.primarySportsmanCard != null}">
                    <div>
                        Your federation is: <br/>
                            ${command.federation.name}
                    </div>
                    <div>
                        Your primary card number is: <br/>
                            ${command.primarySportsmanCard.number}
                    </div>
                </c:if>
            </c:if>

        </div>
            <%----%>
        <c:if test="${isSuccess}">
            <div>
                <s:message code="face.client.editProfile.success"/>
            </div>
        </c:if>
    </div>
</my:securepage>


