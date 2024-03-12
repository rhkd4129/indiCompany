<%@ include file="/views/layout/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
 	<script>
		function boardContent(boardCode){
			console.log(boardCode)
			$.ajax({
				type : "GET",
				url : "/view/board/content.do?boardCode"+boardCode,
				data : {
					boardCode : boardCode
				},
				success : function(response) {
				    window.location.href = "/view/board/content.do?boardCode="+boardCode;
				},
				error : function(xhr, status, error) {
					console.error("게시판 상세보기 불러오기 실패");
				}
			});
		}
	</script> 
</head>
<body>
<div class ="board">
	<h2>게시판</h2>
	<a href="/view/board/insertForm.do">새글 등록</a><br>
	<a href="/view/error/error.do">에러페이지</a><br>
	
	<c:choose>
	    <c:when test="${empty boardList}">
	        <p>현재 글이 없습니다.</p>
	    </c:when>
	    <c:otherwise>
	        <table border="1">
	            <tr>
	                <th>글번호</th>
	                <th>제목</th>
	                <th>내용</th>
	            </tr>
	            <c:forEach var="board" items="${boardList}">
	                <tr>
	                    <td>${board.boardCode}</td>
<!--                      <td><a href="/view/board/content.do?boardCode=${board.boardCode}">${board.boardTitle}</a></td>-->	
						  <td onclick="boardContent(${board.boardCode})">${board.boardTitle}</td>
	                    <td>${board.boardContent}</td>
	                </tr>	
	            </c:forEach>
	        </table>
	    </c:otherwise>
	</c:choose>

</div>
	
</body>
</html>