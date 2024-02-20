<%@page import="dto.BoardDto"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


<h2>게시판 상세보기</h2>

		<table border="1">
		    <tr>
		        <th>게시물 번호</th>
		        <th>제목</th>
		        <th>내용</th>
		    </tr>
		    <tr>
		          <td>${board.boardCode}</td>
		          <td>${board.title}</td>
		          <td>${board.content}</td>
		    </tr>
		
		</table>

		<div>
			<input type="button" value="수정" 
					onclick="location.href='boardUpdateForm.do?boardCode=${board.boardCode}'">
					 
					<input type="button" value="삭제" 
					onclick="location.href='boardDeleteForm.do?boardCode=${board.boardCode}'">

				
					<input type="button" value="목록"
					onclick="location.href='boardList.do'">
		</div>
				

</body>