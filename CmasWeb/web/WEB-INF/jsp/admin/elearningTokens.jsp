<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>

<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:adminpage title="Add e-learning tokens"
              customScripts="/js/model/fileUpload_model.js,/js/controller/fileUpload_controller.js">

    <h2>Add e-learning tokens</h2>

    <ff:form submitText="Upload" action="/admin/uploadElearningTokens.html" commandName="tokenFileFormObject" method="POST"
             noRequiredText="true" enctype="multipart/form-data" id="tokenUpload">
        <ff:file path="file" label="Text file with tokens" inputId="tokenUploadInput"/>
    </ff:form>
</my:adminpage>