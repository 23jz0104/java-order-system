<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"href="./assets/css/style.css">
<title>お会計</title>
</head>
<body>
	<header id="auth_header">
		<h1>お会計</h1>
	</header>
	<main id="payment">
		<h2>ありがとうございます</h2>
		<em>お会計は、￥${totalPrice}です</em>
		<form action="OrderServlet" method="post">
			<button type="submit" name="type" value="checkoutRequestConfirmed">お会計</button>
		</form>
	</main>
	<footer>
		<ul>
			<li><a href="CallServlet?categoryId=${categoryId}">サービス一覧</a></li> 
			<li><a href="OrderServlet?type=topPage&categoryId=${categoryId}">TOP</a></li>
			<li><a href="OrderServlet?type=orderHistory&categoryId=${categoryId}">注文履歴</a></li>
			<li><a href="OrderServlet?type=checkoutRequest&categoryId=${categoryId}">お会計</a></li>
		</ul>
	</footer>
</body>
</html>