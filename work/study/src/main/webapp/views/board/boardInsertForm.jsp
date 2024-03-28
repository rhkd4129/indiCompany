<%@ include file="/views/layout/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<body>
	<div class="board">
			<h2>등록하기</h2>
		<form action="/board/insert.do" method="post" enctype="multipart/form-data" >
			<div>
				<label for="boardTitle" >제목</label>
		        <input type="text" name="boardTitle">
			</div>
			<div>
				<label for="boardContent" >내용</label>
		        <input type="text" name="boardContent">
			</div>
			파일 선택: <input type="file" name="file" multiple="multiple" />
			
			<input type="submit" id ="saveButton" value="저장">
		</form>				
		<input type="button" value="목록"  onclick="location.href='/board/list.do'">    
	</div>

</body>
</html>	