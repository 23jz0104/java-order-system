<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./assets/css/style.css">
<title>キッチンタスク管理システム</title>
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
	<c:if test="${empty productDTOList}">
		<p style="color : red">オーダーはありません</p>
	</c:if>
	<c:if test="${not empty productDTOList}">
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
			<tbody class="scroll">
			<c:forEach var="productDTO" items="${productDTOList}">
			<tr>
				<td>${productDTO.formattedOrderDate}</td>
				<td>${productDTO.tableNumber}</td>
				<td>${productDTO.cart.orderId}</td>
				<td>
					<c:choose>
						<c:when test="${productDTO.cart.statusId eq 1}">
							<!-- 注文状態更新用フォーム -->
							<form action="CookServlet" method="post">
								<input type="hidden" name="id" value="${productDTO.cart.id}">
								<input type="hidden" name="statusId" value="1">
								<input type="hidden" name="type" value="setCookingStatusToCompleted">
								<button type="submit">${productDTO.status.name}</button>
							</form>
						</c:when>
						<c:otherwise>
							${productDTO.status.name}
						</c:otherwise>
					</c:choose>
				</td>
				
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
	</c:if>
	</main>
</body>
</html>