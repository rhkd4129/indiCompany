<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/views/layout/header.jsp"%>

<head>
<script type="text/javascript">
// init
// 초기화 컴포넌트 (라이브러리) 초기화
// 초기화 이벤트 
	// $("#").on("click") {(
	//		)}
// 초기화 데이터
// 함수

// service


$(function() {
	initData();
	initEvent();
	// 초기화 데이터
});


function initData() {
	var jsonData = "${board}" || null;
	boardCode = "${board.boardCode}";
	
	if(jsonData === null){	
		alert("정보가 없습니다,");
		window.location.href = "/view/error/error.do";
		return;
	}

	getCommentList(boardCode);
	
	var comment = $('<input>').attr({
		'type' : 'text',
		'id' : 'commentContent',
		'name' : 'commentContent',
		'placeholder' : '댓글'
	});
	$('.commentInsert').append(comment);
	
	
	comment.keydown(function(e) {
		if (e.keyCode == 13) {
			var commentContent = $(this).val();
			if ($.trim(commentContent) === "") {
				alert("필수 항목입니다.");
			} else {
				console.log(commentContent);
				$.ajax({
					type : "POST",
					url : "/json/comment/insert.do",
					data : {
						boardCode : boardCode,
						commentContent : commentContent
					},
					success : function(response) {
						$('#commentContent').val('');
						console.log(response);
						console.log("글이 성공적으로 작성되었습니다.");
						getCommentList(boardCode);
					},
					error : function(xhr, status, error) {
						console.error("글 작성 중 오류가 발생했습니다.");
					}
				});
			}
		}
	});
};

function initEvent() {
	 // 수정 버튼 클릭 이벤트 처리
   $('#update-btn').click(function() {
       window.location.href = '/view/board/updateForm.do?boardCode=' + boardCode;
   });

   // 삭제 버튼 클릭 이벤트 처리
   $('#delete-btn').click(function() {
       boardDelete(boardCode);
   });

   // 목록 버튼 클릭 이벤트 처리
   $('#list-btn').click(function() {
       window.location.href = '/view/board/list.do';
   });
};

//함수

function drawBoard(commentList) {
	var commentListDiv = $('.commentList');
	if (!commentList || commentList.length < 1) {
		commentListDiv.append('<p>등록된 댓글이 없습니다.</p>');
		return false;
	}

	commentListDiv.empty(); // 기존에 있던 댓글을 비우고 새로운 목록으로 갱신
	var table = $('<table border="1">');
	var headerRow = $('<tr>').append($('<th>').text('내용'));
	table.append(headerRow);

	$.each(commentList, function(index, comment) {
		var row = $('<tr>').append(
				$('<td>').text(comment.commentContent));
		table.append(row);
	});
	commentListDiv.append(table);
};



function getCommentList(boardCode) {
	$.ajax({
		type : "GET",
		url : "/json/comment/list.do",
		dataType : 'json',
		data : {
			boardCode : boardCode
		},
		success : function(response) {
			console.log(response);
			console.log("댓글목록 불러오기 성공");
			drawBoard(response.commentList);
		},
		error : function(xhr, status, error) {
			console.error("댓글 목록 불러오기 실패");
		}
	});
};



function boardDelete(boardCode) {
	console.log("boardDelete");
	if (!boardCode) {
		alert("삭제 오류");
		return false;
	}
	$.ajax({
		type : "POST",
		url : "/json/board/delete.do",
		data : {
			boardCode : boardCode
		},
		success : function(response) {
			let result = response.result;
			if(result>=1){
				 setTimeout(function() { 
					 window.location.href = "/view/board/list.do";
					 alert("삭제 완료");
					 },  1000);
			}				
			else{
				alert("알 수 없는 오류");
			}
		},
		error : function(xhr, status, error) {
			console.error("삭제 오류");
		}
	});
}
</script>
</head>
<body>
	<div class="board">
		<h2>게시판 상세보기</h2>
		<table border="1">
			<tr>
				<th>글 번호</th>
				<th>제목</th>
				<th>내용</th>
				<th>셍성시간</th>
			</tr>
			<tr>
				<td>${board.boardCode}</td>
				<td>${board.boardTitle}</td>
				<td>${board.boardContent}</td>
				<td><fmt:formatDate value="${board.boardCreateAt}"
						pattern="yyyy-MM-dd HH:mm" /></td>
			</tr>
		</table>

		<div class="btn_group">
			<!--  <input type="button" value="수정json" onclick="boardUpdate(${board.boardCode})"> -->

			<input type="button" value="수정"  id="update-btn" >
			<input type="button" value="삭제"  id="delete-btn" > 
			<input type="button" value="목록"  id="list-btn">
		</div>
	</div>
	<div class="board">
		<div class="comment">
			<div class="commentInsert"></div>
			<div class="commentList"></div>

		</div>
	</div>

</body>
</html>
