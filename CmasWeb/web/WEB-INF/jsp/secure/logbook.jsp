<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>


<my:securepage title="cmas.face.index.header"
               customScripts="js/model/util_model.js,js/model/logbook_feed_model.js,js/controller/util_controller.js,js/controller/country_controller.js,js/controller/logbook_feed_controller.js,js/controller/logbook_controller.js"
>

    <div class="content">
        <div class="tabs clearfix">
            <span class="firstTab" id="myTab"><s:message code="cmas.face.logbook.tab.me"/></span>
            <span class="secondTab inactive" id="friendsTab"><s:message
                    code="cmas.face.logbook.tab.friends"/></span>
        </div>
    </div>

    <div id="myLogbook" class="logbookParent">
        <div class="logbookHeader">
            <div class="">
                <button class="form-button enter-button" id="createLogbookEntryButton">
                    <s:message code="cmas.face.logbook.addNew"/>
                </button>
            </div>
            <!--add search form !-->
            <form id="logbookEntrySearchForm" action="">
                <div class="search-form-logbook">
                    <label><s:message code="cmas.face.logbook.diveDate"/>:</label>
                    <div class="form-row-logbook">
                        <div class="form-row-logbook-half">
                            <label><s:message code="cmas.face.logbook.from.search"/></label>
                            <div class="calendar-container">
                                <img class="calendar-input-ico-logbook">
                                <input name="diveDateFrom" id="diveDateFrom"/>
                            </div>
                        </div>
                        <div class="form-row-logbook-half">
                            <label class="form-row-logbook-half-label2"><s:message
                                    code="cmas.face.logbook.to.search"/></label>
                            <div class="calendar-container">
                                <img class="calendar-input-ico-logbook">
                                <input name="diveDateTo" id="diveDateTo" />
                            </div>
                        </div>
                    </div>
                    <div class="error"></div>
                    <label><s:message code="cmas.face.logbook.country.search"/>:</label>
                    <div class="form-row-logbook">
                        <select name="country" id="country" style="width: 99%" size=1
                                onChange="">
                            <c:forEach items="${countries}" var="country">
                                <option value='${country.code}'>${country.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="error" id="reg_error_country"></div>
                    <label><s:message code="cmas.face.logbook.depth.search"/>:</label>
                    <div class="form-row-logbook">
                        <div class="form-row-logbook-half">
                            <label><s:message code="cmas.face.logbook.from.search"/></label>
                            <input type="text" name="diveDepthFrom" id="diveDepthFrom"
                                   placeholder="<s:message code="cmas.face.logbook.depth.placeholder"/>"/>
                        </div>
                        <div class="form-row-logbook-half">
                            <label class="form-row-logbook-half-label2"><s:message
                                    code="cmas.face.logbook.to.search"/></label>
                            <input type="text" name="diveDepthTo" id="diveDepthTo"
                                   placeholder="<s:message code="cmas.face.logbook.depth.placeholder"/>"/>
                        </div>
                    </div>
                    <div class="error"></div>
                    <label><s:message code="cmas.face.logbook.duration"/>:</label>
                    <div class="form-row-logbook">
                        <div class="form-row-logbook-half">
                            <label><s:message code="cmas.face.logbook.from.search"/></label>
                            <input type="text" name="diveDurationFrom" id="diveDurationFrom"
                                   placeholder="<s:message code="cmas.face.logbook.duration.placeholder"/>">
                        </div>
                        <div class="form-row-logbook-half">
                            <label class="form-row-logbook-half-label2"><s:message
                                    code="cmas.face.logbook.to.search"/></label>
                            <input type="text" name="diveDurationTo" id="diveDurationTo"
                                   placeholder="<s:message code="cmas.face.logbook.duration.placeholder"/>">
                        </div>
                    </div>
                    <div class="error"></div>
                </div>
                <div class="">
                    <button type="button" class="form-button enter-button">
                        Search Dive
                    </button>
                </div>
            </form>
        </div>
        <div class="myLogbookFeed" id="myLogbookFeed"></div>
    </div>

    <div class="content-left" id="friendsLogbook" style="display: none">
        <div id="friendsLogbookFeed" class="clearfix">

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

</my:securepage>


