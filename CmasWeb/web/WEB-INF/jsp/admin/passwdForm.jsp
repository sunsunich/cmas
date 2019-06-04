<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<my:adminpage title="Change password for user">
<ff:form submitText="Change" action="/admin/processPasswdChange.html">
    <ff:password path="passwd" label="New password"/>
    <ff:password path="passwdRe" label="Repeat new password"/>
    <ff:hidden path="userId" />
</ff:form>
</my:adminpage>