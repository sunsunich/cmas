<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>

<ef:row path="${path}" label="${label}">
    <form:input path="${path}" htmlEscape="true" cssErrorClass="errorInput" cssClass="field text small" id="date_${path}" cssStyle="width:10em"/>
    <img id="datetrig_${path}" src="/js/calendar/calendar.gif" height="16" width="16" style="margin-left:1px;cursor:pointer" title="Выбрать дату">
    <script type="text/javascript" defer="defer">
      Calendar.setup(
        {
          showsTime      :    true,
          inputField  : "date_${path}",         // ID of the input field
          ifFormat    : "%d.%m.%Y %H:%M" ,    // the date format
          button      : "datetrig_${path}"       // ID of the button
        }
      );
</script>
</ef:row>