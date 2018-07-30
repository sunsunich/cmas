<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="hideFooter" required="false" %>
<%@ attribute name="hideMenu" required="false" %>
<%@ attribute name="customScripts" required="false" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>

<c:if test="${empty hideMenu}">
    <c:set var="hideMenu" value="false"/>
</c:if>
<c:if test="${empty hideFooter}">
    <c:set var="hideFooter" value="false"/>
</c:if>

<my:basePage bodyId="secureBody" title="${title}" hideFooter="${hideFooter}"
             doNotDoAuth="true" customScripts="/js/controller/diver_menu_controller.js,${customScripts}">
    <c:set var="isCMAS"
           value="${diver.diverRegistrationStatus.name == 'CMAS_FULL' || diver.diverRegistrationStatus.name == 'CMAS_BASIC'}"/>

    <script type="application/javascript">
        var isCMAS = ${isCMAS};
    </script>
    <my:header>
        <c:if test="${!hideMenu}">
            <div id="headerMenu">
                <div class="menu-link menu-item-left menu-item menu-item-first">
                    <a href="/secure/profile/getUser.html">
                        <span><s:message code="cmas.face.client.menu.personalData"/></span>
                    </a>
                </div>
                <c:if test="${isCMAS}">
                    <div class="menu-link menu-item-left menu-item">
                        <a href="/secure/cards.html">
                            <span><s:message code="cmas.face.client.menu.myCards"/></span>
                        </a>
                    </div>
                </c:if>
                    <%--todo implement--%>
                <%--<div class="menu-link menu-item-left menu-item">--%>
                    <%--<a href="/secure/social.html">--%>
                        <%--<span><s:message code="cmas.face.client.menu.friends"/></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="menu-link menu-item-left menu-item">--%>
                    <%--<a href="/secure/showLogbook.html">--%>
                        <%--<span><s:message code="cmas.face.client.menu.logbook"/></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
                <%--<div class="menu-link menu-item-left menu-item ">--%>
                    <%--<a href="/secure/showSpots.html">--%>
                        <%--<span><s:message code="cmas.face.client.menu.divingSpots"/></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
            </div>
            <div class="cssmenu" id="cssMenu" style="display: none">
                <ul>
                    <li class="has-sub">
                        <div id="menuButton"></div>
                        <ul id="menuItems">
                            <li>
                                <a href="/secure/profile/getUser.html">
                                    <span><s:message code="cmas.face.client.menu.personalData"/></span>
                                </a>
                            </li>
                            <c:if test="${isCMAS}">
                                <li>
                                    <a href="/secure/cards.html">
                                        <span><s:message code="cmas.face.client.menu.myCards"/></span>
                                    </a>
                                </li>
                            </c:if>
                                <%--todo implement--%>
                            <%--<li>--%>
                                <%--<a href="/secure/social.html">--%>
                                    <%--<span><s:message code="cmas.face.client.menu.friends"/></span>--%>
                                <%--</a>--%>
                            <%--</li>--%>
                            <%--<li>--%>
                                <%--<a href="/secure/showLogbook.html">--%>
                                    <%--<span><s:message code="cmas.face.client.menu.logbook"/></span>--%>
                                <%--</a>--%>
                            <%--</li>--%>
                            <%--<li>--%>
                                <%--<a href="/secure/showSpots.html">--%>
                                    <%--<span><s:message code="cmas.face.client.menu.divingSpots"/></span>--%>
                                <%--</a>--%>
                            <%--</li>--%>
                        </ul>
                    </li>
                </ul>
            </div>
        </c:if>
        <div class="menu-item-right menu-item menu-small-button userpic-button-off" id="userpicMenuButton">
        </div>

        <div class="usermenu" id="userMenu" style="display: none">
            <ul>
                <li class="bottomBorder first-list-item">
                    <a href="${pageContext.request.contextPath}/secure/pay.html">
                        <span><s:message code="cmas.face.client.menu.payment"/></span>
                    </a>
                </li>
                <c:if test="${!hideMenu}">
                    <li>
                        <a href="${pageContext.request.contextPath}/secure/editPassword.html">
                            <span><s:message code="cmas.face.client.menu.changePass"/></span>
                        </a>
                    </li>
                    <li class="bottomBorder">
                        <a href="${pageContext.request.contextPath}/secure/editEmail.html">
                            <span><s:message code="cmas.face.client.menu.changeEmail"/></span>
                        </a>
                    </li>
                </c:if>
                <li class="last-list-item">
                    <a href="${pageContext.request.contextPath}/logout.html">
                        <span><s:message code="cmas.face.client.exit"/></span>
                    </a>
                </li>
            </ul>
        </div>
        <c:choose>
            <c:when test="${diver.userpicUrl == null}">
                <div class="menu-item-right menu-button no-userpic-ico">
                    <img src="/i/no-userpic-ico.png"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="menu-item-right menu-button userpic-ico">
                    <img src="${pageContext.request.contextPath}${userpicRoot}${diver.userpicUrl}"/>
                </div>
            </c:otherwise>
            <%--<div class="menu-item-right menu-item menu-button userpic-ico" style="background-image: url(--%>
            <%--<c:choose>--%>
            <%--<c:when test="${diver.userpicUrl == null}">--%>
            <%--/i/no-userpic-ico.png--%>
            <%--</c:when>--%>
            <%--<c:otherwise>--%>
            <%--${diver.userpicUrl}--%>
            <%--</c:otherwise>--%>
            <%--</c:choose>--%>
            <%--)"></div>--%>
        </c:choose>
        <span class="menu-item-right menu-text menu-item username-text">
            <b>${diver.firstName}</b>
        </span>

    </my:header>

    <div id="Wrapper-content">
        <div id="loading" class="loader" title="Please wait..."></div>
        <jsp:doBody/>
        <my:dialog id="errorDialog"
                   title="cmas.face.error.title"
                   buttonText="cmas.face.error.submitText">
            <div id="errorDialogText"></div>
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
