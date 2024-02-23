<%@page import="dto.BoardDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>게시판 상세 페이지</title>
	<link rel="stylesheet" type="text/css" href="views/css/layout.css">
	<script type="text/javascript">
    $(function() {
        var boardCode = "${board.boardCode}";
        getCommentList(boardCode)
     
        var comment = $('<input>').attr({
            'type': 'text',
            'id': 'commentContent',
            'name': 'commentContent',
            'placeholder': '댓글'
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
                        type: "POST",
                        url: "/json/commentInsertPro.do",
                        data: {
                            boardCode: boardCode,
                            commentContent: commentContent
                        },
                        success: function(response) {
                        	$('#commentContent').val('');
                            console.log("글이 성공적으로 작성되었습니다.");
                            getCommentList(boardCode);
                          
                        },
                        error: function(xhr, status, error) {
                            console.error("글 작성 중 오류가 발생했습니다.");
                        }
                    });
                }
            }
        });
        function getCommentList(boardCode){
      	  $.ajax({
                type: "GET",
                url: "/json/commentListPro.do",
                data: {
                    boardCode: boardCode
                },
                success: function(commentList) {
                    console.log("댓글목록 불러오기 성공");
                    drawBoard(commentList);
                },
                error: function(xhr, status, error) {
                    console.error("댓글 목록 불러오기 실패");
                }
            });
      	}
      
        
      
        function drawBoard(commentList) {
        	
            var commentListDiv = $('.commentList') || null;
            if (commentList === null || commentList.length < 1) {
                commentListDiv.append('<p>등록된 댓글이 없습니다.</p>');
                return false;
            }
          
            commentListDiv.empty(); // 기존에 있던 댓글을 비우고 새로운 목록으로 갱신
            var table = $('<table border="1">');
            var headerRow = $('<tr>').append($('<th>').text('내용')).append($('<th>').text('추천수'));
            table.append(headerRow);

            $.each(commentList, function(index, comment) {
                var row = $('<tr>').append($('<td>').text(comment.commentContent)).append($('<td>').text(comment.recommend));
                table.append(row);
            });

            commentListDiv.append(table);
            

//             if (commentList.length === 0) {
//                 commentListDiv.append('<p>등록된 댓글이 없습니다.</p>');
//             } else {
//                 var table = $('<table border="1">');
//                 var headerRow = $('<tr>').append($('<th>').text('내용')).append($('<th>').text('추천수'));
//                 table.append(headerRow);

//                 $.each(commentList, function(index, comment) {
//                     var row = $('<tr>').append($('<td>').text(comment.commentContent)).append($('<td>').text(comment.recommend));
//                     table.append(row);
//                 });

//                 commentListDiv.append(table);
//             }
        }
    });
</script>
		
</head>
<body>

	<div class="board">
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
	
			<div class="btn_group">
				<input type="button" value="수정" 
						onclick="location.href='boardUpdateForm.do?boardCode=${board.boardCode}'">
						 
						<input type="button" value="삭제" 
						onclick="location.href='boardDeletePro_redirect.do?boardCode=${board.boardCode}'">
		
					
						<input type="button" value="목록"
						onclick="location.href='boardList.do'">
			</div>
	</div>
	<div class="board">
		<div class="comment">
			<div class="commentInsert"></div>
			<div class="commentList"></div>
			
		</div>	
	</div>
		
	
				

</body>