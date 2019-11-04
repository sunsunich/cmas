<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="cards" scope="request" type="java.util.List<org.cmas.entities.cards.PersonalCard>"/>
<jsp:useBean id="pendingCardApprovalRequests" scope="request"
             type="java.util.List<org.cmas.entities.cards.CardApprovalRequest>"/>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<my:securepage title="cmas.face.index.header"
               activeMenuItem="cards"
               customScripts="js/model/profile_model.js,js/controller/cards_controller.js"
>
    <script type="application/javascript">
        var cmas_cardIds = [];
        <c:forEach items="${cards}" var="card" varStatus="st">
        cmas_cardIds[${st.count - 1}] = "${card.id}";
        </c:forEach>
    </script>

    <div class="content-center-wide" id="content">
        <div class="panel panel-header">
            <span class="header2-text"><s:message code="cmas.face.card.header"/></span>
            <b id="createLogbookEntryButton" onclick="window.location='/secure/addCard.html'">
                <div class="add-button logbook-button-right"></div>
                <label class="logbook-form-checkbox-label logbook-button-right">
                    <s:message code="cmas.face.card.addCard"/>
                </label>
            </b>
            <c:if test="${diver.diverRegistrationStatus.name != 'CMAS_FULL' && diver.diverRegistrationStatus.name != 'CMAS_BASIC'}">
                <div class="basic-text"><s:message code="cmas.face.card.becomeCmas"/></div>
            </c:if>
        </div>
        <c:if test="${not empty cards}">
            <div class="panel-cards">
                <div class="clearfix">
                    <c:forEach items="${cards}" var="card">
                        <div class="content-card">
                            <div class="card-container">
                                <img id="${card.id}" src="${cardsRoot}${card.imageUrl}"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty pendingCardApprovalRequests}">
            <div class="panel panel-header">
                <span class="header2-text"><s:message code="cmas.face.card.pending.header"/></span>
            </div>
            <div class="panel-cards">
                <div class="clearfix">
                    <c:forEach items="${pendingCardApprovalRequests}" var="card">
                        <div class="content-card">
                            <div class="card-container">
                                <img id="${card.id}"
                                     src="${cardApprovalRequestImagesRoot}${card.frontImage.fileUrl}"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </div>
</my:securepage>


