
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<my:adminpage title="Список зарегистрированных пользователей.">
    <h2>Список зарегистрированных пользователей. Готовых к заведению в системе.</h2>
    <c:if test="${!empty regs}">
    <table border="0" cellpadding="4" cellspacing="2" bgcolor="white">
        <tr class="infoHeader">
            <th>Ф.И.О</th>
            <th>E-mail</th>
            <th>Sites</th>
            <th>Дата регистрации</th>
            <th align="center">Действия</th>
        </tr>
        <c:forEach items="${regs}" var="reg" varStatus="st">
            <tr class="info">
                <td>${reg.shopName}</td>
                <td>${reg.city}</td>
                <td>${reg.webAddress}</td>
                <td>${reg.email}</td>
                <td>
                    <fmt:formatDate value="${reg.dateReg}" pattern="dd.MM.yyyy HH:mm"/>
                </td>
                <td>
                    <a href="/admin/registration/add.html?regId=${reg.id}">Зарегистрировать</a>&nbsp;&nbsp;
                    <a onclick="return confirm('Вы действительно хотите удалить запись?')" href="/admin/registration/deleteReg.html?regId=${reg.id}">Удалить</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    </c:if>

</my:adminpage>