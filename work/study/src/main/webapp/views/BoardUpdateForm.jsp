<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	 <form action = "boardUpdatePro.do" method="post">
	        

				     <label for="boardCode" >글번호</label>:  ${board.boardCode}
				     
                     <input type="hidden"  id="boardCode" name="boardCode" value="${board.boardCode}" readonly>
				
					<div>
                        <label for="title">제목</label>
                        <input type="text" name="title" value="${board.title}">

                    </div>
							
					<div>
                        <label for="content">내용</label>
                        <input type="text" name="content" value="${board.content}">

                    </div>
            
  
                    <button type="submit">수정하기</button>
                </form>
</body>
</html>