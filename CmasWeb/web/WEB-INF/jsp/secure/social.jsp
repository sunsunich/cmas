<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>

<my:securepage title="cmas.face.index.header"
               activeMenuItem="social"
               customScripts="js/model/util_model.js,js/model/social_model.js,js/model/fast_search_friends_model.js,js/controller/util_controller.js,js/controller/country_controller.js,js/controller/social_settings_controller.js,js/controller/fast_search_friends_controller.js"
>
    <div class="content-left content-large">
        <div class="panel-header">
            <span class="header2-text"><s:message code="cmas.face.client.social.team.header"/></span>
        </div>

        <div class="panel-header">
            <div id="findDiverByNameForm">
                <div class="form-row search-row">
                    <input id="searchFriendInput" type="text"
                           placeholder="<s:message code="cmas.face.diver.fast.search.placeholderText"/>"/>
                    <div class="error" id="searchFriends_error_input"></div>
                </div>
            </div>
        </div>

        <div class="panel-header panel-wider">
            <div class="sub-panel-narrow menu-header" id="listHeader">
                <span class="header2-text"><s:message code="cmas.face.client.social.lists.allFriends.header"/></span>
            </div>
            <div id="foundFriendList" class="found-friend-list clearfix">
                <div id="noDiversFoundMessage" style="display: none">
                <span class="foundFriendList-text">
                    <s:message code="cmas.face.diver.fast.search.notFound"/>
                </span>
                </div>
                <div id="foundFriendListContent" class="found-friend-list-content clearfix"></div>
            </div>
        </div>
    </div>

    <div class="content-right">
        <div class="panel-header panel-wider">
            <div class="sub-panel-narrow menu-header">
                <span class="header2-text"><s:message code="cmas.face.client.social.lists"/></span>
            </div>
            <div class="panel-menu-item panel-menu-item-active" id="allFriends">
                <s:message code="cmas.face.client.social.lists.allFriends.header"/>
            </div>
            <div class="panel-menu-item" id="friendRequestsTo">
                <s:message code="cmas.face.client.social.friendRequestsTo.header"/>
            </div>
            <div class="panel-menu-item" id="friendRequestsFrom">
                <s:message code="cmas.face.client.social.friendRequestsFrom.header"/>
            </div>
        </div>
    </div>

    <%--<div class="content-left" id="mainContent" style="display: none">--%>
    <%--<div id="socialSettings">--%>
    <%--<div class="panel" style="display: none" id="buddieRequestsPanel">--%>
    <%--<div class="header">--%>
    <%--<s:message code="cmas.face.client.social.buddieRequests.header"/>--%>
    <%--</div>--%>
    <%--<div id="buddieRequests">--%>
    <%--</div>--%>
    <%--</div>--%>

    <%--<div class="panel" style="display: none" id="toRequestsPanel">--%>
    <%--<div class="header">--%>
    <%--<s:message code="cmas.face.client.social.friendRequestsTo.header"/>--%>
    <%--</div>--%>
    <%--<div id="toRequests">--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<form name="searchFriends">--%>
    <%--<div class="search-friend-form-logbook">--%>
    <%--<input type="text" id="searchFriendInput"--%>
    <%--placeholder="<s:message code="cmas.face.diver.fast.search.placeholderText"/>">--%>
    <%--<div id="foundFriendList" class="foundFriendList">--%>
    <%--<div id="foundFriendListContent"></div>--%>
    <%--<div id="noDiversFoundMessage" style="display: none">--%>
    <%--<span class="foundFriendList-text">--%>
    <%--<s:message code="cmas.face.diver.fast.search.notFound"/>--%>
    <%--</span>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<div>--%>
    <%--<label class="error" id="searchFriends_error_input"></label>--%>
    <%--</div>--%>
    <%--<div>--%>
    <%--<button type="button" class="form-button enter-button searchFriendsButton"--%>
    <%--id="searchFriendsButton">--%>
    <%--<s:message code="cmas.face.diver.fast.search.buttonText"/>--%>
    <%--</button>--%>
    <%--</div>--%>
    <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
    <%--</form>--%>

    <%--<div class="panel">--%>
    <%--<div class="header">--%>
    <%--<s:message code="cmas.face.client.social.team.header"/>--%>
    <%--</div>--%>
    <%--<div id="friendsPanel">--%>
    <%--</div>--%>
    <%--<div class="panel-row text" id="noFriendsText">--%>
    <%--<s:message code="cmas.face.client.social.team.empty"/>--%>
    <%--</div>--%>

    <%--<div class="panel-row button-container">--%>

    <%--<button class="centerUserInfo-button reg-button" id="findDiverButton">--%>
    <%--<s:message code="cmas.face.client.social.advancedSearch"/>--%>
    <%--</button>--%>

    <%--</div>--%>
    <%--</div>--%>

    <%--<div class="panel" style="display: none" id="fromRequestsPanel">--%>
    <%--<div class="header">--%>
    <%--<s:message code="cmas.face.client.social.friendRequestsFrom.header"/>--%>
    <%--</div>--%>
    <%--<div id="fromRequests">--%>
    <%--</div>--%>
    <%--</div>--%>

    <%--<div class="panel">--%>
    <%--<div class="header">--%>
    <%--<s:message code="cmas.face.client.social.newsfeed.header"/>--%>
    <%--</div>--%>
    <%--<div class="panel-row panel-row-bottom-margin">--%>
    <%--<input type="checkbox" id="addLocationCountryToNewsFeed"--%>
    <%--<c:if test="${diver.newsFromCurrentLocation}">checked="checked"</c:if>--%>
    <%--/>--%>
    <%--<span class="text"><s:message code="cmas.face.client.social.newfeed.currentLocation"/></span>--%>
    <%--</div>--%>
    <%--<div id="newsCountries">--%>
    <%--</div>--%>
    <%--<div class="panel-row">--%>
    <%--<div class="button-container">--%>
    <%--<button class="centerUserInfo-button reg-button" id="addCountryButton">--%>
    <%--<s:message code="cmas.face.client.social.addCountry"/>--%>
    <%--</button>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>

    <%--<div class="content-left" id="diversList" style="display: none">--%>
    <%--<div id="diversListContent">--%>

    <%--</div>--%>
    <%--<div class="panel">--%>
    <%--<div class="button-container">--%>
    <%--<button class="form-button enter-button" id="newDiverSearch">--%>
    <%--<s:message code="cmas.face.diverList.submitText"/>--%>
    <%--</button>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>

    <%--<div class="content-right" id="accountFeed"></div>--%>

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
        <div><s:message code="cmas.face.diverList.noDivers"/></div>
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
               title="cmas.face.showDiver.title">
    </my:dialog>

    <div id="showLogbookEntry" class="logbookEntry" style="display: none">
        <img id="showLogbookEntryClose" src="${pageContext.request.contextPath}/i/close.png?v=${webVersion}"
             class="dialogClose"/>

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
            <form id="userpicFileForm">
                <input id="userpicFileInput" name="userpicFileInput" type="file" accept="image/*">
            </form>
            <img id="userpicPreview" class="userpicPreview"
                 src="${pageContext.request.contextPath}/i/no_img.png?v=${webVersion}"/>
        </div>
        <div class="error" id="selectUserpic_error_file"></div>
        <div class="dialog-form-row" id="cameraSelect">
            <img src="${pageContext.request.contextPath}/i/photo_ico_gray.png?v=${webVersion}"/>
            <a href="#">
                <s:message code="cmas.face.client.profile.selectUserpic.camera"/>
            </a>
        </div>
        <div class="dialog-form-row" id="fileFromDiscSelect">
            <img src="${pageContext.request.contextPath}/i/mobile_ico_gray.png?v=${webVersion}"/>
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

    <my:dialog id="friendRequestSuccessDialog"
               title="cmas.face.friendRequest.success"
               buttonText="cmas.face.friendRequest.success.button">
    </my:dialog>

</my:securepage>


