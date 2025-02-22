<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./assets/css/style.css">
<title>G03 Mr.meast</title>
</head>
<body>
	<header id="auth_header">
    	<h1>いらっしゃいませ</h1>
  	</header>
  	<main>
  		<div id="init">
		    <img src="./assets/img/auth/G03.jpg" alt="">
	    	<form action="InitServlet" method="post">
				<input type="hidden" name="type" value="topPage">
				<button type="submit">注文を始める</button>
	    	</form>
  		</div>
	 </main>
	 <footer id="init_footer">
	 	<p>&copy;2024 肉林</p>
	 </footer>
</body>
</html>