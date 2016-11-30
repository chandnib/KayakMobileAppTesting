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

public class ValidLoginEmail {
	private static AndroidDriver<WebElement> driver;

	private static Properties testData;
	private static Properties capabilitiesValues;
	/*
	 * Initial set-up to set the emulator
	 * Mapping the required desired capabilities
	 * Please check if the URL port matches the APPIUM port default port is :4723
	 */
	@Before
    public void setUp() throws FileNotFoundException, IOException{
	
	DesiredCapabilities capabilities=DesiredCapabilities.android();
	
	capabilitiesValues  = new Properties();
	capabilitiesValues.load(new FileInputStream("testdata/capabilities.properties"));
	capabilities.setCapability(CapabilityType.BROWSER_NAME,"");
	capabilities.setCapability("deviceName", capabilitiesValues.getProperty("deviceName"));
	capabilities.setCapability("platformVersion", capabilitiesValues.getProperty("platformVersion"));
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
	 * Test For Valid Login using email-log in  option
	 */
	
	@Test
    public void testEValidLogin() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElementByName("Sign in with your email").click();
		//read number of test scripts to be executed
		int noOfTests = Integer.parseInt(testData.getProperty("noofvescripts"));
		
		int count=noOfTests;//Set the count equal to number of test cases
		while(noOfTests > 0)
			{
			System.out.println("Test CASE"+noOfTests);
		//set the driver to select the Sign in email option for count=noOfTests the driver is already set on line 55
			if(count!=noOfTests)
				{
					driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					driver.findElementByName("Sign in with your email").click();
					//select Sign-up Option as all the cases where Count!= noOfTests teh page loaded is welcome back page and in order o test sign-up page is needed
					driver.findElement(By.id("com.kayak.android:id/signupBtn")).click();
				}
		
			//Set the input field for email from the properties file
			driver.findElementByName("Save up to 35% with hotel deals").sendKeys(testData.getProperty("nameve"+noOfTests));
			//Set the input field for Password from the properties file
			driver.findElementByName("Get flight status alerts").sendKeys(testData.getProperty("passve"+noOfTests));
			//Set the preference for email option(yes /no)
			String email=testData.getProperty("emailve"+noOfTests);
		
			String ischecked=driver.findElementById("com.kayak.android:id/preferences_signup_deal").getAttribute("checked");
			//Uncheck the default selected "Email me deals" if the value is set as no in the properties file
			if(email.equalsIgnoreCase("no") && ischecked.equals("true"))
			{
			
			driver.findElementById("com.kayak.android:id/preferences_signup_deal").click();
			}
			//Check the default selected "Email me deals" if the value is set as yes in the properties file
			if(email.equalsIgnoreCase("yes") && ischecked.equals("false"))
			{
			
			driver.findElementById("com.kayak.android:id/preferences_signup_deal").click();
			
			}
			//hide Keyboard
			driver.hideKeyboard();
			driver.findElementByName("Sign Up").click();
		
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			//The Verify alert box appears only for the first login as it then sets the preferences 
			//which get cleared only when the app is relaunched.So a check is done to select the alert.
			if(count==noOfTests)
				driver.findElement(By.id("android:id/button1")).click();
			//Select the Navigation Drawer
			driver.findElementByAccessibilityId("Open navigation drawer").click();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			//Select the current signed-in account
			driver.findElement(By.id("com.kayak.android:id/navigation_drawer_email_text")).click();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			//Sign out from the current account
			driver.findElementByName("Sign out").click();
			//Select the Navigator Option to navigate back to signup page
			driver.findElementByAccessibilityId("Open navigation drawer").click();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.findElement(By.id("com.kayak.android:id/navigation_drawer_sign_in_text")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			noOfTests--;
		}
	}
	
	@After
    public void closureActivities(){
        driver.quit();
    }
}
