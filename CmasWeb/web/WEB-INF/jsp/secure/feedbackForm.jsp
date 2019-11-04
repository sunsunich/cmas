<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="command" scope="request" type=" org.cmas.presentation.model.FeedbackFormObject"/>
<jsp:useBean id="captchaError" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="reCaptchaPublicKey" scope="request" type="java.lang.String"/>




<my:securepage title="cmas.face.feedback.title"
               activeMenuItem="reportError"
               customScripts="js/controller/multiple_fileUpload_controller.js,js/controller/feedback_controller.js">

    <script type="application/javascript">
        var reCaptchaOnLoadCallback = function () {
            feedback_controller.renderRecaptcha('${reCaptchaPublicKey}');
        };
    </script>

    <script src="https://www.google.com/recaptcha/api.js?onload=reCaptchaOnLoadCallback&render=explicit" defer>
    </script>

    <div class="content-feedback" id="content">
        <div class="panel">
            <form:form id="feedbackForm"
                       enctype="multipart/form-data"
                       action="${pageContext.request.contextPath}/secure/submitFeedback.html"
                       method="POST">
                <div class="clearfix" id="feedbackBlock">
                    <div class="header1-text">
                        <c:choose>
                            <c:when test="${diveSpot != null}">
                                <s:message code="cmas.face.feedback.header.spot"/>
                                <input type="hidden" name="feedbackType" value="DIVE_SPOT">
                                <input type="hidden" name="diveSpotId" value="${diveSpot.id}">
                            </c:when>
                            <c:when test="${logbookEntry != null}">
                                <s:message code="cmas.face.feedback.header.logbook"/>
                                <input type="hidden" name="feedbackType" value="LOGBOOK_ENTRY">
                                <input type="hidden" name="logbookEntryId" value="${logbookEntry.id}">
                            </c:when>
                            <c:otherwise>
                                <s:message code="cmas.face.feedback.header.general"/>
                                <input type="hidden" name="feedbackType" value="GENERAL">
                            </c:otherwise>
                        </c:choose>

                    </div>
                    <div class="form-description">
                        <c:choose>
                            <c:when test="${diveSpot != null}">
                                <b>Name:</b> ${diveSpot.latinName} <br/>
                                <b>Country:</b> ${diveSpot.country.name} <br/>
                                <b>Coordinates:</b> latitude ${diveSpot.latitude}, longitude ${diveSpot.longitude} <br/>
                            </c:when>
                            <c:when test="${logbookEntry != null}">
                                <b>Name:</b> ${logbookEntry.name} <br/>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div>
                        <div class="form-row">
                        <textarea id="feedback_text" rows="6" name="text" style="width: 80%"
                                  placeholder="<s:message code="cmas.face.feedback.description"/>"></textarea>

                            <img src="/i/ic_error.png" class="error-input-ico" id="feedback_error_ico_text"
                                 style="display: none">
                            <div class="error" id="feedback_error_text">
                                <s:bind path="text">
                                    <c:if test="${status.error}">
                                        <form:errors path="text" htmlEscape="true" delimiter=", "/>
                                    </c:if>
                                </s:bind>
                            </div>
                        </div>
                    </div>
                    <div class="form-row clearfix-no-width">
                        <div id="feedback_captcha" class="capcha-block">
                        </div>
                        <div class="error" id="feedback_error">
                            <c:if test="${captchaError}">
                                <s:message code="cmas.face.captcha.incorrect"/>
                            </c:if>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="clearfix">
                            <div class="header2-text feedback-photo-header"><s:message
                                    code="cmas.face.feedback.images"/></div>
                            <div id="addImage"
                                 class="feedback-button-right positive-button logbook-button logbook-inline-button">
                                <img class="logbook-inline-button-icon" src="/i/ic_plus_white.png"/>
                                <span class="logbook-inline-button-icon-text"><s:message
                                        code="cmas.face.feedback.image.add"/></span>
                            </div>
                        </div>
                        <div class="error" id="feedback_error_photo"></div>
                    </div>
                    <div id="photoListContainer" class="clearfix">
                    </div>
                    <button class="positive-button form-item-right form-button-single" id="feedbackSubmit">
                        <s:message code="cmas.face.feedback.submit"/>
                    </button>
                </div>
            </form:form>
        </div>
    </div>

</my:securepage>