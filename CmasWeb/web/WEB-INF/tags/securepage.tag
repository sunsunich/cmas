<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="hideFooter" required="false" %>
<%@ attribute name="hideMenu" required="false" %>
<%@ attribute name="customScripts" required="false" %>
<%@ attribute name="activeMenuItem" required="false" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<c:if test="${empty hideMenu}">
    <c:set var="hideMenu" value="false"/>
</c:if>
<c:if test="${empty hideFooter}">
    <c:set var="hideFooter" value="false"/>
</c:if>

<my:basePage bodyId="secureBody" title="${title}" hideFooter="${hideFooter}"
             doNotDoAuth="true" customScripts="/js/controller/diver_menu_controller.js,${customScripts}">

    <c:choose>
        <c:when test="${diver.diverRegistrationStatus.name == 'CMAS_FULL' || diver.diverRegistrationStatus.name == 'CMAS_BASIC'}">
            <c:set var="certificateCode" value="cmas.face.client.menu.myCards"/>
        </c:when>
        <c:otherwise>
            <c:set var="certificateCode" value="cmas.face.client.menu.becomeCmas"/>
        </c:otherwise>
    </c:choose>

    <my:header>
        <c:if test="${!hideMenu}">
            <div id="headerMenu">
                <div class="menu-link menu-item-left menu-item <c:if test="${activeMenuItem == 'personal'}">menu-link-active</c:if>">
                    <a href="${pageContext.request.contextPath}/secure/profile/getUser.html">
                        <span><s:message code="cmas.face.client.menu.personalData"/></span>
                    </a>
                </div>
                <div class="menu-link menu-item-left menu-item <c:if test="${activeMenuItem == 'cards'}">menu-link-active</c:if>">
                    <a href="${pageContext.request.contextPath}/secure/cards.html">
                        <span><s:message code="${certificateCode}"/></span>
                    </a>
                </div>
                <div class="menu-link menu-item-left menu-item <c:if test="${activeMenuItem == 'social'}">menu-link-active</c:if>">
                    <a href="${pageContext.request.contextPath}/secure/social.html">
                        <span><s:message code="cmas.face.client.menu.friends"/></span>
                    </a>
                </div>
                <div class="menu-link menu-item-left menu-item <c:if test="${activeMenuItem == 'logbook'}">menu-link-active</c:if>">
                    <a href="${pageContext.request.contextPath}/secure/showLogbook.html">
                        <span><s:message code="cmas.face.client.menu.logbook"/></span>
                    </a>
                </div>
                <div class="menu-link menu-item-left menu-item <c:if test="${activeMenuItem == 'spots'}">menu-link-active</c:if>">
                    <a href="${pageContext.request.contextPath}/secure/showSpots.html">
                        <span><s:message code="cmas.face.client.menu.divingSpots"/></span>
                    </a>
                </div>
                <c:if test="${diver.diverRegistrationStatus.name == 'CMAS_FULL' || diver.diverRegistrationStatus.name == 'CMAS_BASIC'}">
                    <div class="menu-link menu-item-left menu-item <c:if test="${activeMenuItem == 'mobile'}">menu-link-active</c:if>">
                        <a href="${pageContext.request.contextPath}/secure/mobile.html">
                            <span><s:message code="cmas.face.client.menu.mobile"/></span>
                        </a>
                    </div>
                </c:if>
