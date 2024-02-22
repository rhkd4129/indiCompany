<%@page import="dto.BoardDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	 <link rel="stylesheet" type="text/css" href="views/css/layout.css">
	 <script src="views/js/jquery.js"></script>
	 
	 <script>
	 $(document).ready(function(){
				 $("#aa").click(function(e){
						drawTable(); 
				 });
		 	
	 	 		$("#submit").click(function(e){
	 	 	        //e.preventDefault();	 	 	      
	 	 	        var title = $("input[name='title']").val();
	 	 	        var content = $("input[name='content']").val();
		 	 	      $.ajax({
		 	             type: "POST",
		 	             url: "/boardInsertPro_redirect.do", // 데이터를 처리할 서버 URL 지정
		 	             data: { // 전송할 데이터 객체 형태 지정
		 	                 title: title,
		 	                 content: content
		 	             },
		 	             success: function(response) { // 성공적으로 서버 응답을 받았을 때 동작 
		 	                 console.log("글이 성공적으로 작성되었습니다.");
		 	             	 
		 	                 
		 	             },
		 	             error: function(xhr, status, error) { // 서버 요청 실패했을 때의 동작
		 	                 console.error("글 작성 중 오류가 발생했습니다.");  
		 	             }
		 	         });
	 	 	    });
	 	 		function drawTable(){
	 	 			$.ajax({
		 	             type: "GET",
		 	             url: "/boardList.do", // 데이터를 처리할 서버 URL 지정
		 	  
		 	             success: function(response) { // 성공적으로 서버 응답을 받았을 때 동작 
		 	                 console.log(response);
		 	             	 
		 	                 
		 	             },
		 	             error: function(xhr, status, error) { // 서버 요청 실패했을 때의 동작
		 	                 console.error("글 작성 중 오류가 발생했습니다.");  
		 	             }
		 	         });
	 	 		}
	 	 	
	 	 	  function drawBoard(boardList) {
	 	         var $table = $('.board .tab');
	
	 	         // 테이블 초기화
	 	         $table.empty();
	 	         
	 	         // 테이블에 데이터 추가
	 	         $.each(boardList, function(index, board) {
	 	             var $row = $('<tr>');
	 	             var $numCell = $('<td>').text(board.num);
	 	             var $titleCell = $('<td>').html('<a href="boardContent.do?boardCode=' + board.boardCode + '">' + board.title + '</a>');
	 	             var $contentCell = $('<td>').text(board.content);
	 	             
	 	             $row.append($numCell, $titleCell, $contentCell);
	 	             $table.append($row);
	 	         });
	 	     }

	 	 	});
	 	 
	 </script>
</head>
<body>

<div class ="board">
	<h2>게시판</h2>
	<a href="boardInsertForm.do">새글 작성하기</a>
	
	<table border="1">
	    <tr>
	        <th>게시물 번호</th>
	        <th>제목</th>
	        <th>내용</th>
	    </tr>
	    <c:forEach var="board" items="${boardList}">
	        <tr>
	            <td>${board.num}</td>
	            <td><a href = 'boardContent.do?boardCode=${board.boardCode}'>${board.title}</a></td>
	            <td>${board.content}</td>
	        </tr>
	    </c:forEach>
	</table>
</div>
	<div class="board">
			<h2>등록하기</h2>
			
			<div>
				<label for="title" >제목</label>
		        <input type="text" name="title">
			</div>	
			<div>
				<label for="content" >내용</label>
		        <input type="text" name="content">
			</div>
			<input id="submit" type="button" value="저장">
	</div>


	
</body>
</html>