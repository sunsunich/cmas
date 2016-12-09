<%@ tag body-content="scriptless" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="comment" required="false" %>
<%@ attribute name="required" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
    <td align="right"
    <form:label path="${path}" cssErrorClass="errorLabel" htmlEscape="true" cssClass="formLabel">
        <c:if test="${required}"><span class="reqMark">* </span></c:if>${label}:
    </form:label>
    </td>
    <td>
        <jsp:doBody/>
        <c:if test="${not empty comment}">
            <div style="font-size:80%">${comment}</div>
        </c:if></td>
    <td style="color:darkred;"><form:errors path="${path}" htmlEscape="true"/></td>
</tr>
