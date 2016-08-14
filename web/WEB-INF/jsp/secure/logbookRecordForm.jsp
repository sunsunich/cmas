<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<jsp:useBean id="spot" scope="request" type="org.cmas.entities.divespot.DiveSpot"/>
<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="federationCountries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="visibilityTypes" scope="request" type="org.cmas.entities.logbook.LogbookVisibility[]"/>

<my:securepage title="cmas.face.index.header"
               customScripts="js/model/util_model.js,js/model/logbook_record_model.js,js/controller/util_controller.js,js/controller/country_controller.js,js/controller/logbook_record_controller.js">

    <script type="application/javascript">
        var visibilityTypes = [];
        <c:forEach items="${visibilityTypes}" var="visibilityType" varStatus="st">
        visibilityTypes[${st.index}] = '${visibilityType.name}';
        </c:forEach>
        var logbookVisibility = "${diver.defaultVisibility}";
        logbook_record_model.diverId = "${diver.id}";
        logbook_record_model.spotId = "${spot.id}";
        <c:choose>
        <c:when test="${logbookEntry == null}">
        <c:if test="${diver.addFriendsToLogbookEntries}">
        <c:forEach items="${diver.friends}" var="buddie">
        logbook_record_model.buddiesIds.push("${buddie.id}");
        </c:forEach>
        </c:if>
        </c:when>
        <c:otherwise>
        logbook_record_model.logbookEntryId = "${logbookEntry.id}";
        logbookVisibility = "${logbookEntry.visibility}";
        <c:if test="${logbookEntry.buddies != null}">
        logbook_record_model.buddiesIds = [
            <c:forEach var="buddie" items="${logbookEntry.buddies}">
            "${buddie.id}",
            </c:forEach>
        ];
        </c:if>
        <c:if test="${logbookEntry.instructor != null}">
        logbook_record_model.instructorId = "${logbookEntry.instructor.id}";
        </c:if>
        logbook_record_model.score = "${logbookEntry.score.name}";
        switch (logbook_record_model.score) {
            case "ZERO_STAR" :
                logbook_record_model.scoreInt = 0;
                break;
            case "ONE_STAR" :
                logbook_record_model.scoreInt = 1;
                break;
            case "TWO_STAR" :
                logbook_record_model.scoreInt = 2;
                break;
            case "THREE_STAR" :
                logbook_record_model.scoreInt = 3;
                break;
            case "FOUR_STAR" :
                logbook_record_model.scoreInt = 4;
                break;
            case "FIVE_STAR" :
                logbook_record_model.scoreInt = 5;
                break;
        }
        <c:forEach items="${logbookEntry.buddies}" var="buddie">
        logbook_record_model.buddiesIds.push("${buddie.id}");
        </c:forEach>
        </c:otherwise>
        </c:choose>
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

        <div class="dialog-title"><s:message code="cmas.face.logbook.create.title"/></div>

        <div class="white-form-content" id="createLogbookEntryContent">
            <div class="white-form-row">
                <label class="white-form-label"><s:message code="cmas.face.logbook.spotLabel"/>: </label>
                <span class="white-form-text">${spot.name}, ${spot.country.name}</span>
            </div>

            <div class="white-form-row clearfix">
                <div class="diverList-elem-left">
                    <img class="userpic" src="${userpicSrc}"/>
                </div>
                <div class="diverList-elem-right">
                    <p><span>${diver.firstName} ${diver.lastName}</span></p>
                </div>
            </div>

            <div class="date-form-row">
                <img class="date-form-calendar-input-ico">
                <input id="diveDate" type="text"
                       placeholder="<s:message code="cmas.face.logbook.diveDate"/>"
                        <c:if test="${logbookEntry != null}">
                            value="<fmt:formatDate value="${logbookEntry.diveDate}" pattern="dd/MM/yyyy"/>"
                        </c:if>
                        />
            </div>
            <div class="error" id="create_error_diveDate"></div>

            <div class="white-form-row">
                <select name="visibility" id="visibility" style="width: 100%" size=1 onChange="">
                </select>
            </div>
            <div class="error" id="create_error_visibility"></div>

            <div class="white-form-row" id="buddies">
                <div class="dialog-sub-title">
                    <s:message code="cmas.face.buddies.title"/>
                </div>
                <div id="buddiesList"></div>
            </div>
            <div class="white-form-row center">
                <a class="white-form-href link" id="addBuddies" href="#">
                    <s:message code="cmas.face.logbook.addBuddies"/>
                </a>
            </div>

            <div class="white-form-row" id="instructor">
                <div class="dialog-sub-title">
                    <s:message code="cmas.face.instructor.title"/>
                </div>
                <div id="instructorContainer"></div>
            </div>
            <div class="white-form-row center">
                <a class="white-form-href link" id="addInstructor" href="#">
                    <s:message code="cmas.face.logbook.addInstructor"/>
                </a>
            </div>

            <div class="white-form-row">
                <c:choose>
                    <c:when test="${logbookEntry == null}">
                        <textarea class="white-form-textarea" id="comments" rows="6"
                                  placeholder="<s:message code="cmas.face.logbook.comments"/>"></textarea>
                    </c:when>
                    <c:otherwise>
                <textarea class="white-form-textarea" id="comments" rows="6"
                          placeholder="<s:message code="cmas.face.logbook.comments"/>">${logbookEntry.note}</textarea>
                    </c:otherwise>
                </c:choose>
            </div>

            <c:choose>
                <c:when test="${logbookEntry == null}">
                    <c:set var="photoSrc" value="${pageContext.request.contextPath}/i/no_img.png"/>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${logbookEntry.photoBase64 == null}">
                            <c:set var="photoSrc" value="${pageContext.request.contextPath}/i/no_img.png"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="photoSrc" value="data:image/png;base64,${logbookEntry.photoBase64}"/>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>

            <div class="white-form-row clearfix">
                <div class="diverList-elem-left">
                    <input id="photoFileInput" name="photoFileInput" type="file" accept="image/*" style="display: none">
                    <img id="logbookPic" class="userpicPreview" src="${photoSrc}"/>
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
                    <input id="duration" type="text" placeholder="<s:message code="cmas.face.logbook.duration"/>"
                            <c:if test="${logbookEntry != null}">
                                value="${logbookEntry.durationMinutes}"
                            </c:if>
                            />
                </div>
                <div class="error" id="create_error_duration"></div>
                <div class="white-form-row">
                    <input id="depth" type="text" placeholder="<s:message code="cmas.face.logbook.depth"/>"
                            <c:if test="${logbookEntry != null}">
                                value="${logbookEntry.depthMeters}"
                            </c:if>
                            />
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
        <div id="findDiverByNameForm">
            <div class="horizontal-radio-group clearfix" id="findDiverTypeChoose">
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
        </div>
        <div id="findDiverByFederationCardNumberForm" style="display: none">
            <div class="dialog-form-row">
                <select name="findDiverFederationCountry" id="findDiverFederationCountry" style="width: 100%" size=1
                        onChange="">
                    <c:forEach items="${federationCountries}" var="country">
                        <option value='${country.code}'>${country.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="error" id="findDiver_error_federationCountry"></div>
            <div class="dialog-form-row">
                <input id="findDiverFederationCardNumber" type="text"
                       placeholder="<s:message code="cmas.face.findDiver.form.label.federationCardNumber"/>"/>
            </div>
            <div class="error" id="findDiver_error_federationCardNumber"></div>
        </div>
        <div id="findDiverByCmasCardForm" style="display: none">
            <div class="dialog-form-row">
                <input id="findDiverCmasCardNumber" type="text"
                       placeholder="<s:message code="cmas.face.findDiver.form.label.cmasCardNumber"/>"/>
            </div>
            <div class="error" id="findDiver_error_cmasCardNumber"></div>
        </div>
        <div class="error" style="display: none" id="findDiver_error">
        </div>
        <div class="button-container">
            <button class="longText-button reg-button" id="searchByName" style="display: none">
                <s:message code="cmas.face.findDiver.searchByName"/>
            </button>
            <button class="longText-button reg-button" id="searchByFedNumber">
                <s:message code="cmas.face.findDiver.searchByFedNumber"/>
            </button>
            <button class="longText-button reg-button" id="searchByCmasCard">
                <s:message code="cmas.face.findDiver.searchByCmasCard"/>
            </button>
        </div>
    </my:dialog>

    <my:dialog id="noDiversFound"
               title="cmas.face.diverList.title"
               buttonText="cmas.face.diverList.submitText">
        <div id="noDiversFoundText"><s:message code="cmas.face.diverList.noDivers"/></div>
    </my:dialog>

    <my:dialog id="showDiver"
               title="cmas.face.showDiver.title"
               buttonText="cmas.face.showDiver.submitText">
    </my:dialog>

</my:securepage>

