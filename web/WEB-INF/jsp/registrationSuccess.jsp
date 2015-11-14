<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="email" scope="request" type="java.lang.String"/>
<jsp:useBean id="passwordStrength" scope="request" type="java.lang.String"/>

<my:basePage title="РЕГИСТРАЦИЯ" indexpage="false"
             customScripts="/js/scroll.js"
             customCSSFiles="/c/main.css,/c/registration.css"
        >


    <!-- end of Navigation menu -->

    <div class="reg-block">

        <div class="reg_rules">Вы успешно завершили регистрацию и после проверки нашим администратором на ваш e-mail
            придет подтверждение<p>
                <br/>email: ${email}
            </p>

        </div>


    </div>


    <!-- end of Content -->


</my:basePage>