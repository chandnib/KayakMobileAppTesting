package kayak.project.testing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;

public class InvalidLoginFacebook {
	private static AndroidDriver<WebElement> driver;

	private static Properties testData;
	@Before
    public void setUp() throws FileNotFoundException, IOException{
	
DesiredCapabilities capabilities=DesiredCapabilities.android();
	
	capabilities.setCapability(CapabilityType.BROWSER_NAME,"");
	capabilities.setCapability("deviceName", "MyAndroid");
	capabilities.setCapability("platformVersion", "5.0.2");
	capabilities.setCapability("platformName","Android");

	testData = new Properties();
    testData.load(new FileInputStream("testdata/credentials.properties"));
    capabilities.setCapability("appPackage","com.kayak.android");
    capabilities.setCapability("appActivity","com.kayak.android.Splash");
    driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	/*
	 * Written By:Madhumita
	 * Date:20th Nov 2016
	 * Test For InValid Login using Facebook  option
	 */
	
	
	@Test
    public void testInvalidFBLogin() {
		driver.findElementByName("Connect with Facebook").click();
		System.out.println("Test For Invalid Facebook");
		//driver.findElementByName("Connect with Facebook").click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		int noOfTests = Integer.parseInt(testData.getProperty("noofscriptsIF"));
		
		//switch context to webview as this is an hybrid app supporting both the Native APP context and WEBVIEW aontext
		driver.context("WEBVIEW");
	
		while(noOfTests > 0)
			{
			System.out.println("***************************************");
			System.out.println("TEST CASE:"+noOfTests);
			//set the facebook Login name or email id from the properties file
			System.out.println("USERNAME:"+testData.getProperty("namefb"+noOfTests));
			driver.findElement(By.name("email")).sendKeys(testData.getProperty("namefb"+noOfTests));
			System.out.println("USERNAME:"+testData.getProperty("passfb"+noOfTests));
			//set the facebook password name or email id from the properties file
			driver.findElement(By.name("pass")).sendKeys(testData.getProperty("passfb"+noOfTests));
			//hide keyboatd to select Login 
			driver.hideKeyboard();
			//Click on Login 
			driver.findElement(By.name("login")).click();
			System.out.println("In-Valid Login Test Pass for Facebook credentials:"+noOfTests);
			System.out.println("***************************************");
			noOfTests--;
		}
	}
	
	@After
    public void closureActivities(){
        driver.quit();
    }
}
