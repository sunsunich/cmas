<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="siteEmail" scope="request" type="java.lang.String"/>

<my:helppage>
    <div class="header2-text faq-header"><s:message code="cmas.face.client.contact"/></div>
    <div class="header3-text question">
        CMAS HEADQUARTERS
    </div>
    <div class="answer">
        <div class="basic-text">
            Viale Tiziano, 74 <br/>
            00196 Roma <br/>
            Italy <br/>
            Tel: 0039 06 3211 0594 / 3 <br/>
            Fax: 0039 06 3211 0595 <br/>
        </div>
        <div class="header3-text">
            Mrs. CAFINI Giusy
        </div>
        <div class="basic-text">
            General Administration<br/>
            e-mail: <a href="mailto:cmas@cmas.org">cmas@cmas.org</a>
        </div>
        <div class="header3-text">
            Mr. ROSORANI Daniele
        </div>
        <div class="basic-text">
            Resp. Technical Committee<br/>
            e-mail: <a href="mailto:tec@cmas.org">tec@cmas.org</a>
        </div>
        <div class="header3-text">
            Sport Committee Issues
        </div>
        <div class="basic-text">
            e-mail: <a href="mailto:spo@cmas.org">spo@cmas.org</a>
        </div>
    </div>
    <div class="header3-text question">
        AquaLink technical Support
    </div>
    <div class="answer">
        <div class="header3-text">
            ID Sport Ltd
        </div>
        <div class="basic-text">
            171, Old Bakery Street<br/>
            Valletta, VLT 1455<br/>
            Malta<br/>
            e-mail: <a href="mailto:${siteEmail}">${siteEmail}</a>
        </div>
    </div>
</my:helppage>
