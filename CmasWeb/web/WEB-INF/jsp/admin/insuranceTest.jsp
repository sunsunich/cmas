<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<jsp:useBean id="insuranceRequest" scope="request" type="org.cmas.entities.loyalty.InsuranceRequest"/>
<jsp:useBean id="countries" scope="request" type="org.cmas.entities.Country[]"/>
<jsp:useBean id="genders" scope="request" type="org.cmas.entities.Gender[]"/>

<my:adminpage title="Test insurance">
    <link rel="stylesheet" type="text/css" href="/c/select2.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/select2-bootstrap.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/jquery-ui.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/jquery-ui.structure.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/jquery-ui.theme.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/jquery.timepicker.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/checkbox.css?v=${webVersion}" media="all"/>

    <link rel="stylesheet" type="text/css" href="/c/print.css?v=${webVersion}" media="print"/>
    <c:choose>
        <c:when test="${intrenal}">
            <link rel="stylesheet" type="text/css" href="/c/internal-form.css?v=${webVersion}" media="all"/>
        </c:when>
        <c:otherwise>
            <link rel="stylesheet" type="text/css" href="/c/form.css?v=${webVersion}" media="all"/>
        </c:otherwise>
    </c:choose>
    <link rel="stylesheet" type="text/css" href="/c/buttons.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/feed.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/menu.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/loader.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/styles.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/logbook.css?v=${webVersion}" media="all"/>

    <script src="/js/lib/html5shiv.min.js?v=${webVersion}"></script>
    <script src="/js/lib/html5shiv-printshiv.min.js?v=${webVersion}"></script>
    <![endif]-->
    <script type="text/javascript">
        function PrintMail(argName) {
            document.write('<A href="mailto:' + argName + '@cmasdata.org">' + argName + '@cmasdata.org</A>');
        }
    </script>

    <%--add localization hl param--%>

    <script type="text/javascript" src="/js/lib/modernizr-custom.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/json.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/jquery-1.12.2.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/jquery-ui.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/jquery.timepicker.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/ejs_production.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/select2.full.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/checkbox_patch.js?v=${webVersion}"></script>

    <script type="text/javascript">
        // Picture element HTML5 shiv
        document.createElement("picture");
    </script>
    <script type="text/javascript" src="/js/lib/pf.intrinsic.min.js?v=${webVersion}" async=""></script>
    <script type="text/javascript" src="/js/lib/picturefill.js?v=${webVersion}" async=""></script>

    <script type="text/javascript" src="/js/i18n/error_codes.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/i18n/labels.js?v=${webVersion}"></script>

    <script type="text/javascript" src="/js/util.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/model/basic_client.js?v=${webVersion}"></script>

    <script type="text/javascript" src="/js/controller/cookie_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/controller/loader_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/controller/validation_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/controller/error_dialog_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/controller/util_controller.js?v=${webVersion}"></script>


    <h2>Test insurance</h2>
    <ff:form submitText="Test" action="/admin/testInsurance.html" >
        <ff:select path="gender" label="Gender" options="${genders}"/>

<%--        Address --%>
        <ff:select path="countryCode" label="Country" options="${countries}" itemValue="code" itemLabel="name"/>
        <ff:input path="region" label="Region/Province/County (optional)" required="false"/>
        <ff:input path="zipCode" label="Zip/Post code" required="true"/>
        <ff:input path="city" label="City" required="true"/>
        <ff:input path="street" label="Street" required="true"/>
        <ff:input path="number" label="House number or name" required="true"/>

        <ff:hidden path="userId" />

    </ff:form>

    <form id="regForm" action="">
        <div class="reg-block" id="registrationBlock">
            <div class="form-row">
                <select name="gender" id="gender" size=1 onChange="">
                </select>
                <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_country"
                     style="display: none">
                <div class="error" id="reg_error_country"></div>
            </div>
            <div class="form-row">
                <input id="reg_firstName" type="text"
                       placeholder="<s:message code="cmas.face.registration.form.label.firstName"/>"/>
                <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_firstName"
                     style="display: none">
                <div class="error" id="reg_error_firstName"></div>
            </div>
            <div class="form-row">
                <input id="reg_lastName" type="text"
                       placeholder="<s:message code="cmas.face.registration.form.label.lastName"/>"/>
                <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_lastName"
                     style="display: none">
                <div class="error" id="reg_error_lastName"></div>
            </div>
            <div class="form-row">
                <input id="reg_dob" type="text"
                       placeholder="<s:message code="cmas.face.registration.form.label.dob"/>"/>
                <img src="/i/ic_calendar.png" class="error-input-ico" id="reg_ico_dob">
                <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_dob"
                     style="display: none">
                <div class="error" id="reg_error_dob"></div>
            </div>
            <div class="form-row">
                <select name="country" id="reg_country" size=1 onChange="">
                    <c:forEach items="${countries}" var="country">
                        <option value='${country.code}'>${country.name}</option>
                    </c:forEach>
                </select>
                <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_country"
                     style="display: none">
                <div class="error" id="reg_error_country"></div>
            </div>
            <div class="form-row">
                <input type="checkbox" name="termsAndCondAccepted" id="reg_termsAndCondAccepted"
                       class="css-checkbox">
                <label for="reg_termsAndCondAccepted"
                       class="css-label radGroup1 clr">
                            <span class="form-checkbox-label">
                                <s:message code="cmas.face.registration.form.label.termsAndCond"/>
                            </span>
                </label>
                <div class="error" id="reg_error_termsAndCondAccepted"></div>
                <div class="error" id="reg_error"></div>
            </div>
            <div class="form-row">
            </div>
            <button class="positive-button form-item-right form-button-single" id="regSubmit">
                <s:message code="cmas.face.registration.form.next"/>
            </button>
        </div>
        <div class="login-block" id="loginBlock" style="display: none">
            <div class="form-row">
                <input id="login_email" type="text"
                       placeholder="<s:message code="cmas.face.login.form.label.login"/>"/>
                <img src="/i/ic_error.png" class="error-input-ico" id="login_error_ico_email"
                     style="display: none">
                <div class="error" id="login_error_email"></div>
            </div>
            <div class="form-row">
                <input id="login_password" type="password"
                       placeholder="<s:message code="cmas.face.login.form.label.password"/>"/>
                <img src="/i/ic_error.png" class="error-input-ico" id="login_error_ico_password"
                     style="display: none">
                <div class="error" id="login_error_password"></div>
            </div>
            <div class="form-row">
                <input type="checkbox" name="noCertificate" id="login_remember" class="css-checkbox">
                <label for="login_remember"
                       class="css-label radGroup1 clr">
                            <span class="form-checkbox-label">
                                <s:message code="cmas.face.login.form.label.remember"/>
                            </span>
                </label>
                <div class="error" id="login_error">
                </div>
            </div>
            <button class="white-button form-item-left form-button-bigger" id="forgotPassword">
                <s:message code="cmas.face.login.form.link.lostPasswd"/>
            </button>
            <input type="submit" class="positive-button form-item-right form-button-smaller" id="loginSubmit"
                   value="<s:message code="cmas.face.login.form.submitText"/>"/>
        </div>
    </form>

</my:adminpage>