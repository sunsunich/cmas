<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="enumItems" required="true" type="java.lang.Enum[]"%>
<%@ attribute name="arrayVarName" required="true" type="java.lang.String"%>

<c:forEach items="${enumItems}" var="enumItem" varStatus="st">
  ${arrayVarName}[${st.index}] = '${enumItem.name}';
</c:forEach>