<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<my:securepage title="cmas.face.index.header" customScripts="js/model/spots_model.js,js/controller/spots_controller.js">

    <div class="menu-left" id="spotsMenu">
        <div class="menu-title">
            <s:message code="cmas.face.spots.chooseSpot"/>
        </div>
        <div class="dialog-content" id="noSpotsText">
            <s:message code="cmas.face.spots.empty"/>
        </div>

        <div id="foundSpots"></div>
    </div>

    <div id="map">

    </div>

    <script type="text/javascript"
            src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBKmJC1xgN8Did9WLS_5VeUIi3WBqY6fdQ">
    </script>

    <my:dialog id="createLogbookEntry"
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

