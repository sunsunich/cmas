<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<my:securepage title="cmas.face.index.header"
               customScripts="js/controller/registration_flow_controller.js"
               hideMenu="true"
>

    <script type="application/javascript">
        $(document).ready(function () {
            registration_flow_controller.init('simple', {backgroundImageId: 'regImageBackground'});
        });
    </script>
    <div class="content" id="Content">
        <div id="formImage" class="formImage">
            <my:advert diverRegistrationStatus="${diver.diverRegistrationStatus.name}"/>
        </div>
        <div class="formWrapper" id="formWrapper">
            <div id="welcomeBlock">
                <div class="header1-text">
                    <s:message code="cmas.face.first.login.form.header"/>
                </div>
                <div class="form-description">
                    <s:message code="cmas.face.first.login.form.account.created"/>
                    <c:choose>
                        <c:when test="${diver.diverRegistrationStatus.name == 'DEMO'}">
                            <s:message code="cmas.face.first.login.form.account.demo"/>
                        </c:when>
                        <c:when test="${diver.diverRegistrationStatus.name == 'CMAS_BASIC'}">
                            <s:message code="cmas.face.first.login.form.account.new_cmas_basic"/>
                        </c:when>
                    </c:choose>
                </div>
                <c:if test="${diver.generatedPassword != null}">
                    <div class="clearfix">
                        <div class="form-description">
                            <s:message code="cmas.face.first.login.form.changePassword"/>
                        </div>
                        <button class="positive-button form-item-left form-button-single"
                                onclick="return window.location = '/secure/editPassword.html'">
                            <s:message code="cmas.face.client.menu.changePass"/>
                        </button>
                    </div>
                </c:if>
                <br/>
                <br/>
                <div class="form-description">
                    <p><s:message code="cmas.face.first.login.form.getFullAccess.describtion"/></p>
                    <p><s:message code="cmas.face.first.login.form.getFullAccess"/></p>
                </div>
                <c:if test="${diver.diverRegistrationStatus.name == 'DEMO'}">
                    <button class="white-button form-item-left form-button-bigger"
                            onclick="return window.location = '/secure/chooseNoPayment.html'">
                        <s:message code="cmas.face.first.login.form.link.cmas_basic"/>
                    </button>
                </c:if>
                <button class="positive-button form-item-right form-button-smaller"
                        onclick="return window.location = '/secure/pay.html'">
                    <s:message code="cmas.face.first.login.form.link.cmas_full"/>
                </button>
            </div>
        </div>
    </div>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/regImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/regImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/regImage.png 1x, /i/regImage@2x.png 2x, /i/regImage@3x.png 3x" media="(min-width: 631px)">
        <source srcset="/i/regImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/regImageMob.png 1x, /i/regImageMob@2x.png 2x, /i/regImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="regImageBackground" alt="registration background" style="display: none">
    </picture>

</my:securepage>


