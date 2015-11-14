<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="navigationStatistics" scope="request" type="org.cmas.presentation.model.user.NavigationStatisticsFormObject"/>

<jsp:useBean id="passwordStrength" scope="request" type="java.lang.String"/>

<my:securepage title="ИЗМЕНЕНИЕ ПАРОЛЯ"
        >

    <div id="navFaq" class="navigation">   <!-- Navigation menu -->
        <div class="navigation_content">

            <div class="controls-block">
                <div id="pop" class="tag_name">Настройки             <!-- ID категории -->
                    <div class="tag_line"></div>
                </div>

            </div>

            <div class="bottom_line"></div>

        </div>
    </div>
    <!-- end of Navigation menu -->


    <div class="setting-block">

        <div class="setting_rules">Изменение текущего пароля

        </div>

         <form:form htmlEscape="true"
                   action="/secure/processEditPasswd.html"
                   method="POST"
                   id="">             

            <ef:password path="oldPassword" label="Введите текущий пароль:"/>
            <ef:password path="password" label="Введите новый пароль:"/>
            <ef:password path="checkPassword" label="Повторите новый пароль:"/>

            <input class="setting_agree" type="submit" value="Подтвердить"/>

        </form:form>


    </div>


    <!-- end of Content -->
</my:securepage>