<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>


<my:securepage title="cmas.face.index.header"
               customScripts="js/model/util_model.js,js/model/logbook_feed_model.js,js/model/fast_search_friends_model.js,js/controller/util_controller.js,js/controller/country_controller.js,js/controller/logbook_feed_controller.js,js/controller/logbook_search_controller.js,js/controller/logbook_controller.js,js/controller/fast_search_friends_controller.js"
>

    <%--<div class="content">--%>
        <%--<div class="tabs clearfix">--%>
            <%--<span class="firstTab" id="myTab"><s:message code="cmas.face.logbook.tab.me"/></span>--%>
            <%--<span class="secondTab inactive" id="friendsTab">--%>
                <%--<s:message code="cmas.face.logbook.tab.friends"/>--%>
            <%--</span>--%>
        <%--</div>--%>
    <%--</div>--%>

    <div id="myLogbook" class="content-left content-large">
        <div class="panel-header">
            <span class="header2-text"><s:message code="cmas.face.client.profile.mylogbook"/></span>
            <b id="createLogbookEntryButton" onclick="window.location='/secure/createLogbookRecordForm.html'">
                <div class="add-button logbook-button-right"></div>
                <label class="logbook-form-checkbox-label logbook-button-right">
                    <s:message code="cmas.face.logbook.addLogbookEntry"/>
                </label>
            </b>
            <%--<form id="logbookEntrySearchForm" action="">--%>
                <%--<div class="search-form-logbook">--%>
                    <%--<label><s:message code="cmas.face.logbook.diveDate"/>:</label>--%>
                    <%--<div class="form-row-logbook clearfix">--%>
                        <%--<div class="form-row-logbook-half">--%>
                            <%--<label class="form-row-logbook-half-label" for="fromDate"><s:message--%>
                                    <%--code="cmas.face.logbook.search.from"/></label>--%>
                            <%--<div class="calendar-container">--%>
                                <%--<img class="calendar-input-ico-logbook">--%>
                                <%--<input name="fromDate" id="fromDate" class="calendar-container-input"/>--%>
                            <%--</div>--%>
                            <%--<label class="error" id="search_error_fromDate"></label>--%>
                        <%--</div>--%>
                        <%--<div class="form-row-logbook-half">--%>
                            <%--<div class="calendar-container">--%>
                                <%--<img class="calendar-input-ico-logbook2">--%>
                                <%--<input class="form-row-logbook-half-input2 calendar-container-input" name="toDate"--%>
                                       <%--id="toDate"/>--%>
                            <%--</div>--%>
                            <%--<label class="form-row-logbook-half-label2">--%>
                                <%--<s:message code="cmas.face.logbook.search.to"/>--%>
                            <%--</label>--%>
                            <%--<label class="error" id="search_error_toDate"></label>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<label><s:message code="cmas.face.logbook.search.country"/>:</label>--%>
                    <%--<div class="form-row-logbook">--%>
                        <%--<select name="country" id="country" style="width: 100%" size=1--%>
                                <%--onChange="">--%>
                            <%--<c:forEach items="${countries}" var="country">--%>
                                <%--<option value='${country.code}'>${country.name}</option>--%>
                            <%--</c:forEach>--%>
                        <%--</select>--%>
                    <%--</div>--%>
                    <%--<label><s:message code="cmas.face.logbook.depth.search"/>:</label>--%>
                    <%--<div class="form-row-logbook clearfix">--%>
                        <%--<div class="form-row-logbook-half">--%>
                            <%--<label class="form-row-logbook-half-label"><s:message--%>
                                    <%--code="cmas.face.logbook.search.from"/></label>--%>
                            <%--<input type="text" name="fromMeters" id="fromMeters" class="form-row-logbook-input"--%>
                                   <%--placeholder="<s:message code="cmas.face.logbook.depth.placeholder"/>"/>--%>
                            <%--<label class="error" id="search_error_fromMeters"></label>--%>
                        <%--</div>--%>
                        <%--<div class="form-row-logbook-half">--%>
                            <%--<input type="text" name="toMeters" id="toMeters"--%>
                                   <%--class="form-row-logbook-input form-row-logbook-half-input2"--%>
                                   <%--placeholder="<s:message code="cmas.face.logbook.depth.placeholder"/>"/>--%>
                            <%--<label class="form-row-logbook-half-label2">--%>
                                <%--<s:message code="cmas.face.logbook.search.to"/>--%>
                            <%--</label>--%>
                            <%--<label class="error" id="search_error_toMeters"></label>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<label><s:message code="cmas.face.logbook.duration"/>:</label>--%>
                    <%--<div class="form-row-logbook clearfix">--%>
                        <%--<div class="form-row-logbook-half">--%>
                            <%--<label class="form-row-logbook-half-label">--%>
                                <%--<s:message code="cmas.face.logbook.search.from"/>--%>
                            <%--</label>--%>
                            <%--<input type="text" name="fromMinutes" id="fromMinutes" class="form-row-logbook-input"--%>
                                   <%--placeholder="<s:message code="cmas.face.logbook.duration.placeholder"/>">--%>
                            <%--<label class="error" id="search_error_fromMinutes"></label>--%>
                        <%--</div>--%>
                        <%--<div class="form-row-logbook-half">--%>
                            <%--<input type="text" name="toMinutes" id="toMinutes"--%>
                                   <%--class="form-row-logbook-input form-row-logbook-half-input2"--%>
                                   <%--placeholder="<s:message code="cmas.face.logbook.duration.placeholder"/>">--%>
                            <%--<label class="form-row-logbook-half-label2">--%>
                                <%--<s:message code="cmas.face.logbook.search.to"/>--%>
                            <%--</label>--%>
                            <%--<label class="error" id="search_error_toMinutes"></label>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="">--%>
                    <%--<button id="searchRecords" type="button" class="form-button enter-button">--%>
                        <%--<s:message code="cmas.face.logbook.search.buttonText"/>--%>
                    <%--</button>--%>
                <%--</div>--%>
            <%--</form>--%>

        </div>
        <div class="myLogbookFeed" id="myLogbookFeed"></div>
    </div>

    <div id="friendsLogbook" style="display: none">
        <div class="content-left">
            <div id="friendsLogbookFeed" class="clearfix"></div>
        </div>
        <div class="content-left">
            <form name="searchFriends">
                <div class="search-friend-form-logbook">
                    <input type="text" id="searchFriendInput"
                           placeholder="<s:message code="cmas.face.diver.fast.search.placeholderText"/>">
                    <div id="foundFriendList" class="foundFriendList">
                        <div id="foundFriendListContent"></div>
                        <div id="noDiversFoundMessage" style="display: none">
                        <span class="foundFriendList-text">
                            <s:message code="cmas.face.diver.fast.search.notFound"/>
                        </span>
                        </div>
                    </div>
                    <div>
                        <label class="error" id="searchFriends_error_input"></label>
                    </div>
                    <div>
                        <button type="button" class="form-button enter-button searchFriendsButton"
                                id="searchFriendsButton">
                            <s:message code="cmas.face.diver.fast.search.buttonText"/>
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <my:dialog id="recordDeleteDialog"
               title="cmas.face.logbook.delete.title"
               buttonText="cmas.face.logbook.delete.ok">
        <div class="dialog-form-row"><s:message code="cmas.face.logbook.delete.question"/></div>
    </my:dialog>

    <my:dialog id="showDiver"
               title="cmas.face.showDiver.title"
               buttonText="cmas.face.showDiver.submitText">
    </my:dialog>

    <my:dialog id="friendRequestSuccessDialog"
               title="cmas.face.friendRequest.success"
               buttonText="cmas.face.friendRequest.success.button">
    </my:dialog>

</my:securepage>


