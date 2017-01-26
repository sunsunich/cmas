<%--suppress ALL --%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="federationCountries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>

<jsp:useBean id="visibilityTypes" scope="request" type="org.cmas.entities.logbook.LogbookVisibility[]"/>

<jsp:useBean id="weatherTypes" scope="request" type="org.cmas.entities.logbook.WeatherType[]"/>
<jsp:useBean id="surfaceTypes" scope="request" type="org.cmas.entities.logbook.SurfaceType[]"/>
<jsp:useBean id="currentTypes" scope="request" type="org.cmas.entities.logbook.CurrentType[]"/>
<jsp:useBean id="underWaterVisibilityTypes" scope="request"
             type="org.cmas.entities.logbook.UnderWaterVisibilityType[]"/>
<jsp:useBean id="waterTypes" scope="request" type="org.cmas.entities.logbook.WaterType[]"/>

<jsp:useBean id="temperatureMeasureUnits" scope="request" type="org.cmas.entities.logbook.TemperatureMeasureUnit[]"/>
<jsp:useBean id="divePurposeTypes" scope="request" type="org.cmas.entities.logbook.DivePurposeType[]"/>
<jsp:useBean id="entryTypes" scope="request" type="org.cmas.entities.logbook.EntryType[]"/>
<jsp:useBean id="diveSuitTypes" scope="request" type="org.cmas.entities.logbook.DiveSuitType[]"/>

<jsp:useBean id="volumeMeasureUnits" scope="request" type="org.cmas.entities.logbook.VolumeMeasureUnit[]"/>
<jsp:useBean id="pressureMeasureUnits" scope="request" type="org.cmas.entities.logbook.PressureMeasureUnit[]"/>
<jsp:useBean id="supplyTypes" scope="request" type="org.cmas.entities.logbook.TankSupplyType[]"/>

