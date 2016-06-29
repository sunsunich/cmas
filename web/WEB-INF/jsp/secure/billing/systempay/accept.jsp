<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<form id="systempay" name="payment" action="https://systempay.cyberpluspaiement.com/vads-payment/" method="post">
    <input type="hidden" name="vads_page_action" value="PAYMENT">
    <input type="hidden" name="vads_payment_config" value="SINGLE">
    <input type="hidden" name="vads_action_mode" value="INTERACTIVE">
    <input type="hidden" name="vads_version" value="V2">
    <input type="hidden" name="vads_return_mode" value="GET">
    <input type="hidden" name="vads_ctx_mode" value="${data.vads_ctx_mode}">
    <input type="hidden" name="vads_amount" value="${data.vads_amount}">
    <input type="hidden" name="vads_currency" value="${data.vads_currency}">
    <input type="hidden" name="vads_cust_email" value="${data.vads_cust_email}">
    <input type="hidden" name="vads_order_id" value="${data.vads_order_id}">
    <input type="hidden" name="vads_language" value="${data.vads_language}">
    <input type="hidden" name="vads_site_id" value="${data.vads_site_id}">
    <input type="hidden" name="vads_trans_date" value="${data.vads_trans_date}">
    <input type="hidden" name="vads_trans_id" value="${data.vads_trans_id}">
    <input type="hidden" name="signature" value="${data.signature}">
</form>
<script type="text/javascript">document.getElementById('systempay').submit();</script>
</body>
</html>