<%@page import="dto.BoardDto"%>
<%@page import="java.util.List"%>
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
	
</body>
</html>