<%@ include file="/views/layout/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
<!-- 	<script>
		$(document).ready(function() {
		    $('#saveButton').on('click', function(e) {
		        e.preventDefault(); // 폼의 기본 제출 동작 방지
		
		        var formData = new FormData();
		        var fileInput = $('input[type=file]')[0].files; // 파일 입력에서 파일 가져오기
		
		        // 선택된 모든 파일을 formData 객체에 추가
		        for (var i = 0; i < fileInput.length; i++) {
		            formData.append('file[]', fileInput[i]);
		        }
		
		        // 파일이 하나라도 선택되었는지 확인
		        if (fileInput.length > 0) {
		            // 파일 데이터를 비동기적으로 전송
		            $.ajax({
		                url: '/json/board/uploadFile.do', // 파일을 처리할 서버의 URL
		                type: 'POST',
		                data: formData,
		                processData: false, // jQuery가 데이터를 처리하지 않도록 설정
		                contentType: false, // jQuery가 컨텐츠 타입을 설정하지 않도록 설정
		                success: function(data) {
		                	alert("성공")
		                	alert(data)
		                	
		                    $('form').unbind('submit').submit();
		                },
		                error: function(xhr, status, error) {
		                    console.error("Error: " + error);
		                    // 에러 처리 로직
		                }
		            });
		        } else {
		            // 파일이 선택되지 않았다면, 바로 폼 제출
		            $('form').unbind('submit').submit();
		        }
    });
}); 

</script>
-->

</head>

<body>
	<div class="board">
			<h2>등록하기</h2>
		<form action="/redirect/board/insert.do" method="post" enctype="multipart/form-data" >
	
			
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
		<input type="button" value="목록"  onclick="location.href='/view/board/list.do'">    
	</div>

</body>
</html>	