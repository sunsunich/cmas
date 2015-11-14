<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="passwordStrength" scope="request" type="java.lang.String"/>

<my:basePage title="СМЕНА ПАРОЛЯ" indexpage="false"

        >
    <!-- end of Navigation menu -->

    <div class="reg-block">

        <div class="reg_rules">Изменение текущего пароля

        </div>



        <form:form htmlEscape="true"
                   action="/changePasswd.html"
                   method="POST"
                   id="">
            <ef:password path="password" label="Введите новый пароль:" />
            <ef:password path="checkPassword" label="Повторите новый пароль:" />
            <input type="hidden" name="code" value="${command.code}"/>

            <input class="reg_agree" type="submit" value="Подтвердить"/>

        </form:form>

    </div>

    <!-- end of Content -->

</my:basePage>
