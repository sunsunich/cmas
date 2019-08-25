<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="url" required="true" type="java.lang.String"%>
<%@ attribute name="alt" required="false" type="java.lang.String"%>
<%@ attribute name="confirmMessage" required="false" type="java.lang.String"%>
<c:if test="${empty alt}">    
    <c:set var="alt">
        Delete diver
    </c:set>
</c:if>
<c:if test="${empty confirmMessage}">
    <c:set var="confirmMessage">
        Are you sure you want to delete theis diver?
    </c:set>
</c:if>
<button type="button" onclick="if(confirm('${confirmMessage}')) window.location = '${url}'">
    ${alt}
</button>
