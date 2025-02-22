<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="./assets/css/style.css">
	<title>キッチンタスク管理システム 注文履歴</title>
	</head>
	<body>
		<header class="employee_header">
		<ul>
			<li><a href="CookServlet">オーダー</a></li>
			<li><a href="CookServlet?type=delivery">配膳</a></li>
			<li><a href="CookServlet?type=orderHistory">注文履歴</a></li>
		</ul>
		</header>
		<main id="employee"> 
		<div class="scroll">
			<table border="1" class="task_list">
				<thead>
					<tr>
						<th>注文時刻</th>
						<th>テーブルNo.</th>
						<th>注文Id</th>
						<th>注文状態</th>
						<th>商品名</th>		
						<th>数量</th>	
					</tr>	
				</thead>
				<tbody>
				<c:forEach var="productDTO" items="${productDTOList}">
					<tr>
						<td>${productDTO.formattedOrderDate}</td>
						<td>${productDTO.tableNumber}</td>
						<td>${productDTO.cart.orderId}</td>
						<td>${productDTO.status.name}</td>
						<td>${productDTO.product.name}
							<c:if test="${not empty productDTO.size}">
								${productDTO.size.name}サイズ
							</c:if>
						</td>
						<td>${productDTO.cart.quantity}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		</main>
	</body>
</html>