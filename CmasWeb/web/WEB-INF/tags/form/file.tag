<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>

<ff:row path="${path}" label="${label}">
    <input type="file" name="${path}" cssErrorClass="errorInput" cssClass="blue"/>
</ff:row>
