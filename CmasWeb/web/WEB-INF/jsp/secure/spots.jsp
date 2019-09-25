<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<my:securepage title="cmas.face.index.header" hideFooter="true"
               activeMenuItem="spots"
               customScripts="js/model/spots_model.js,js/controller/country_controller.js,js/controller/spots_controller.js">

    <script type="application/javascript">
        <c:if test="${logbookEntryId != null}">
        spots_model.logbookEntryId = "${logbookEntryId}";
        </c:if>
        <c:choose>
        <c:when test="${diver.diverRegistrationStatus.name == 'CMAS_FULL' || diver.diverRegistrationStatus.name == 'GUEST'}">
        var isPaid = true;
        </c:when>
        <c:otherwise>
        var isPaid = false;
        </c:otherwise>
        </c:choose>
    </script>

    <div class="menu-left" id="spotsMenu">
        <div class="menu-title">
            <s:message code="cmas.face.spots.chooseSpot"/>
        </div>
        <div class="dialog-content" id="noSpotsText">
            <s:message code="cmas.face.spots.empty"/>
        </div>

        <div id="foundSpots"></div>
        <c:if test="${diver.diverRegistrationStatus.name == 'CMAS_FULL'}">
            <div class="dialog-content">
                <s:message code="cmas.face.spots.hint"/>
            </div>
        </c:if>
        <c:if test="${logbookEntryId != null}">
            <div class="menu-row clearfix">
                <div class="menuElemLeft">
                    <a href="${pageContext.request.contextPath}/secure/editLogbookRecordForm.html?logbookEntryId=${logbookEntryId}">
                        <img src="${pageContext.request.contextPath}/i/arrow_right_gray.png?v=${webVersion}"/>
                    </a>
                </div>
                <div class="menuElemLeft">
                    <a href="${pageContext.request.contextPath}/secure/editLogbookRecordForm.html?logbookEntryId=${logbookEntryId}">
                        <span><s:message code="cmas.face.spots.back"/></span>
                    </a>
                </div>
            </div>
        </c:if>
    </div>

    <div id="map">

    </div>

    <my:dialog id="createSpot"
               title="cmas.face.spots.create.title"
               buttonText="cmas.face.spots.create.sumbit">
        <div class="form-row">
            <input id="createSpot_latinName" type="text" class="clearfix"
                   placeholder="<s:message code="cmas.face.spots.create.latinName.label"/>"/>
            <img src="/i/ic_error.png" class="error-input-ico" id="createSpot_error_ico_latinName"
                 style="display: none">
            <div class="error" id="createSpot_error_latinName"></div>
        </div>
        <div class="form-row">
            <input id="createSpot_name" type="text" class="clearfix"
                   placeholder="<s:message code="cmas.face.spots.create.name.label"/>"/>
            <img src="/i/ic_error.png" class="error-input-ico" id="createSpot_error_ico_name"
                 style="display: none">
            <div class="error" id="createSpot_error_name"></div>
        </div>
        <div class="form-row">
            <input id="createSpot_toponymName" type="text" class="clearfix"
                   placeholder="<s:message code="cmas.face.spots.create.toponym.label"/>"/>
            <img src="/i/ic_error.png" class="error-input-ico" id="createSpot_error_ico_toponymName"
                 style="display: none">
            <div class="error" id="createSpot_error_toponymName"></div>
        </div>
        <div class="form-row">
            <select name="createSpot_countryCode" id="createSpot_countryCode" size=1 onChange="" style="width: 100%">
                <c:forEach items="${countries}" var="country">
                    <option value='${country.code}'>${country.name}</option>
                </c:forEach>
            </select>
            <img src="/i/ic_error.png" class="error-input-ico" id="createSpot_error_ico_countryCode"
                 style="display: none">
            <div class="error" id="createSpot_error_countryCode"></div>
            <div class="error" style="display: none" id="createSpot_error">
            </div>
        </div>
    </my:dialog>

    <my:dialog id="deleteSpot"
               title="cmas.face.spots.delete.title"
               buttonText="cmas.face.spots.delete">
        <s:message code="cmas.face.spots.delete.text"/>
    </my:dialog>

    <script type="text/javascript"
            src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBKmJC1xgN8Did9WLS_5VeUIi3WBqY6fdQ">
    </script>

</my:securepage>

