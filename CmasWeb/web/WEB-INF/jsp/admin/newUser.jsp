
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<jsp:useBean id="timezones" scope="request" type="java.util.Collection"/>
<jsp:useBean id="roleList" scope="request" type="java.util.Collection"/>

<my:adminpage title="Create new user">
<h2>Create new user</h2>
<ff:form submitText="Save" action="/admin/addClient.html">
    
    <ff:input path="firstName" label="First Name"/>

    <ff:input path="email" label="E-Mail" required="true"/>

    <ff:row path="roles" label="Roles">
        <form:checkboxes path="roles" items="${roleList}" itemLabel="humanName" itemValue="acegiRoleName" delimiter="<br/>"/>
    </ff:row>

</ff:form>
</my:adminpage>