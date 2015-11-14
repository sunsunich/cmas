<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="it" tagdir="/WEB-INF/tags/items" %>

<jsp:useBean id="navigationStatistics" scope="request"
             type="org.cmas.presentation.model.user.NavigationStatisticsFormObject"/>
<jsp:useBean id="finLogs" scope="request" type="java.util.List"/>
<jsp:useBean id="count" scope="request" type="java.lang.Integer"/>

<my:securepage title="ЗАКАЗЫ">
    <div id="navBk" class="navigation">   <!-- Navigation menu -->
        <div class="navigation_content">



            <div class="controls-block">
                <div id="" class="tag_name">Платежи           <!-- ID категории -->
                    <div class="tag_line"></div>
                </div>

                <it:pagerListItems url="/secure/finstat.html"
                                   items="${finLogs}"
                                   count="${count}"
                                   formObject="${command}"
                                   isError="false"
                        >
                
                </it:pagerListItems>

            <div class="bottom_line"></div>

        </div>
    </div>
    <!-- end of Navigation menu -->
    </div>

    <div class="content">           <!-- Content -->
        <table class="orders">
            <tr>
                <th>№№</th>
                <th>Дата операции</th>
                <th>Сумма</th>
                <th>Тип заказа</th>
            </tr>

            <c:forEach items="${finLogs}" var="finlog" varStatus="st">
                <tr class="list">
                    <td class="count">
                    ${st.count}
                    </td>
                    <td class="c-date">
                            ${finlog.recordDate}
                    </td>
                    <td class="price-cost">
                            <c:choose>
                            <c:when test="${finlog.operationType.name == 'IN'}">
                                <span class="select-in">${finlog.amount}&nbsp;$</span>
                            </c:when>
                            <c:when test="${finlog.operationType.name == 'PURCHASE'}">
                                <span class="select-purchase">${-1*finlog.amount}&nbsp;$</span>
                            </c:when>
                            <c:when test="${finlog.operationType.name == 'RETURN'}">
                                <span class="select-return">${finlog.amount}&nbsp;$</span>
                            </c:when>
                        </c:choose>
                    </td>
                    <td class="fin-status">
                        <c:choose>
                            <c:when test="${finlog.operationType.name == 'IN'}">
                                Пополнение счета
                            </c:when>
                            <c:when test="${finlog.operationType.name == 'PURCHASE'}">
                                Покупка товара
                            </c:when>
                            <c:when test="${finlog.operationType.name == 'RETURN'}">
                                Возврат средств
                            </c:when>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>

        </table>

    </div>
    <!-- end of Content -->
</my:securepage>
