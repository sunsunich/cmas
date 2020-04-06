<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<jsp:useBean id="diverTypes" scope="request" type="org.cmas.entities.diver.DiverType[]"/>
<jsp:useBean id="users" scope="request" type="java.util.List<org.cmas.entities.diver.Diver>"/>
<jsp:useBean id="count" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="showErrors" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.user.AddingToFederationFormObject"/>

<my:fed_adminpage title="Find divers with Demo/Guest accounts" customScripts="/js/controller/fed_add_diver_to_fed_controller.js">
    <script type="application/javascript">
        <c:choose>
        <c:when test="${command.dob == null}">
        var dob = "";
        </c:when>
        <c:otherwise>
        var dob = "${command.dob}";
        </c:otherwise>
        </c:choose>
    </script>


    <h2>Find divers with Demo/Guest accounts to add to your federation</h2>

    <div>
        <div style="float: left">
            <ff:form submitText="Find" action="/fed/addDiversToFederationSubmit.html" method="GET"
                     noRequiredText="false">
                <ff:input path="firstName" label="First Name" maxLen="250" required="true"/>
                <ff:input path="lastName" label="Last Name" maxLen="250" required="true"/>
                <ff:input id="dob" path="dob" label="Date of birth" maxLen="250" required="true"/>
                <input type="hidden" name="sort" value="${command.sort}"/>
                <input type="hidden" name="dir" value="${command.dir}"/>
            </ff:form>
        </div>
    </div>

    <c:choose>
        <c:when test="${!empty users}">
            <c:set var="initURL" value="/fed/addDiversToFederationSubmit.html"/>
            <c:set var="url" value="${initURL}?"/>
            <c:set var="urlEmpty" value="true"/>

            <c:if test="${!empty command.dob}">
                <c:set var="url" value="${url}dob=${command.dob}&"/>
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
                    <th><my:sort url="${url}" title="First name" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="firstName"/>
                    </th>
                    <th><my:sort url="${url}" title="Last name" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="lastName"/>
                    </th>
                    <th>Date of birth</th>
                    <th>Country</th>
                    <th>Action</th>
                </tr>
                <c:forEach items="${users}" var="user" varStatus="st">
                    <tr class="info" <c:if test="${!user.enabled}"> style="color:#999999"</c:if>>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td><fmt:formatDate value="${user.dob}" pattern="dd.MM.yyyy"/></td>
                        <td>${user.country.name}</td>
                        <td>
                            <button type="button"
                                    onclick="window.location = '/fed/addToFederation.html?diverId=${user.id}'">
                                Add diver to your federation
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            <c:if test="${showErrors}">
                <div>
                    No users found.
                </div>
            </c:if>
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
</my:fed_adminpage>
