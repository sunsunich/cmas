
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<my:adminpage title="Редактирование пользователя в системе">
<h2>Редактирование пользователя в системе</h2>
<ff:form submitText="Сохранить" action="/admin/updateUser.html" >
   
    <ff:input path="shopName" label="Имя магазина" required="true"/>
    <ff:input path="email" label="E-Mail (клиента)" required="true"/>
    <ff:input path="webAddress" label="Адрес в Интернете" required="true"/>
    <ff:input path="phone" label="Номер телефона" required="true"/>
    <ff:input path="city" label="Город" required="true"/>
    <ff:input path="discountPercent" label="Скидка (в %)" required="true"/>

    <ff:hidden path="id" />

</ff:form>
</my:adminpage>