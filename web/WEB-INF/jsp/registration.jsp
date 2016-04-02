<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>

<my:basePage title="Members' Area" indexpage="false"
             customScripts="js/lib/select2.full.js,js/model/registration_model.js,js/controller/registration_controller.js"
             customCSSFiles="c/select2.css"
        >

    <div class="content" id="Content">
        <div class="form-logo">
            <img src="${pageContext.request.contextPath}/i/logo.png">
        </div>
        <form id="regForm" action="">
            <div class="reg-block">
                <div class="form-row">
                    <img class="email-input-ico">
                    <select name="oplCountries" id="oplCountries" style="width: 100%" size=1 onChange="">
                        <option value=''><s:message code="cmas.face.registration.form.label.country"/></option>
                        <c:forEach items="${countries}" var="country">
                            <option value='${country.code}'>${country.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-row">
                    <img class="email-input-ico">
                    <input id="firstNameField" type="text"
                           placeholder="<s:message code="cmas.face.registration.form.label.firstName"/>"/>

                </div>
                <div class="form-row">
                    <img class="email-input-ico">
                    <input id="lastNameField" type="text"
                           placeholder="<s:message code="cmas.face.registration.form.label.lastName"/>"/>

                </div>
                <div class="form-row">
                    <img class="email-input-ico">
                    <input id="dobField" type="date"
                           placeholder="<s:message code="cmas.face.registration.form.label.lastName"/>"/>

                </div>
            </div>
            <div class="error" style="display: none" id="error">
                <s:message code="cmas.face.login.form.errorMessage"/>
            </div>
            <button class="form-button reg-button" id="regLink">
                <s:message code="cmas.face.login.form.link.reg"/>
            </button>
            <div class="pass_link">
                <a class="link" href="${pageContext.request.contextPath}">
                    <s:message code="cmas.face.registration.form.link.login"/>
                </a>
            </div>
        </form>
    </div>

</my:basePage>
