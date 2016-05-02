<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.BackendUser"/>

<my:securepage title="cmas.face.index.header"
               customScripts="js/model/profile_model.js,js/controller/profile_controller.js,js/controller/cards_controller.js"
        >
    <script type="application/javascript">
        var cmas_primaryCardId = "${diver.primaryPersonalCard.id}";
        var cmas_secondaryCardIds = [];
        <c:forEach items="${diver.secondaryPersonalCards}" var="card" varStatus="st">
        cmas_secondaryCardIds[${st.count - 1}] = "${card.id}";
        </c:forEach>
    </script>

    <div class="content" id="content">


    </div>

</my:securepage>


