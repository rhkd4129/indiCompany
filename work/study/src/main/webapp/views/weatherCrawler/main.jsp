<%@ include file="/views/layout/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/views/static/css/map.css">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
<title>이미지 크롤링 결과</title>
<script>
	$(function() {
		initData();
		initEvent();
	});
	// KST는  한국의 표준시간대를  UTC+9 시간대에 해당
	function initData() {
		$('#date').val(new Date().toISOString().substring(0, 10));
		drawSelectBox(function() {
			// 콜백 함수 내에서 시간 선택 값 읽기
			var selectedDate = $('#date').val(); // 날짜 가져오기
			var selectedTime = $('#searchTime').val().split(' ')[1].replace(
					':', ''); // 시간 가져오
			console.log(selectedDate, selectedTime);
			checkTodayData(selectedDate, selectedTime);
		});
	}

	function initEvent() {
		$('.searchBox').on(
				'change',
				'input, select',
				function() {
					console.log("modify");
					var selectedDate = $('#date').val(); // 날짜 가져오기
					var selectedTime = $('#searchTime').val().split(' ')[1]
							.replace(':', '');
					checkTodayData(selectedDate, selectedTime);
				});

		$('.movie').on('click', function() {
			var selectData = getSelectedData()
			showMovie(selectData.date, selectData.time)
		});

	}

	function drawSelectBox(callback) {
		var selectedDate = $('#date').val(); // 날짜 가져오기
		$.ajax({
			url : '/weatherCrawler/drawSelectBox.do',
			type : 'GET',
			data : {
				selectedDate : selectedDate,
			},
			dataType : 'json',
			success : function(data) {
				$('#searchTime').empty();
				data.kstTimeList.reverse();
				console.log(data);
				$.each(data.kstTimeList, function(index, value) {
					var time = value.split(" ")[1];
					$('#searchTime').append($('<option>', {
						value : value,
						text : time + " KST"
					}));
				});
				$('#searchTime option:first').prop('selected', true);
				// 콜백 함수가 제공되면 실행
				if (callback && typeof (callback) === "function") {
					callback();
				}
			},
			error : function(xhr, status, error) {
				console.error("Error occurred: " + error);
			}
		});
	}

	function checkTodayData(selectedDate, selectedTime) {
		$.ajax({
			url : '/weatherCrawler/imageShow.do',
			type : 'GET',
			data : {
				selectedDate : selectedDate,
				selectedTime : selectedTime
			},
			dataType : 'json',
			success : function(data) {
				console.log(data);
				var imgSrc = 'data:image/png;base64,' + data.image;
				changeImageSrc(imgSrc);
			},
			error : function(xhr, status, error) {
				console.error("Error occurred: " + error);
			}
		});
	}

	function changeImageSrc(newSrc) {
		$('#imageContainer img').attr('src', newSrc);
	}

	function showMovie(date, time) {
		$.ajax({
			url : '/weatherCrawler/movieShow.do',
			type : 'GET',
			data : {
				selectedDate : date,
				selectedTime : time
			},
			dataType : 'json',
			success : function(images) {
				let index = 0;
				var intervalId = setInterval(function() {
					// 이미지 변경 로직 실행
					var imageUrl = "http://localhost:8080/IMG/"
							+ date.replace(/-/g, "") + "/"
							+ images.fileNameList[index];
					console.log(imageUrl);
					changeImageSrc(imageUrl);
					index++;
					// fileNameList의 길이를 넘어서면 인터벌을 정지하고 마지막 이미지 유지
					if (index >= images.fileNameList.length) {
						clearInterval(intervalId);
						// 마지막 이미지로 설정하기 위해 인덱스를 마지막 요소로 설정
						// 이미 위에서 마지막 이미지를 설정했으므로 여기서 추가로 설정할 필요는 없음
					}
				}, 500); // 0.5초마다 이미지 변경
			},
			error : function(xhr, status, error) {
				console.error("Error occurred: " + error);
			}
		});
	}
