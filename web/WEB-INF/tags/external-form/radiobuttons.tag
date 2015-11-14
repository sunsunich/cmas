<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="false" %>
<%@ attribute name="onclick" required="false" %>
<%@ attribute name="items" required="true" type="java.lang.Object[]" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${items}" var="shoppingCartItem" varStatus="loop">
    <ef:row path="${path}" >
        <label class="b-form-checkbox" for="${path}${loop.count}">
        <form:radiobutton path="${path}" value="${shoppingCartItem.value}"
                      htmlEscape="true" cssErrorClass="errorInput"
                      onclick="${onclick}"               />
            ${shoppingCartItem.descr}</label>
    </ef:row>
</c:forEach>
