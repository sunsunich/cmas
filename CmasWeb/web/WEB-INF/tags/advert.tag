<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="diverRegistrationStatus" required="true" type="java.lang.String" %>

<c:choose>
    <c:when test="${diverRegistrationStatus == 'CMAS_FULL' || diverRegistrationStatus == 'CMAS_BASIC'}">
        <c:choose>
            <c:when test="${diverRegistrationStatus == 'CMAS_FULL'}">
                <div class="advert-header">
                    <s:message code="cmas.face.advert.cmasFull.header"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="advert-header">
                    <s:message code="cmas.face.advert.cmasBasic.header"/>
                </div>
            </c:otherwise>
        </c:choose>
        <ul class="advert-list">
            <li>
                <s:message code="cmas.face.advert.guest.elem1"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.guest.elem2"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.cmasFull.elem1"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.cmasFull.elem2"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.cmasFull.elem3"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.cmasBasic.elem1"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.cmasBasic.elem2"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.cmasBasic.elem3"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.cmasBasic.elem4"/>
            </li>
        </ul>
    </c:when>
    <c:otherwise>
        <%--Guest and Demo--%>
        <c:choose>
            <c:when test="${diverRegistrationStatus == 'GUEST'}">
                <div class="advert-header">
                    <s:message code="cmas.face.advert.guest.header"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="advert-header">
                    <s:message code="cmas.face.advert.demo.header"/>
                </div>
            </c:otherwise>
        </c:choose>
        <ul class="advert-list">
            <li>
                <s:message code="cmas.face.advert.guest.elem1"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.guest.elem2"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.demo.elem1"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.demo.elem2"/>
            </li>
            <li>
                <s:message code="cmas.face.advert.demo.elem3"/>
            </li>
        </ul>
    </c:otherwise>
</c:choose>