<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>

<my:basePage title="cmas.face.index.header" indexpage="false"
             customScripts="js/model/registration_model.js,js/controller/registration_controller.js"
        >
    <script>
        var labels = {
            "cmas.face.registration.form.label.country": '<s:message code="cmas.face.registration.form.label.country"/>'
        }
    </script>

    <div class="content" id="Content">
        <div class="form-logo">
            <a href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/i/logo.png">
            </a>
        </div>
        <form id="regForm" action="">
            <div class="reg-block">
                <div class="form-row">
                    <select name="oplCountries" id="oplCountries" style="width: 100%" size=1 onChange="">
                        <option value=''><s:message code="cmas.face.registration.form.label.country"/></option>
                        <c:forEach items="${countries}" var="country">
                            <option value='${country.code}'>${country.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="error" id="error_country"></div>
                <div class="form-row">
                    <img class="name-input-ico">
                    <input id="firstNameField" type="text"
                           placeholder="<s:message code="cmas.face.registration.form.label.firstName"/>"/>
                </div>
                <div class="error" id="error_firstName"></div>
                <div class="form-row">
                    <img class="name-input-ico">
                    <input id="lastNameField" type="text"
                           placeholder="<s:message code="cmas.face.registration.form.label.lastName"/>"/>

                </div>
                <div class="error" id="error_lastName"></div>
                <div class="form-row">
                    <img class="calendar-input-ico">
                    <input id="dobField" type="text"
                           placeholder="<s:message code="cmas.face.registration.form.label.dob"/>"/>
                </div>
                <div class="error" id="error_dob"></div>
            </div>
            <div class="error" style="display: none" id="error">
            </div>
            <button class="form-button reg-button" id="regSubmit">
                <s:message code="cmas.face.login.form.link.reg"/>
            </button>
        </form>
        <div class="pass_link">
            <a class="link" href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.registration.form.link.login"/>
            </a>
        </div>
    </div>

    <div id="dialog" class="dialog" style="display: none">
        <img id="dialogClose" src="${pageContext.request.contextPath}/i/close.png"/>
        <p class="dialog-title"> <s:message code="cmas.face.registration.success.title"/></p>
        <p class="dialog-content" id="dialogContent">
            <s:message code="cmas.face.registration.success.text1"/>
            <br/>
            <s:message code="cmas.face.registration.success.text2"/>
        </p>
        <button class="form-button enter-button" id="dialogOk">
            <s:message code="cmas.face.registration.success.continue"/>
        </button>
    </div>

</my:basePage>
