<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="diverTypes" scope="request" type="org.cmas.entities.diver.DiverType[]"/>
<jsp:useBean id="diverStatuses" scope="request" type="org.cmas.entities.diver.DiverRegistrationStatus[]"/>
<jsp:useBean id="users" scope="request" type="java.util.List<org.cmas.entities.diver.Diver>"/>
<jsp:useBean id="count" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.user.UserSearchFormObject"/>

<my:fed_adminpage title="Divers"
                  customScripts="/js/model/fileUpload_model.js,/js/model/fed_billing_model.js,/js/controller/fed_billing_controller.js,/js/controller/fileUpload_controller.js,/js/controller/fed_index_controller.js">

    <h2>Your federation divers</h2>

    <ff:form submitText="Upload" action="/fed/uploadUsers.html" commandName="xlsFileFormObject" method="POST"
             noRequiredText="true" enctype="multipart/form-data" id="diverUpload">
        <ff:file path="file" label="Xls file with divers" inputId="diverUploadInput"/>
    </ff:form>

    <div id="progressText"></div>
    <div id="fileUploadError" class="error"></div>
    <div class="fileUpload-progress-container">
        <div class="fileUpload-progress-outer" id="progressOuter" style="display: none">
            <div id="progress" class="fileUpload-progress"></div>
        </div>
    </div>

    <div>
        <div style="float: left">
            <ff:form submitText="Find" action="/fed/index.html" method="GET" noRequiredText="true">
                <ff:input path="email" label="E-mail" maxLen="250" required="false"/>
                <ff:input path="firstName" label="First Name" maxLen="250" required="false"/>
                <ff:input path="lastName" label="Last Name" maxLen="250" required="false"/>
                <ff:select path="diverType" label="Diver type" options="${diverTypes}" itemValue="name"
                           itemLabel="name"/>
                <ff:select path="diverStatus" label="Account status" options="${diverStatuses}" itemValue="name"
                           itemLabel="name"/>
                <input type="hidden" name="sort" value="${command.sort}"/>
                <input type="hidden" name="dir" value="${command.dir}"/>
            </ff:form>
            <button type="button" style="margin: 0 83px" onclick="window.location = '/fed/addDiver.html'">
                Add new diver
            </button>
        </div>
        <div style="float: right; margin-right: 20px" id="payForDivers">
            <div><b>Divers to buy licence</b></div>
            <div id="diverFromPaymentList" style="margin-top: 16px; margin-bottom: 16px">

            </div>
            <button type="button" onclick="window.location = '/fed/payForDivers.html'">
                Pay for divers
            </button>
        </div>
    </div>


    <c:choose>
        <c:when test="${!empty users}">
            <c:set var="initURL" value="/fed/index.html"/>
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
            <c:if test="${!empty command.diverType}">
                <c:set var="url" value="${url}diverType=${command.diverType}&"/>
                <c:set var="urlEmpty" value="false"/>
            </c:if>
            <c:if test="${!empty command.diverStatus}">
                <c:set var="url" value="${url}diverStatus=${command.diverStatus}&"/>
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
                    <th>Account status</th>
                    <th><my:sort url="${url}" title="Date edited" dir="${command.dir}"
                                 columnNumber="${command.sort}" sortColumn="dateEdited"/></th>
                    <th>Global CMASDATA ID</th>
                    <th>Certificates</th>
                        <%--                    <th>CMAS certificate expires</th>--%>
                    <th><my:sort url="${url}" title="Registration date" dir="${command.dir}"
                                 columnNumber="${command.sort}" sortColumn="dateReg"/>
                    </th>
                    <th>Action</th>
                </tr>
                <c:forEach items="${users}" var="user" varStatus="st">
                    <tr class="info" <c:if test="${!user.enabled}"> style="color:#999999"</c:if>>
                        <td class="email"><a href="mailto:${user.email}">${user.email}</a></td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td><fmt:formatDate value="${user.dob}" pattern="dd.MM.yyyy"/></td>
                        <td>${user.diverType.name}</td>
                        <td>${user.diverLevel.name}</td>
                        <td>${user.diverRegistrationStatus.name}</td>
                        <td><fmt:formatDate value="${user.dateEdited}" pattern="dd.MM.yyyy HH:mm"/></td>
                        <td style="white-space: nowrap;">
                            <c:choose>
                                <c:when test="${user.primaryPersonalCard == null}">
                                    Not registered at AquaLink
                                </c:when>
                                <c:otherwise>
                                    ${user.primaryPersonalCard.printNumber}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:forEach var="card" items="${user.cards}">
                                <div>${card.printName} ${card.number}</div>
                                <br/>
                            </c:forEach>
                        </td>
                            <%--                        <td>--%>
                            <%--                            <fmt:formatDate value="${user.dateLicencePaymentIsDue}" pattern="dd.MM.yyyy HH:mm"/>--%>
                            <%--                        </td>--%>
                        <td>
                            <fmt:formatDate value="${user.dateReg}" pattern="dd.MM.yyyy HH:mm"/>
                        </td>
                        <td>
                            <button type="button" onclick="window.location = '/fed/editDiver.html?userId=${user.id}'">
                                Edit diver
                            </button>
                            <my:delete url="/fed/deleteDiver.html?userId=${user.id}"/>
                            <button type="button" class="addDiverToPaymentList" id="addDiverToPaymentList_${user.id}">
                                Buy licence
                            </button>
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
        <pg:param name="diverType"/>
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

    <my:dialog id="uploadSuccess"
               title="cmas.face.fed.diver.saveTitle"
               buttonText="cmas.face.dialog.ok">
        <div>
            <span><s:message code="cmas.face.fed.diver.uploadText1"/>&nbsp;</span><b id="diverCnt"></b>
            <span><s:message code="cmas.face.fed.diver.uploadText2"/></span>
        </div>
    </my:dialog>

</my:fed_adminpage>
