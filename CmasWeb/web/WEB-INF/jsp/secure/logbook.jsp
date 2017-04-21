<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>


<my:securepage title="cmas.face.index.header"
               customScripts="js/model/util_model.js,js/model/logbook_feed_model.js,js/controller/util_controller.js,js/controller/logbook_feed_controller.js,js/controller/logbook_controller.js"
        >

    <div class="content">
        <div class="tabs clearfix">
            <span class="firstTab" id="myTab"><s:message code="cmas.face.logbook.tab.me"/></span>
        <span class="secondTab inactive" id="friendsTab"><s:message
                code="cmas.face.logbook.tab.friends"/></span>
        </div>
    </div>

    <div id="myLogbook" class="logbookParent">
        <div class="createEntryHeader">
            <div class="button-container">
                <button class="form-button enter-button" id="createLogbookEntryButton">
                    <s:message code="cmas.face.logbook.addNew"/>
                </button>
            </div>
            <!--add search form !-->
            <form id="regForm" action="">
                <div class="search-form-logbook">
                    <label>Dive date:</label>
                    <div class="form-row-logbook">
                        <div class="form-row-logbook-half" style="display: inline">
                            <label>From</label>
                            <img class="calendar-input-ico-logbook">
                            <input type="date" name="divedatefrom"></div>
                        <div class="form-row-logbook-half" style="display: inline">
                            <label style="padding-left: 10px">To</label>
                            <img class="calendar-input-ico-logbook">
                            <input type="date" name="divedateto"></div>
                    </div>
                    <div class="error"></div>
                    <div class="form-row-logbook">
                        <select name="country" id="country" style="width: 90%; position:relative; left: 8%;" size=1 onChange="">
                            <c:forEach items="${countries}" var="country">
                                <option value='${country.code}'>${country.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="error" id="reg_error_country"></div>
                    <label>Depth:</label>
                    <div class="form-row-logbook">
                        <div class="form-row-logbook-half-two" style="display: inline">
                            <label>From</label>
                            <input type="text" name="divedepthfrom" placeholder="meters"></div>
                        <div class="form-row-logbook-half-two" style="display: inline">
                            <label style="padding-left: 10px">To</label>
                            <input type="text" name="divedepthto" placeholder="meters"></div>
                    </div>
                    <div class="error"></div>
                    <label>Duration:</label>
                    <div class="form-row-logbook">
                        <div class="form-row-logbook-half-two" style="display: inline">
                        <label>From</label>
                        <input type="text" name="divedurationfrom" placeholder="minutes"></div>
                        <div class="form-row-logbook-half-two" style="display: inline">
                        <label style="padding-left: 10px">To</label>
                        <input type="text" name="divedurationto" placeholder="minutes"></div>
                    </div>
                    <div class="error"></div>
                </div>
                <div class="error" style="display: none" id="reg_error">
                </div>
                <div class="button-container">
                    <button class="form-button enter-button">
                        Search Dive
                    </button>
                </div>
            </form>
        </div>
        <div id="myLogbookFeed" ></div>
    </div>

    <div class="content-right" id="friendsLogbook" style="display: none">
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


