<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="./assets/css/style.css">
	<title>レジシステム レジ画面</title>
	</head>
	<body>
		<header id="auth_header">
			<h1>レジシステム</h1>
		</header>
		<main id="pay">
			<form action="PaymentServlet" method="post">
				<label>
					テーブル番号 : <input type="text" name="tableNumber">
				</label>
				<button type="submit" name="type" value="paymentConfirm" class="btn">確定</button>
			</form>
			
			<c:if test="${not empty errorMessage}">
				<p style="color : red">${errorMessage}
			</c:if>
		</main>
	</body>
</html>