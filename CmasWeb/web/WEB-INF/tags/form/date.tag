<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>

<ff:row path="${path}" label="${label}">
    <form:input path="${path}" htmlEscape="true" cssErrorClass="errorInput" cssClass="blue" id="date_${path}" cssStyle="width:7em"/>
    <img id="datetrig_${path}" src="/js/calendar/calendar.gif" height="16" width="16" style="margin-left:1px;cursor:pointer" title="Выбрать дату">
    <script type="text/javascript" defer="defer">
      Calendar.setup(
        {
          inputField  : "date_${path}",         // ID of the input field
          ifFormat    : "%d.%m.%Y",    // the date format
          button      : "datetrig_${path}"       // ID of the button
        }
      );
</script>
</ff:row>