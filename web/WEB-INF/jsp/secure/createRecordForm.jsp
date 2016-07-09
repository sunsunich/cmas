<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<jsp:useBean id="spot" scope="request" type="org.cmas.entities.divespot.DiveSpot"/>
<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="visibilityTypes" scope="request" type="org.cmas.entities.logbook.LogbookVisibility[]"/>

<my:securepage title="cmas.face.index.header"
               customScripts="js/model/logbook_model.js,js/controller/country_controller.js,js/controller/logbook_controller.js">

    <script type="application/javascript">
        labels["PRIVATE"] = '<s:message code="cmas.face.client.social.logbook.private"/>';
        labels["FRIENDS"] = '<s:message code="cmas.face.client.social.logbook.friends"/>';
        labels["PUBLIC"] = '<s:message code="cmas.face.client.social.logbook.public"/>';
        var visibilityTypes = [];
        <c:forEach items="${visibilityTypes}" var="visibilityType" varStatus="st">
        visibilityTypes[${st.index}] = '${visibilityType.name}';
        </c:forEach>
        var logbookVisibility = "${diver.defaultVisibility}";
        logbook_model.spotId = "${spot.id}";
    </script>

    <c:choose>
        <c:when test="${diver.photo == null}">
            <c:set var="userpicSrc" value="/i/no_img.png"/>
        </c:when>
        <c:otherwise>
            <c:set var="userpicSrc" value="data:image/png;base64,${diver.photo}"/>
        </c:otherwise>
    </c:choose>
    <div id="createLogbookEntry" class="white-form">

        <p class="dialog-title"><s:message code="cmas.face.logbook.create.title"/></p>

        <div class="white-form-content" id="createLogbookEntryContent">
            <div class="white-form-row">
                <label class="white-form-label"><s:message code="cmas.face.logbook.spotLabel"/>: </label>
                <span class="white-form-text">${spot.name}, ${spot.country.name}</span>
            </div>

            <div class="white-form-row clearfix">
                <div class="diverList-elem-left">
                    <img id="userpic" src="${userpicSrc}"/>
                </div>
                <div class="diverList-elem-right">
                    <p><span>${diver.firstName} ${diver.lastName}</span></p>
                </div>
            </div>

            <div class="date-form-row">
                <img class="date-form-calendar-input-ico">
                <input id="diveDate" type="text"
                       placeholder="<s:message code="cmas.face.logbook.diveDate"/>"/>
            </div>
            <div class="error" id="create_error_diveDate"></div>

            <div class="white-form-row">
                <select name="visibility" id="visibility" style="width: 100%" size=1 onChange="">
                </select>
            </div>
            <div class="error" id="create_error_visibility"></div>

            <div class="white-form-row center">
                <a class="white-form-href link" id="viewBuddies" href="#"><s:message
                        code="cmas.face.logbook.viewBuddies"/></a>
            </div>

            <div class="white-form-row center">
                <a class="white-form-href link" id="addInstructor" href="#"><s:message
                        code="cmas.face.logbook.addInstructor"/></a>
            </div>

            <div class="white-form-row">
                <textarea class="white-form-textarea" id="comments" rows="6"
                          placeholder="<s:message code="cmas.face.logbook.comments"/>"></textarea>
            </div>

            <div class="white-form-row clearfix">
                <div class="diverList-elem-left">
                    <input id="photoFileInput" name="photoFileInput" type="file" accept="image/*" style="display: none">
                    <img id="logbookPic" class="userpicPreview" src="${pageContext.request.contextPath}/i/no_img.png"/>
                </div>
                <div class="diverList-elem-right" id="fileFromDiscSelect">
                    <img src="${pageContext.request.contextPath}/i/photo_ico_gray.png"/>
                    <a href="#" class="white-form-href link">
                        <s:message code="cmas.face.logbook.selectPic"/>
                    </a>
                </div>
            </div>
            <div class="error" id="create_error_photo"></div>

            <div class="specifications" id="specifications">
                <div class="header">
                    <span><s:message code="cmas.face.logbook.specTitle"/></span>
                </div>
                <div class="white-form-row">
                    <input id="duration" type="text" placeholder="<s:message code="cmas.face.logbook.duration"/>"/>
                </div>
                <div class="error" id="create_error_duration"></div>
                <div class="white-form-row">
                    <input id="depth" type="text" placeholder="<s:message code="cmas.face.logbook.depth"/>"/>
                </div>
                <div class="error" id="create_error_depth"></div>
            </div>

            <div class="white-form-row">
                <label class="white-form-label"><s:message code="cmas.face.logbook.scoreLabel"/></label>
            </div>
            <div class="white-form-row">
                <img src="${pageContext.request.contextPath}/i/heart-empty.png" class="diveScore" id="diveScore_1"/>
                <img src="${pageContext.request.contextPath}/i/heart-empty.png" class="diveScore" id="diveScore_2"/>
                <img src="${pageContext.request.contextPath}/i/heart-empty.png" class="diveScore" id="diveScore_3"/>
                <img src="${pageContext.request.contextPath}/i/heart-empty.png" class="diveScore" id="diveScore_4"/>
                <img src="${pageContext.request.contextPath}/i/heart-empty.png" class="diveScore" id="diveScore_5"/>
            </div>
            <div class="button-container">
                <button class="form-button enter-button" id="createLogbookEntryButton">
                    <s:message code="cmas.face.logbook.create"/>
                </button>
            </div>
        </div>
    </div>

    <div class="content" id="diversList" style="display: none">
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

</my:securepage>

