
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<jsp:useBean id="countries" scope="request" type="org.cmas.entities.Country[]"/>

<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.admin.FederationFormObject"/>

<my:adminpage title="Create new federation">
<h2>Create new federation</h2>
<ff:form submitText="Save" action="/admin/addFederationSubmit.html">
    
    <ff:input path="name" label="Federation Name" required="true"/>

    <ff:input path="email" label="Federation E-Mail" required="true"/>

    <ff:select path="countryCode" label="Country" itemLabel="name" itemValue="code" options="${countries}"/>

</ff:form>
</my:adminpage>