<%-- Вывод колонки по сортировке --%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="url" required="true" type="java.lang.String"%>
<%@ attribute name="title" required="true" type="java.lang.String"%>
<%@ attribute name="columnNumber" required="true" type="java.lang.String"%>
<%@ attribute name="sortColumn" required="true" type="java.lang.String"%>
<%@ attribute name="dir" required="true" type="java.lang.Boolean"%>
<a href="${url}sort=${sortColumn}&dir=${!dir}">${title}</a>
<c:if test="${sortColumn == columnNumber}">
    <c:choose>
        <c:when test='${dir}'>
            <img src="/i/sort_up.gif"/>
        </c:when>
        <c:otherwise>
            <img src="/i/sort_down.gif"/>
        </c:otherwise>
    </c:choose>
</c:if>
