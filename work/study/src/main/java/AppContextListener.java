import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import commonUtils.WeatherCrawlerUtil;
import test.DownloadTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AppContextListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	// 스케줄러를 초기화합니다.
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // 서버 시작과 동시에 단 한 번 실행되는 작업
        scheduler.schedule(() -> {
           WeatherCrawlerUtil.downloadRecentImages();
        }, 0, TimeUnit.SECONDS);

        
    	
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::scheduleTask, 0, 1, TimeUnit.MINUTES);
    }
    //1분마다 실행
    private void scheduleTask() {
		/*
		 * Calendar calendar = Calendar.getInstance(); int currentMinute =
		 * calendar.get(Calendar.MINUTE); scheduler.schedule(new
		 * ImageDownloaderTask(currentMinute), 1, TimeUnit.MINUTES);
		 */
        
    }

    class ImageDownloaderTask implements Runnable {
    	public int minute;
        public ImageDownloaderTask(int minute) {
            this.minute = minute;
        }
        @Override
        public void run() {
        	 System.out.println("이미지 다운로드 스케줄러 시작");
        	 LocalDateTime now = LocalDateTime.now();

             // 날짜와 시간을 원하는 형태로 포맷팅합니다. "yyyy-MM-dd HH"
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH");
             String formattedNow = now.format(formatter);
        	  
        	
            
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
