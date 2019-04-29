<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="loyaltyProgramItems" scope="request" type="java.util.List<org.cmas.entities.fin.LoyaltyProgramItem>"/>

<my:securepage title="cmas.face.loyalty.title" activeMenuItem="loyaltyProgram">

    <div class="content-center-wide" id="content">

        <div class="panel panel-header">
            <span class="header2-text"><s:message code="cmas.face.loyalty.subtitle"/></span>
        </div>
        <div class="panel-cards">
            <div class="clearfix">
                <c:forEach items="${loyaltyProgramItems}" var="loyaltyProgramItem">
                    <c:set var="itemId" value="${loyaltyProgramItem.id}"/>
                    <div class="content-card">
                        <div class="card-container">
                            <div onclick="window.location = '/secure/loyaltyProgramItem.html?itemId=${itemId}'"
                                 style="cursor: pointer"
                            >
                                <div class="header3-text"><s:message
                                        code="cmas.face.loyalty.item.${itemId}.small.header"/></div>
                                <img id="${loyaltyProgramItem.id}" style="border-radius: 15px"
                                     src="${pageContext.request.contextPath}/i/loyalty/small_item_${itemId}.png"/>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <!-- end of Content -->

</my:securepage>
