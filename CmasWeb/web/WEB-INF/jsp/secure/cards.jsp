<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="cards" scope="request" type="java.util.List<org.cmas.entities.PersonalCard>"/>

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
            <span class="header2-text"><s:message code="cmas.face.client.certificates.header"/></span>
        </div>
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
    </div>
</my:securepage>


