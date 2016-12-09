<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>

<jsp:useBean id="navigationStatistics" scope="request" type="org.cmas.presentation.model.user.NavigationStatisticsFormObject"/>

<my:securepage title="ПОПОЛНЕНИЕ счета">

    <div id="navFaq" class="navigation">   <!-- Navigation menu -->
        <div class="navigation_content">

            <div class="controls-block">
                <div id="pop" class="tag_name">Пополнение баланса             <!-- ID категории -->
                    <div class="tag_line"></div>
                </div>

            </div>

            <div class="bottom_line"></div>

        </div>
    </div>
    <!-- end of Navigation menu -->


    <div class="pay-block">

        <div class="pay_rules">Введите сумму пополнения и вы будете переведены на страницу платежного сервиса

        </div>


        <form:form action="/secure/pay.html" method="POST" id="payForm">
            <form:hidden path="paymentType"/>

            <ef:input path="amount" label="Введите сумму в Euro:"/>

            <input class="pay_agree" type="submit" value="Подтвердить"/>

        </form:form>

    </div>


    <!-- end of Content -->

</my:securepage>
