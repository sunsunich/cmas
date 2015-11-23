<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="lightHeader" required="false" %>

<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.BackendUser"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html itemtype="http://schema.org/Product" itemscope="" xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>${title}</title>

    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>

    <link href="/favicon.ico" rel="shortcut icon"/>


</head>

<body>


<div class="wrapper">       <!-- Wrapper -->

    <div id="header" class="header">    <!-- Header -->
        <!--<div class="f"></div>-->

    </div>
    <!-- end of Header -->

    <!--<div id="loading" class="loader" title="Подождите..."></div>-->
    <jsp:doBody/>

    
</div>
<!-- end of Wrapper -->


<div class="footer_wrapper">            <!-- Footer -->

</div>
<!-- end of Footer -->

</body>
</html>
