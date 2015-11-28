<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<authz:authorize ifAnyGranted="ROLE_AMATEUR" ifNotGranted="ROLE_ADMIN">
    <script type="text/javascript">
        cookie_controller.addAuthCookie();
        window.location = '/secure/index.html';
    </script>
</authz:authorize>

<authz:authorize ifAnyGranted="ROLE_SPORTSMAN" ifNotGranted="ROLE_ADMIN">
    <script type="text/javascript">
        cookie_controller.addAuthCookie();
        window.location = '/secure/index.html';
    </script>
</authz:authorize>

<authz:authorize ifAnyGranted="ROLE_ADMIN">
    <script type="text/javascript">
        cookie_controller.addAuthCookie();
        window.location = '/admin/index.html';
    </script>
</authz:authorize>


<authz:authorize ifNotGranted="ROLE_ADMIN, ROLE_SPORTSMAN, ROLE_AMATEUR">
    <script type="text/javascript">
        cookie_controller.removeAuthCookie();
    </script>
</authz:authorize>