<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<form id="interkassa" name="payment" action="https://www.interkassa.com/lib/payment.php" method="post"
      enctype="application/x-www-form-urlencoded" accept-charset="cp1251">
    <input type="hidden" name="ik_shop_id" value="${data.ik_shop_id}">
    <input type="hidden" name="ik_payment_amount" value="${data.ik_payment_amount}">
    <input type="hidden" name="ik_payment_id" value="${data.ik_payment_id}">
    <input type="hidden" name="ik_payment_desc" value="${data.ik_payment_desc}">
</form>
<script type="text/javascript">document.getElementById('interkassa').submit();</script>
</body>
</html>