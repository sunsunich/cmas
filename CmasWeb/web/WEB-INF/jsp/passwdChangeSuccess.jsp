<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="passwordStrength" scope="request" type="java.lang.String"/>

<my:basePage title="ИЗМЕНЕНИЕ ПАРОЛЯ"
        >

    <!-- end of Navigation menu -->

    <div class="setting-block">

        <div class="setting_rules">Вы успешно сменили пароль
            <p>
                <br/><a class="go-main" href="/">Переход на главную страницу</a>
            </p>

        </div>


    </div>


    <!-- end of Content -->


</my:basePage>