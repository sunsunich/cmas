<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="hideMenu" required="false" %>
<%@ attribute name="customScripts" required="false" %>

<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.BackendUser"/>

<c:if test="${empty hideMenu}">
    <c:set var="hideMenu" value="false"/>
</c:if>

<my:basePage bodyId="secureBody" title="${title}" hideMenu="${hideMenu}" intrenal="true" indexpage="false"
             doNotDoAuth="true" customCSSFiles="/c/menu.css" customScripts="${customScripts}">
    <c:if test="${!hideMenu}">
        <div id="cssmenu">
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/logout.html">
                        <i class="menu-ico menu-ico-logout"></i>
                        <span><s:message code="cmas.face.client.exit"/></span>
                    </a>
                </li>
                <li class="has-sub">
                    <a href="#" id="menuButton">
                        <i class="menu-ico menu-ico-menu"></i>
                        <span><s:message code="cmas.face.client.menu"/></span>
                    </a>
                    <ul id="menuItems">
                        <li>
                            <a href="/secure/profile/getUser.html">
                                <i class="menu-ico menu-ico-my-account"></i>
                                <span><s:message code="cmas.face.client.menu.personalData"/></span>
                            </a>
                        </li>
                        <li>
                            <a href="/secure/showLogbook.html">
                                <i class="menu-ico menu-ico-logbook"></i>
                                <span><s:message code="cmas.face.client.menu.logbook"/></span>
                            </a>
                        </li>
                        <li>
                            <a href="/secure/showSpots.html">
                                <i class="menu-ico menu-ico-diving-spots"></i>
                                <span><s:message code="cmas.face.client.menu.divingSpots"/></span>
                            </a>
                        </li>
                        <li>
                            <a href="/secure/cards.html">
                                <i class="menu-ico menu-ico-my-cards"></i>
                                <span><s:message code="cmas.face.client.menu.myCards"/></span>
                            </a>
                        </li>
                    </ul>
                </li>
                    <%--<li>--%>
                    <%--<a href="${pageContext.request.contextPath}/faq.html">--%>
                    <%--<span><s:message code="cmas.face.client.faq"/></span>--%>
                    <%--</a>--%>
                    <%--</li>--%>
            </ul>
        </div>
    </c:if>
    <jsp:doBody/>
    <c:if test="${!hideMenu}">
        <script type="application/javascript">
            $('#menuButton').click(function (e) {
                e.preventDefault();
                $('#menuItems').show();
            });
        </script>
    </c:if>
</my:basePage>
