package test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ImageDownloader {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
    private static final String imageFolderPath = "path/to/your/images/folder";
    private static final String imageUrlPattern = "your/image/url/pattern";

    public static void main(String[] args) {
        try {
            downloadMissedImages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadMissedImages() throws Exception {
        File folder = new File(imageFolderPath);
        File[] listOfFiles = folder.listFiles();

        // 이미 폴더에 존재하는 이미지 파일의 시간을 저장합니다.
        Set<String> existingImages = new HashSet<>();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    // 파일 이름에서 시간 정보 추출 (예: "image_20240405_1500.jpg" -> "20240405_1500")
                    String dateTime = fileName.substring(fileName.indexOf('_') + 1, fileName.lastIndexOf('.'));
                    existingImages.add(dateTime);
                }
            }
        }

        // 현재 시간부터 마지막으로 다운로드한 이미지 시간까지 검사하여 누락된 이미지 다운로드
        // 여기서는 로직 간소화를 위해 직접 누락된 시간을 지정하거나 계산하는 부분은 생략합니다.
        // 예시: "20240405_1505", "20240405_1515" 등
//
//        for (String missedDateTime : getMissedDateTimes()) {
//            if (!existingImages.contains(missedDateTime)) {
//                // 누락된 이미지 다운로드
//                String imageUrl = imageUrlPattern.replace("{dateTime}", missedDateTime);
//                downloadImage(imageUrl, missedDateTime);
//            }
//        }
    }

    private static void downloadImage(String imageUrl, String dateTime) throws Exception {
        // 이미지 URL에서 이미지 다운로드 로직 구현
        // 예: Files.copy(new URL(imageUrl).openStream(), Paths.get(imageFolderPath + "image_" + dateTime + ".jpg"));
        System.out.println("Downloading image for datetime: " + dateTime);
    }

    private static Set<String> getMissedDateTimes() {
        // 누락된 시간들을 계산하거나 지정하여 반환하는 로직 구현
        // 예시를 위해 여기서는 빈 Set을 반환
        return new HashSet<>();
    }
}
