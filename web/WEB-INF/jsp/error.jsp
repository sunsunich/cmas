<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<my:basePage title="cmas.face.error.title" indexpage="false" doNotDoAuth="true"
        >
    <!-- end of Navigation menu -->
    <div id="content" class="content" style="padding: 270px 0 !important">           <!-- Content -->
        <div class="page404"><s:message code="cmas.face.error.message"/> </div>
    </div>
    <!-- end of Content -->
</my:basePage>
