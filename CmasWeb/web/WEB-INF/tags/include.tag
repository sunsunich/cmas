<%@ tag import="org.cmas.i18n.LocaleBasedViewResolver" %>
<%@ tag import="java.util.Enumeration" %>
<%@ tag import="java.util.HashMap" %>
<%@ tag import="java.util.Map" %>
<%@ tag body-content="scriptless" pageEncoding="UTF-8" dynamic-attributes="attrMap" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- тут делается include с учетом поиска ресурса по локализованным путям
если вы хотите заинклудить локализованную jsp , т.е. такую
для котороый существует несколько вариантов на нескольких языках, нужно
 1) положить файл в /web-inf/jsp/path/file.jsp, и /web-inf/jsp/locale/path/file.jsp
 2) написать в месте инклуда <ns:include path="path/file"/>
 где ns - namespace с которым импортирована наша библиотека тегов,
 а от полного пути к файлу оторваны все части, которые добавляет вью резолвер,
 т.е. /web-inf/jsp/ и .jsp     

также теперь тег принимает произвольные строковые атрибуты для передачи их в инклуднутый файл
--%>
<%@ attribute name="path" required="true" %>

<%
    Map<String, Object> oldAttrs = new HashMap<String, Object>();
    for (Enumeration anames = request.getAttributeNames();anames.hasMoreElements();) {
        String name = (String)anames.nextElement();
        oldAttrs.put(name, request.getAttribute(name));
    }

    Map<String,String> attributesMap = (Map<String,String>)jspContext.getAttribute("attrMap");
    for (Map.Entry<String, String> item : attributesMap.entrySet()) {
        request.setAttribute(item.getKey(), item.getValue());
    }

    LocaleBasedViewResolver vr = (LocaleBasedViewResolver)request.getAttribute(LocaleBasedViewResolver.class.getName());
    String realPath = vr!=null?vr.buildLocalizedPath(path):path;
    RequestDispatcher rd = request.getRequestDispatcher(realPath);
    out.flush(); // иначе вставится хз куда, а не в том месте где нам надо.
    rd.include(request, response);

    for (Map.Entry<String, Object> item : oldAttrs.entrySet()) {
        request.setAttribute(item.getKey(), item.getValue());
    }

%>
