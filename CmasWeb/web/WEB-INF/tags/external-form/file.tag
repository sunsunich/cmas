<%@ tag body-content="empty" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>

<ef:row path="${path}" label="${label}">
    <input type="file" name="${path}" cssErrorClass="errorInput" cssClass="field text small"/>
</ef:row>
