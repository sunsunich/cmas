
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.BackendUser"/>

<my:adminpage title="Информация о пользователе в системе">
<h2>Информация о пользователе в системе</h2>
    <table border="0" cellpadding="4" cellspacing="2" bgcolor="white">
        <tr class="info">
            <td align="right">Имя магазина</td>
            <td align="left">${user.shopName}</td>
        </tr>
        <tr class="info">
            <td align="right">E-Mail</td>
            <td align="left">${user.email}</td>
        </tr>
        <tr class="info">
            <td align="right">Адрес в Интернете</td>
            <td align="left">${user.webAddress}</td>
        </tr>
        <tr class="info">
            <td align="right">Номер телефона</td>
            <td align="left">${user.contactInfo.phone}</td>
        </tr>
        <tr class="info">
            <td align="right">Город</td>
            <td align="left">${user.contactInfo.city}</td>
        </tr>
        <tr class="info">
            <td align="right">Баланс</td>
            <td align="left">${user.userFinances.balance} $</td>
        </tr>
       
        <tr class="info">
            <td align="right">Скидка</td>
            <td align="left">${user.userFinances.discountPercent} %</td>
        </tr>
    </table>
</my:adminpage>