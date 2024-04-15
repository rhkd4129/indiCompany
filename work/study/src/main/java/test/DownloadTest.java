package test;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    public static void downloadRecentImages() {
        System.out.println("시작..");
        String latestFolderName = findLatestFolder(filePath);
        if (latestFolderName != null) {
            logger.info("가장 최근 폴더: {}", latestFolderName);
            String latestTime = findLatestTimeInFolder(filePath + latestFolderName);
            if (!latestTime.equals("-1")) {
                logger.info("가장 최근 시간: {}시", latestTime);
                ZonedDateTime startDate = ZonedDateTime.parse(latestFolderName + latestTime, DATETIME_FORMATTER2.withZone(UTC_ZONE_ID));
                ZonedDateTime nowUTC = ZonedDateTime.now(UTC_ZONE_ID).truncatedTo(ChronoUnit.HOURS);

                if (startDate.isEqual(nowUTC)) {
                    logger.info("최근 데이터 시간이 현재 시간과 동일합니다. 작업을 종료합니다.");
                    return; //                                            현재 시간과 최근 데이터 시간이 같으면 작업 종료
                }

                Map<String, List<String>> timeMap = generateTimeMap(startDate, nowUTC, 10);
                timeMap.forEach((date, times) -> logger.info("{}: {}", date, times));
                processDownload(timeMap);
            } else {
                logger.info("적합한 파일이 없습니다.");
            }
        } else {
            logger.info("적합한 폴더가 없습니다.");
        }
    }

    /**
     * 지정된 디렉토리 경로에서 파일 목록을 검색.
     * @param directoryPath 검색할 디렉토리의 경로.
     * @return 지정된 디렉토리의 파일 객체 배열.
     */
    private static File[] getFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        return directory.listFiles();
    }

    /**
     * 주어진 디렉토리 경로 내에서 날짜 패턴과 일치하는 가장 최신 폴더 찾기.
     * @param directoryPath 폴더가 위치한 경로.
     * @return 날짜 패턴과 일치하는 가장 최신 폴더의 이름.
     */
    public static String findLatestFolder(String directoryPath) {
        File[] files = getFilesInDirectory(directoryPath);
        String latestFolderName = null;
        long latest = 0;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && file.getName().matches("\\d{8}")) {
                    long folderDate = Long.parseLong(file.getName());
                    if (folderDate > latest) {
                        latest = folderDate;
                        latestFolderName = file.getName();
                    }
                }
            }
        }
        return latestFolderName;
    }

    /**
     * 주어진 폴더 경로 내에서 가장 최근의 시간을 찾기.
     * @param folderPath 시간을 찾을 폴더의 경로.
     * @return 가장 최근 시간의 문자열. 시간을 찾지 못하면 "-1" 반환.
     */
    public static String findLatestTimeInFolder(String folderPath) {
        File[] files = getFilesInDirectory(folderPath);
        int latestTime = -1;
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (name.matches(".+\\d{12}\\.srv\\.png")) {
                    int time = Integer.parseInt(name.substring(41, 43));
                    if (time > latestTime) {
                        latestTime = time;
                    }
                }
            }
        }
        // 사용 예: 두 자리 숫자 형식으로 반환
        return String.format("%02d", latestTime);
    }

    /**
     * 주어진 시작 및 종료 시간 사이에 일정 간격으로 시간을 생성하여 맵에 저장합.
     * @param start 시작 시간(ZonedDateTime 형식).
     * @param end 종료 시간(ZonedDateTime 형식).
     * @param intervalInMinutes 시간 간격(분).
     * @return 생성된 시간 맵.
     */
    public static Map<String, List<String>> generateTimeMap(ZonedDateTime start, ZonedDateTime end, int intervalInMinutes) {
        Map<String, List<String>> dateTimes = new TreeMap<>();
        long minutesBetween = ChronoUnit.MINUTES.between(start, end);
        IntStream.rangeClosed(0, (int) minutesBetween / intervalInMinutes)
                .mapToObj(i -> start.plusMinutes(i * intervalInMinutes)).forEach(dateTime -> {
                    String dateKey = dateTime.format(DATE_FORMATTER);
                    String timeValue = dateTime.format(TIME_FORMATTER);
                    dateTimes.computeIfAbsent(dateKey, k -> new java.util.ArrayList<>()).add(timeValue);
                });
        return dateTimes;
    }

    /**
     * 생성된 시간 맵을 사용하여 각 날짜와 시간에 대한 image download.
     * @param timeMap 날짜와 시간이 저장된 맵.
     */
    public static void processDownload(Map<String, List<String>> timeMap) {
        timeMap.forEach((date, times) -> {
            times.forEach(time -> {
                WeatherCrawlerUtil.downloadImage(date, time);
            });
        });
    }
}