<%--                <div class="menu-link menu-item-left menu-item <c:if test="${activeMenuItem == 'insurance'}">menu-link-active</c:if>">--%>
<%--                    <a href="${pageContext.request.contextPath}/secure/insurance.html">--%>
<%--                        <span><s:message code="cmas.face.client.menu.insurance"/></span>--%>
<%--                    </a>--%>
<%--                </div>--%>
                    <%--                <div class="menu-link menu-item-left menu-item <c:if test="${activeMenuItem == 'loyaltyProgram'}">menu-link-active</c:if>">--%>
                    <%--                    <a href="/secure/loyaltyProgram.html">--%>
                    <%--                        <span><s:message code="cmas.face.client.menu.loyaltyProgram"/></span>--%>
                    <%--                    </a>--%>
                    <%--                </div>--%>
            </div>
            <div class="cssmenu" id="cssMenu" style="display: none">
                <ul>
                    <li class="has-sub">
                        <div id="menuButton"></div>
                        <ul id="menuItems">
                            <c:choose>
                            <c:when test="${activeMenuItem == 'personal'}">
                            <li class="menu-active">
                                </c:when>
                                <c:otherwise>
                            <li class="menu-inactive">
                                </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/secure/profile/getUser.html">
                                    <span><s:message code="cmas.face.client.menu.personalData"/></span>
                                </a>
                            </li>
                            <c:choose>
                            <c:when test="${activeMenuItem == 'cards'}">
                            <li class="menu-active">
                                </c:when>
                                <c:otherwise>
                            <li class="menu-inactive">
                                </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/secure/cards.html">
                                    <span><s:message code="${certificateCode}"/></span>
                                </a>
                            </li>
                            <c:choose>
                            <c:when test="${activeMenuItem == 'social'}">
                            <li class="menu-active">
                                </c:when>
                                <c:otherwise>
                            <li class="menu-inactive">
                                </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/secure/social.html">
                                    <span><s:message code="cmas.face.client.menu.friends"/></span>
                                </a>
                            </li>
                            <c:choose>
                            <c:when test="${activeMenuItem == 'logbook'}">
                            <li class="menu-active">
                                </c:when>
                                <c:otherwise>
                            <li class="menu-inactive">
                                </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/secure/showLogbook.html">
                                    <span><s:message code="cmas.face.client.menu.logbook"/></span>
                                </a>
                            </li>
                            <c:choose>
                            <c:when test="${activeMenuItem == 'spots'}">
                            <li class="menu-active">
                                </c:when>
                                <c:otherwise>
                            <li class="menu-inactive">
                                </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/secure/showSpots.html">
                                    <span><s:message code="cmas.face.client.menu.divingSpots"/></span>
                                </a>
                            </li>
                                <c:if test="${diver.diverRegistrationStatus.name == 'CMAS_FULL' || diver.diverRegistrationStatus.name == 'CMAS_BASIC'}">
                                    <c:choose>
                                        <c:when test="${activeMenuItem == 'mobile'}">
                                            <li class="menu-active">
                                        </c:when>
                                        <c:otherwise>
                                            <li class="menu-inactive">
                                        </c:otherwise>
                                    </c:choose>
                                    <a href="${pageContext.request.contextPath}/secure/mobile.html">
                                        <span><s:message code="cmas.face.client.menu.mobile"/></span>
                                    </a>
                                    </li>
                                </c:if>
