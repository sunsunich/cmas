
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>

<my:adminpage title="Создание пользователя в системе">
<h2>Создание пользователя в системе</h2>    
<ff:form submitText="Создать" action="/admin/registration/add.html">
    <ff:input path="shopName" label="Фамилия"/>
    <ff:checkbox path="enable" label="Активировать"/>
    <input type="hidden" name="regId" value="${command.regId}"/>
</ff:form>    
</my:adminpage>