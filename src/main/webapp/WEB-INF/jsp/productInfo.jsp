<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./assets/css/style.css">
<title>商品詳細</title>
</head>
<body>
	<header id="auth_header">
		<h1>商品詳細</h1>
	</header>
	<main>
		<div class="wrap">
			<div class="txt">
				<h2 class="name">${productDTOList[0].product.name}</h2>
				
				<!-- <p>￥${productDTOList[0].product.price}</p> -->
				<p class="desc">${productDTOList[0].product.supplement}</p>
			</div>
			<figure class="info_img"><img src="./assets/img/products/${productDTOList[0].product.image}.png"></figure>
		</div>
		<form action="OrderServlet" method="post" class="add_form">
			<input type="hidden" name="type" value="addCart">
			<input type="hidden" name="productId" value="${productDTOList[0].product.id}">
			<input type="hidden" name="categoryId" value="${productDTOList[0].category.id}">
			<input type="hidden" name="page" value="${page}">
			<input type="hidden" name="quantity" value="1">
			
			<!-- カテゴリ1(サイズがあるメインメニューの場合 -->
			<c:if test="${productDTOList[0].category.id eq 1}">
				<c:forEach var="productDTO" items="${productDTOList}">
					<button type="submit" name="sizeId" value="${productDTO.size.id}" class="add_btn">
						${productDTO.size.name} ￥${productDTO.adjustedPrice}
					</button>
				</c:forEach>
			</c:if>
			
			<!-- カテゴリ1以外(サイズが存在しないメニュー -->
			<c:if test="${productDTOList[0].category.id ne 1}">
				<button type="submit" class="add_btn">カートに追加</button> 
			</c:if>
		</form>
		<a href="OrderServlet?type=topPage&categoryId=${productDTOList[0].category.id}&page=${page}" class="back_btn">戻る</a>
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