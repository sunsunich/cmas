<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="passwordStrength" scope="request" type="java.lang.String"/>

<jsp:useBean id="navigationStatistics" scope="request" type="org.cmas.presentation.model.user.NavigationStatisticsFormObject"/>

<my:securepage title="ИЗМЕНЕНИЕ ПАРОЛЯ"
        >

    <div id="navFaq" class="navigation">   <!-- Navigation menu -->
        <div class="navigation_content">

            <div class="controls-block">
                <div id="pop" class="tag_name">Смена пароля             <!-- ID категории -->
                    <div class="tag_line"></div>
                </div>

            </div>

            <div class="bottom_line"></div>

        </div>
    </div>
    <!-- end of Navigation menu -->


    <div class="setting-block">

        <div class="setting_rules">Вы успешно сменили пароль

        </div>


    </div>

</my:securepage>