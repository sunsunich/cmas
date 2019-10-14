<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<jsp:useBean id="roles" scope="request" type="org.cmas.entities.Role[]"/>
<jsp:useBean id="diverTypes" scope="request" type="org.cmas.entities.diver.DiverType[]"/>
<jsp:useBean id="countries" scope="request" type="org.cmas.entities.Country[]"/>

<jsp:useBean id="users" scope="request" type="java.util.List<org.cmas.entities.diver.Diver>"/>
<jsp:useBean id="count" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.user.UserSearchFormObject"/>

<my:adminpage title="Users of AquaLink">
    <h2>Users of AquaLink</h2>
    <ff:form submitText="Find" action="/admin/index.html" method="GET" noRequiredText="true">

        <ff:input path="email" label="E-mail" maxLen="250" required="false"/>
        <ff:input path="firstName" label="First Name" maxLen="250" required="false"/>
        <ff:input path="lastName" label="Last Name" maxLen="250" required="false"/>
        <ff:select path="userRole" label="User type" options="${roles}" itemValue="name" itemLabel="label"/>
        <ff:select path="diverType" label="Diver type" options="${diverTypes}" itemValue="name" itemLabel="name"/>
        <ff:select path="countryCode" label="Country" options="${countries}" itemValue="code" itemLabel="name"/>
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
            <c:if test="${!empty command.firstName}">
                <c:set var="url" value="${url}firstName=${command.firstName}&"/>
                <c:set var="urlEmpty" value="false"/>
            </c:if>
            <c:if test="${!empty command.lastName}">
                <c:set var="url" value="${url}lastName=${command.lastName}&"/>
                <c:set var="urlEmpty" value="false"/>
            </c:if>
            <table border="0" cellpadding="4" cellspacing="2">
                <tr class="infoHeader">
                    <th><my:sort url="${url}" title="E-mail" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="email"/>
                    </th>
                    <th><my:sort url="${url}" title="First name" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="firstName"/>
                    </th>
                    <th><my:sort url="${url}" title="Last name" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="lastName"/>
                    </th>
                    <th>Date of birth</th>
                    <th>Instructor or Diver</th>
                    <th>Diver Level</th>
                    <th>CMAS card number</th>
                    <th>Certificates</th>
                    <th><my:sort url="${url}" title="Registration date" dir="${command.dir}"
                                 columnNumber="${command.sort}" sortColumn="dateReg"/>
                    </th>
                    <th><my:sort url="${url}" title="Last profile edit" dir="${command.dir}"
                                 columnNumber="${command.sort}" sortColumn="lastAction"/>
                    </th>
                    <th align="center">Actions</th>
                </tr>
                <c:forEach items="${users}" var="user" varStatus="st">
                    <tr class="info" <c:if test="${!user.enabled}"> style="color:#999999"</c:if>>
                        <td class="email"><a href="mailto:${user.email}">${user.email}</a></td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td><fmt:formatDate value="${user.dob}" pattern="dd.MM.yyyy"/></td>
                        <td>${user.diverType.name}</td>
                        <td>${user.diverLevel.name}</td>
                        <td>
                            <c:choose>
                                <c:when test="${user.primaryPersonalCard == null}">
                                    Not registered at AquaLink
                                </c:when>
                                <c:otherwise>
                                    ${user.primaryPersonalCard.number}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:forEach var="card" items="${user.cards}">
                                <div>${card.printName}</div>
                                <br/>
                            </c:forEach>
                        </td>
                        <td>
                            <fmt:formatDate value="${user.dateReg}" pattern="dd.MM.yyyy HH:mm"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${user.lastAction}" pattern="dd.MM.yyyy HH:mm"/>
                        </td>
                        <td nowrap>
                            <c:if test="${user.role == 'ROLE_DIVER' && user.primaryPersonalCard == null }">
                                <span>WARNING: USER HAS NO PRIMARY CARD</span> <br/>
                            </c:if>
                            <a href="/admin/toUser.html?userId=${user.id}&userRole=${user.role.name}">Login as User</a>
                            <br/>
                            <c:if test="${user.role == 'ROLE_DIVER'}">
                                <a href="/admin/cloneUser.html?userId=${user.id}&userRole=${user.role.name}">Clone
                                    user</a>
                                <a href="/admin/testInsuranceForm.html?diverId=${user.id}">Test
                                    insurance</a>
                            </c:if>
                                <%--<my:info url="/admin/userInfo.html?userId=${user.id}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
                                <%--<my:edit url="/admin/loadUser.html?userId=${user.id}"/>&nbsp;--%>
                                <%--<my:passwd url="/admin/passwdForm.html?userId=${user.id}"/>&nbsp;--%>
                                <%--<my:delete url="/admin/deleteUser.html?userId=${user.id}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            No users found.
        </c:otherwise>
    </c:choose>
    <br><br>
    <%--<c:choose>--%>
    <%--<c:when test="${urlEmpty}">--%>
    <%--<c:set var="pagerURL" value="${initURL}"/>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
    <%--<c:set var="pagerURL" value="${url}"/>--%>
    <%--</c:otherwise>--%>
    <%--</c:choose>--%>

    <pg:pager url="${initURL}" maxIndexPages="20" maxPageItems="${command.limit}" items="${count}"
              export="currentPageNumber=pageNumber,offSet=pageOffset">
        <pg:param name="sort"/>
        <pg:param name="email"/>
        <pg:param name="firstName"/>
        <pg:param name="lastName"/>
        <pg:param name="dir"/>
        <pg:index>
            <font face=Helvetica size="-1">Users found (${count}):
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
