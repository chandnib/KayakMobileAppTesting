package kayak.project.testing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

public class ValidLoginFacebook {
	private static AndroidDriver<WebElement> driver;

	private static Properties testData;
	/*
	 * Initial set-up to set the emulator
	 * Mapping the required desired capabilities
	 * Please check if the URL port matches the APPIUM port default port is :4723
	 */
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
	 * Date:27th Nov 2016
	 * Test For Valid Login using Facebook account option
	 */
	
	@Test
    public void testValidFBLogin() throws InterruptedException {
		System.out.println("Test For Valid Login with FaceBook");
		//Set the fbemail with a valid mail id.This is left blank for security reason.While running the script a valid value will be added
		String fbemail="";
		//Set the fbpass with a valid password.This is left blank for security reason.While running the script a valid value will be added		
		String fbpass="";
		driver.findElementByName("Connect with Facebook").click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		 //Switch web driver context to WEBVIEW
		Set<String> contextHandles=driver.getContextHandles();
		for(String s:contextHandles)
				{
				
				if(s.contains("WEBVIEW"))
					{
			
					driver.context(s);
					}
				}
			
			//Set the fb email id
		driver.findElement(By.xpath("//input[@name='email']")).sendKeys(fbemail);
			//Set the fb password
		driver.findElement(By.xpath("//input[@name='pass']")).sendKeys(fbpass);
			//hide keyboard
		driver.hideKeyboard();
			//click on Login
		driver.findElement(By.name("login")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			//Cancel 
		driver.findElement(By.xpath("//button[@name='__CANCEL__']")).click();
			//Switch back to the native APP
		Set<String> contextHandles1=driver.getContextHandles();
		for(String s:contextHandles1)
			{
			
				if(s.contains("NATIVE_APP"))
					{
			
					driver.context(s);
					}
			}
		System.out.println("Valid Login  for Facebook credentials passed:");
		System.out.println("***************************************");
		}
		
	
	@After
    public void closureActivities(){
        driver.quit();
    }
}
