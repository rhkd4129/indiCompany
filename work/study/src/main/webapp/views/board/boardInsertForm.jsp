<%@ include file="/views/layout/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<body>
	<div class="board">
			<h2>등록하기</h2>
		<form action="/redirect/board/insert.do" method="post">
	
			
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
		
		<input type="button" value="목록"
						onclick="location.href='/view/board/list.do'">
	
	
	</div>

</body>
</html>