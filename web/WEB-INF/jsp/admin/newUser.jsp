
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<jsp:useBean id="timezones" scope="request" type="java.util.Collection"/>
<jsp:useBean id="roleList" scope="request" type="java.util.Collection"/>

<my:adminpage title="Создание пользователя в системе">
<h2>Создание пользователя в системе</h2>                   
<ff:form submitText="Сохранить" action="/admin/addClient.html">
    
    <ff:input path="shopName" label="Имя Магазина"/>

    <ff:input path="email" label="E-Mail (клиента)" required="true"/>

    <ff:row path="timezone" label="Временная зона">
        <form:select path="timezone" htmlEscape="true" cssErrorClass="errorInput" cssClass="blue">
            <form:options items="${timezones}"/>
        </form:select>
    </ff:row>
    <ff:row path="roles" label="Роли">
        <form:checkboxes path="roles" items="${roleList}" itemLabel="humanName" itemValue="acegiRoleName" delimiter="<br/>"/>
    </ff:row>

    <ff:input path="saleManager" label="sale manager"/>
    <ff:input path="accountManager" label="account manager"/>

</ff:form>
</my:adminpage>