<my:securepage title="cmas.face.index.header"
               customScripts="js/model/util_model.js,js/model/logbook_record_model.js,js/controller/util_controller.js,js/controller/country_controller.js,js/controller/logbook_record_controller.js,js/controller/logbook_record_diveProfile_controller.js,js/controller/logbook_record_publish_controller.js">

    <script type="application/javascript">
        <my:enumToJs enumItems="${visibilityTypes}" arrayVarName="logbook_record_model.visibilityTypes"/>
        logbook_record_model.logbookEntry.visibility = "${diver.defaultVisibility}";

        <my:enumToJs enumItems="${weatherTypes}" arrayVarName="logbook_record_model.weatherTypes"/>
        <my:enumToJs enumItems="${surfaceTypes}" arrayVarName="logbook_record_model.surfaceTypes"/>
        <my:enumToJs enumItems="${currentTypes}" arrayVarName="logbook_record_model.currentTypes"/>
        <my:enumToJs enumItems="${underWaterVisibilityTypes}" arrayVarName="logbook_record_model.underWaterVisibilityTypes"/>
        <my:enumToJs enumItems="${waterTypes}" arrayVarName="logbook_record_model.waterTypes"/>

        <my:enumToJs enumItems="${temperatureMeasureUnits}" arrayVarName="logbook_record_model.temperatureMeasureUnits"/>
        <my:enumToJs enumItems="${divePurposeTypes}" arrayVarName="logbook_record_model.divePurposeTypes"/>
        <my:enumToJs enumItems="${entryTypes}" arrayVarName="logbook_record_model.entryTypes"/>
        <my:enumToJs enumItems="${diveSuitTypes}" arrayVarName="logbook_record_model.diveSuitTypes"/>

        <my:enumToJs enumItems="${volumeMeasureUnits}" arrayVarName="logbook_record_model.volumeMeasureUnits"/>
        <my:enumToJs enumItems="${pressureMeasureUnits}" arrayVarName="logbook_record_model.pressureMeasureUnits"/>
        <my:enumToJs enumItems="${supplyTypes}" arrayVarName="logbook_record_model.supplyTypes"/>

        logbook_record_model.diverId = "${diver.id}";

        <c:choose>
        <c:when test="${logbookEntry == null}">
        <c:if test="${diver.addFriendsToLogbookEntries}">
        <c:forEach items="${diver.friends}" var="buddie">
        logbook_record_model.buddiesIds.push("${buddie.id}");
        </c:forEach>
        </c:if>
        </c:when>
        <c:otherwise>
        logbook_record_model.logbookEntry = JSON.parse('${logbookEntryJson}');
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
        </c:otherwise>
        </c:choose>

        <c:if test="${spot != null}">
        logbook_record_model.logbookEntry.diveSpot = JSON.parse('${spotJson}');
        </c:if>
    </script>

    <div id="createLogbookEntry">

        <div class="createEntryHeader">
            <div class="createEntryHeaderTitle"><s:message code="cmas.face.logbook.create.title"/></div>
            <div class="content-left">
                <div id="tabs" class="tabs clearfix">
                <span class="firstTab" id="diveProfileTab">
                    <s:message code="cmas.face.logbook.diveProfile"/>
                </span>
                <span class="secondTab" id="publishTab" class="inactive">
                    <s:message code="cmas.face.logbook.publish"/>
                </span>
                </div>
            </div>
        </div>
        <div id="diveProfile" class="clearfix">
            <div class="white-form-left">
                <div class="white-form-row white-form-top-row clearfix">
                    <div class="white-form-row-date">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.diveDate"/></label>
                        <img class="date-form-calendar-input-ico white-form-row-img">
                        <input class="white-form-date-input" id="diveDate" type="text"
                               placeholder="<s:message code="cmas.face.logbook.diveDate"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="<fmt:formatDate value="${logbookEntry.diveDate}" pattern="dd/MM/yyyy"/>"
                                </c:if>
                                />
                        <label class="error" id="create_error_diveDate"></label>
                    </div>

                    <div class="white-form-row-time">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.diveTime"/></label>
                        <input class="white-form-time-input" id="diveTime" type="text"
                               placeholder="<s:message code="cmas.face.logbook.diveTime"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="<fmt:formatDate value="${logbookEntry.diveDate}" pattern="HH:mm"/>"
                                </c:if>
                                />
                        <label class="error" id="create_error_diveTime"></label>
                    </div>

                </div>

                <div class="white-form-row clearfix">
                    <div class="white-form-row-date">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.prevDiveDate"/></label>
                        <img class="date-form-calendar-input-ico white-form-row-img">
                        <input class="white-form-date-input" id="prevDiveDate" type="text"
                               placeholder="<s:message code="cmas.face.logbook.prevDiveDate"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="<fmt:formatDate value="${logbookEntry.prevDiveDate}" pattern="dd/MM/yyyy"/>"
                                </c:if>
                                />
                        <label class="error" id="create_error_prevDiveDate"></label>
                    </div>
                    <div class="white-form-row-time">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.prevDiveTime"/></label>
                        <input class="white-form-time-input" id="prevDiveTime" type="text"
                               placeholder="<s:message code="cmas.face.logbook.prevDiveTime"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="<fmt:formatDate value="${logbookEntry.prevDiveDate}" pattern="HH:mm"/>"
                                </c:if>
                                />
                        <label class="error" id="create_error_prevDiveTime"></label>
                    </div>
                </div>

                <div class="form-row center">
                    <a class="white-form-href link" id="chooseSpot" href="#">
                        <s:message code="cmas.face.spots.chooseSpot"/>
                    </a>
                </div>

                <div class="white-form-row">
                    <label class="white-form-label"><s:message code="cmas.face.logbook.spotName"/></label>
                    <input class="white-form-row-input" id="spotName" type="text"
                           placeholder="<s:message code="cmas.face.logbook.spotName"/>"
                            <c:choose>
                                <c:when test="${spot == null}">
                                    <c:if test="${logbookEntry != null}">
                                        value="${logbookEntry.name}"
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    value="${spot.name}"
                                </c:otherwise>
                            </c:choose>
                            />
                    <label class="error" id="create_error_name"></label>
                </div>

                <div class="white-form-row clearfix">
                    <div class="white-form-row-half">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.latitude"/></label>
                        <input class="white-form-row-input" id="latitude" type="text"
                               placeholder="<s:message code="cmas.face.logbook.latitude"/>"
                                <c:choose>
                                    <c:when test="${spot == null}">
                                        <c:if test="${logbookEntry != null}">
                                            value="${logbookEntry.latitude}"
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        value="${spot.latitude}"
                                    </c:otherwise>
                                </c:choose>
                                />
                        <label class="error" id="create_error_latitude"></label>
                    </div>
                    <div class="white-form-row-half-right">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.latitude"/></label>
                        <input class="white-form-row-input" id="longitude" type="text"
                               placeholder="<s:message code="cmas.face.logbook.longitude"/>"
                                <c:choose>
                                    <c:when test="${spot == null}">
                                        <c:if test="${logbookEntry != null}">
                                            value="${logbookEntry.longitude}"
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        value="${spot.longitude}"
                                    </c:otherwise>
                                </c:choose>
                                />
                        <label class="error" id="create_error_longitude"></label>
                    </div>
                </div>

                <div class="white-form-row clearfix">
                    <div class="white-form-row-third">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.duration"/></label>
                        <input class="white-form-row-input" id="duration" type="text"
                               placeholder="<s:message code="cmas.face.logbook.duration.placeholder"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="${logbookEntry.diveSpec.durationMinutes}"
                                </c:if>
                                />
                        <label class="error" id="create_error_durationMinutes"></label>
                    </div>
                    <div class="white-form-row-third">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.depth"/></label>
                        <input class="white-form-row-input" id="depth" type="text"
                               placeholder="<s:message code="cmas.face.logbook.depth.placeholder"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="${logbookEntry.diveSpec.maxDepthMeters}"
                                </c:if>
                                />
                        <label class="error" id="create_error_maxDepthMeters"></label>
                    </div>
                    <div class="white-form-row-third-right">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.avgDepth"/></label>
                        <input class="white-form-row-input" id="avgDepth" type="text"
                               placeholder="<s:message code="cmas.face.logbook.avgDepth.placeholder"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="${logbookEntry.diveSpec.avgDepthMeters}"
                                </c:if>
                                />
                        <label class="error" id="create_error_avgDepthMeters"></label>
                    </div>
                </div>

                <div class="white-form-row white-form-top-row clearfix">
                    <div class="white-form-row-third">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.weather"/></label>
                        <select name="weather" id="weather" style="width: 100%" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_weather"></label>
                    </div>
                    <div class="white-form-row-third">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.surface"/></label>
                        <select name="surface" id="surface" style="width: 100%" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_surface"></label>
                    </div>
                    <div class="white-form-row-third-right">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.waterType"/></label>
                        <select name="waterType" id="waterType" style="width: 100%" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_current"></label>
                    </div>
                </div>

                <div class="white-form-row clearfix">
                    <div class="white-form-row-half">
                        <label class="white-form-label">
                            <s:message code="cmas.face.logbook.underWaterVisibility"/>
                        </label>
                        <select name="underWaterVisibility" id="underWaterVisibility" style="width: 100%" size=1
                                onChange="">
                        </select>
                        <label class="error" id="create_error_underWaterVisibility"></label>
                    </div>
                    <div class="white-form-row-half-right">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.current"/></label>
                        <select name="current" id="current" style="width: 100%" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_waterType"></label>
                    </div>
                </div>

                <div class="white-form-row clearfix">
                    <div class="white-form-row-third">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.airTemp"/></label>
                        <input class="white-form-row-input" id="airTemp" type="text"
                               placeholder="<s:message code="cmas.face.logbook.airTemp"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="${logbookEntry.diveSpec.airTemp}"
                                </c:if>
                                />
                        <label class="error" id="create_error_airTemp"></label>
                    </div>
                    <div class="white-form-row-third">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.waterTemp"/></label>
                        <input class="white-form-row-input" id="waterTemp" type="text"
                               placeholder="<s:message code="cmas.face.logbook.waterTemp"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="${logbookEntry.diveSpec.waterTemp}"
                                </c:if>
                                />
                        <label class="error" id="create_error_waterTemp"></label>
                    </div>
                    <div class="white-form-row-third-right">
                        <label class="white-form-label"><s:message
                                code="cmas.face.logbook.temperatureMeasureUnit"/></label>
                        <select name="temperatureMeasureUnit" id="temperatureMeasureUnit" style="width: 100%" size=1
                                onChange="">
                        </select>
                        <label class="error" id="create_error_temperatureMeasureUnit"></label>
                    </div>
                </div>

                <div class="white-form-row white-form-top-row clearfix">
                    <div class="white-form-row-half">
                        <label class="white-form-label">
                            <s:message code="cmas.face.logbook.divePurpose"/>
                        </label>
                        <select name="divePurpose" id="divePurpose" style="width: 100%" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_divePurpose"></label>
                    </div>
                    <div class="white-form-row-half-right">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.entryType"/></label>
                        <select name="entryType" id="entryType" style="width: 100%" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_entryType"></label>
                    </div>
                </div>

                <div class="white-form-row clearfix">
                    <div class="white-form-row-half">
                        <label class="white-form-label">
                            <s:message code="cmas.face.logbook.addWeight"/>
                        </label>
                        <input class="white-form-row-input" id="addWeight" type="text"
                               placeholder="<s:message code="cmas.face.logbook.addWeight"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="${logbookEntry.diveSpec.additionalWeightKg}"
                                </c:if>
                                />
                        <label class="error" id="create_error_additionalWeightKg"></label>
                    </div>
                    <div class="white-form-row-half-right">
                        <label class="white-form-label"><s:message code="cmas.face.logbook.diveSuit"/></label>
                        <select name="diveSuit" id="diveSuit" style="width: 100%" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_diveSuit"></label>
                    </div>
                </div>

                <div class="white-form-row">
                    <input id="hasSafetyStop" type="checkbox"
                            <c:if test="${logbookEntry != null}">
                                <c:if test="${logbookEntry.diveSpec.hasSafetyStop}">checked="checked"</c:if>
                            </c:if>
                            />
                    <label class="white-form-checkbox-label">
                        <s:message code="cmas.face.logbook.hasSafetyStop"/>
                    </label>
                </div>
                <div class="white-form-row">
                    <label class="white-form-label"><s:message code="cmas.face.logbook.cnsToxicity"/></label>
                    <input class="white-form-row-input" id="cnsToxicity" type="text"
                           placeholder="<s:message code="cmas.face.logbook.cnsToxicity"/>"
                            <c:if test="${logbookEntry != null}">
                                value="${logbookEntry.diveSpec.cnsToxicity}"
                            </c:if>
                            />
                    <label class="error" id="create_error_cnsToxicity"></label>
                </div>

                <div class="white-form-row">
                    <label class="white-form-label"><s:message code="cmas.face.logbook.decoStepsComments"/></label>
                    <c:choose>
                        <c:when test="${logbookEntry == null}">
                        <textarea class="white-form-textarea" id="decoStepsComments" rows="6"
                                  placeholder="<s:message code="cmas.face.logbook.decoStepsComments"/>"></textarea>
                        </c:when>
                        <c:otherwise>
                <textarea class="white-form-textarea" id="decoStepsComments" rows="6"
                          placeholder="<s:message code="cmas.face.logbook.decoStepsComments"/>">${logbookEntry.diveSpec.decoStepsComments}</textarea>
                        </c:otherwise>
                    </c:choose>
                    <label class="error" id="create_error_decoStepsComments"></label>
                </div>


                <div class="white-form-row">
                    <label class="white-form-label"><s:message code="cmas.face.logbook.comments"/></label>
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
                    <label class="error" id="create_error_note"></label>
                </div>

                <div class="white-form-row white-form-top-row">
                    <div class="button-container">
                        <button class="form-button reg-button" id="saveDraftLogbookEntryButton">
                            <s:message code="cmas.face.logbook.saveDraft"/>
                        </button>
                        <button class="the-only-button enter-button" id="saveLogbookEntryButton">
                            <s:message code="cmas.face.logbook.submit"/>
                        </button>
                    </div>
                </div>
            </div>

            <div class="white-form-left">
                <div class="dialog-title"><s:message code="cmas.face.logbook.tanks.title"/></div>
                <div class="white-form-row">
                    <input id="isApnea" type="checkbox"
                            <c:if test="${logbookEntry != null}">
                                <c:if test="${logbookEntry.diveSpec.isApnea}">checked="checked"</c:if>
                            </c:if>
                            />
                    <label class="white-form-checkbox-label">
                        <s:message code="cmas.face.logbook.isApnea"/>
                    </label>
                </div>
                <div class="error" id="create_error_scubaTanks"></div>
                <div id="tanksSection">
                    <div id="tanks">

                    </div>
                    <div class="white-form-row">
                        <div class="button-container">
                            <button class="form-button enter-button" id="addTankButton">
                                <s:message code="cmas.face.logbook.addTank"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="publish" class="clearfix">
            <div class="white-form-left">
                <div class="white-form-row white-form-top-row">
                    <label class="white-form-label"><s:message code="cmas.face.logbook.visibility"/></label>
                    <select name="visibility" id="visibility" style="width: 100%" size=1 onChange="">
                    </select>
                    <label class="error" id="create_error_visibility"></label>
                </div>


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
                        <input class="white-form-row-input" id="photoFileInput" name="photoFileInput" type="file"
                               accept="image/*"
                               style="display: none">
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

                <div class="button-container">
                    <button class="form-button enter-button" id="publishLogbookEntryButton">
                        <s:message code="cmas.face.logbook.publishEntry"/>
                    </button>
                </div>
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

    <my:dialog id="saveDraftSuccess"
               title="cmas.face.logbook.saveDraftSuccessTitle"
               buttonText="cmas.face.dialog.ok">
        <div id="noDiversFoundText"><s:message code="cmas.face.logbook.saveDraftSuccessText"/></div>
    </my:dialog>

    <my:dialog id="submitSuccess"
               title="cmas.face.logbook.submitTitle"
               buttonText="cmas.face.dialog.ok">
        <div id="noDiversFoundText"><s:message code="cmas.face.logbook.submitText"/></div>
    </my:dialog>

    <my:dialog id="showDiver"
               title="cmas.face.showDiver.title"
               buttonText="cmas.face.showDiver.submitText">
    </my:dialog>

</my:securepage>

