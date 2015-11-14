<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<jsp:useBean id="passwordStrength" scope="request" type="java.lang.String"/>

<my:basePage title="РЕГИСТРАЦИЯ" indexpage="false"

        >
 

    <!-- end of Navigation menu -->

    <div class="reg-block">

        <div class="reg_rules">Заполните все поля указанной формы и после проверки ваших данных нашим администратором,
            вы получите письмо с подтверждением регистрации

        </div>



        <form:form htmlEscape="true"
                   action="/register-user-submit.html"
                   method="POST"
                   id="">
            <ef:input label="E-mail:*" path="email"/>
            <ef:password label="Пароль:*" path="password"/>
            <ef:password label="Повторите пароль:*" path="checkPassword"/>

            <input class="reg_agree" type="submit" value="Подтвердить"/>

        </form:form>

    </div>

    <!-- end of Content -->

</my:basePage>
