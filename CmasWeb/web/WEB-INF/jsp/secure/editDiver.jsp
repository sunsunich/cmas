<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.user.DiverFormObject"/>
<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="areas" scope="request" type="org.cmas.entities.diver.AreaOfInterest[]"/>

<my:securepage title="cmas.face.editDiver.form.page.title"
               activeMenuItem="editDiver"
               customScripts="js/model/profile_model.js,js/controller/registration_flow_controller.js,js/controller/edit_diver_controller.js"
>
    <script type="application/javascript">
        <my:enumToJs enumItems="${areas}" arrayVarName="profile_model.areas"/>

        $(document).ready(function () {
            edit_diver_controller.init(
                '${command.areaOfInterest}',
                '${command.countryCode}',
                '${diver.diverRegistrationStatus.name}' == 'CMAS_FULL'
                || '${diver.diverRegistrationStatus.name}' == 'CMAS_BASIC'
            );
        });
    </script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">
        </div>
        <div class="formWrapper" id="formWrapper">
            <form id="editDiverForm" action="">
                <div id="editDiverBlock">
                    <div class="header1-text">
                        <s:message code="cmas.face.editDiver.form.header"/>
                    </div>
                    <div class="form-row"></div>
                    <div class="form-row">
                        <input id="editDiver_firstName" type="text" value="${command.firstName}"
                               placeholder="<s:message code="cmas.face.registration.form.label.firstName"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="editDiver_error_ico_firstName"
                             style="display: none">
                        <div class="error" id="editDiver_error_firstName"></div>
                    </div>
                    <div class="form-row">
                        <input id="editDiver_lastName" type="text" value="${command.lastName}"
                               placeholder="<s:message code="cmas.face.registration.form.label.lastName"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="editDiver_error_ico_lastName"
                             style="display: none">
                        <div class="error" id="editDiver_error_lastName"></div>
                    </div>
                    <div class="form-row">
                        <input id="editDiver_dob" type="text" autocomplete="off" name="date_of_birth"
                               value="${command.dob}"
                               placeholder="<s:message code="cmas.face.registration.form.label.dob"/>"/>
                        <img src="/i/ic_calendar.png" class="error-input-ico" id="editDiver_ico_dob">
                        <img src="/i/ic_error.png" class="error-input-ico" id="editDiver_error_ico_dob"
                             style="display: none">
                        <div class="error" id="editDiver_error_dob"></div>
                    </div>
                    <div class="form-row">
                        <select name="countryCode" id="editDiver_countryCode" size=1 onChange="">
                            <c:forEach items="${countries}" var="country">
                                <c:choose>
                                    <c:when test="${command.countryCode == country.code}">
                                        <option value='${country.code}' selected="selected">${country.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value='${country.code}'>${country.name}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>`
                        </select>
                        <img src="/i/ic_error.png" class="error-input-ico" id="editDiver_error_ico_countryCode"
                             style="color: #f4225b; display: none">
                        <div class="error" id="editDiver_error_countryCode"></div>
                    </div>
                    <div class="form-description" id="cmasDiversInfo" style="display: none">
                        <s:message code="cmas.face.editDiver.cmasDiver"/>
                    </div>
                    <div class="form-row">
                        <select name="editDiver_areaOfInterest" id="editDiver_areaOfInterest" size=1 onChange="">
                        </select>
                        <img src="/i/ic_error.png" class="error-input-ico" id="editDiver_error_ico_areaOfInterest"
                             style="display: none">
                        <div class="error" id="editDiver_error_areaOfInterest"></div>
                    </div>

                    <button class="positive-button form-item-right form-button-single" id="editDiverButton">
                        <s:message code="cmas.face.editDiver.form.submitText"/>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/requestImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/requestImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/requestImage.png 1x, /i/requestImage@2x.png 2x, /i/requestImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/requestImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/requestImageMob.png 1x, /i/requestImageMob@2x.png 2x, /i/requestImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="editDiverImageBackground" alt="change email background" style="display: none">
    </picture>

    <my:dialog id="diverSaveSuccess"
               title="cmas.face.fed.diver.saveTitle"
               buttonText="cmas.face.dialog.ok">
        <div><s:message code="cmas.face.editDiver.saveText"/></div>
    </my:dialog>

</my:securepage>
