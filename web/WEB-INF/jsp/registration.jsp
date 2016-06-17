<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>

<my:basePage title="cmas.face.index.header" indexpage="false"
             customScripts="js/model/registration_model.js,js/controller/country_controller.js,js/controller/registration_controller.js"
        >

    <div class="content" id="Content">
        <div class="form-logo">
            <a href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/i/logo.png">
            </a>
        </div>
        <form id="regForm" action="">
            <div class="reg-block">
                <div class="form-row">
                    <select name="country" id="country" style="width: 100%" size=1 onChange="">
                        <c:forEach items="${countries}" var="country">
                            <option value='${country.code}'>${country.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="error" id="reg_error_country"></div>
                <div class="form-row">
                    <img class="name-input-ico">
                    <input id="firstNameField" type="text"
                           placeholder="<s:message code="cmas.face.registration.form.label.firstName"/>"/>
                </div>
                <div class="error" id="reg_error_firstName"></div>
                <div class="form-row">
                    <img class="name-input-ico">
                    <input id="lastNameField" type="text"
                           placeholder="<s:message code="cmas.face.registration.form.label.lastName"/>"/>

                </div>
                <div class="error" id="reg_error_lastName"></div>
                <div class="form-row">
                    <img class="calendar-input-ico">
                    <input id="dobField" type="text"
                           placeholder="<s:message code="cmas.face.registration.form.label.dob"/>"/>
                </div>
                <div class="error" id="reg_error_dob"></div>
            </div>
            <div class="error" style="display: none" id="reg_error">
            </div>
            <div class="button-container">
                <button class="form-button reg-button" id="regSubmit">
                    <s:message code="cmas.face.login.form.link.reg"/>
                </button>
            </div>
        </form>
        <div class="pass_link">
            <a class="link" href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.registration.form.link.login"/>
            </a>
        </div>
    </div>

    <my:dialog title="cmas.face.registration.success.title"
               buttonText="cmas.face.registration.success.continue">
        <s:message code="cmas.face.registration.success.text1"/>
        <br/>
        <s:message code="cmas.face.registration.success.text2"/>
    </my:dialog>

</my:basePage>
