<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:basePage title="cmas.face.index.header" indexpage="false" doNotDoAuth="true" >

    <div class="content" id="Content">
        <div class="header"><span><b><s:message code="cmas.face.client.faq"/></b></span></div>
        <div class="return">
            <a href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.link.back.text"/>
            </a>
        </div>
        <div class="question">
            <s:message code="cmas.face.faq.q1"/>
        </div>
        <div class="answer">
            <s:message code="cmas.face.faq.a1"/>
        </div>
        <div class="question">
            <s:message code="cmas.face.faq.q2"/>
        </div>
        <div class="answer">
            <s:message code="cmas.face.faq.a2"/>
        </div>
        <div class="question">
            <s:message code="cmas.face.faq.q3"/>
        </div>
        <div class="answer">
            <s:message code="cmas.face.faq.a3"/>
            <ul>
                <li><s:message code="cmas.face.faq.a3.point1"/></li>
                <li><s:message code="cmas.face.faq.a3.point2"/></li>
                <li><s:message code="cmas.face.faq.a3.point3"/></li>
                <li><s:message code="cmas.face.faq.a3.point4"/></li>
                <li><s:message code="cmas.face.faq.a3.point5"/></li>
            </ul>
        </div>
        <div class="question">
            <s:message code="cmas.face.faq.q4"/>
        </div>
        <div class="answer">
            <s:message code="cmas.face.faq.a4"/>
        </div>
    </div>

</my:basePage>