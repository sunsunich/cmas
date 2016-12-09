<%-- Действие удаление --%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="url" required="true" type="java.lang.String"%>
<%@ attribute name="alt" required="false" type="java.lang.String"%>
<%@ attribute name="confirmMessage" required="false" type="java.lang.String"%>
<c:if test="${empty alt}">    
    <c:set var="alt">
        Удалить
    </c:set>
</c:if>
<c:if test="${empty confirmMessage}">
    <c:set var="confirmMessage">
        Вы действительно хотите удалить данного пользователя? 
    </c:set>
</c:if>
<a href="${url}" onclick="return confirm('${confirmMessage}');"><span class="sign icn-delete" title="${alt}"></span></a>
