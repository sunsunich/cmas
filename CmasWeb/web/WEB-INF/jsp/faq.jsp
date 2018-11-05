<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:helppage>
    <div class="header2-text faq-header"><s:message code="cmas.face.client.faq"/></div>

    <div class="header3-text question">
        <s:message code="cmas.face.faq.q1"/>
    </div>
    <div class="basic-text answer">
        <s:message code="cmas.face.faq.a1"/>
    </div>
    <div class="header3-text question">
        <s:message code="cmas.face.faq.q2"/>
    </div>
    <div class="basic-text answer">
        <s:message code="cmas.face.faq.a2"/>
    </div>
    <div class="header3-text question">
        <s:message code="cmas.face.faq.q3"/>
    </div>
    <div class="basic-text answer">
        <s:message code="cmas.face.faq.a3"/>
    </div>
    <div class="header3-text question">
        <s:message code="cmas.face.faq.q4"/>
    </div>
    <div class="basic-text answer">
        <s:message code="cmas.face.faq.a4"/>
    </div>
    <div class="header3-text question">
        <s:message code="cmas.face.faq.q5"/>
    </div>
    <div class="basic-text answer">
        <s:message code="cmas.face.faq.a5"/>
            <%--todo change li to stars--%>
        <ul class="purchased-feature-list">
            <li><s:message code="cmas.face.faq.a5.point1"/></li>
            <li><s:message code="cmas.face.faq.a5.point2"/></li>
            <li><s:message code="cmas.face.faq.a5.point3"/></li>
            <li><s:message code="cmas.face.faq.a5.point4"/></li>
            <li><s:message code="cmas.face.faq.a5.point5"/></li>
        </ul>
    </div>
    <div class="header3-text question">
        <s:message code="cmas.face.faq.q6"/>
    </div>
    <div class="basic-text answer">
        <s:message code="cmas.face.faq.a6"/>
    </div>

</my:helppage>
