<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="path" required="false" %>
<%@ attribute name="pathList" required="false" type="java.util.Collection" %>

<c:if test="${not empty path}">
    <script type="text/javascript" src="${path}"></script>
</c:if>
<c:forEach var="onePath" items="${pathList}">
    <c:if test="${not empty onePath}">
        <script type="text/javascript" src="${onePath}"></script>
    </c:if>
</c:forEach>

