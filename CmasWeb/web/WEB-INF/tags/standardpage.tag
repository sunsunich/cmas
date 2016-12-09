<%-- Стандартная страница с контентом на всю ширину экрана --%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8"  %>

<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="lightHeader" required="false" %>

<my:basePage title="${title}" lightHeader="${lightHeader}">

    <jsp:doBody/>
</my:basePage>
