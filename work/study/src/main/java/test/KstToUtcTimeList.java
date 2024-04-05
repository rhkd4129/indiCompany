package test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class KstToUtcTimeList {

    public static void main(String[] args) {
        // KST 기준 날짜 설정
        String kstDate = "2024-05-04";
        
        // 날짜와 시간을 UTC로 변환하고 리스트로 반환하는 함수 호출
        List<String> utcTimeList = convertKstToUtcTimeList(kstDate);
        
        // 결과 출력
        for (String utcTime : utcTimeList) {
            System.out.println(utcTime);
        }
    }


}
