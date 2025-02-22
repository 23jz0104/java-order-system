<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="./assets/css/style.css">
	<title>呼び出し中</title>
	</head>
	<body>
		<header id="auth_header">
			<h1>スタッフ呼び出し中</h1>
		</header>
		<main id="call">
			<strong>
				呼び出し中です...<br>
				少々お待ちください
			</strong>
			<form action="CallServlet" method="post">
				<input type="hidden" name="id" value="${call.id}">
				<button type="submit" name="type" value="cancel" class="btn">呼び出しをキャンセル</button>
			</form>
		</main>
	</body>
</html> 