<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./assets/css/style.css">
<title>注文履歴</title>
</head>
<body>
	<header id="auth_header">
		<h1>注文履歴</h1>
	</header>
	<main class="payment">
		<!-- DBに登録されているテーブル番号に応じた商品の注文履歴を一覧表示 -->
		<ul class="order_history">
			<c:forEach var="item" items="${orderHistoryList}">
				<li>
					<p>
						<!-- <img src="${item.product.image}"> -->
						${item.product.name} 
						<c:if test="${not empty item.size.name}">
							${item.size.name}サイズ
						</c:if>${item.adjustedPrice}円 
						${item.quantity}件
					</p>
				</li>
			</c:forEach>
		</ul>
		
		<h2 class="total">合計金額 : ￥${totalPrice}</h2>
		
		<!-- topPageに戻るボタン -->
		<a href="OrderServlet?type=topPage&categoryId=${categoryId}" class="back_btn">戻る</a>
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