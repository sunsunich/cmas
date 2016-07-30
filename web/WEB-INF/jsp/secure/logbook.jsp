<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>


<my:securepage title="cmas.face.index.header"
               customScripts="js/model/logbook_feed_model.js,js/controller/logbook_feed_controller.js,js/controller/logbook_controller.js"
        >

    <div class="content" id="mainContent">
    <div class="tabs clearfix">
        <span class="firstTab" id="myTab"><s:message code="cmas.face.logbook.tab.me"/></span>
        <span class="secondTab" id="friendsTab" class="inactive"><s:message
                code="cmas.face.logbook.tab.friends"/></span>
    </div>
    <div id="myLogbook">
        <div class="button-container">
            <button class="form-button enter-button" id="createLogbookEntryButton">
                <s:message code="cmas.face.logbook.addNew"/>
            </button>
        </div>
        <div id="myLogbookFeed"></div>
    </div>

    <div id="friendsLogbookFeed" style="display: none">

    </div>

    <my:dialog id="recordDeleteDialog"
               title="cmas.face.logbook.delete.title"
               buttonText="cmas.face.logbook.delete.ok">
        <div class="dialog-form-row"><s:message code="cmas.face.logbook.delete.question"/></div>
    </my:dialog>

</my:securepage>


