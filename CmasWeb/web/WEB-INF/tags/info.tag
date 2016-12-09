<%-- Действие просмотр информации --%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="url" required="true" type="java.lang.String"%>
<%@ attribute name="alt" required="false" type="java.lang.String"%>
<c:if test="${empty alt}">
    <c:set var="alt">
        <s:message code="tags.info.alt"/>
    </c:set>
</c:if>
<a href="${url}"><span class="sign icn-info" alt="${alt}" title="${alt}"></span></a>