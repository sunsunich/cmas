<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="it" tagdir="/WEB-INF/tags/items" %>

<%@ attribute name="url" required="true" %>

<%@ attribute name="items" type="java.util.List" required="true" %>
<%@ attribute name="count" required="true" type="java.lang.Integer" %>
<%@ attribute name="formObject" required="true" type="org.cmas.presentation.model.SortPaginatorImpl" %>
<%@ attribute name="isError" required="true" type="java.lang.Boolean" %>



<c:choose>
    <c:when test="${!empty items}">
        <pg:pager url="${url}" maxIndexPages="10" maxPageItems="${formObject.limit}" items="${count}"
                  export="currentPageNumber=pageNumber,offSet=pageOffset">


            <pg:param name="fromPrice"/>
            <pg:param name="toPrice"/>
            <pg:param name="category"/>
            <pg:param name="keyWord"/>


            <%--<it:listItems items="${items}"/>--%>

            <pg:index>
                <div class="paginator">
                    <pg:pages>
                        <c:if test="${pageNumber < 10}">
                            &nbsp;
                        </c:if>
                        <c:choose>
                            <c:when test="${pageNumber == currentPageNumber}">
                                <b>${pageNumber}</b>
                            </c:when>
                            <c:otherwise>
                                <%--<a href="${pageUrl}">${pageNumber}</a>--%>
                                <a href="${pageUrl}&sort=${formObject.sort}&dir=${formObject.dir}">${pageNumber}</a>
                            </c:otherwise>
                        </c:choose>
                    </pg:pages>
                </div>
            </pg:index>
        </pg:pager>
    </c:when>
    <c:otherwise>
        <span class="search-error">
        <c:choose>
            <c:when test="${isError}">
                Ошибка соединения. Повторите поиск еще раз
            </c:when>
            <c:otherwise>
                Ничего не найдено
            </c:otherwise>
        </c:choose>
        </span>
    </c:otherwise>
</c:choose>
    
