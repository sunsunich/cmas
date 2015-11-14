<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>
<%@ attribute name="title" required="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ru" xml:lang="ru">
<head>
    <link rel="stylesheet" type="text/css" href="/c/admin.css"/>
    <link rel="stylesheet" type="text/css" href="/c/main.css"/>
    
    <title>Административная часть</title>

    <script type="text/javascript" src="/js/util.js"></script>
    
</head>
<body>
    <div id="Wrapper" class="wrapper">       <!-- Wrapper -->

    <!-- END HEADER -->


    <div class="header" id="header">    <!-- Header -->
        <div class="f"></div>


    </div>
    <!-- end of Header -->


    <div id="" class="content">           <!-- Content -->
        <div class="content-body">
    <table>
        <tr>
            <td><a href="/admin/index.html">Пользователи</a>&nbsp;&nbsp;&nbsp;</td>
            <td><a href="/admin/registration/readyToCreate.html">Юзеры&nbsp;для&nbsp;регистрации</a>&nbsp;&nbsp;&nbsp;</td>       

            <td width="100%"></td>
            <td><a href="/logout.html">Выйти</a>
        </tr>
        <%--<tr>--%>
            <%--<td><a href="/admin/listPartner.html">Партнеры</a></td>--%>
            <%--<td colspan="10">&nbsp;&nbsp;&nbsp;</td>--%>
        <%--</tr>--%>
    </table>


<jsp:doBody/>
    </div>
    </div> <!-- end of Content -->
    </div>
<!-- end of Wrapper -->

<div id="Footer" class="footer_wrapper">            <!-- Footer -->
    <div class="footer">
        <div class="footer_content">

        </div>
    </div>
</div>
    
    




</body>
</html>
