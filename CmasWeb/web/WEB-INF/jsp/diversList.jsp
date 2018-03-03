<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="divers" scope="request" type="java.util.List<org.cmas.entities.diver.Diver>"/>

<my:basePage title="cmas.face.index.header">
    <div class="content">
        <div class="pass_link">
            <a class="link" href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.link.back.text"/>
            </a>
        </div>
        <c:forEach items="${divers}" var="diver">
            <div class="panel clearfix">
                <div class="diverList-elem-left">
                    <img class="userpic"
                            <c:choose>
                                <c:when test="${diver.photo == null}">
                                    src="${pageContext.request.contextPath}/i/no_img.png?v=${webVersion}"
                                </c:when>
                                <c:otherwise>
                                    src="data:image/png;base64,${diver.photo}"
                                </c:otherwise>
                            </c:choose>
                            />
                </div>
                <div class="diverList-elem-right">
                    <p><span class="panel-text">${diver.firstName} ${diver.lastName}</span></p>

                    <p><span class="panel-text">${diver.diverType}</span>
                    </p>
                    <c:choose>
                        <c:when test="${diver.diverLevel == 'ONE_STAR'}">
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                        </c:when>
                        <c:when test="${diver.diverLevel == 'TWO_STAR'}">
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                        </c:when>
                        <c:when test="${diver.diverLevel == 'THREE_STAR'}">
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                        </c:when>
                        <c:when test="${diver.diverLevel == 'FOUR_STAR'}">
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                            <img class="star" src="${pageContext.request.contextPath}/i/star.png?v=${webVersion}"/>
                        </c:when>
                    </c:choose>
                    <c:choose>
                        <c:when test="${diver.primaryPersonalCard == null}">
                            <p><span class="panel-text">Not registered at CMAS Data</span></p>
                        </c:when>
                        <c:otherwise>
                            <p><span class="panel-text">${diver.primaryPersonalCard.printNumber}</span></p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
        <div class="pass_link">
            <a class="link" href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.link.back.text"/>
            </a>
        </div>
    </div>
</my:basePage>


