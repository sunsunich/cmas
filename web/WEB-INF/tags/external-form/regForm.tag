<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<%@ attribute name="passwordStrength" required="true" %>
<%@ attribute name="action" required="true" %>

<%@ attribute name="onsubmit" required="false" %>


<ef:form submitText="face.client.registration.form.submitText" action="${action}" id="regForm"
         submitImg="/i/gray/registration.png" submitImgHeight="42" submitImgWidth="301" submitPosition="relative"
         submitPositionTop="0" submitPositionLeft="170"
         cssClass="registration" onsubmit="${onsubmit}">

    <ef:input label="face.client.registration.form.label.email" path="email" id="emailfor"
              required="true"/>
    <ef:password label="face.client.registration.form.label.password" path="password" id="passwordfor"
                 required="true"/>
    <ef:password label="face.client.registration.form.label.checkPassword" path="checkPassword"
                 id="chpasswordfor" required="true"/>
    <br/>
    <label id="pass_strength_field">
        <s:message code="face.client.form.label.passwordStrength"/> ${passwordStrength}
    </label>


    <ef:checkbox path="termsAndCondsAgreed"
                 label="face.client.registration.form.terms.label" value="false"
                 id="pravmin" cssClass="" required="true"/>

</ef:form>