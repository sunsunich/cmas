<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="command" scope="request" type=" org.cmas.presentation.model.registration.DiverVerificationFormObject"/>
<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="captchaError" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="reCaptchaPublicKey" scope="request" type="java.lang.String"/>

<jsp:useBean id="isSuccessFormSubmit" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="divers" scope="request" type="java.util.List<org.cmas.entities.diver.Diver>"/>

<my:nonsecurepage title="cmas.face.index.header"
                  doNotDoAuth="true"
                  customScripts="js/model/profile_model.js,js/controller/country_controller.js,js/controller/panel_resize_controller.js,js/controller/cards_controller.js,js/controller/diver_verification_controller.js,https://www.google.com/recaptcha/api.js"
>
    <script type="application/javascript">

        var divers_cards = [];
        <c:if test="${divers != null && not empty divers}">
        <c:forEach items="${divers}" var="diver">
        var cmas_cardIds_${diver.id} = [];
        <c:forEach items="${diver.cards}" var="card" varStatus="st">
        cmas_cardIds_${diver.id}[${st.count - 1}] = "${card.id}";
        </c:forEach>
        divers_cards.push({'diverId': '${diver.id}', 'cardIds': cmas_cardIds_${diver.id}});
        </c:forEach>
        </c:if>
        $(document).ready(function () {
            diver_verification_controller.init('${command.country}', divers_cards);
        });
    </script>
    <script type="application/javascript">
        var reCaptchaOnLoadCallback = function () {
            diver_verification_controller.renderRecaptcha('${reCaptchaPublicKey}');
        };
    </script>

    <script src="https://www.google.com/recaptcha/api.js?onload=reCaptchaOnLoadCallback&render=explicit" defer>
    </script>

    <div class="content-center">
        <div class="panel panel-header">
            <span class="header2-text"><s:message code="cmas.face.diver.verification.header"/></span>
        </div>
        <div class="panel panel-header">
            <form:form id="diverVerificationForm"
                       action="${pageContext.request.contextPath}/diver-verification-submit.html"
                       method="POST">
                <div class="form-row">Please fill in all the fields below to verify the diver</div>
                <div class="horizontalRow">
                    <div class="form-row form-elem-large">
                        <input id="diverVerification_name"
                               style="width: 100%"
                               name="name" type="text" value="${command.name}"
                               placeholder="<s:message code="cmas.face.registration.form.label.name"/>"
                        />
                        <img src="${pageContext.request.contextPath}/i/ic_error.png?v=${webVersion}"
                             class="error-input-ico" id="diverVerification_error_ico_name"
                             style="display: none">
                        <div class="error" id="diverVerification_error_name">
                            <s:bind path="name">
                                <c:if test="${status.error}">
                                    <form:errors path="name" htmlEscape="true"/>
                                </c:if>
                            </s:bind>
                        </div>
                    </div>
                    <div class="form-row form-elem-large">
                        <input id="diverVerification_dob"
                               style="width: 100%"
                               name="dob" type="text" value="${command.dob}"
                               placeholder="<s:message code="cmas.face.registration.form.label.dob"/>"
                        />
                        <img src="${pageContext.request.contextPath}/i/ic_calendar.png?v=${webVersion}"
                             class="error-input-ico" id="diverVerification_ico_dob">
                        <img src="${pageContext.request.contextPath}/i/ic_error.png?v=${webVersion}"
                             class="error-input-ico" id="diverVerification_error_ico_dob"
                             style="display: none">
                        <div class="error" id="diverVerification_error_dob">
                            <s:bind path="dob">
                                <c:if test="${status.error}">
                                    <form:errors path="dob" htmlEscape="true"/>
                                </c:if>
                            </s:bind>
                        </div>
                    </div>

                    <div class="form-row form-elem-large">
                        <select name="country" id="diverVerification_country" style="width: 100%">
                            <c:forEach items="${countries}" var="country">
                                <c:choose>
                                    <c:when test="${command.country == country.code}">
                                        <option value='${country.code}' selected="selected">${country.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value='${country.code}'>${country.name}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                        <img src="/i/ic_error.png" class="error-input-ico" id="diverVerification_error_ico_country"
                             style="display: none">
                        <div class="error" id="diverVerification_error_country">
                            <s:bind path="country">
                                <c:if test="${status.error}">
                                    <form:errors path="country" htmlEscape="true"/>
                                </c:if>
                            </s:bind>
                        </div>
                    </div>
                </div>
                <div class="form-row">
                    <div id="diverVerification_captcha">
                    </div>
                    <div class="error" id="diverVerification_error">
                        <c:if test="${captchaError}">
                            <s:message code="cmas.face.captcha.incorrect"/>
                        </c:if>
                    </div>
                </div>
                <div class="clearfix">
                    <button class="positive-button form-item-left form-elem-large" id="diverVerificationSubmit">
                        <s:message code="cmas.face.findDiver.form.submitText"/>
                    </button>
                </div>
            </form:form>
        </div>
        <c:if test="${isSuccessFormSubmit}">
            <div class="panel">
                <c:choose>
                    <c:when test="${divers == null || empty divers}">
                        <div class="form-row">
                            <span class="basic-text"><s:message code="cmas.face.diver.verification.noDivers"/></span>
                        </div>
                        <div class="header3-text question">
                            <s:message code="cmas.face.diver.verification.noDivers.reasons"/>
                        </div>
                        <div class="basic-text answer">
                            <ul class="purchased-feature-list">
                                <li><s:message code="cmas.face.diver.verification.noDivers.reason1"/></li>
                                <li><s:message code="cmas.face.diver.verification.noDivers.reason2"/></li>
                            </ul>
                        </div>
                        <div class="form-row">
                            <span class="basic-text"><s:message
                                    code="cmas.face.diver.verification.noDivers.advice"/></span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${divers}" var="diver">
                            <c:if test="${diver.primaryPersonalCard !=null}">
                                <c:set var="cardUrl" value="${diver.primaryPersonalCard.imageUrl}"/>
                            </c:if>

                            <c:forEach items="${diver.cards}" var="card">
                                <c:if test="${card.cardType.name == 'APNOEA'}">
                                    <c:set var="isApnea" value="true"/>
                                    <c:set var="cardUrl" value="${card.imageUrl}"/>
                                </c:if>
                            </c:forEach>

                            <div id="${diver.id}_foundDiver" class="foundFriendList-elem clearfix">
                                <div class="friendList-elem-left">
                                    <c:choose>
                                        <c:when test="${diver.userpicUrl == null}">
                                            <div class="friendList-no-userpic friendList-userpic-margin">
                                                <img src="/i/no-userpic-ico.png?v=${webVersion}"/>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="userpic-ico friendList-userpic friendList-userpic-margin">
                                                <div class="rounded friendList-userpic"
                                                     style='background : url("${userpicRoot}${diver.userpicUrl}") center no-repeat; background-size: cover'>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="friendList-elem-left">
                                    <div>
                                        <span class="basic-text friendList-text">
                                            <b>${diver.firstName} ${diver.lastName}</b>
                                        </span>
                                    </div>
                                    <div>
                                        <c:choose>
                                            <c:when test="${diver.diverLevel == 'ONE_STAR'}">
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                            </c:when>
                                            <c:when test="${diver.diverLevel == 'TWO_STAR'}">
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                            </c:when>
                                            <c:when test="${diver.diverLevel == 'THREE_STAR'}">
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                            </c:when>
                                            <c:when test="${diver.diverLevel == 'FOUR_STAR'}">
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                                <img class="friendList-star"
                                                     src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                                            </c:when>
                                        </c:choose>
                                        <c:if test="${isApnea}">
                                            <span class="secondary-large-text friendList-text convert-with-labels"><s:message
                                                    code="cmas.face.logbook.apnea"/></span>&nbsp;
                                        </c:if>
                                        <c:if test="${diver.diverType == 'INSTRUCTOR'}">
                                            <span class="secondary-large-text friendList-text convert-with-labels"><s:message
                                                    code="cmas.face.instructor.title"/></span>&nbsp;
                                        </c:if>
                                        <span class="secondary-large-text friendList-text convert-with-labels">${diver.diverRegistrationStatus}</span>
                                    </div>
<%--                                    <div class="friendList-elem-left">--%>
<%--                                        <img class="friendList-ico"--%>
<%--                                             src="${pageContext.request.contextPath}/i/ic_calendar.png?v=${webVersion}"/>--%>
<%--                                        <span class="secondary-large-text friendList-text">--%>
<%--                                            <fmt:formatDate value="${diver.dob}" pattern="dd/MM"/>--%>
<%--                                        </span>--%>
<%--                                    </div>--%>
                                    <div class="friendList-elem-right">
                                        <span class="secondary-large-text friendList-text">${diver.country.name}</span>
                                    </div>
                                    <c:if test="${diver.primaryPersonalCard != null}">
                                        <div class="clearfix">
                                            <div class="friendList-elem-left">
                                                <span class="secondary-large-text friendList-text">${diver.primaryPersonalCard.printNumber}</span>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                                <c:if test="${cardUrl != null}">
                                    <div class="found-diver-card-image">
                                        <img class="form-item-right" src="${cardsRoot}${cardUrl}"
                                             style="margin-right: 15px;"/>
                                        <c:if test="${not empty diver.cards}">
                                            <button class="positive-button form-item-right"
                                                    id="${diver.id}_showAllCertificates"
                                                    style="margin-right: 15px; margin-top: 15px; margin-left: 8px">
                                                SHOW ALL CERTIFICATES
                                            </button>
                                        </c:if>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </div>
    <c:if test="${isSuccessFormSubmit && divers != null && not empty divers}">
        <c:forEach items="${divers}" var="diver">
            <my:dialog id="${diver.id}_cards"
                       title="${diver.firstName} ${diver.lastName} Certificates"
                       buttonText="cmas.face.dialog.close"
            >
                <div class="clearfix">
                    <c:forEach items="${diver.cards}" var="card">
                        <div class="content-card">
                            <div class="card-container">
                                <img id="${card.id}" src="${cardsRoot}${card.imageUrl}"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </my:dialog>
        </c:forEach>
    </c:if>
</my:nonsecurepage>
