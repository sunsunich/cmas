<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<my:securepage title="cmas.face.index.header"
               activeMenuItem="mobile"
>
    <div class="content-center-wide" id="content">
        <div class="panel panel-header">
            <span class="header2-text"><s:message code="cmas.face.mobile.header"/></span>
            <div class="form-description clearfix">
                To use the CMAS E-cards mobile App please follow the steps below:<br/>
                <ol>
                    <li>
                        open this page on your mobile device
                    </li>
                    <li>
                        install the CMAS E-cards mobile App from Apple Store:<br/>
                        <button class="positive-button"
                                onclick="window.location='itms-apps://apps.apple.com/app/id1482247868'">
                            INSTALL CMAS E-CARDS
                        </button>
                    </li>
                    <li>
                        once the CMAS E-cards mobile App is installed login by tapping the button:<br/>
                        <button class="positive-button"
                                onclick="window.location='cmas://login?token=${diver.mobileAuthToken}'">
                            LOGIN TO CMAS E-CARDS
                        </button>
                    </li>
                </ol>
            </div>

<%--            <div class="form-description clearfix">--%>
<%--                <span>The button below only works if you opened this page on your mobile device</span>--%>
<%--                <br/>--%>
<%--                <span>Open this page on your mobile device and tap the button</span>--%>
<%--            </div>--%>
<%--            --%>
<%--            <div class="panel">--%>
<%--                <div class="form-description clearfix">--%>
<%--                    <span class="error">Only Apple devices (iPhone and iPad) are supported. Android will be supported soon.</span>--%>
<%--                </div>--%>
<%--                <div class="clearfix">--%>
<%--                        <button class="positive-button"--%>
<%--                                onclick="window.location='cmas://login?token=${diver.mobileAuthToken}'">--%>
<%--                            LOGIN TO CMAS E-CARDS--%>
<%--                        </button>--%>
<%--                </div>--%>
<%--            </div>--%>
        </div>

    </div>
</my:securepage>


