<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="inputId" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>

<ff:row path="${path}" label="${label}">
    <input id="${inputId}" type="file" name="${path}" cssErrorClass="errorInput" cssClass="blue"/>
</ff:row>
