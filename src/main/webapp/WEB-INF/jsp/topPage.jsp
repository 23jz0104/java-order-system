<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./assets/css/style.css">
<title>Top Page</title>
</head>
<body>
<main>
<div class="wrap">
  	<div class="menu">
		<!-- カテゴリ一覧を表示 -->
	    <ul class="menuber">
		<c:forEach var="category" items="${categoryList}">
			<li><a href="OrderServlet?type=topPage&categoryId=${category.id}">${category.name}</a></li>
		</c:forEach>
		</ul>
		
		<!-- カテゴリ番号に応じた商品を一覧表示 -->
		<ul class="menus">
		<c:forEach var="product" items="${productList}">
			<input type="hidden" name="productName" value="${product.name}">
			<input type="hidden" name="price" value="${product.price}">
			<input type="hidden" name="quantity" value="1">
			<li class="menu_item">
				<a href="OrderServlet?type=product&productId=${product.id}&categoryId=${product.categoryId}&page=${page}">
					<figure class="menu_img"><img src="./assets/img/products/${product.image}.png"></figure>
					<p>${product.name}</p>
					<p>￥${product.price}</p>
				</a>
			</li>
		</c:forEach>
		</ul>
		<!-- ページ進む戻るボタン -->
		<div class="preview">
			<a href="OrderServlet?type=topPage&categoryId=${categoryId}&page=${prevPage}" class="previewbtn back">戻る</a>
			<p class="now_page">${page} / ${maxPage}</p>
			<a href="OrderServlet?type=topPage&categoryId=${categoryId}&page=${nextPage}" class="previewbtn next">進む</a>
		</div>
	</div>
	<!-- セッションにcartListが存在していれば、その一覧を表示 -->
	<div class="cart">
        <p class="ttl">カート内商品</p>
        <ul>
		<c:if test="${not empty cartList}">
			<c:forEach var="item" items="${cartList}">
				<c:set var="cart" value="${item.value}" />
				<li class="item">
					<!-- 商品数更新用フォーム -->
					<form action="OrderServlet" method="post">
						${cart.product.name} 
						<c:if test="${not empty cart.size.name}">
						${cart.size.name}サイズ
						</c:if><br>
						 ${cart.adjustedPrice}円 ${cart.quantity}件
						<input type="hidden" name="type" value="updateQuantity">
						<input type="hidden" name="cartListKey" value="${item.key}">
						<input type="hidden" name="categoryId" value="${categoryId}">
						<input type="hidden" name="page" value="${page}">
						<button type="submit" name="updateQuantity" value="decrease">-</button>
						<button type="submit" name="updateQuantity" value="increase">+</button>
					</form>
				</li>
			</c:forEach>
		</c:if>
        </ul>
			
		<!-- セッションに存在しているcartListをDBに登録する為のフォーム -->
		<form action="OrderServlet" method="post" class="order_btn">
			<input type="hidden" name="type" value="addCartToDB">
			<input type="hidden" name="categoryId" value="${categoryId}">
			<input type="hidden" name="cartList" value="${cartList}">
			<span>${totalProduct}点 ${totalPrice}円</span>
			<c:if test="${not empty cartList}">
				<button type="submit">注文する</button>
			</c:if>
		</form>
    </div>
	</div>
	<footer>
		<ul>
			<li><a href="CallServlet?categoryId=${categoryId}">サービス一覧</a></li> 
			<li><a href="OrderServlet?type=topPage&categoryId=${categoryId}">TOP</a></li>
			<li><a href="OrderServlet?type=orderHistory&categoryId=${categoryId}">注文履歴</a></li>
			<li><a href="OrderServlet?type=checkoutRequest&categoryId=${categoryId}">お会計</a></li>
		</ul>
	</footer>
</main>
</body>
</html>