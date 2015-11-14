<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<authz:authorize ifAnyGranted="ROLE_USER" ifNotGranted="ROLE_ADMIN">
    <script type="text/javascript">
        addAuthCookie();
        window.location = '/secure/index.html';
    </script>
</authz:authorize>

<authz:authorize ifAnyGranted="ROLE_ADMIN">
    <script type="text/javascript">
        addAuthCookie();
        window.location = '/admin/index.html';
    </script>
</authz:authorize>


<authz:authorize ifNotGranted="ROLE_ADMIN, ROLE_USER">
    <script type="text/javascript">
        removeAuthCookie();
    </script>
</authz:authorize>