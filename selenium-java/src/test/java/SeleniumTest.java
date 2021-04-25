import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;


public class SeleniumTest {

	// Navigate to this URL using another browser https://the-internet.herokuapp.com/
	// Navigate to this URL https://example.cypress.io/
	// Navigate to https://react-shopping-cart-67954.firebaseapp.com/
	// Using WebDriverManager

	//This method will run once before all of the tests in our class
//    @BeforeClass
//    public static void setupClass() {
//        WebDriverManager.edgedriver().setup();
//    }
	@Test
	public void safariTest() {
		WebDriver driver = new SafariDriver();
		driver.get("https://the-internet.herokuapp.com/");
		driver.quit();
	}

	@Test
	public void cypressTest() throws InterruptedException {
		WebDriver driver = new SafariDriver();
		driver.get("https://example.cypress.io/");
		Thread.sleep(3000);
		driver.quit();
	}

	@Test
	public void shoppingCartTest() throws InterruptedException {
		WebDriver driver = new SafariDriver();
		driver.get("https://react-shopping-cart-67954.firebaseapp.com/");
		Thread.sleep(3000);
		driver.quit();
	}

	@Test
	public void testGoogleSearch() throws InterruptedException {
		// Optional. If not specified, WebDriver searches the PATH for chromedriver.
		System.setProperty("webdriver.chrome.driver", "D:\\Workspaces\\Java\\selenium-java\\bin\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.get("http://www.google.com/");
		Thread.sleep(5000);  // Let the user actually see something!
		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("ChromeDriver");
		searchBox.submit();
		Thread.sleep(5000);  // Let the user actually see something!
		driver.quit();
	}
}
