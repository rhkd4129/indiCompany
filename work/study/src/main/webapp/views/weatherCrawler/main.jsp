<%@ include file="/views/layout/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/views/static/css/map.css">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
<title>이미지 크롤링 결과</title>
<script src="/views/static/js/moment.min.js"></script>
<script src="/views/static/js/moment-timezone-with-data.min.js"></script>
<script src="/views/static/js/fullcalendar-6.1.11/dist/index.global.js"></script>
<script>
	const metaData = {
			"imageURL": "%ROOT_PATH/IMG/%Y%m%d/gk2a_ami_le1b_rgb-s-true_ea020lc_%Y%m%d%H%M.srv.png",
			};

	$(function() {
		initData();
		initEvent();
	});
	
	function replaceMetaData(urlTemplate, utcDate, utcTime) {
	    return urlTemplate
	        .replace(/%ROOT_PATH/g, 'http://localhost:8080')
	        .replace(/%Y%m%d/g, utcDate)
	        .replace(/%H%M/g, utcTime);
	}	
	// KST는  한국의 표준시간대를  UTC+9 시간대에 해당
	 function initData() {
		    // 최대 선택 가능 날짜를 오늘 날짜로 설정
		    var today = moment().format('YYYY-MM-DD');
		    $('#date').val(today);
		    $('#date').attr('max', today);
		    var selectDate = moment($('#date').val()).format('YYYYMMDD');
	     getSelectionOption(selectDate);	 
	}

	function initEvent() {
	    $('.searchBox').on('change', 'input, select', function() {
	        console.log("modify");
	        var selectDate = moment($('#date').val()).format('YYYYMMDD');
	        console.log('선택된 날짜: ', selectDate);
	        getSelectionOption(selectDate); // 선택된 날짜와 이전에 선택된 시간을 함수로 전달
	    });
	    $('.movie').on('click', showMovie);
	}

	/**
	 * 선택된 날짜에 대한 시간 옵션을 불러와 선택 상자를 업데이트하는 함수
	 * 서버에서 선택된 날짜에 해당하는 시간 목록을 가져오고, 이를 바탕으로 시간 select option구성
	 * 이전에 선택된 시간이 존재하면 해당 시간을 다시 선택.
	 *
	 * @param {string} selectDate - 사용자가 선택한 날짜 (YYYYMMDD 형식)
	 */
	function getSelectionOption(selectDate) {
	    var previouslySelectedTime = $('#searchTime').val(); // 현재 선택된 시간을 가져옴

	    $.ajax({
	        url: '/weatherCrawler/dateList.do',
	        type: 'GET',
	        data: { selectDate: selectDate },
	        dataType: 'json',
	        success: function(data) {
	            console.log(data);
	         	// 서버로부터 유효한 데이터를 받지 못했을 경우
	            if (!data || !data.kstTimeList || data.kstTimeList.length === 0) { 
	            	
	                console.error("date List 받아오기 실패");
	                $('#showImage').attr({
	                    'src': '',
	                    'alt': '이미지 정보를 받아오지 못했습니다.'
	                });
	                $('#searchTime').empty().append($('<option>', {
	                    value: "",
	                    text: "시간 정보를 받아오지 못했습니다."
	                }));
	                return;
	            }

	            $('#searchTime').empty(); // 이전에 불러온 시간 목록을 비움
	            data.kstTimeList.forEach(function(time) { // 새로운 시간 데이터로 목록 채우기
	                $('#searchTime').append($('<option>', {
	                    value: time,
	                    text: time.substr(8, 2) + ':' + time.substr(10, 2) + " KST" // 시간 형식 설정
	                }));
	            });
	            // 기존에 선택된 값이 존재하고 옵션에 존재하는 경우, 해당 값을 다시 선택
	            if (previouslySelectedTime && $('#searchTime option[value="' + previouslySelectedTime + '"]').length > 0) {
	                $('#searchTime').val(previouslySelectedTime);
	            } else {
	                $('#searchTime').val($('#searchTime option:first').val());// 기존 선택된 값이 없으면 첫 번째 옵션을 선택 
	            }
	            updateImageDisplay(); // 이미지 표시 업데이트
	        },
	        error: function(xhr, status, error) {
	            console.error("Error loading times:", error);
	            
	            
	            $('#showImage').attr({
                    'src': '',
                    'alt': '이미지 로딩 중 오류'
                });
	            $('#searchTime').append($('<option>', {
	                value: "",
	                text: "시간 정보 로딩 오류"
	            }));
	        }
	    });
	}

	//날짜 정보로 utc로 변환 후 url 생성 
	function updateImageDisplay() {
		//image가 완전히 로드 되면 다음 이미지 생성해야함
		var { date, time } = convertToUTC($('#searchTime').val());
		var imgUrl = replaceMetaData(metaData.imageURL, date, time);
		$('#showImage').attr('src', imgUrl);
	}
	
	function showMovie() {
	    var dateList = getAllOptionValues();  // 옵션 값들을 가져옴
	    let index = 0;  // 현재 인덱스 초기화
	    var delay = 300;

	    function loadImage() {
	        if (index >= dateList.length) {
	        	updateImageDisplay();  // 모든 이미지 로드 후, 선택된 시간의 이미지로 다시 업데이트
	            return;  // 모든 이미지 처리 완료
	        }
	        var selectDate = dateList[index++];
	        var { date, time } = convertToUTC(selectDate);  // 선택된 날짜를 UTC로 변환
	        var imgUrl = replaceMetaData(metaData.imageURL, date, time);
	        $('#showImage').attr('src', imgUrl).off('load').on('load', function() {
	            setTimeout(loadImage, delay);  // 다음 이미지 로드를 위한 딜레이
	        });
	    }
	    loadImage();  // 최초 이미지 로딩 시작
	    
	}

	/**
	 * 현재 select box 안에 있는 모든 option 값을 get
	 * @return {Array<string>} 선택된 모든 옵션 값의 배열
	 */
	function getAllOptionValues(){
	    var optionValueList = [];
	    $("#searchTime option").each(function() {
	    	optionValueList.push($(this).val()); // 각 option의 값을 배열에 추가
	    });
	    return optionValueList;
	}

	 /**
	  * 주어진 날짜 문자열을 KST ->  UTC로 변환
	  * @param {string} dateStr - (KST)날짜 문자열 (YYYYMMDDHHmm 형식)
	  * @returns {{date: string, time: string}} 변환된 UTC 날짜와 시간
	  */
 	function convertToUTC(dateStr) {
        var kstMoment = moment.tz(dateStr, "YYYYMMDDHHmm", "Asia/Seoul");
        var utcMoment = kstMoment.clone().tz("UTC");
        return { date: utcMoment.format("YYYYMMDD"), time: utcMoment.format("HHmm") };
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

			<input type="date" id="date">
		</div>
		<select id="searchTime" class="searchTime">


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
		<img id="showImage" alt="" src="">
	</div>
</body>
</html>

