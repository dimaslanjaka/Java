package driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestChrome {
	static WebDriver driver;

	static {
		System.setProperty("webdriver.chrome.driver", "D:\\Workspaces\\Java\\selenium-java\\bin\\chromedriver.exe");
	}

	public static void main(String[] args) throws InterruptedException {
		driver = new ChromeDriver();
		driver.manage().window().maximize();

		System.out.println("Success");
	}

	void load() throws InterruptedException {
		driver.get("http://www.google.com/");
		Thread.sleep(5000);  // Let the user actually see something!
		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("ChromeDriver");
		searchBox.submit();
		Thread.sleep(5000);  // Let the user actually see something!
		driver.quit();
	}
}
