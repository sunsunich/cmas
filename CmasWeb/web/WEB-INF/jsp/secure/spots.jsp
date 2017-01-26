<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>

<my:securepage title="cmas.face.index.header"
               customScripts="js/model/spots_model.js,js/controller/country_controller.js,js/controller/spots_controller.js">

    <script type="application/javascript">
        <c:if test="${logbookEntryId != null}">
        spots_model.logbookEntryId = "${logbookEntryId}";
        </c:if>
    </script>

    <div class="menu-left" id="spotsMenu">
        <div class="menu-title">
            <s:message code="cmas.face.spots.chooseSpot"/>
        </div>
        <div class="dialog-content" id="noSpotsText">
            <s:message code="cmas.face.spots.empty"/>
        </div>

        <div id="foundSpots"></div>
        <div class="dialog-content">
            <s:message code="cmas.face.spots.hint"/>
        </div>
    </div>

    <div id="map">

    </div>

    <my:dialog id="createSpot"
               title="cmas.face.spots.create.title"
               buttonText="cmas.face.spots.create.sumbit">

        <div class="dialog-form-row">
            <input id="createSpotName" type="text"
                   placeholder="<s:message code="cmas.face.spots.create.name.label"/>"/>
        </div>
        <div class="error" id="createSpot_error_name"></div>

        <div class="dialog-form-row">
            <input id="createSpotToponymName" type="text"
                   placeholder="<s:message code="cmas.face.spots.create.toponym.label"/>"/>
        </div>
        <div class="error" id="createSpot_error_toponymName"></div>

        <div class="dialog-form-row">
            <select name="createSpotCountry" id="createSpotCountry" style="width: 100%" size=1 onChange="">
                <c:forEach items="${countries}" var="country">
                    <option value='${country.code}'>${country.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="error" id="createSpot_error_countryCode"></div>

        <div class="error" style="display: none" id="createSpot_error">
        </div>
    </my:dialog>

    <script type="text/javascript"
            src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBKmJC1xgN8Did9WLS_5VeUIi3WBqY6fdQ">
    </script>

</my:securepage>

