<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="hideMenu" required="false" %>

<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.BackendUser"/>

<my:basePage title="${title}" indexpage="false" doNotDoAuth="true" customCSSFiles="/c/menu.css">

    <c:if test="${!hideMenu}">
        <div id="cssmenu">
            <ul>
                <li><a href="${pageContext.request.contextPath}/logout.html">
                    <i class="menu-ico menu-ico-logout"></i><span>Sign out</span></a>
                </li>
                <li class="has-sub"><a style="pointer-events: none;" href="#">
                    <i class="menu-ico menu-ico-menu"></i><span>Menu</span></a>
                    <ul>
                        <li><a href="#">
                            <i class="menu-ico menu-ico-my-account"></i><span>My Account</span></a>
                        </li>
                        <li><a href="#">
                            <i class="menu-ico menu-ico-logbook"></i><span>Logbook</span></a>
                        </li>
                        <li><a href="#">
                            <i class="menu-ico menu-ico-diving-spots"></i><span>Diving spots</span></a>
                        </li>
                        <li><a href="#">
                            <i class="menu-ico menu-ico-my-cards"></i><span>My cards</span></a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </c:if>
    <jsp:doBody/>

</my:basePage>
