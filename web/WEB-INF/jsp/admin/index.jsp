<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<my:adminpage title="Пользователи системы">
    <h2>Пользователи системы</h2>

    <ff:form submitText="Найти" action="/admin/index.html" method="GET">

        <ff:input path="email" label="E-mail" maxLen="250" required="false"/>
        <ff:input path="shopName" label="Имя магазина" maxLen="250" required="false"/>
        <input type="hidden" name="sort" value="${command.sort}"/>
        <input type="hidden" name="dir" value="${command.dir}"/>
    </ff:form>


    <c:choose>
        <c:when test="${!empty users}">
            <c:set var="initURL" value="/admin/index.html"/>
            <c:set var="url" value="${initURL}?"/>
            <c:set var="urlEmpty" value="true"/>

            <c:if test="${!empty command.email}">
                <c:set var="url" value="${url}email=${command.email}&"/>
                <c:set var="urlEmpty" value="false"/>
            </c:if>
            <c:if test="${!empty command.shopName}">
                <c:set var="url" value="${url}shopName=${command.shopName}&"/>
                <c:set var="urlEmpty" value="false"/>
            </c:if>
            <table border="0" cellpadding="4" cellspacing="2">
                <tr class="infoHeader">
                    
                    <th><my:sort url="${url}" title="Имя магазина" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="shopName"/></th>
                    <th><my:sort url="${url}" title="E-mail" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="email"/></th>

                    <th><my:sort url="${url}" title="Дата регистрации" dir="${command.dir}"
                                 columnNumber="${command.sort}" sortColumn="dateReg"/></th>
                    <th align="center">Действия</th>
                </tr>
                <c:forEach items="${users}" var="user" varStatus="st">
                    <tr class="info" <c:if test="${!user.enabled}"> style="color:#999999"</c:if>>
                        <td>
                            <a onclick="return confirm('Вы действительно хотите переключиться на данного пользователя?');"
                               href="/admin/toUser.html?userId=${user.nullableId}">${user.shopName}</a><br/>
                        <td><a href="mailto:${user.email}">${user.email}</a></td>

                        <td>
                            <fmt:formatDate value="${user.dateReg}" pattern="dd.MM.yyyy HH:mm"/>
                        </td>
                            <%--<td>--%>
                            <%--<fmt:formatDate value="${user.lastLoggedTime}" pattern="dd.MM.yyyy HH:mm"/>--%>
                            <%--</td>--%>
                        <td nowrap>
                            <my:info url="/admin/userInfo.html?userId=${user.nullableId}" alt="Информация о клиенте"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <my:edit url="/admin/loadUser.html?userId=${user.nullableId}" alt="Редактировать"/>&nbsp;
                            <my:passwd url="/admin/passwdForm.html?userId=${user.nullableId}"/>&nbsp;
                            <my:delete url="/admin/deleteUser.html?userId=${user.nullableId}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            Ничего не найдено.
        </c:otherwise>
    </c:choose>
    <br><br>
    <c:choose>
        <c:when test="${urlEmpty}">
            <c:set var="pagerURL" value="${initURL}"/>
        </c:when>
        <c:otherwise>
            <c:set var="pagerURL" value="${url}"/>
        </c:otherwise>
    </c:choose>

    <pg:pager url="${pagerURL}" maxIndexPages="20" maxPageItems="${command.limit}" items="${count}"
              export="currentPageNumber=pageNumber,offSet=pageOffset">
        <pg:param name="sort"/>
        <pg:param name="email"/>
        <pg:param name="shopName"/>
        <pg:param name="dir"/>
        <pg:index>
            <font face=Helvetica size="-1">Найдено записей (${count}):
                <pg:prev>&nbsp;<a href="${pageUrl}">[&lt;&lt;]</a></pg:prev>
                <pg:pages>
                    <c:if test="${pageNumber < 10}">
                        &nbsp;
                    </c:if>
                    <c:choose>
                        <c:when test="${pageNumber == currentPageNumber}">
                            <b>${pageNumber}</b>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageUrl}">${pageNumber}</a>
                        </c:otherwise>
                    </c:choose>
                </pg:pages>
                <pg:next>&nbsp;<a href="${pageUrl}">[&gt;&gt;]</a></pg:next>
                <br></font>
        </pg:index>
    </pg:pager>
</my:adminpage>