<%--                            <c:choose>--%>
<%--                            <c:when test="${activeMenuItem == 'insurance'}">--%>
<%--                            <li class="menu-active">--%>
<%--                                </c:when>--%>
<%--                                <c:otherwise>--%>
<%--                            <li class="menu-inactive">--%>
<%--                                </c:otherwise>--%>
<%--                                </c:choose>--%>
<%--                                <a href="${pageContext.request.contextPath}/secure/insurance.html">--%>
<%--                                    <span><s:message code="cmas.face.client.menu.insurance"/></span>--%>
<%--                                </a>--%>
<%--                            </li>--%>
                                <%--                                <c:choose>--%>
                                <%--                                <c:when test="${activeMenuItem == 'loyaltyProgram'}">--%>
                                <%--                                <li class="menu-active">--%>
                                <%--                                    </c:when>--%>
                                <%--                                    <c:otherwise>--%>
                                <%--                                <li class="menu-inactive">--%>
                                <%--                                    </c:otherwise>--%>
                                <%--                                    </c:choose>--%>
                                <%--                                    <a href="/secure/loyaltyProgram.html">--%>
                                <%--                                        <span><s:message code="cmas.face.client.menu.loyaltyProgram"/></span>--%>
                                <%--                                    </a>--%>
                                <%--                                </li>--%>
                        </ul>
                    </li>
                </ul>
            </div>
        </c:if>
        <div class="menu-item-right menu-item menu-small-button userpic-button-off" id="userpicMenuButton">
        </div>

        <div class="usermenu" id="userMenu" style="display: none">
            <ul>
                <li
                        <c:choose>
                            <c:when test="${activeMenuItem == 'pay'}">
                                class="bottomBorder first-list-item user-menu-active"
                            </c:when>
                            <c:otherwise>
                                class="bottomBorder first-list-item user-menu-inactive"
                            </c:otherwise>
                        </c:choose>
                        onclick="window.location = '${pageContext.request.contextPath}/secure/pay.html';"
                >
                    <a href="${pageContext.request.contextPath}/secure/pay.html">
                        <span><s:message code="cmas.face.client.menu.payment"/></span>
                    </a>
                </li>
                <li
                        <c:choose>
                            <c:when test="${activeMenuItem == 'editDiver'}">
                                class="user-menu-active"
                            </c:when>
                            <c:otherwise>
                                class="user-menu-inactive"
                            </c:otherwise>
                        </c:choose>
                        onclick="window.location = '${pageContext.request.contextPath}/secure/profile/editDiver.html';"
                >
                    <a href="${pageContext.request.contextPath}/secure/profile/editDiver.html">
                        <span><s:message code="cmas.face.client.menu.editDiver"/></span>
                    </a>
                </li>
                <li
                        <c:choose>
                            <c:when test="${activeMenuItem == 'editPassword'}">
                                class="user-menu-active"
                            </c:when>
                            <c:otherwise>
                                class="user-menu-inactive"
                            </c:otherwise>
                        </c:choose>
                        onclick="window.location = '${pageContext.request.contextPath}/secure/editPassword.html';"
                >
                    <a href="${pageContext.request.contextPath}/secure/editPassword.html">
                        <span><s:message code="cmas.face.client.menu.changePass"/></span>
                    </a>
                </li>
                <li
                        <c:choose>
                            <c:when test="${activeMenuItem == 'editEmail'}">
                                class="bottomBorder user-menu-active"
                            </c:when>
                            <c:otherwise>
                                class="bottomBorder user-menu-inactive"
                            </c:otherwise>
                        </c:choose>
                        onclick="window.location = '${pageContext.request.contextPath}/secure/editEmail.html';"
                >
                    <a href="${pageContext.request.contextPath}/secure/editEmail.html">
                        <span><s:message code="cmas.face.client.menu.changeEmail"/></span>
                    </a>
                </li>

                <li
                        <c:choose>
                            <c:when test="${activeMenuItem == 'reportError'}">
                                class="bottomBorder user-menu-active"
                            </c:when>
                            <c:otherwise>
                                class="bottomBorder user-menu-inactive"
                            </c:otherwise>
                        </c:choose>
                        onclick="window.location = '${pageContext.request.contextPath}/secure/reportError.html';"
                >
                    <a href="${pageContext.request.contextPath}/secure/reportError.html">
                        <span><s:message code="cmas.face.client.menu.reportError"/></span>
                    </a>
                </li>

                <li class="last-list-item user-menu-inactive"
                    onclick="window.location = '${pageContext.request.contextPath}/logout.html';">
                    <a href="${pageContext.request.contextPath}/logout.html">
                        <span><s:message code="cmas.face.client.exit"/></span>
                    </a>
                </li>
            </ul>
        </div>
        <c:choose>
            <c:when test="${diver.userpicUrl == null}">
                <div class="menu-item-right menu-button no-userpic-ico">
                    <img src="${pageContext.request.contextPath}/i/no-userpic-ico.png?v=${webVersion}"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class=" menu-item-right menu-button userpic-ico">
                    <div class="rounded"
                         style='background : url("${pageContext.request.contextPath}${userpicRoot}${diver.userpicUrl}") center no-repeat; background-size: cover'>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        <span class="menu-item-right menu-text menu-item username-text">
            <b>${diver.firstName}</b>
        </span>

    </my:header>

    <div id="Wrapper-content" class="clearfix">
        <div id="loading" class="loader" title="Please wait..."></div>
        <jsp:doBody/>
        <my:dialog id="errorDialog"
                   title="cmas.face.error.title"
                   buttonText="cmas.face.error.submitText">
            <div class="dialog-form-row" id="errorDialogText"></div>
        </my:dialog>
    </div>
    <c:if test="${!hideMenu}">
        <script type="application/javascript">
            $('#menuButton').click(function (e) {
                e.preventDefault();
                $('#menuItems').show();
            });
        </script>
    </c:if>
</my:basePage>
