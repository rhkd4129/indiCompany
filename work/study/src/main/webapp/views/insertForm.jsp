<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>등록하기</h2>
	<form action="boardInsertPro.do" method="post">

		
		<div>
			<label for="title" >제목</label>
	        <input type="text" name="title">
		</div>
		
		
		<div>
			<label for="content" >내용</label>
	        <input type="text" name="content">
		</div>
		<input type="submit" value="저장">
	</form>
</body>
</html>