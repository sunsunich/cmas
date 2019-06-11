<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
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

    <script type="text/javascript" src="/js/model/insurance_request_model.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/controller/validation_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/controller/insurance_request_controller.js?v=${webVersion}"></script>

    <h2>Test insurance</h2>

    <script type="application/javascript">
        insurance_request_model.url = '/admin/testInsurance.html';
        <my:enumToJs enumItems="${genders}" arrayVarName="insurance_request_model.genders"/>

        insurance_request_model.insuranceRequest.diver.id = "${diver.id}";
    </script>

    <form action="">
        <div class="reg-block">
            <div class="form-row">
                <select name="insuranceRequest_gender" style="width: 272px" id="insuranceRequest_gender" size=1
                        onChange="">
                </select>
                <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_gender"
                     style="display: none">
                <div class="error" id="insuranceRequest_error_gender"></div>
            </div>
            <div class="form-row">
                <select name="insuranceRequest_country" id="insuranceRequest_country" size=1 onChange="">
                    <c:forEach items="${countries}" var="country">
                        <option value='${country.code}'>${country.name}</option>
                    </c:forEach>
                </select>
                <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_country"
                     style="display: none">
                <div class="error" id="insuranceRequest_error_country"></div>
            </div>
            <div class="form-row">
                <input id="insuranceRequest_region" type="text"
                       placeholder="<s:message code="cmas.loyalty.insurance.address.region"/>"/>
                <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_region"
                     style="display: none">
                <div class="error" id="insuranceRequest_error_region"></div>
            </div>
            <div class="form-row">
                <input id="insuranceRequest_zipCode" type="text"
                       placeholder="<s:message code="cmas.loyalty.insurance.address.zipCode"/>"/>
                <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_zipCode"
                     style="display: none">
                <div class="error" id="insuranceRequest_error_zipCode"></div>
            </div>
            <div class="form-row">
                <input id="insuranceRequest_city" type="text"
                       placeholder="<s:message code="cmas.loyalty.insurance.address.city"/>"/>
                <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_city"
                     style="display: none">
                <div class="error" id="insuranceRequest_error_city"></div>
            </div>
            <div class="form-row">
                <input id="insuranceRequest_street" type="text"
                       placeholder="<s:message code="cmas.loyalty.insurance.address.street"/>"/>
                <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_street"
                     style="display: none">
                <div class="error" id="insuranceRequest_error_street"></div>
            </div>
            <div class="form-row">
                <input id="insuranceRequest_house" type="text"
                       placeholder="<s:message code="cmas.loyalty.insurance.address.house"/>"/>
                <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_house"
                     style="display: none">
                <div class="error" id="insuranceRequest_error_house"></div>
            </div>
            <div class="form-row">
            </div>
            <button class="positive-button form-item-right form-button-single" id="insuranceRequestSubmit">
                <s:message code="cmas.face.registration.form.next"/>
            </button>
        </div>
    </form>

    <my:dialog id="insuranceRequestSuccess"
               title="cmas.loyalty.insurance.successTitle"
               buttonText="cmas.face.dialog.ok">
        <div><s:message code="cmas.loyalty.insurance.success"/></div>
    </my:dialog>

</my:adminpage>