<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="diverRegistrationStatus" required="true" type="java.lang.String" %>

<div class="advert-header">
    <s:message code="cmas.face.advert.registration.gold.header"/>
</div>
<ul class="advert-list">
    <li>
        <s:message code="cmas.face.advert.registration.gold.elem1"/>
    </li>
</ul>

<%--<c:if test="${diverRegistrationStatus == 'NEVER_REGISTERED'--%>
<%--    || diverRegistrationStatus == 'DEMO'--%>
<%--    || diverRegistrationStatus == 'INACTIVE'--%>
<%--    || diverRegistrationStatus == 'CMAS_BASIC'--%>
<%--    }"--%>
<%-->--%>
<div class="advert-header">
    <s:message code="cmas.face.advert.registration.guest.header"/>
</div>
<ul class="advert-list">
    <li>
        <s:message code="cmas.face.advert.registration.guest.elem1"/>
    </li>
    <li>
        <s:message code="cmas.face.advert.registration.guest.elem2"/>
    </li>
    <li>
        <s:message code="cmas.face.advert.registration.guest.elem3"/>
    </li>
    <li>
        <s:message code="cmas.face.advert.registration.guest.elem4"/>
    </li>
</ul>
<div class="advert-header">
    <s:message code="cmas.face.advert.registration.cmas.header"/>
</div>
<ul class="advert-list">
    <li>
        <s:message code="cmas.face.advert.registration.cmas.elem1"/>
    </li>
</ul>
<%--</c:if>--%>

