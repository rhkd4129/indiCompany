package test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumEx {
	public static void main(String[] args) {
		 System.setProperty("webdriver.chrome.driver", "C:\\gitRepository\\indiCompany\\work\\chromedriver.exe");
		 WebDriver driver = new ChromeDriver();
		 driver.quit();
	}
}
