<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./assets/css/style.css">
<title>店員呼び出し中</title>
</head>
<body>
	<header id="auth_header">
		<h1>サービス一覧</h1>
	</header>
	<main id="services">
		<div class="service_btn">
			<form action="CallServlet" method="post" class="call_btn"> 
				<input type="hidden" name="type" value="call">
				<c:forEach var="event" items="${eventList}">
					<button type="submit" name="eventId" value="${event.id}" class="btn">${event.name}</button>
				</c:forEach>
			</form>
		</div>
		
	<a href="OrderServlet" class="back_btn">戻る</a>
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