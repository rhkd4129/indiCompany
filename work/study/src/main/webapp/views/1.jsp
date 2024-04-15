<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
   <table border="1">
      <tr>
         <th>No.</th> <th>작성일</th> <th>제목</th> <th>내용</th>
      </tr>
      
      <c:forEach var="post" items="${postList}">
          <tr>
              <td>${post.postNo}</td>
              <td>${post.createDate}</td>
              <td>${post.postName}</td>
              <td>${post.postContent}</td>
          </tr>
      </c:forEach>
      
   </table>
   
</body>
</html>a