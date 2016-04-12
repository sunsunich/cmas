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

    <script type="application/javascript">
        var cmas_primaryCardId = "${diver.primaryPersonalCard.id}";
    </script>

    <div class="content">
        <div class="tabs">

        </div>
        <div class="panel">
            <div class="userpic-selection">
                <img src="${pageContext.request.contextPath}/i/no_img.png" class="userpic-selection-left"/>
                <img src="${pageContext.request.contextPath}/i/photo_ico.png"/>
                <a href="#" class="link" style="pointer-events: none;">
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
            <div class="button-container" id="noCard">
                <div>
                <span>
                    <s:message code="cmas.face.client.profile.noCard"/>
                </span>
                </div>
                <button class="form-button reg-button" id="cardReload">
                    <s:message code="cmas.face.client.profile.cardReload"/>
                </button>
            </div>
            <div class="button-container" id="card">
                <img id="cardImg" width="90%"/>
            </div>
            <div class="pass_link">
                <a class="link" href="#"><s:message code="cmas.face.client.profile.showAllCards"/></a>
            </div>
        </div>
        <div class="panel">
            <div class="button-container">
                <button class="form-button enter-button" id="changeEmailButton">
                    <s:message code="cmas.face.changeEmail.form.page.title"/>
                </button>
                <button class="form-button enter-button" id="changePasswordButton">
                    <s:message code="cmas.face.changePasswd.form.page.title"/>
                </button>
            </div>
        </div>
    </div>

    <my:dialog id="selectUserpic"
               title="cmas.face.client.profile.selectUserpic"
               buttonText="cmas.face.client.profile.dialog.submitText">

    </my:dialog>

    <my:dialog id="changePassword"
               title="cmas.face.changePasswd.form.page.title"
               buttonText="cmas.face.client.profile.dialog.password.submitText">
        <div id="changePasswordForm">
            <div class="dialog-form-row">
                <input id="oldPassword" type="password"
                       placeholder="<s:message code="cmas.face.changePasswd.form.label.oldPassword"/>"/>
            </div>
            <div class="error" id="changePassword_error_oldPassword"></div>
            <div class="dialog-form-row">
                <input id="password" type="password"
                       placeholder="<s:message code="cmas.face.changePasswd.form.label.password"/>"/>
            </div>
            <div class="error" id="changePassword_error_password"></div>
            <div class="dialog-form-row">
                <input id="checkPassword" type="password"
                       placeholder="<s:message code="cmas.face.changePasswd.form.label.checkPassword"/>"/>
            </div>
            <div class="error" id="changePassword_error_checkPassword"></div>
            <div class="error" style="display: none" id="changePassword_error">
            </div>
        </div>
        <div id="changePasswordSuccessMessage">
            <div class="dialog-form-row"><s:message code="cmas.face.changePasswd.success.message"/></div>
            <div class="button-container">
                <button class="form-button enter-button" id="changePasswordFinishedOk">
                    <s:message code="cmas.face.dialog.ok"/>
                </button>
            </div>
        </div>
    </my:dialog>

    <my:dialog id="changeEmail"
               title="cmas.face.changeEmail.form.page.title"
               buttonText="cmas.face.client.profile.dialog.email.submitText">
        <div id="changeEmailForm">
            <div class="dialog-form-row">
                <input id="changeEmailPassword" type="password"
                       placeholder="<s:message code="cmas.face.changeEmail.form.label.password"/>"/>
            </div>
            <div class="error" id="changeEmail_error_password"></div>
            <div class="dialog-form-row">
                <input id="email" type="email"
                       placeholder="<s:message code="cmas.face.changeEmail.form.label.newEmail"/>"/>
            </div>
            <div class="error" id="changeEmail_error_email"></div>
            <div class="error" style="display: none" id="changeEmail_error">
            </div>
        </div>
        <div id="changeEmailSuccessMessage">
            <div class="dialog-form-row"><s:message code="cmas.face.changeEmail.sent.message"/></div>
            <div class="button-container">
                <button class="form-button enter-button" id="changeEmailFinishedOk">
                    <s:message code="cmas.face.dialog.ok"/>
                </button>
            </div>
        </div>
    </my:dialog>

</my:securepage>


