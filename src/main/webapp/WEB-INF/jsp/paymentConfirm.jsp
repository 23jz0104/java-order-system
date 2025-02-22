<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="./assets/css/style.css">
	<title>レジ画面 支払い確認画面</title>
	</head>
	<body>
		<header>
			<h1>テーブル番号 : ${tableNumber}</h1>
			<h1>総合計 : ￥${totalPrice}</h1>
		</header>
		<main id="pay">
			<div class="pay_scroll">
				<table border="1px" class="order_data">
					<thead>
						<tr>
							<th>注文日時</th>
							<th>商品名</th>
							<th>数量</th>
							<th>金額</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="productDTO" items="${productDTOList}">
							<tr>
								<td>${productDTO.formattedOrderDate}</td>
								<td>
									${productDTO.product.name}
									<c:if test="${not empty productDTO.size}">
										${productDTO.size.name}サイズ
									</c:if>
								</td>
								<td>${productDTO.cart.quantity}</td>
								<td>￥${productDTO.adjustedPrice}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
			<form action="PaymentServlet" method="post">
				<input type="hidden" name="tableNumber" value="${tableNumber}">
				<button type="submit" name="type" value="paymentComplete" class="btn">支払い確定</button>
			</form>
			
			<a href="PaymentServlet" class="back_btn">戻る</a>
		</main>
	</body>
</html>