<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/views/layout/header.jsp"%>


<head>

<script type="text/javascript">
	$(function() {
		initData();
		initEvent();
		// 초기화 데이터
	});

	function initData() {
		var jsonData = "${board}" || null;
		boardCode = "${board.boardCode}";
		if (jsonData === null) {
			alert("정보가 없습니다,");
			window.location.href = "/view/error/error.do";
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
		var boardCode = $('#boardCode').val();
		var boardTitle = $('#boardTitle').val();
		var boardContent = $('#boardContent').val();

		$.ajax({
			type : "POST",
			url : "/redirect/board/update.do",
			data : {
				boardCode : boardCode,
				boardTitle : boardTitle,
				boardContent : boardContent
			},
			success : function(response) {
				alert('게시물이 성공적으로 수정되었습니다.');
				window.location.href = "/view/board/list.do";
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
			type="hidden" id="boardCode" name="boardCode"
			value="${board.boardCode}" readonly>
		<div>
			<label for="title">제목</label> <input type="text" name="boardTitle"
				id="boardTitle" value="${board.boardTitle}">
		</div>

		<div>
			<label for="content">내용</label> <input type="text"
				name="boardContent" id="boardContent" value="${board.boardContent}">
		</div>
		<button id="update-btn">수정</button>
	</div>

</body>
</html>