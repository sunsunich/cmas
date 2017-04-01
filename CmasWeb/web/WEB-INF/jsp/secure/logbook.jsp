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

    <div id="myLogbook">
        <div class="createEntryHeader">
            <div class="button-container">
                <button class="form-button enter-button" id="createLogbookEntryButton">
                    <s:message code="cmas.face.logbook.addNew"/>
                </button>
            </div>
        </div>
        <div id="myLogbookFeed" class="clearfix"></div>
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


