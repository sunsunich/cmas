<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.BackendUser"/>

<my:securepage title="cmas.face.index.header"
               customScripts="js/model/profile_model.js,js/controller/profile_controller.js"
        >

    <div class="content">
        <div class="tabs">

        </div>
        <div class="panel">
            <div class="userpic-selection">
                <a href="#" class="link">
                    <img src="${pageContext.request.contextPath}/i/no_img.png" class="userpic-selection-left"/>
                    <img src="${pageContext.request.contextPath}/i/photo_ico.png"/>
                    <s:message code="cmas.face.client.profile.selectUserpic"/>
                </a>
            </div>
            <div class="panel-row">
                <span>${diver.firstName} ${diver.lastName}</span>
            </div>

            <div class="panel-row">
                <label><s:message code="cmas.face.client.profile.form.label.dob"/>&nbsp;</label>
                <span><fmt:formatDate value="${diver.dob}" pattern="dd.MM.yyyy"/></span>
            </div>
        </div>
        <div class="panel">
            <div></div>
            <div class="pass_link">
                <a class="link" href="#"><s:message code="cmas.face.client.profile.showAllCards"/></a>
            </div>
        </div>
        <div class="panel">
            <div class="button-container">
                <button class="form-button enter-button" id="changeEmail">
                    <s:message code="cmas.face.changeEmail.form.page.title"/>
                </button>
                <button class="form-button enter-button" id="changePassword">
                    <s:message code="cmas.face.changePasswd.form.page.title"/>
                </button>
            </div>
        </div>
    </div>

    <%--<my:dialog id="selectUserpic"--%>
               <%--title="cmas.face.client.profile.selectUserpic"--%>
               <%--buttonText="cmas.face.client.profile.dialog.submitText">--%>

    <%--</my:dialog>--%>

    <my:dialog id="changePassword"
               title="cmas.face.changePasswd.form.page.title"
               buttonText="cmas.face.client.profile.dialog.password.submitText">

    </my:dialog>

    <%--<my:dialog id="changeEmail"--%>
               <%--title="cmas.face.changeEmail.form.page.title"--%>
               <%--buttonText="cmas.face.client.profile.dialog.email.submitText">--%>

    <%--</my:dialog>--%>

</my:securepage>


