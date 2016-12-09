
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<jsp:useBean id="user" scope="request" type="org.cmas.entities.User"/>

<my:fed_adminpage title="Diver Info">
<h2>Diver Info</h2>
    <table border="0" cellpadding="4" cellspacing="2" bgcolor="white">
        <tr class="info">
            <td align="right">E-Mail</td>
            <td align="left">${user.email}</td>
        </tr>
        <tr class="info">
            <td align="right">Country</td>
            <td align="left">${user.country.name}</td>
        </tr>

    </table>
</my:fed_adminpage>