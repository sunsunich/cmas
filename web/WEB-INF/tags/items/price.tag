<%-- Действие удаление --%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="price" required="true" type="java.math.BigDecimal"%>

<c:choose>
    <c:when test="${price == null || price <= 0}">
        Цена неизвестна 
    </c:when>
    <c:otherwise>
        <fmt:formatNumber value="${price}" pattern="##.##"/> $
    </c:otherwise>
</c:choose>
