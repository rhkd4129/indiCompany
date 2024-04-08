import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AppContextListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::scheduleTask, 0, 1, TimeUnit.MINUTES);
    }
    //1분마다 실행
    private void scheduleTask() {
        Calendar calendar = Calendar.getInstance();
        int currentMinute = calendar.get(Calendar.MINUTE);
        if (currentMinute == 5 || currentMinute == 15 || currentMinute == 25 ||
            currentMinute == 35 || currentMinute == 45 || currentMinute == 55) {
            scheduler.schedule(new ImageDownloaderTask(currentMinute), 1, TimeUnit.MINUTES);
        }
    }

    class ImageDownloaderTask implements Runnable {
    	public int minute;
        public ImageDownloaderTask(int minute) {
            this.minute = minute;
        }
        @Override
        public void run() {
            System.out.println(minute);
            
            
            
            System.out.println("ImageDownloaderTask is executed.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
