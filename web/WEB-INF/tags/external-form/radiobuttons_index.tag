<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="onclick" required="false" %>
<%@ attribute name="items" required="true" type="java.util.List<java.lang.String>" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${items}" var="shoppingCartItem" varStatus="loop">
    <form:radiobutton path="${path}" value="${loop.index}" label="${shoppingCartItem}"
                      htmlEscape="true" cssErrorClass="errorInput"
                      onclick="${onclick}"               />
</c:forEach>
