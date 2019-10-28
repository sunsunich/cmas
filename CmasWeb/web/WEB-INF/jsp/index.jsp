<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>

<my:basePage title="cmas.face.index.header"
             customCSSFiles="/c/landingPage.css,/c/bf_insurance.css"
             customScripts="/js/controller/country_controller.js,/js/controller/menu_controller.js,/js/controller/landing_page_controller.js"
             hideFooter="true"
>
    <div id="firstScreen" class="backgroundContainer landingPageFirstScreen"
         style="background-image: url(/i/firstScreenBackground_land_480.png)">
        <div class="landingPageTextPart" id="landingPageTextPart">
            <div class="imageContainer">
                <picture>
                    <!--[if IE 9]>
                    <video style="display: none;"><![endif]-->
                    <source srcset="/i/logo-white.png 1x, /i/logo-white@2x.png 2x, /i/logo-white@3x.png 3x">
                    <!--[if IE 9]></video><![endif]-->
                    <img alt="white logo" id="landingPageLogo">
                </picture>
            </div>
            <div id="landingPageHeader" class="landingPageHeader">
                <span id="landingPageHeaderText"><s:message code="cmas.face.landing.first.screen.header"/></span>
            </div>
            <div id="frontTextContainer">
                <div id="frontText" class="landingPageFrontText firstPageAdjustableText">
                    <s:message code="cmas.face.landing.first.screen.frontText"/>
                </div>
                <ul class="landing-page-list landingPageFeatureText">
                    <li id="insuranceFeature">
                        <span id="insuranceFeatureText" class="feature-list-text firstPageAdjustableText">
                        <s:message code="cmas.face.landing.first.screen.list.feature.insurance"/>
                            </span>
                    </li>

                    <li id="logbookFeature" class="firstPageAdjustableText">
                        <span class="feature-list-text firstPageAdjustableText">
                            <s:message code="cmas.face.landing.first.screen.list.feature.logbook"/></span>
                    </li>
                    <li id="spotsFeature" class="firstPageAdjustableText">
                        <span class="feature-list-text firstPageAdjustableText">
                            <s:message code="cmas.face.landing.first.screen.list.feature.spots"/></span>
                    </li>
                    <li id="friendsFeature" class="firstPageAdjustableText">
                        <span class="feature-list-text firstPageAdjustableText">
                            <s:message code="cmas.face.landing.first.screen.list.feature.friends"/></span>
                    </li>
                    <li id="verificationFeature" class="firstPageAdjustableText">
                        <span class="feature-list-text firstPageAdjustableText">
                            <s:message code="cmas.face.landing.first.screen.list.feature.verification"/></span>
                    </li>
                    <li id="futureFeature" class="firstPageAdjustableText">
                        <span class="feature-list-text firstPageAdjustableText">
                            <s:message code="cmas.face.landing.first.screen.list.feature.future"/></span>
                    </li>
                </ul>
            </div>
            <div class="landingPageFirstScreenButtons">
                <button id="joinButton" class="positive-button adjustableButton"
                        onclick="window.location='/diver-registration.html'">
                    <s:message code="cmas.face.landing.first.screen.button.registration"/>
                </button>
                <button id="signInButton" class="inverse-positive-button adjustableButton"
                        onclick="window.location='/login-form.html'">
                    <s:message code="cmas.face.landing.first.screen.button.login"/>
                </button>
            </div>
        </div>
        <div id="interfaceExamples" class="landingPageInterfaceExamples" style="display: none">
            <picture>
                <!--[if IE 9]>
                <video style="display: none;"><![endif]-->
                <source srcset="/i/big-screen-1@3x.png 1x"
                        media="(min-width: 2700px)">
                <source srcset="/i/big-screen-1@2x.png"
                        media="(min-width: 1800px)">
                <source srcset="/i/big-screen-1.png 1x, /i/big-screen-1@2x.png 2x, /i/big-screen-1@3x.png 3x"
                        media="(min-width: 600px)">

                <!--[if IE 9]></video><![endif]-->
                <img id="bigExampleBackground" src="/i/big-screen-1.png" alt="big example background">
            </picture>
            <div id="smallExampleBackgroundWrapper">
                <picture>
                    <!--[if IE 9]>
                    <video style="display: none;"><![endif]-->
                    <source srcset="/i/small-screen-1@3x.png 1x"
                            media="(min-width: 2700px)">
                    <source srcset="/i/small-screen-1@2x.png"
                            media="(min-width: 1800px)">
                    <source srcset="/i/small-screen-1.png 1x, /i/small-screen-1@2x.png 2x, /i/small-screen-1@3x.png 3x"
                            media="(min-width: 600px)">

                    <!--[if IE 9]></video><![endif]-->
                    <img id="smallExampleBackground" src="/i/small-screen-1.png" alt="small example background">
                </picture>
            </div>

        </div>
        <div id="insuranceFeaturePanel" class="panel feature-panel" style="display: none">
            <my:insuranceInfo/>
        </div>
    </div>
    <div id="secondScreen" class="secondScreen">
        <div class="secondScreenHeader" id="secondScreenHeader"><s:message
                code="cmas.face.landing.second.screen.header"/></div>
        <div class="landingPageText adjustableText"><s:message code="cmas.face.landing.second.screen.text"/></div>
        <div class="horizontalRow" id="diveSearchForm">
            <div class="form-row">
                <input id="name" type="text" name="name"
                       placeholder="<s:message code="cmas.face.registration.form.label.name"/>"/>
            </div>
            <div class="form-row">
                <select name="country" id="country" size=1 onChange="">
                    <c:forEach items="${countries}" var="country">
                        <option value='${country.code}'>${country.name}</option>
                    </c:forEach>
                </select>
            </div>
            <button class="positive-button" id="findDiver">
                <s:message code="cmas.face.landing.second.screen.submit"/>
            </button>
        </div>
    </div>
    <div id="thirdScreen" class="thirdScreen">
        <div id="insurance">
            <div class="imageContainer">
                    <picture>
                    <!--[if IE 9]>
                    <video style="display: none;"><![endif]-->
                    <source srcset="/i/insurance_land_2133.png" media="(min-width: 4266px)">
                    <source srcset="/i/insurance_land_1920.png" media="(min-width: 3840px)">
                    <source srcset="/i/insurance_land_1680.png" media="(min-width: 3360px)">
                    <source srcset="/i/insurance_land_1440.png" media="(min-width: 2880px)">
                    <source srcset="/i/insurance_land_1280.png" media="(min-width: 2560px)">
                    <source srcset="/i/insurance_land_1024.png 1x, /i/insurance_land_2133.png 2x"
                    media="(min-width: 2048px)">
                    <source srcset="/i/insurance_land_962.png 1x, /i/insurance_land_1920.png 2x, /i/insurance_land_2133.png 3x"
                    media="(min-width: 1920px)">
                    <source srcset="/i/insurance_land_840.png 1x, /i/insurance_land_1680.png 2x, /i/insurance_land_2133.png 3x"
                    media="(min-width: 1680px)">
                    <source srcset="/i/insurance_land_720.png, /i/insurance_land_1440.png 2x, /i/insurance_land_2133.png 3x"
                    media="(min-width: 1440px)">
                    <source srcset="/i/insurance_land_640.png 1x, /i/insurance_land_1280.png 2x, /i/insurance_land_1920.png 3x"
                    media="(min-width: 1280px)">
                    <source srcset="/i/insurance_land_512.png, /i/insurance_land_1024.png 2x, /i/insurance_land_1680.png 3x, /i/insurance_land_2133.png 4x"
                    media="(min-width: 1024px)">
                    <source srcset="/i/insurance_land_480.png 1x, /i/insurance_land_962.png 2x, /i/insurance_land_1440.png 3x, /i/insurance_land_1920.png 4x"
                    media="(min-width: 960px)">
                    <source srcset="/i/insurance_land_320.png 1x, /i/insurance_land_640.png 2x, /i/insurance_land_962.png 3x, /i/insurance_land_1280.png 4x"
                    media="(min-width: 640px)">
                    <source srcset="/i/insurance_land_240.png 1x, /i/insurance_land_480.png 2x, /i/insurance_land_720.png 3x, /i/insurance_land_962.png 4x"
                    >
                    <!--[if IE 9]></video><![endif]-->
                    <img id="insuranceIllustration" alt="insurance illustration">
                    </picture>
