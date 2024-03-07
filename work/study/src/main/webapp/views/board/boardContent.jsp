<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/views/layout/header.jsp"%>

<head>
<script type="text/javascript">
	$(function() {
		var boardCode = "${board.boardCode}";
		getCommentList(boardCode)
		let a = 'ab';
		console.log(a);
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
						url : "/json/commentInsertPro.do",
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
		function getCommentList(boardCode) {
			$.ajax({
				type : "GET",
				url : "/json/commentListPro.do",
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
		}

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
		}
	});
	
	function boardDelete(boardCode) {
		console.log("boardDelete");
		if (!boardCode) {
			alert("삭제 오류");
			return false;
		}
		$.ajax({
			type : "POST",
			url : "/redirect/boardDeletePro.do",
			data : {
				boardCode : boardCode
			},
			success : function(result) {
				console.log(result);
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
				<td><fmt:formatDate value="${board.boardCreateAt}" pattern="yyyy-MM-dd HH:mm" /></td>
			</tr>
		</table>

		<div class="btn_group">
			<input type="button" value="수정"
				onclick="location.href='/view/boardUpdateForm.do?boardCode=${board.boardCode}'">

			<input type="button" value="삭제" onclick="boardDelete(${board.boardCode})">
				
				
			<input type="button" value="목록" onclick="location.href='/view/boardList.do'">
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