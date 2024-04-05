/*
 * import javax.servlet.ServletContext; import
 * javax.servlet.ServletContextEvent; import
 * javax.servlet.ServletContextListener; import
 * javax.servlet.annotation.WebListener;
 * 
 * import commonUtils.FileUtil; import commonUtils.ImageDownloaderTask;
 * 
 * import java.util.concurrent.Executors; import
 * java.util.concurrent.ScheduledExecutorService; import
 * java.util.concurrent.TimeUnit;
 * 
 * 
 * @WebListener public class AppContextListener implements
 * ServletContextListener {
 * 
 * private ScheduledExecutorService scheduler;
 * 
 * @Override public void contextInitialized(ServletContextEvent sce) {
 * 
 * // ServletContext servletContext = sce.getServletContext(); // // 서블릿 컨텍스트를
 * 전역 변수에 저장하거나, 필요한 클래스에 전달합니다. // FileUtil.initialize(servletContext);
 * 
 * 
 * 
 * scheduler = Executors.newSingleThreadScheduledExecutor();
 * scheduler.scheduleAtFixedRate(new ImageDownloaderTask(), 0, 1,
 * TimeUnit.MINUTES); System.out.println("Scheduler has been started"); }
 * 
 * @Override public void contextDestroyed(ServletContextEvent sce) {
 * scheduler.shutdownNow(); System.out.println("Scheduler has been stopped"); }
 * }
 */