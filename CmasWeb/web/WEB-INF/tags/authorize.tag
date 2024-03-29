<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<%--
	ROLE_ATHLETE(Role.ROLE_ATHLETE),
	ROLE_DIVER(Role.ROLE_DIVER),
--%>
<authz:authorize ifAnyGranted="ROLE_DIVER" ifNotGranted="ROLE_ADMIN">
    <script type="text/javascript">
        cookie_controller.addAuthCookie();
        window.location = '/secure/index.html';
    </script>
</authz:authorize>

<authz:authorize ifAnyGranted="ROLE_FEDERATION_ADMIN" ifNotGranted="ROLE_ADMIN">
    <script type="text/javascript">
        cookie_controller.addAuthCookie();
        window.location = '/fed/index.html';
    </script>
</authz:authorize>

<authz:authorize ifAnyGranted="ROLE_ADMIN">
    <script type="text/javascript">
        cookie_controller.addAuthCookie();
        window.location = '/admin/index.html';
    </script>
</authz:authorize>


<authz:authorize ifNotGranted="ROLE_ADMIN,ROLE_ATHLETE,ROLE_DIVER,ROLE_FEDERATION_ADMIN,ROLE_AMATEUR">
    <script type="text/javascript">
        cookie_controller.removeAuthCookie();
    </script>
</authz:authorize>