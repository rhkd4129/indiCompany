<%@ include file="/views/layout/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
    <title>이미지 크롤링 결과</title>
<script>
$(document).ready(function() {
    $.ajax({
        url: '/jsop/listShow.do', // 서블릿 URL
        type: 'GET', // HTTP 메소드
        dataType: 'json', // 응답 데이터 타입
        success: function(data) {
            console.log(data);
            if(data.json) { // data.json과 data.titles가 모두 존재하는지 확인
                var imgUrls = JSON.parse(data.json); // JSON 문자열을 파싱하여 배열로 변환
				console.log(imgUrls);
                // 이미지 URL과 제목에 대해 반복 처리
                $.each(imgUrls, function(index, imgUrl) {
                        // URL에서 시작과 끝의 "%22" 제거
                        //imgUrl = imgUrl.replace(/%22/g, '');
                        // HTML 엔티티 "&amp;"를 "&"로 변경
                        //imgUrl = imgUrl.replace(/&amp;/g, '&');
                        var $img = $('<img>', {
                            src: imgUrl,
                            style: 'width:300px;height:auto;'
                        });

                        var $div = $('<div>').append($img);
                        $('#imageContainer').append($div);                   
                });      
            }
        },
        error: function(xhr, status, error) {
            console.error("Error occurred: " + error);
        }
    });
});

</script>

</head>
<body>

	hello
  <div id="imageContainer">
  </div>
</body>
</html>	