<%--                <img id="insuranceIllustration" src="${pageContext.request.contextPath}/i/camera_discount.png"/>--%>
            </div>
            <div id="insuranceTextPart">
                <div id="insuranceHeader" class="secondaryHeader">
                    <s:message code="cmas.face.landing.third.screen.header"/>
                </div>
                <div id="insuranceText" class="secondaryText">
                    <s:message code="cmas.face.landing.third.screen.text"/>
                </div>
                    <div id="insuranceLink" class="landingPageText">
                    <%--https://www.sevencorners.com/cmas--%>
                    <a target="_blank" rel="noopener noreferrer" href="https://www.balticfinance.com/en/aqualink/"><b><s:message code="cmas.face.landing.third.screen.more"/></b>
                    <img id="insuranceLinkArrow" src="/i/ic_see-more.png" alt="see more image"/>
                    </a>
                    </div>
            </div>
        </div>
        <div id="features">
            <div class="feature">
                <div class="imageContainer">
                    <picture>
                        <!--[if IE 9]>
                        <video style="display: none;"><![endif]-->
                        <source srcset="/i/certificates_land_1065.png" media="(min-width: 4260px)">
                        <source srcset="/i/certificates_land_1024.png" media="(min-width: 4096px)">
                        <source srcset="/i/certificates_land_962.png" media="(min-width: 3848px)">
                        <source srcset="/i/certificates_land_640.png" media="(min-width: 2560px)">
                        <source srcset="/i/certificates_land_480.png 1x, /i/certificates_land_962.png 2x"
                                media="(min-width: 1920px)">
                        <source srcset="/i/certificates_land_420.png 1x, /i/certificates_land_962.png 2x"
                                media="(min-width: 1680px)">
                        <source srcset="/i/certificates_land_360.png 1x, /i/certificates_land_962.png 2x, /i/certificates_land_1065.png 3x"
                                media="(min-width: 1440px)">
                        <source srcset="/i/certificates_land_320.png 1x, /i/certificates_land_640.png 2x, /i/certificates_land_962.png 3x"
                                media="(min-width: 1280px)">
                        <source srcset="/i/certificates_land_256.png 1x, /i/certificates_land_640.png 2x, /i/certificates_land_962.png 3x, /i/certificates_land_1024.png 4x"
                                media="(min-width: 1024px)">
                        <source srcset="/i/certificates_land_240.png 1x, /i/certificates_land_480.png 2x, /i/certificates_land_640.png 3x, /i/certificates_land_962.png 4x"
                                media="(min-width: 960px)">
                        <source srcset="/i/certificates_land_160.png 1x, /i/certificates_land_320.png 2x, /i/certificates_land_480.png 3x, /i/certificates_land_640.png 4x"
                                media="(min-width: 660px)">
                        <source srcset="/i/certificates_land_320.png 1x, /i/certificates_land_640.png 2x, /i/certificates_land_962.png 3x"
                                media="(min-width: 639px)">
                        <source srcset="/i/certificates_land_256.png 1x, /i/certificates_land_640.png 2x, /i/certificates_land_962.png 3x, /i/certificates_land_1024.png 4x"
                                media="(min-width: 512px)">
                        <source srcset="/i/certificates_land_240.png 1x, /i/certificates_land_480.png 2x, /i/certificates_land_640.png 3x, /i/certificates_land_962.png 4x"
                                media="(min-width: 480px)">
                        <source srcset="/i/certificates_land_160.png 1x, /i/certificates_land_320.png 2x, /i/certificates_land_480.png 3x, /i/certificates_land_640.png 4x"
                                media="(min-width: 320px)">
                        <source srcset="/i/certificates_land_120.png 1x, /i/certificates_land_240.png 2x, /i/certificates_land_360.png 3x, /i/certificates_land_480.png 4x"
                        >
                        <!--[if IE 9]></video><![endif]-->
                        <img alt="certificates illustration" class="featureIllustration">
                    </picture>
                </div>
                <div class="featureDescription" id="certificatesDescription">
                    <div class="featureHeader" id="certificatesHeader"><s:message
                            code="cmas.face.landing.third.screen.certificates.header"/></div>
                    <div class="featureText" id="certificatesText">
                        <s:message code="cmas.face.landing.third.screen.certificates.text"/>
                    </div>
                </div>
            </div>
            <div class="feature">
                <div class="imageContainer">
                    <picture>
                            <%--
                            1024, 962, 640, 480, 420, 360, 320, 256, 240, 160, 120
                            --%>
                        <!--[if IE 9]>
                        <video style="display: none;"><![endif]-->
                        <source srcset="/i/buddies_land_1065.png" media="(min-width: 4260px)">
                        <source srcset="/i/buddies_land_1024.png" media="(min-width: 4096px)">
                        <source srcset="/i/buddies_land_962.png" media="(min-width: 3848px)">
                        <source srcset="/i/buddies_land_640.png" media="(min-width: 2560px)">
                        <source srcset="/i/buddies_land_480.png 1x, /i/buddies_land_962.png 2x"
                                media="(min-width: 1920px)">
                        <source srcset="/i/buddies_land_420.png 1x, /i/buddies_land_962.png 2x"
                                media="(min-width: 1680px)">
                        <source srcset="/i/buddies_land_360.png 1x, /i/buddies_land_962.png 2x, /i/buddies_land_1065.png 3x"
                                media="(min-width: 1440px)">
                        <source srcset="/i/buddies_land_320.png 1x, /i/buddies_land_640.png 2x, /i/buddies_land_962.png 3x"
                                media="(min-width: 1280px)">
                        <source srcset="/i/buddies_land_256.png 1x, /i/buddies_land_640.png 2x, /i/buddies_land_962.png 3x, /i/buddies_land_1024.png 4x"
                                media="(min-width: 1024px)">
                        <source srcset="/i/buddies_land_240.png 1x, /i/buddies_land_480.png 2x, /i/buddies_land_640.png 3x, /i/buddies_land_962.png 4x"
                                media="(min-width: 960px)">
                        <source srcset="/i/buddies_land_160.png 1x, /i/buddies_land_320.png 2x, /i/buddies_land_480.png 3x, /i/buddies_land_640.png 4x"
                                media="(min-width: 660px)">
                        <source srcset="/i/buddies_land_320.png 1x, /i/buddies_land_640.png 2x, /i/buddies_land_962.png 3x"
                                media="(min-width: 639px)">
                        <source srcset="/i/buddies_land_256.png 1x, /i/buddies_land_640.png 2x, /i/buddies_land_962.png 3x, /i/buddies_land_1024.png 4x"
                                media="(min-width: 512px)">
                        <source srcset="/i/buddies_land_240.png 1x, /i/buddies_land_480.png 2x, /i/buddies_land_640.png 3x, /i/buddies_land_962.png 4x"
                                media="(min-width: 480px)">
                        <source srcset="/i/buddies_land_160.png 1x, /i/buddies_land_320.png 2x, /i/buddies_land_480.png 3x, /i/buddies_land_640.png 4x"
                                media="(min-width: 320px)">
                        <source srcset="/i/buddies_land_120.png 1x, /i/buddies_land_240.png 2x, /i/buddies_land_360.png 3x, /i/buddies_land_480.png 4x"
                        >
                        <!--[if IE 9]></video><![endif]-->
                        <img alt="buddies illustration" class="featureIllustration">
                    </picture>
                </div>
                <div class="featureDescription" id="buddiesDescription">
                    <div class="featureHeader" id="buddiesHeader"><s:message
                            code="cmas.face.landing.third.screen.buddies.header"/></div>
                    <div class="featureText" id="buddiesText">
                        <s:message code="cmas.face.landing.third.screen.buddies.text"/>
                    </div>
                </div>
            </div>
            <div class="feature">
                <div class="imageContainer">
                    <picture>
                            <%--
                            1024, 962, 640, 480, 420, 360, 320, 256, 240, 160, 120
                            --%>
                        <!--[if IE 9]>
                        <video style="display: none;"><![endif]-->
                        <source srcset="/i/spots_land_1065.png" media="(min-width: 4260px)">
                        <source srcset="/i/spots_land_1024.png" media="(min-width: 4096px)">
                        <source srcset="/i/spots_land_962.png" media="(min-width: 3848px)">
                        <source srcset="/i/spots_land_640.png" media="(min-width: 2560px)">
                        <source srcset="/i/spots_land_480.png 1x, /i/spots_land_962.png 2x"
                                media="(min-width: 1920px)">
                        <source srcset="/i/spots_land_420.png 1x, /i/spots_land_962.png 2x"
                                media="(min-width: 1680px)">
                        <source srcset="/i/spots_land_360.png 1x, /i/spots_land_962.png 2x, /i/spots_land_1065.png 3x"
                                media="(min-width: 1440px)">
                        <source srcset="/i/spots_land_320.png 1x, /i/spots_land_640.png 2x, /i/spots_land_962.png 3x"
                                media="(min-width: 1280px)">
                        <source srcset="/i/spots_land_256.png 1x, /i/spots_land_640.png 2x, /i/spots_land_962.png 3x, /i/spots_land_1024.png 4x"
                                media="(min-width: 1024px)">
                        <source srcset="/i/spots_land_240.png 1x, /i/spots_land_480.png 2x, /i/spots_land_640.png 3x, /i/spots_land_962.png 4x"
                                media="(min-width: 960px)">
                        <source srcset="/i/spots_land_160.png 1x, /i/spots_land_320.png 2x, /i/spots_land_480.png 3x, /i/spots_land_640.png 4x"
                                media="(min-width: 660px)">
                        <source srcset="/i/spots_land_320.png 1x, /i/spots_land_640.png 2x, /i/spots_land_962.png 3x"
                                media="(min-width: 639px)">
                        <source srcset="/i/spots_land_256.png 1x, /i/spots_land_640.png 2x, /i/spots_land_962.png 3x, /i/spots_land_1024.png 4x"
                                media="(min-width: 512px)">
                        <source srcset="/i/spots_land_240.png 1x, /i/spots_land_480.png 2x, /i/spots_land_640.png 3x, /i/spots_land_962.png 4x"
                                media="(min-width: 480px)">
                        <source srcset="/i/spots_land_160.png 1x, /i/spots_land_320.png 2x, /i/spots_land_480.png 3x, /i/spots_land_640.png 4x"
                                media="(min-width: 320px)">
                        <source srcset="/i/spots_land_120.png 1x, /i/spots_land_240.png 2x, /i/spots_land_360.png 3x, /i/spots_land_480.png 4x"
                        >
                        <!--[if IE 9]></video><![endif]-->
                        <img alt="spots illustration" class="featureIllustration">
                    </picture>
                </div>
                <div class="featureDescription" id="spotsDescription">
                    <div class="featureHeader" id="spotsHeader"><s:message
                            code="cmas.face.landing.third.screen.spots.header"/></div>
                    <div class="featureText" id="spotsText">
                        <s:message code="cmas.face.landing.third.screen.spots.text"/>
                    </div>
                </div>
            </div>
            <div class="feature">
                <div class="imageContainer">
                    <picture>
                            <%--
                            1024, 962, 640, 480, 420, 360, 320, 256, 240, 160, 120
                            --%>
                        <!--[if IE 9]>
                        <video style="display: none;"><![endif]-->
                        <source srcset="/i/memories_land_1065.png" media="(min-width: 4260px)">
                        <source srcset="/i/memories_land_1024.png" media="(min-width: 4096px)">
                        <source srcset="/i/memories_land_962.png" media="(min-width: 3848px)">
                        <source srcset="/i/memories_land_640.png" media="(min-width: 2560px)">
                        <source srcset="/i/memories_land_480.png 1x, /i/memories_land_962.png 2x"
                                media="(min-width: 1920px)">
                        <source srcset="/i/memories_land_420.png 1x, /i/memories_land_962.png 2x"
                                media="(min-width: 1680px)">
                        <source srcset="/i/memories_land_360.png 1x, /i/memories_land_962.png 2x, /i/memories_land_1065.png 3x"
                                media="(min-width: 1440px)">
                        <source srcset="/i/memories_land_320.png 1x, /i/memories_land_640.png 2x, /i/memories_land_962.png 3x"
                                media="(min-width: 1280px)">
                        <source srcset="/i/memories_land_256.png 1x, /i/memories_land_640.png 2x, /i/memories_land_962.png 3x, /i/memories_land_1024.png 4x"
                                media="(min-width: 1024px)">
                        <source srcset="/i/memories_land_240.png 1x, /i/memories_land_480.png 2x, /i/memories_land_640.png 3x, /i/memories_land_962.png 4x"
                                media="(min-width: 960px)">
                        <source srcset="/i/memories_land_160.png 1x, /i/memories_land_320.png 2x, /i/memories_land_480.png 3x, /i/memories_land_640.png 4x"
                                media="(min-width: 660px)">
                        <source srcset="/i/memories_land_320.png 1x, /i/memories_land_640.png 2x, /i/memories_land_962.png 3x"
                                media="(min-width: 639px)">
                        <source srcset="/i/memories_land_256.png 1x, /i/memories_land_640.png 2x, /i/memories_land_962.png 3x, /i/memories_land_1024.png 4x"
                                media="(min-width: 512px)">
                        <source srcset="/i/memories_land_240.png 1x, /i/memories_land_480.png 2x, /i/memories_land_640.png 3x, /i/memories_land_962.png 4x"
                                media="(min-width: 480px)">
                        <source srcset="/i/memories_land_160.png 1x, /i/memories_land_320.png 2x, /i/memories_land_480.png 3x, /i/memories_land_640.png 4x"
                                media="(min-width: 320px)">
                        <source srcset="/i/memories_land_120.png 1x, /i/memories_land_240.png 2x, /i/memories_land_360.png 3x, /i/memories_land_480.png 4x"
                        >
                        <!--[if IE 9]></video><![endif]-->
                        <img alt="memories illustration" class="featureIllustration">
                    </picture>
                </div>
                <div class="featureDescription" id="memoriesDescription">
                    <div class="featureHeader" id="memoriesHeader"><s:message
                            code="cmas.face.landing.third.screen.memories.header"/></div>
                    <div class="featureText" id="memoriesText">
                        <s:message code="cmas.face.landing.third.screen.memories.text"/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="fourthScreen" class="fourthScreen">
        <div id="submissionDescription">
            <div class="secondaryHeader" id="submissionHeader">
                <s:message code="cmas.face.landing.fourth.screen.header"/>
            </div>
            <div class="secondaryText" id="submissionText">
                <s:message code="cmas.face.landing.fourth.screen.text"/>
            </div>
            <div>
                <button id="bottomJoinButton" class="positive-button"
                        onclick="window.location='/diver-registration.html'">
                    <s:message code="cmas.face.landing.first.screen.button.registration"/>
                </button>
            </div>
        </div>
        <div id="submissionIllustration">
            <picture>
                <!--[if IE 9]>
                <video style="display: none;"><![endif]-->
                <source srcset="/i/subscription-img@3x.png" media="(min-width: 3000px)">
                <source srcset="/i/subscription-img@2x.png" media="(min-width: 2000px)">
                <source srcset="/i/subscription-img.png 1x, /i/subscription-img@2x.png 2x, /i/subscription-img@3x.png 3x"
                >

                <!--[if IE 9]></video><![endif]-->
                <img id="submissionBackground" alt="submission background">
            </picture>
        </div>

    </div>

    <div id="footer" class="backgroundContainer">
        <div class="footer-link footer-item-left footer-item footer-item-first">
            <a href="${pageContext.request.contextPath}/faq.html"><s:message code="cmas.face.client.faq"/></a>
        </div>
        <div class="footer-link footer-item-left footer-item" style="visibility: hidden">
            <a href="${pageContext.request.contextPath}/paymentInfo.html">
                <s:message code="cmas.face.client.paymentInfo"/>
            </a>
        </div>
        <div class="footer-link footer-item-left footer-item" id="termsAndCond">
            <a href="${pageContext.request.contextPath}/privacyPolicy.html">
                <s:message code="cmas.face.client.termsAndCond"/>
            </a>
        </div>
            <%--todo implement--%>
        <div class="footer-link footer-item-left footer-item" id="languageChange" style="color: #FFFFFF">
            <a style="visibility: hidden" href="${pageContext.request.contextPath}/privacyPolicy.html">
                ENGLISH
            </a>
        </div>
        <div class="footer-text footer-item-right" id="credits">
            <span class="secondary-large-text footer-large-text"><s:message code="cmas.face.client.creditsCMAS"/></span><br/>
            <span class="secondary-text footer-text"><s:message code="cmas.face.client.credits"/></span>
        </div>
    </div>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;"><![endif]-->
        <source srcset="/i/firstScreenBackground_land_2840.png" media="(min-width: 2800px)">
        <source srcset="/i/firstScreenBackground_land_2560.png" media="(min-width: 2560px)">
        <source srcset="/i/firstScreenBackground_land_1920.png" media="(min-width: 1920px)">
        <source srcset="/i/firstScreenBackground_land_1680.png" media="(min-width: 1680px)">
        <source srcset="/i/firstScreenBackground_land_1440.png, /i/firstScreenBackground_land_2840.png 2x"
                media="(min-width: 1440px)">
        <source srcset="/i/firstScreenBackground_land_1280.png, /i/firstScreenBackground_land_2560.png 2x"
                media="(min-width: 1280px)">
        <source srcset="/i/firstScreenBackground_land_1024.png, /i/firstScreenBackground_land_2560.png 2x"
                media="(min-width: 1024px)">
        <source srcset="/i/firstScreenBackground_land_962.png, /i/firstScreenBackground_land_1920.png 2x, /i/firstScreenBackground_land_2840.png 3x"
                media="(min-width: 962px)">
        <source srcset="/i/firstScreenBackground_land_640.png 1x, /i/firstScreenBackground_land_1280.png 2x, /i/firstScreenBackground_land_1920.png 3x, /i/firstScreenBackground_land_2560.png 4x"
                media="(min-width: 640px)">
        <source srcset="/i/firstScreenBackground_land_480.png 1x, /i/firstScreenBackground_land_962.png 2x, /i/firstScreenBackground_land_1440.png 3x, /i/firstScreenBackground_land_1920.png 4x"
        >

        <!--[if IE 9]></video><![endif]-->
        <img id="firstScreenBackground" alt="First Screen background" style="display: none">
    </picture>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;"><![endif]-->
        <source srcset="/i/pattern-3@3x.png" media="(min-width: 3200px)">
        <source srcset="/i/pattern-3@2x.png" media="(min-width: 1800px)">
        <source srcset="/i/pattern-3.png">
        <!--[if IE 9]></video><![endif]-->
        <img alt="pattern" id="insuranceTextBackground" style="display: none">
    </picture>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;"><![endif]-->
        <source srcset="/i/firstScreenFooter_land_2842.png" media="(min-width: 2800px)">
        <source srcset="/i/firstScreenFooter_land_2560.png" media="(min-width: 2560px)">
        <source srcset="/i/firstScreenFooter_land_1920.png" media="(min-width: 1920px)">
        <source srcset="/i/firstScreenFooter_land_1680.png" media="(min-width: 1680px)">
        <source srcset="/i/firstScreenFooter_land_1440.png, /i/firstScreenFooter_land_2842.png 2x"
                media="(min-width: 1440px)">
        <source srcset="/i/firstScreenFooter_land_1280.png, /i/firstScreenFooter_land_2560.png 2x"
                media="(min-width: 1280px)">
        <source srcset="/i/firstScreenFooter_land_1024.png, /i/firstScreenFooter_land_2560.png 2x"
                media="(min-width: 1024px)">
        <source srcset="/i/firstScreenFooter_land_962.png, /i/firstScreenFooter_land_1920.png 2x, /i/firstScreenFooter_land_2842.png 3x"
                media="(min-width: 962px)">
        <source srcset="/i/firstScreenFooter_land_640.png 1x, /i/firstScreenFooter_land_1280.png 2x, /i/firstScreenFooter_land_1920.png 3x, /i/firstScreenFooter_land_2560.png 4x"
                media="(min-width: 640px)">
        <source srcset="/i/firstScreenFooter_land_480.png 1x, /i/firstScreenFooter_land_962.png 2x, /i/firstScreenFooter_land_1440.png 3x, /i/firstScreenFooter_land_1920.png 4x"
        >

        <!--[if IE 9]></video><![endif]-->
        <img id="firstScreenFooter" alt="First Screen footer background" style="display: none">
    </picture>

</my:basePage>
