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
        
        // 서버 시작과 동시에 작업을 실행하고, 이후 15분마다 반복 실행.
        scheduler.scheduleAtFixedRate(() -> {
            WeatherCrawlerUtil.downloadRecentImages();
        }, 0, 15, TimeUnit.MINUTES);
    
    }
    


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
