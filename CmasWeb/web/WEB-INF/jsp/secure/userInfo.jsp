<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="visibilityTypes" scope="request" type="org.cmas.entities.logbook.LogbookVisibility[]"/>

<my:securepage title="cmas.face.index.header"
               customScripts="js/model/util_model.js,js/model/profile_model.js,js/model/social_model.js,js/model/logbook_feed_model.js,js/controller/util_controller.js,js/controller/country_controller.js,js/controller/logbook_feed_controller.js,js/controller/profile_controller.js,js/controller/userpic_controller.js,js/controller/social_settings_controller.js"
        >
    <script type="application/javascript">
        var visibilityTypes = [];
        <c:forEach items="${visibilityTypes}" var="visibilityType" varStatus="st">
        visibilityTypes[${st.index}] = '${visibilityType.name}';
        </c:forEach>
        var logbookVisibility = "${diver.defaultVisibility}";
        var cmas_primaryCardId = "${diver.primaryPersonalCard.id}";
    </script>

    <div class="content-left" id="mainContent">
        <div class="tabs clearfix">
            <span class="firstTab" id="privateTab"><s:message code="cmas.face.client.profile.private"/></span>
            <span class="secondTab" id="socialTab" class="inactive"><s:message code="cmas.face.client.profile.social"/></span>
        </div>
        <div id="privateSettings">
            <div class="panel">
                <div class="userpic-selection" id="userpicSelectButton">
                    <img id="userpic" src="${pageContext.request.contextPath}/i/no_img.png"
                         class="userpic userpicPreview userpic-selection-left"/>

                    <div class="userpic-selection-right">
                        <img src="${pageContext.request.contextPath}/i/photo_ico.png"/>
                        <a href="#" class="panel-href link">
                            <s:message code="cmas.face.client.profile.selectUserpic"/>
                        </a>
                    </div>
                </div>
                <div class="panel-row">
                    <span class="panel-text">${diver.firstName} ${diver.lastName}</span>
                </div>

                <div class="panel-row">
                    <label class="panel-label"><s:message code="cmas.face.client.profile.form.label.dob"/>&nbsp;</label>
                    <span class="panel-text"><fmt:formatDate value="${diver.dob}" pattern="dd.MM.yyyy"/></span>
                </div>
            </div>
            <div class="panel">
                <div class="button-container" id="noCard">
                    <div>
                <span class="panel-text">
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
                    <a class="panel-href link" href="${pageContext.request.contextPath}/secure/cards.html">
                        <s:message code="cmas.face.client.profile.showAllCards"/>
                    </a>
                </div>
            </div>
            <div class="panel">
                <div class="button-container">
                    <button class="userInfo-button reg-button" id="changeEmailButton">
                        <s:message code="cmas.face.changeEmail.form.page.title"/>
                    </button>
                    <button class="userInfo-button reg-button" id="changePasswordButton">
                        <s:message code="cmas.face.changePasswd.form.page.title"/>
                    </button>
                </div>
            </div>
        </div>
        <div id="socialSettings" style="display: none">
            <div class="panel" style="display: none" id="buddieRequestsPanel">
                <div class="header">
                    <s:message code="cmas.face.client.social.buddieRequests.header"/>
                </div>
                <div id="buddieRequests">
                </div>
            </div>

            <div class="panel" style="display: none" id="toRequestsPanel">
                <div class="header">
                    <s:message code="cmas.face.client.social.friendRequestsTo.header"/>
                </div>
                <div id="toRequests">
                </div>
            </div>

            <div class="panel">
                <div class="header">
                    <s:message code="cmas.face.client.social.team.header"/>
                </div>

                <div id="friendsPanel">
                </div>
                <div class="panel-row text" id="noFriendsText">
                    <s:message code="cmas.face.client.social.team.empty"/>
                </div>

                <div class="panel-row button-container">
                    <button class="centerUserInfo-button reg-button" id="findDiverButton">
                        <s:message code="cmas.face.client.social.addMember"/>
                    </button>
                </div>

                <%--<div class="panel-row">--%>
                    <%--<input type="checkbox" id="addTeamToLogbook"--%>
                           <%--<c:if test="${diver.addFriendsToLogbookEntries}">checked="checked"</c:if>--%>
                            <%--/>--%>
                    <%--<span class="text"><s:message code="cmas.face.client.social.addToLogbook"/></span>--%>
                <%--</div>--%>
            </div>

            <div class="panel" style="display: none" id="fromRequestsPanel">
                <div class="header">
                    <s:message code="cmas.face.client.social.friendRequestsFrom.header"/>
                </div>
                <div id="fromRequests">
                </div>
            </div>

            <div class="panel">
                <div class="header">
                    <s:message code="cmas.face.client.social.logbook.settings.header"/>
                </div>
                <div class="panel-row">
                    <select name="visibilityType" id="visibilityType" style="width: 100%" size=1 onChange="">
                    </select>
                </div>
            </div>
            <div class="panel">
                <div class="header">
                    <s:message code="cmas.face.client.social.newsfeed.header"/>
                </div>
                <div class="panel-row panel-row-bottom-margin">
                    <input type="checkbox" id="addLocationCountryToNewsFeed"
                           <c:if test="${diver.newsFromCurrentLocation}">checked="checked"</c:if>
                            />
                    <span class="text"><s:message code="cmas.face.client.social.newfeed.currentLocation"/></span>
                </div>
                <div id="newsCountries">
                </div>
                <div class="panel-row">
                    <div class="button-container">
                        <button class="centerUserInfo-button reg-button" id="addCountryButton">
                            <s:message code="cmas.face.client.social.addCountry"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="content-left" id="diversList" style="display: none">
        <div id="diversListContent">

        </div>
        <div class="panel">
            <div class="button-container">
                <button class="form-button enter-button" id="newDiverSearch">
                    <s:message code="cmas.face.diverList.submitText"/>
                </button>
            </div>
        </div>
    </div>

    <div class="content-right" id="accountFeed" ></div>

    <my:dialog id="findDiver"
               title="cmas.face.findDiver.form.page.title"
               buttonText="cmas.face.findDiver.form.submitText">
        <div id="findDiverForm">
            <div class="horizontal-radio-group clearfix">
                <div>
                    <label for="findDiverTypeDiver">
                        <input type="radio" name="findDiverType" id="findDiverTypeDiver" value="DIVER"/>
                        Diver
                    </label>
                </div>
                <div>
                    <label for="findDiverTypeInstructor">
                        <input type="radio" name="findDiverType" id="findDiverTypeInstructor" value="INSTRUCTOR"/>
                        Instructor
                    </label>
                </div>
            </div>
            <div class="error" id="findDiver_error_diverType"></div>
            <div class="dialog-form-row">
                <select name="findDiverCountry" id="findDiverCountry" style="width: 100%" size=1 onChange="">
                    <c:forEach items="${countries}" var="country">
                        <option value='${country.code}'>${country.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="error" id="findDiver_error_country"></div>
            <div class="dialog-form-row">
                <input id="findDiverName" type="text"
                       placeholder="<s:message code="cmas.face.findDiver.form.label.name"/>"/>
            </div>
            <div class="error" id="findDiver_error_name"></div>

            <div class="error" style="display: none" id="findDiver_error">
            </div>
        </div>
    </my:dialog>

    <my:dialog id="noDiversFound"
               title="cmas.face.diverList.title"
               buttonText="cmas.face.diverList.submitText">
        <div id="noDiversFoundText"><s:message code="cmas.face.diverList.noDivers"/></div>
    </my:dialog>

    <my:dialog id="friendRemove"
               title="cmas.face.friendRemove.title"
               buttonText="cmas.face.friendRemove.submitText">
        <div>
            <s:message code="cmas.face.friendRemove.question"/>
            <span> <b id="removeDiverName"></b>?</span>
        </div>
    </my:dialog>

    <my:dialog id="friendRequestRemove"
               title="cmas.face.friendRequestRemove.title"
               buttonText="cmas.face.friendRequestRemove.submitText">
        <div>
            <s:message code="cmas.face.friendRequestRemove.question"/>
        </div>
    </my:dialog>

    <my:dialog id="showDiver"
               title="cmas.face.showDiver.title"
               buttonText="cmas.face.showDiver.submitText">
    </my:dialog>

    <div id="showLogbookEntry" class="logbookEntry" style="display: none">
        <img id="showLogbookEntryClose" src="${pageContext.request.contextPath}/i/close.png" class="dialogClose"/>

        <div class="dialog-title" id="showLogbookEntryTitle"><s:message code="cmas.face.showLogbookEntry.title"/></div>

        <div class="dialog-content" id="showLogbookEntryContent">
        </div>

        <div class="button-container">
            <button class="form-button enter-button" id="showLogbookEntryOk">
                <s:message code="cmas.face.showLogbookEntry.submitText"/>
            </button>
        </div>
    </div>


    <my:dialog id="addCountry"
               title="cmas.face.addCountry.title"
               buttonText="cmas.face.addCountry.submitText">
        <div id="addCountryForm">
            <div class="dialog-form-row">
                <select name="countryToNews" id="countryToNews" style="width: 100%" size=1 onChange="">
                    <c:forEach items="${countries}" var="country">
                        <option value='${country.code}'>${country.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="error" id="addCountryForm_error_countryCode"></div>

            <div class="error" style="display: none" id="addCountryForm_error">
            </div>
        </div>
    </my:dialog>

    <my:dialog id="selectUserpic"
               title="cmas.face.client.profile.selectUserpic"
               buttonText="cmas.face.client.profile.dialog.submitText">
        <div class="dialog-form-row">
            <input id="userpicFileInput" name="userpicFileInput" type="file" accept="image/*">
            <img id="userpicPreview" class="userpicPreview" src="${pageContext.request.contextPath}/i/no_img.png"/>
        </div>
        <div class="error" id="selectUserpic_error_file"></div>
        <div class="dialog-form-row" id="cameraSelect">
            <img src="${pageContext.request.contextPath}/i/photo_ico_gray.png"/>
            <a href="#">
                <s:message code="cmas.face.client.profile.selectUserpic.camera"/>
            </a>
        </div>
        <div class="dialog-form-row" id="fileFromDiscSelect">
            <img src="${pageContext.request.contextPath}/i/mobile_ico_gray.png"/>
            <a href="#">
                <s:message code="cmas.face.client.profile.selectUserpic.fromDisc"/>
            </a>
        </div>
    </my:dialog>

    <my:dialog id="cameraPreview"
               title="cmas.face.client.profile.selectUserpic.camera.preview.title"
               buttonText="cmas.face.dialog.ok">
        <video autoplay id="cameraPreviewWindow">
        </video>
        <img id="cameraPreviewImg" src="">
        <canvas style="display:none;"></canvas>
        <div class="button-container">
            <button class="form-button enter-button" id="takePicture">
                <s:message code="cmas.face.client.profile.selectUserpic.camera.preview.takePicture"/>
            </button>
            <button class="form-button enter-button" id="takePictureAgain">
                <s:message code="cmas.face.client.profile.selectUserpic.camera.preview.takePictureAgain"/>
            </button>
        </div>
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


