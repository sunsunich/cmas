<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="passwordStrength" required="true" type="java.lang.String"%>

<c:choose>
	<c:when test="${passwordStrength == 'VERY_WEAK' || passwordStrength == 'WEAK'}">
		<div>
			<s:message code="face.passwd.weak"/>
		</div>
	</c:when>
	<c:when test="${passwordStrength == 'AVERAGE'}">
		<div>
			<s:message code="face.passwd.notenough"/>
		</div>
	</c:when>
</c:choose>