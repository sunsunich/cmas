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
        logbook_record_model.logbookEntry = JSON.parse('${logbookEntryJson}'.replace(/\n/g, "\\n").replace(/\r/g, "\\r"));
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

    <div id="createLogbookEntry" class="content-create-logbook">
        <div id="diveProfile" class="panel panel-wider">
            <div class="createEntryHeader header1-text sub-panel-narrow">
                <s:message code="cmas.face.logbook.create.title"/>
                <div class="logbook-button-right">
                    <span class="logbook-tab-chosen logbook-tab-left" id="requiredTab">
                            <s:message code="cmas.face.logbook.requiredFields"/>
                    </span>
                    <span class="logbook-tab logbook-tab-right" id="certificationTab">
                        <s:message code="cmas.face.logbook.forCertificationFields"/>
                    </span>
                </div>
            </div>
            <div class="sub-panel-narrow clearfix">
                <div class="logbookHeader header2-text "><s:message code="cmas.face.logbook.dive.info.title"/></div>
                <div class="logbook-section-left">
                    <div>
                        <div class="logbook-form-label"><s:message code="cmas.face.logbook.diveDate"/></div>
                        <div class="form-row white-form-row-date">
                            <input id="diveDate" type="text"
                                   placeholder="<s:message code="cmas.face.logbook.diveDate"/>"
                                    <c:if test="${logbookEntry != null}">
                                        value="<fmt:formatDate value="${logbookEntry.diveDate}" pattern="dd/MM/yyyy"/>"
                                    </c:if>
                            />
                            <img src="/i/ic_calendar.png" class="error-input-ico" id="create_ico_diveDate">
                            <img src="/i/ic_error.png" class="error-input-ico" id="create_error_ico_diveDate"
                                 style="display: none">
                            <div class="error" id="create_error_diveDate"></div>
                        </div>
                        <div class="form-row white-form-row-time">
                            <input id="diveTime" type="text"
                                   placeholder="<s:message code="cmas.face.logbook.diveTime"/>"
                                    <c:if test="${logbookEntry != null}">
                                        value="<fmt:formatDate value="${logbookEntry.diveDate}" pattern="HH:mm"/>"
                                    </c:if>
                            />
                            <img src="/i/ic_error.png" class="error-input-ico" id="create_error_ico_diveTime"
                                 style="display: none">
                            <div class="error" id="create_error_diveTime"></div>
                        </div>
                    </div>

                    <div>
                        <div class="form-row white-form-row-third">
                            <label class="logbook-form-label"><s:message code="cmas.face.logbook.duration"/></label>
                            <input id="duration" type="text"
                                   placeholder="<s:message code="cmas.face.logbook.duration.placeholder"/>"
                                    <c:if test="${logbookEntry != null}">
                                        value="${logbookEntry.diveSpec.durationMinutes}"
                                    </c:if>
                            />
                            <label class="error" id="create_error_durationMinutes"></label>
                        </div>
                        <div class="form-row white-form-row-third">
                            <label class="logbook-form-label"><s:message code="cmas.face.logbook.depth"/></label>
                            <input id="depth" type="text"
                                   placeholder="<s:message code="cmas.face.logbook.depth.placeholder"/>"
                                    <c:if test="${logbookEntry != null}">
                                        value="${logbookEntry.diveSpec.maxDepthMeters}"
                                    </c:if>
                            />
                            <label class="error" id="create_error_maxDepthMeters"></label>
                        </div>
                        <div class="form-row white-form-row-third-right" id="avgDepthMeters_container">
                            <label class="logbook-form-label"><s:message code="cmas.face.logbook.avgDepthMeters"/></label>
                            <input id="avgDepthMeters" type="text"
                                   placeholder="<s:message code="cmas.face.logbook.avgDepthMeters.placeholder"/>"
                                    <c:if test="${logbookEntry != null}">
                                        value="${logbookEntry.diveSpec.avgDepthMeters}"
                                    </c:if>
                            />
                            <label class="error" id="create_error_avgDepthMeters"></label>
                        </div>
                    </div>

                    <div id="prevDiveDate_container">
                        <div class="logbook-form-label">
                            <label><s:message code="cmas.face.logbook.prevDiveDate"/></label>
                            <label class="logbook-optional"><s:message code="cmas.face.logbook.optional"/></label>
                        </div>
                        <div class="form-row white-form-row-date">
                            <input id="prevDiveDate" type="text"
                                   placeholder="<s:message code="cmas.face.logbook.prevDiveDate"/>"
                                    <c:if test="${logbookEntry != null}">
                                        value="<fmt:formatDate value="${logbookEntry.prevDiveDate}"
                                                               pattern="dd/MM/yyyy"/>"
                                    </c:if>
                            />
                            <img src="/i/ic_calendar.png" class="error-input-ico" id="create_ico_prevDiveDate">
                            <img src="/i/ic_error.png" class="error-input-ico" id="create_error_ico_prevDiveDate"
                                 style="display: none">
                            <div class="error" id="create_error_prevDiveDate"></div>
                        </div>
                        <div class="form-row white-form-row-time">
                            <input id="prevDiveTime" type="text"
                                   placeholder="<s:message code="cmas.face.logbook.prevDiveTime"/>"
                                    <c:if test="${logbookEntry != null}">
                                        value="<fmt:formatDate value="${logbookEntry.prevDiveDate}" pattern="HH:mm"/>"
                                    </c:if>
                            />
                            <img src="/i/ic_error.png" class="error-input-ico" id="create_error_ico_prevDiveTime"
                                 style="display: none">
                            <div class="error" id="create_error_prevDiveTime"></div>
                        </div>
                    </div>
                </div>
                <div class="logbook-section-right">
                    <div class="logbookHeader header2-text"><s:message code="cmas.face.spots.chooseSpot"/></div>
                    <div class="center">
                        <a id="chooseSpot" href="#">
                            <s:message code="cmas.face.spots.chooseSpot"/>
                        </a>
                    </div>
                    <div class="logbook-spot-data">
                        <div class="form-row logbook-spot-name">
                            <input id="spotName" type="text"
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
                        </div>
                        <div class="logbook-spot-coord-block">
                            <div class="form-row logbook-spot-coord">
                                <input id="latitude" type="text"
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
                            </div>
                            <div class="form-row logbook-spot-coord">
                                <input id="longitude" type="text"
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
                            </div>
                        </div>
                    </div>
                </div>

                <div class="logbook-section-left">
                        <%--todo buddies section--%>
                </div>
            </div>
            <div class="sub-panel-narrow">
                    <%--tanks--%>
                <div class="logbookHeader header2-text"><s:message code="cmas.face.logbook.tanks.title"/></div>
                <div class="form-row">
                    <input type="checkbox" name="isApnea" id="isApnea" class="css-checkbox"
                    <c:if test="${logbookEntry != null}">
                           <c:if test="${logbookEntry.diveSpec.isApnea}">checked="checked"</c:if>
                    </c:if>
                    >
                    <label for="isApnea"
                           class="css-label radGroup1 clr">
                            <span class="form-checkbox-label">
                                <s:message code="cmas.face.logbook.apnea"/>
                            </span>
                    </label>
                    <b id="addTankButton">
                        <div class="add-button logbook-button-right"></div>
                        <label class="logbook-form-checkbox-label logbook-button-right">
                            <s:message code="cmas.face.logbook.addTank"/>
                        </label>
                    </b>
                    <div class="error" id="create_error_scubaTanks"></div>
                </div>
            </div>
            <div id="tanksSection">
                <div id="tanks" class="clearfix">

                </div>
            </div>
            <div id="additional_container" class="sub-panel-narrow">
                <div class="logbookHeader header2-text">
                    <label><s:message code="cmas.face.logbook.additional.title"/></label>
                    <label class="logbook-optional"><s:message code="cmas.face.logbook.optional"/></label>
                </div>
                <div class="logbook-additional-section">
                    <div class="form-row">
                        <div class="logbook-form-label"><s:message code="cmas.face.logbook.entryType"/></div>
                        <select name="entryType" id="entryType" style="width: 272px" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_entryType"></label>
                    </div>
                    <div class="form-row">
                        <div class="logbook-form-label"><s:message code="cmas.face.logbook.diveSuit"/></div>
                        <select name="diveSuit" id="diveSuit" size=1 style="width: 272px" onChange="">
                        </select>
                        <label class="error" id="create_error_diveSuit"></label>
                    </div>
                    <div class="form-row">
                        <div class="logbook-form-label">
                            <s:message code="cmas.face.logbook.divePurpose"/>
                        </div>
                        <select name="divePurpose" id="divePurpose" style="width: 272px" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_divePurpose"></label>
                    </div>
                    <div class="form-row">
                        <label class="logbook-form-label logbook-elem-margin">
                            <s:message code="cmas.face.logbook.additionalWeightKg"/>
                        </label>
                        <input id="additionalWeightKg" type="text" class="logbook-form-middle-elem"
                               placeholder="<s:message code="cmas.face.logbook.additionalWeightKg.placeholder"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="${logbookEntry.diveSpec.additionalWeightKg}"
                                </c:if>
                        />
                        <label class="error" id="create_error_additionalWeightKg"></label>
                    </div>
                </div>
                <div class="logbook-additional-section">
                    <div class="logbook-form-label">
                        <s:message code="cmas.face.logbook.airAndWaterTemp"/>
                    </div>
                    <div>
                        <div class="form-row logbook-elem-margin" style="display: inline-block">
                            <input class="logbook-form-middle-elem" id="airTemp" type="text"
                                   placeholder="<s:message code="cmas.face.logbook.airTemp"/>"
                                    <c:if test="${logbookEntry != null}">
                                        value="${logbookEntry.diveSpec.airTemp}"
                                    </c:if>
                            />
                            <label class="error" id="create_error_airTemp"></label>
                        </div>
                        <div class="form-row logbook-elem-margin" style="display: inline-block">
                            <input class="logbook-form-middle-elem" id="waterTemp" type="text"
                                   placeholder="<s:message code="cmas.face.logbook.waterTemp"/>"
                                    <c:if test="${logbookEntry != null}">
                                        value="${logbookEntry.diveSpec.waterTemp}"
                                    </c:if>
                            />
                            <label class="error" id="create_error_waterTemp"></label>
                        </div>
                        <div class="form-row" style="display: inline-block">
                            <select name="temperatureMeasureUnit" id="temperatureMeasureUnit" style="width: 75px" size=1
                                    onChange="">
                            </select>
                            <label class="error" id="create_error_temperatureMeasureUnit"></label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="logbook-form-label"><s:message code="cmas.face.logbook.surface"/></div>
                        <select name="surface" id="surface" style="width: 100%" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_surface"></label>
                    </div>
                    <div class="form-row">
                        <div class="logbook-form-label">
                            <s:message code="cmas.face.logbook.underWaterVisibility"/>
                        </div>
                        <select name="underWaterVisibility" id="underWaterVisibility" style="width: 100%" size=1
                                onChange="">
                        </select>
                        <label class="error" id="create_error_underWaterVisibility"></label>
                    </div>
                    <div class="form-row">
                        <label class="logbook-form-label logbook-elem-margin">
                            <s:message code="cmas.face.logbook.cnsToxicity"/>
                        </label>
                        <input class="logbook-form-middle-elem" id="cnsToxicity" type="text"
                               placeholder="<s:message code="cmas.face.logbook.cnsToxicity"/>"
                                <c:if test="${logbookEntry != null}">
                                    value="${logbookEntry.diveSpec.cnsToxicity}"
                                </c:if>
                        />
                        <label class="error" id="create_error_cnsToxicity"></label>
                    </div>
                </div>
                <div class="logbook-additional-section">
                    <div class="form-row">
                        <div class="logbook-form-label"><s:message code="cmas.face.logbook.weather"/></div>
                        <select name="weather" id="weather" style="width: 272px" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_weather"></label>
                    </div>
                    <div class="form-row">
                        <div class="logbook-form-label"><s:message code="cmas.face.logbook.current"/></div>
                        <select name="current" id="current" style="width: 272px" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_current"></label>
                    </div>
                    <div class="form-row">
                        <div class="logbook-form-label"><s:message code="cmas.face.logbook.waterType"/></div>
                        <select name="waterType" id="waterType" style="width: 272px" size=1 onChange="">
                        </select>
                        <label class="error" id="create_error_waterType"></label>
                    </div>
                    <div class="form-row">
                        <input id="hasSafetyStop" name="hasSafetyStop" type="checkbox" class="css-checkbox"
                                <c:if test="${logbookEntry != null}">
                                    <c:if test="${logbookEntry.diveSpec.hasSafetyStop}">checked="checked"</c:if>
                                </c:if>
                        />
                        <label for="hasSafetyStop"
                               class="css-label radGroup1 clr">
                            <span class="form-checkbox-label">
                               <s:message code="cmas.face.logbook.hasSafetyStop"/>
                            </span>
                        </label>
                    </div>
                </div>
                <div class="clearfix">
                    <div class="logbook-additional-section">
                        <div class="logbook-form-label"><s:message code="cmas.face.logbook.decoStepsComments"/></div>
                        <div class="form-row">
                            <c:choose>
                                <c:when test="${logbookEntry == null}">
                        <textarea id="decoStepsComments" rows="6"
                                  placeholder="<s:message code="cmas.face.logbook.decoStepsComments"/>"></textarea>
                                </c:when>
                                <c:otherwise>
                        <textarea id="decoStepsComments" rows="6"
                                  placeholder="<s:message code="cmas.face.logbook.decoStepsComments"/>">${logbookEntry.diveSpec.decoStepsComments}</textarea>
                                </c:otherwise>
                            </c:choose>
                            <label class="error" id="create_error_decoStepsComments"></label>
                        </div>
                    </div>
                    <div class="logbook-additional-section-big">
                        <div class="logbook-form-label"><s:message code="cmas.face.logbook.comments"/></div>
                        <div class="form-row">
                            <c:choose>
                                <c:when test="${logbookEntry == null}">
                        <textarea id="comments" rows="6"
                                  placeholder="<s:message code="cmas.face.logbook.comments"/>"></textarea>
                                </c:when>
                                <c:otherwise>
                        <textarea id="comments" rows="6"
                                  placeholder="<s:message code="cmas.face.logbook.comments"/>">${logbookEntry.note}</textarea>
                                </c:otherwise>
                            </c:choose>
                            <label class="error" id="create_error_note"></label>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="sub-panel-narrow">
            <div class="button-container">
                <button class="white-button logbook-button logbook-button-margin" id="saveDraftLogbookEntryButton">
                    <s:message code="cmas.face.logbook.saveDraft"/>
                </button>
                <button class="positive-button logbook-button" id="saveLogbookEntryButton">
                    <s:message code="cmas.face.logbook.submit"/>
                </button>
            </div>
        </div>

        <div id="publish" class="clearfix" style="display: none">
            <div class="white-form-left">
                <div class="white-form-row white-form-top-row">
                    <label class="logbook-form-label"><s:message code="cmas.face.logbook.visibility"/></label>
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
                        <c:set var="photoSrc" value="${pageContext.request.contextPath}/i/no_img.png?v=${webVersion}"/>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${logbookEntry.photoUrl == null}">
                                <c:set var="photoSrc"
                                       value="${pageContext.request.contextPath}/i/no_img.png?v=${webVersion}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="photoSrc" value="${logbookEntryImagesRoot}${logbookEntry.photoUrl}"/>
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
                        <img src="${pageContext.request.contextPath}/i/photo_ico_gray.png?v=${webVersion}"/>
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
        <div><s:message code="cmas.face.diverList.noDivers"/></div>
    </my:dialog>

    <my:dialog id="saveDraftSuccess"
               title="cmas.face.logbook.saveDraftSuccessTitle"
               buttonText="cmas.face.dialog.ok">
        <div><s:message code="cmas.face.logbook.saveDraftSuccessText"/></div>
    </my:dialog>

    <my:dialog id="submitSuccess"
               title="cmas.face.logbook.submitTitle"
               buttonText="cmas.face.dialog.ok">
        <div><s:message code="cmas.face.logbook.submitText"/></div>
    </my:dialog>

    <my:dialog id="showDiver"
               title="cmas.face.showDiver.title"
               buttonText="cmas.face.showDiver.submitText">
    </my:dialog>

</my:securepage>

