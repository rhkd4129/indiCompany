<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/views/layout/header.jsp"%>
<head>
<script type="text/javascript">
	$(function() {
		initData();
		initEvent();
	});

	function initData() {
		var jsonData = "${board}" || null;
		boardCode = "${board.boardCode}";
		if (jsonData === null) {
			alert("정보가 없습니다,");
			window.location.href = "/error/error.do";
			return;
		}
	}
	function initEvent() {
		$('#update-btn').click(function() {
			boardUpdate();
		});
	}
	
	//함수 
	function boardUpdate() {
		var formData = new FormData();
		var files = $('#file')[0].files;
		var boardCode = $('#boardCode').val();
		var boardTitle = $('#boardTitle').val();
		var boardContent = $('#boardContent').val();
		

		var fileDeleteList = [];
		    $('input[name="fileDeleteList"]:checked').each(function() {
		        fileDeleteList.push($(this).val());
		    });

		for (let i = 0; i < files.length; i++) {
			   formData.append("files[]", files[i]);
		}
		    
		formData.append("boardCode" , boardCode);
		formData.append("boardTitle" , boardTitle);
		formData.append("boardContent" , boardContent);
 		formData.append("fileDeleteList",fileDeleteList);
 		
		$.ajax({
			type : "POST",
			url : "/board/update.do",
			dataType:'json',
		    processData: false, // jQuery가 데이터를 처리하지 않도록 설정
		    contentType: false, // 콘텐츠 타입 헤더를 설정하지 않도록 설정
			data : formData,
			
			success : function(response) {
				console.log(response);
						
				alert('게시물이 성공적으로 수정되었습니다.');
				window.location.href = "/board/list.do";					
				
			},
			error : function(xhr, status, error) {
				alert('오류가 발생했습니다. 다시 시도해주세요.');
			}
		});
	}
</script>
</head>

<body>
	<div class="board">
		<h2>게시물 수정</h2>
		<label for="boardCode">글번호</label>: ${board.boardCode} <input
			type="hidden" id="boardCode" name="boardCode"  value="${board.boardCode}" readonly>
		<div>
			<label for="title">제목</label> <input type="text" name="boardTitle" id="boardTitle" value="${board.boardTitle}">
		</div>

		<div>
			<label for="content">내용</label> <input type="text" name="boardContent" id="boardContent" value="${board.boardContent}">
		</div>
		
		파일 선택: <input type="file" name="file" id="file" multiple="multiple" /></br>
		<c:choose>
			<c:when test="${not empty fileRealName}">
				<c:forEach items="${fileRealName}" var="realFileName"
					varStatus="status">
					<c:url var="downloadUrl" value="/board/download.do">
						<c:param name="boardCode" value="${board.boardCode}" />
						<c:param name="fileName" value="${realFileName}" />
					</c:url>
					<a href="${downloadUrl}">${fileNames[status.index]}</a>
					<input type="checkbox" name="fileDeleteList" value="${realFileName}">삭제
					<br />
				</c:forEach>
			</c:when>

			<c:otherwise>
				<p>첨부파일이 없습니다.</p>
				<br />
			</c:otherwise>
		</c:choose>
		<button id="update-btn">수정</button>
	</div>

</body>
</html>		