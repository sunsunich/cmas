
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<jsp:useBean id="user" scope="request" type="org.cmas.presentation.entities.user.BackendUser"/>

<my:basePage title="ВОССТАНОВЛЕНИЕ ПАРОЛЯ" indexpage="false"
             customScripts="/js/scroll.js"
             customCSSFiles="/c/main.css,/c/registration.css"
        >



    <!-- end of Navigation menu -->

    <div class="reg-block">

        <div class="reg_rules">Чтобы начать процесс изменения пароля, пожалуйста, следуйте инструкциям, отправленным на ваш электронный адрес<p>
                <br/>email: ${user.email}
            </p>

        </div>


    </div>


    <!-- end of Content -->


</my:basePage>