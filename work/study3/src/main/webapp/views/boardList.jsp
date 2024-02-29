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
</head>
<body>

<div class ="board">
	<h2>게시판</h2>
	<a href="/boardInsertForm.do">새글 작성하기</a><br>
	<a href="/nonexistent-page.jsp">error page</a>
	
	<c:choose>
	    <c:when test="${empty boardList}">
	        <p>게시물이 없습니다.</p>
	    </c:when>
	    <c:otherwise>
	        <table border="1">
	            <tr>
	                <th>게시물 번호</th>
	                <th>제목</th>
	                <th>내용</th>
	            </tr>
	            <c:forEach var="board" items="${boardList}">
	                <tr>
	                    <td>${board.num}</td>
	                    <td><a href="boardContent.do?boardCode=${board.boardCode}">${board.boardTitle}</a></td>
	                    <td>${board.boardContent}</td>
	                </tr>
	            </c:forEach>
	        </table>
	    </c:otherwise>
	</c:choose>

</div>
	
</body>
</html>