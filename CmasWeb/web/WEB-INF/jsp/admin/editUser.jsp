
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<my:adminpage title="Edit user">
<h2>Edit user</h2>
<ff:form submitText="Save" action="/admin/updateUser.html" >
   
    <ff:input path="firstName" label="First Name" required="true"/>
    <ff:input path="email" label="E-Mail" required="true"/>

    <ff:hidden path="id" />

</ff:form>
</my:adminpage>