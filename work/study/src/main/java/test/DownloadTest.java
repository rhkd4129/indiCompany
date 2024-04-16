package test;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import commonUtils.WeatherCrawlerUtil;

public class DownloadTest {
    private static final Logger logger = LoggerFactory.getLogger(WeatherCrawlerUtil.class);
    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
    private static final DateTimeFormatter DATETIME_FORMATTER2 = DateTimeFormatter.ofPattern("yyyyMMddHH");

    public static final String filePath = "C:\\cr\\";

    /**
     * 가장 최근의 이미지 폴더를 찾아 이미지 다운로드 과정
     * 최신 폴더를 찾고, 시간 맵을 생성하며, 그에 따라 다운로드를 처리.
     */
   
}
