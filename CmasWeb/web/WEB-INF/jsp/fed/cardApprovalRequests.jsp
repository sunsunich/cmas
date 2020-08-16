<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<jsp:useBean id="requestStatuses" scope="request" type="org.cmas.entities.cards.CardApprovalRequestStatus[]"/>
<jsp:useBean id="requests" scope="request" type="java.util.List<org.cmas.entities.cards.CardApprovalRequest>"/>
<jsp:useBean id="count" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.cards.CardApprovalRequestSearchFormObject"/>

<my:fed_adminpage title="Certificate approval requests"
                  customScripts="">

    <h2>Certificate approval requests</h2>

    <div id="progressText"></div>
    <div id="fileUploadError" class="error"></div>
    <div class="fileUpload-progress-container">
        <div class="fileUpload-progress-outer" id="progressOuter" style="display: none">
            <div id="progress" class="fileUpload-progress"></div>
        </div>
    </div>

    <div>
        <div style="float: left">
            <ff:form submitText="Find" action="/fed/cardApprovalRequests.html" method="GET" noRequiredText="true">
                <div>Filter by Diver</div>
                <ff:input path="email" label="E-mail" maxLen="250" required="false"/>
                <ff:input path="firstName" label="First Name" maxLen="250" required="false"/>
                <ff:input path="lastName" label="Last Name" maxLen="250" required="false"/>
                <ff:select path="status" label="Filter by request status" options="${requestStatuses}" itemValue="name"
                           itemLabel="name"/>
                <input type="hidden" name="sort" value="${command.sort}"/>
                <input type="hidden" name="dir" value="${command.dir}"/>
            </ff:form>
        </div>
    </div>


    <c:choose>
        <c:when test="${!empty requests}">
            <c:set var="initURL" value="/fed/cardApprovalRequests.html"/>
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
            <c:if test="${!empty command.status}">
                <c:set var="url" value="${url}status=${command.status}&"/>
                <c:set var="urlEmpty" value="false"/>
            </c:if>
            <table border="0" cellpadding="4" cellspacing="2" style="border-collapse: collapse;">
                <tr class="infoHeader">
                    <th><my:sort url="${url}" title="Request date" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="createDate"/></th>
                    <th><my:sort url="${url}" title="Diver's email" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="email"/>
                    </th>
                    <th><my:sort url="${url}" title="First name" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="firstName"/>
                    </th>
                    <th><my:sort url="${url}" title="Last name" dir="${command.dir}" columnNumber="${command.sort}"
                                 sortColumn="lastName"/>
                    </th>
                    <th>Date of birth</th>
                    <th>Request status</th>
                    <th>Front image</th>
                    <th>Back image</th>
                    <th>Action</th>
                </tr>
                <c:forEach items="${requests}" var="request" varStatus="st">
                    <tr style="border-bottom: 1pt solid black; ">
                        <td><fmt:formatDate value="${request.createDate}" pattern="dd.MM.yyyy"/></td>
                        <td class="email"><a href="mailto:${request.diver.email}">${request.diver.email}</a></td>
                        <td>${request.diver.firstName}</td>
                        <td>${request.diver.lastName}</td>
                        <td><fmt:formatDate value="${request.diver.dob}" pattern="dd.MM.yyyy"/></td>
                        <td>${request.status.name}</td>
                        <td>
                            <img height="150px"
                                 src="${pageContext.request.contextPath}${cardApprovalRequestImagesRoot}${request.frontImage.fileUrl}"/>
                        </td>
                        <td>
                            <c:if test="${request.backImage != null}">
                                <img height="150px"
                                     src="${pageContext.request.contextPath}${cardApprovalRequestImagesRoot}${request.backImage.fileUrl}"/>
                            </c:if>
                        </td>
                        <td>
                            <button type="button" class="positive-button"
                                    onclick="window.location = '/fed/viewCardApprovalRequest.html?requestId=${request.id}'">
                                View request
                            </button>
                            <c:if test="${request.status.name == 'NEW'}">
                                <button type="button" class="negative-button"
                                        onclick="window.location = '/fed/declineCardApprovalRequest.html?requestId=${request.id}'">
                                    Decline request
                                </button>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            No requests found.
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
        <pg:param name="status"/>
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