</script>
</head>
<body>
	<div class="btn_group">
		<input type="button" value="목록"
			onclick="location.href='/board/list.do'">
	</div>
	<div class="searchBox">
		<div class="dateTime">
			<input id="date" type="date"> <select id="searchTime"
				class="searchTime">


			</select>

			<button type="button" class="nowBtn">NOW</button>
		</div>
		<div class="condition">
			<div class="menu" data-idx="0">
				<label class="label" for="satellite">위성</label> <select
					id="satellite" name="sat">
					<option value="GK2A">천리안위성 2A호</option>
					<option value="GK2A">천리안위성 2A호</option>
					<option value="COMS">천리안위성 1호(종료)</option>
					<option value="NOAA">NOAA</option>
					<option value="SNPP">SNPP</option>
					<option value="METOP">METOP</option>
				</select>
			</div>

			<div class="menu" data-idx="1">
				<label class="label" for="dataLevel">자료레벨</label> <select
					id="dataLevel" name="level">
					<option value="LE1B">기본영상</option>
					<option value="LE2-ETC">위험기상</option>
					<option value="LE2-ETC2">구름</option>
					<option value="LE2-ETC3">지면∙해양</option>
					<option value="LE2-ETC4">대기</option>
					<option value="LE2-ETC5">항공</option>
					<option value="LE2-ETC6">복사</option>
					<!-- options omitted for brevity -->
				</select>
			</div>

			<div class="menu" data-idx="2">
				<label class="label" for="dataKind">자료종류</label> <select
					id="dataKind" name="type">
					<optgroup refcode="type1" label="::::: 단일채널 :::::">
						<option value="VI004">가시(0.47μm):파랑</option>
						<option value="VI005">가시(0.51μm):초록</option>
						<option value="VI006">가시(0.64μm):빨강</option>
						<option value="VI008">가시(0.86μm):식생</option>
						<option value="NR013">근적외(1.37μm):권운</option>
						<option value="NR016">근적외(1.6μm):눈/얼음</option>
						<option value="SW038">단파적외(3.8μm):야간안개/하층운</option>
						<option value="WV063">수증기(6.3μm):상층 수증기</option>
						<option value="WV069">수증기(6.9μm):중층 수증기</option>
						<option value="WV073">수증기(7.3μm):하층 수증기</option>
						<option value="IR087">적외(8.7μm):구름상</option>
						<option value="IR096">적외(9.6μm):오존</option>
						<option value="IR105">적외(10.5μm):깨끗한 대기창</option>
						<option value="IR112">적외(11.2μm):대기창</option>
						<option value="IR123">적외(12.3μm):오염된 대기창</option>
						<option value="IR133">적외(13.3μm):이산화탄소</option>
					</optgroup>
					<optgroup refcode="type2" label="::::: RGB :::::">
						<option value="RGB-TRUE">RGB 천연색</option>
						<option value="RGB-S-TRUE">RGB 천연색(AI)</option>
						<option value="RGB-WV-1">RGB 3채널 수증기</option>
						<option value="RGB-NATURAL">RGB 자연색</option>
						<option value="RGB-AIRMASS">RGB 기단</option>
						<option value="RGB-DUST">RGB 황사</option>
						<option value="RGB-DAYNIGHT">RGB 주야간 합성</option>
						<option value="RGB-S-DAYNIGHT">RGB 주야간 합성(AI)</option>
						<option value="RGB-FOG">RGB 주야간 안개</option>
						<option value="RGB-STORM">RGB 주간 대류운</option>
						<option value="RGB-SNOWFOG">RGB 주간적설안개</option>
						<option value="RGB-CLOUD">RGB 운상</option>
						<option value="RGB-ASH">RGB 화산재</option>
					</optgroup>
					<optgroup refcode="type3" label="::::: 컬러강조 :::::">
						<option value="EIR-WV063">컬러수증기(6.3μm) 강조</option>
						<option value="EIR-WV069">컬러수증기(6.9μm) 강조</option>
						<option value="EIR-WV073">컬러수증기(7.3μm) 강조</option>
						<option value="EIR-IR105-COLOR">컬러적외(10.5μm) 강조</option>
					</optgroup>
				</select>
			</div>

			<div class="menu" data-idx="3">
				<label class="label" for="area">영역</label> <select id="area"
					name="area">
					<option value="EA">동아시아</option>
					<option value="KO">한반도</option>
				</select>
			</div>
		</div>

		<div class="conf">
			<div class="help">
				<button class="helpBtn" title="새 창 알림  - 도움말을 확인하세요">
					<span class="txt">도움말</span>
				</button>
				<button class="movie">
					<span class="txt">영상보기</span>
				</button>
			</div>
		</div>
	</div>


	<div id="imageContainer" class="imageContainer">
		<img id="showImage" src="">
	</div>
</body>
</html>
