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
                  customScripts="js/controller/country_controller.js,js/controller/panel_resize_controller.js,js/controller/diver_verification_controller.js,https://www.google.com/recaptcha/api.js"
>
    <script type="application/javascript">
        $(document).ready(function () {
            diver_verification_controller.init('${command.country}');
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
                <div class="horizontalRow">
                    <div class="form-row form-elem-large">
                        <input id="diverVerification_name"
                               style="width: 100%"
                               name="name" type="text" value="${command.name}"
                               placeholder="<s:message code="cmas.face.registration.form.label.name"/>"
                        />
                        <img src="/i/ic_error.png" class="error-input-ico" id="diverVerification_error_ico_name"
                             style="display: none">
                        <div class="error" id="diverVerification_error_lastName">
                            <s:bind path="name">
                                <c:if test="${status.error}">
                                    <form:errors path="name" htmlEscape="true"/>
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
                                    <div class="friendList-elem-left">
                                        <img class="friendList-ico"
                                             src="${pageContext.request.contextPath}/i/ic_calendar.png?v=${webVersion}"/>
                                        <span class="secondary-large-text friendList-text">
                                            <fmt:formatDate value="${diver.dob}" pattern="dd/MM/yyyy"/>
                                        </span>
                                    </div>
                                    <div class="friendList-elem-right">
                                        <span class="secondary-large-text friendList-text">${diver.country.name}</span>
                                    </div>
                                    <c:if test="${diver.primaryPersonalCard != null}">
                                        <div>
                                            <div class="friendList-elem-left">
                                                <span class="secondary-large-text friendList-text">${diver.primaryPersonalCard.printNumber}</span>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                                <c:if test="${cardUrl != null}">
                                    <div class="found-diver-card-image">
                                        <img src="${cardsRoot}${cardUrl}"/>
                                    </div>

                                </c:if>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </div>
</my:nonsecurepage>
