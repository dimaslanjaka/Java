import org.junit.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

public class ChromeDriverTest {
	private static ChromeDriverService service;
	private WebDriver driver;

	@BeforeClass
	public static void createAndStartService() throws IOException {
		service = new ChromeDriverService.Builder()
						.usingDriverExecutable(new File("bin/chromedriver.exe"))
						.usingAnyFreePort()
						.build();
		service.start();
	}

	@AfterClass
	public static void stopService() {
		service.stop();
	}

	@Before
	public void createDriver() {
		ChromeOptions chromeOptions = new ChromeOptions();
		Capabilities capabilities = new DesiredCapabilities();
		driver = new RemoteWebDriver(capabilities);
	}

	@After
	public void quitDriver() {
		driver.quit();
	}

	@Test
	public void testGoogleSearch() {
		driver.get("http://www.google.com");
		// rest of the test...
	}
}
