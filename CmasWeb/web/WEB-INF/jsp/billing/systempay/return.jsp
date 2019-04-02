<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<my:nonsecurepage title="cmas.face.index.header" doNotDoAuth="true"
                  customScripts="js/controller/registration_flow_controller.js"
>
    <script type="application/javascript">
        $(document).ready(function () {
            registration_flow_controller.init('simple', {backgroundImageId: 'paymentReturnImageBackground'});
        });
    </script>
    <div class="content" id="Content">
        <div id="formImage" class="formImage">

        </div>
        <div class="formWrapper" id="formWrapper">
            <div id="paymentReturnBlock">
                <c:choose>
                    <c:when test="${isSuccess}">
                        <div class="header1-text" id="formHeader">
                            <s:message code="cmas.face.payment.success.header"/>
                        </div>
                        <c:set var="hasCmasLicense" value="false"/>
                        <c:set var="hasAdditionalFeatures" value="false"/>
                        <c:forEach var="feature" items="${invoice.requestedPaidFeatures}">
                            <c:choose>
                                <c:when test="${feature.id == 1}">
                                    <c:set var="hasCmasLicense" value="true"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="hasAdditionalFeatures" value="true"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${hasCmasLicense}">
                            <div class="form-description">
                                <s:message code="cmas.face.payment.success.cmasLicense"/>
                            </div>
                        </c:if>
                        <c:if test="${hasAdditionalFeatures}">
                            <div class="form-description">
                                <s:message code="cmas.face.payment.success.additionalFeatures"/>
                            </div>
                            <ul class="purchased-feature-list">
                                <c:forEach var="feature" items="${invoice.requestedPaidFeatures}">
                                    <c:if test="${feature.id != 1}">
                                        <li>
                                            <span class="purchased-feature-header">
                                                <s:message code="${feature.name}"/>
                                            </span>
                                            <br/>
                                            <span class="purchased-feature-text">
                                            <s:message code="${feature.descriptionCode}"/>
                                            </span>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <div class="header1-text" id="formHeader">
                            <s:message code="cmas.face.payment.failed.header"/>
                        </div>
                        <div class="form-description">
                            <s:message code="cmas.face.payment.failed.text"/>
                        </div>
                    </c:otherwise>
                </c:choose>
                <authz:authorize ifAnyGranted="ROLE_DIVER" ifNotGranted="ROLE_ADMIN">
                    <button class="positive-button form-item-right form-button-bigger"
                            onclick="return window.location='/secure/index.html'">
                        <s:message code="cmas.face.account.back"/>
                    </button>
                </authz:authorize>

                <authz:authorize ifAnyGranted="ROLE_FEDERATION_ADMIN" ifNotGranted="ROLE_ADMIN">
                    <button class="positive-button form-item-right form-button-bigger"
                            onclick="return window.location='/fed/index.html'">
                        <s:message code="cmas.face.account.back"/>
                    </button>
                </authz:authorize>

            </div>
        </div>
    </div>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/paymentReturnImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/paymentReturnImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/paymentReturnImage.png 1x, /i/paymentReturnImage@2x.png 2x, /i/paymentReturnImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/paymentReturnImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/paymentReturnImageMob.png 1x, /i/paymentReturnImageMob@2x.png 2x, /i/paymentReturnImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="paymentReturnImageBackground" alt="payment return background" style="display: none">
    </picture>

</my:nonsecurepage>
