<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/views/layout/header.jsp"%>
<head>
</head>
<body>
	<div class="board">
		<form action="/redirect/board/update.do" method="post">

			<h2>게시물 수정</h2>
			<label for="boardCode">글번호</label>: ${board.boardCode} <input
				type="hidden" id="boardCode" name="boardCode"
				value="${board.boardCode}" readonly>

			<div>
				<label for="title">제목</label> <input type="text" name="title"
					value="${board.boardTitle}">

			</div>

			<div>
				<label for="content">내용</label> <input type="text" name="content"
					value="${board.boardContent}">

			</div>


			<button type="submit">수정</button>
		</form>
	</div>

</body>
</html>