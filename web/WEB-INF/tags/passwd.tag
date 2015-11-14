<%-- Действие смена пароля --%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="url" required="true" type="java.lang.String"%>
<%@ attribute name="alt" required="false" type="java.lang.String"%>
<c:if test="${empty alt}">
    <c:set var="alt">
        Сменить пароль
    </c:set>
</c:if>
<a href="${url}"><span class="sign icn-pass" title="${alt}"></span></a>
