<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="submitText" required="true" %>
<%@ attribute name="action" required="false" %>
<%@ attribute name="method" required="false" type="java.lang.String"%>
<%@ attribute name="commandName" required="false" %>
<%@ attribute name="noRequiredText" required="false" %>
<%@ attribute name="id" required="false" %>
<c:set var="command">${(not empty commandName)?(commandName):('command')}</c:set>
<c:if test="${empty method}">
    <c:set var="method" value="POST"/>
</c:if>
<form:form htmlEscape="true" action="${action}" commandName="${command}" method="${method}" cssClass="b-form" id="${id}">
    <spring:hasBindErrors name="${command}">
        <ul>
           <c:forEach items="${errors.globalErrors}" var="error">
                <li><span class="error"><spring:message message="${error}"/></span></li>
           </c:forEach>
        </ul>
    </spring:hasBindErrors>
    <table class="formTable">
        <jsp:doBody/>
        <c:if test="${not noRequiredText}">
            <tr>
                <td colspan="3"><div  style="font-size:80%;margin:8px;">
                    Знаком <span class="reqMark" style="font-size:120%">*</span> помечены поля, обязательные для заполнения
                </div></td>
            </tr>
        </c:if>
        <tr>
            <td>&nbsp;</td>
            <td  align="left">
                <button type="submit">${submitText}</button>
            </td>
            <td>&nbsp;</td>
        </tr>
    </table>
</form:form>