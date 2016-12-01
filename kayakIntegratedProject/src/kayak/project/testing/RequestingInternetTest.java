package kayak.project.testing;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
/*
 * Script to test whether server call ask for internet if its disable
 * Also it will check that pop up wont appear when internet is enable
 * We will get alert on any server call when internet is disabled.
 * */
public class RequestingInternetTest {
	//Global Variables
	private static AndroidDriver<WebElement> driver;
	private static Properties capabilitiesValues;
	private static DesiredCapabilities capabilities;

	//Function to setup the project and environment
	private static void setUp() {
		try {
			capabilities = DesiredCapabilities.android();
			capabilitiesValues  = new Properties();
			capabilities.setCapability(CapabilityType.BROWSER_NAME,"");
			capabilities.setCapability("deviceName", capabilitiesValues.getProperty("deviceName"));
			capabilities.setCapability("platformVersion", capabilitiesValues.getProperty("platformVersion"));
			capabilities.setCapability("platformName","Android");

			//testData = new Properties();
			//testData.load(new FileInputStream("testdata/flightData.properties"));
			capabilities.setCapability("appPackage", "com.kayak.android");
			capabilities.setCapability("appActivity", "com.kayak.android.Splash");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

	public static void main(String[] args) {

		setUp();
		
		try {
			System.out.println("********************************************************");
			System.out.println("Test Case No: 1- Internet Disabled");
				// Setting up driver
				driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				// Getting find hotel button
				WebElement findHotels = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.kayak.android:id/searchButton\")");
				// Clicking on the button - this will make call to server so need internet
				findHotels.click();
				// Now we will see whether we get any alert or not for internet
				WebElement alert = null;
				// Check if their is any alert
				alert = getAlert(alert);
				// If no alert means internet is working
				if(alert==null){
					System.out.println("	Internet - Enabled - So No Alert Popup");
					driver.quit();
				}
				// If we get alert then checking alert is for internet and enabling internet
				else if(getMessage(alert)) {
					System.out.println("\tInternet - Disabled - Alert Popup Appeared");
					System.out.println("\tAlert Message is - "+ alert.getText());
					System.out.println("");
					System.out.println("Test Case No: 2-  Enabling Internet in device and checking again");
					System.out.println("\tChecking again");
					//go to the settings
					WebElement settings = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/button1\")");
					settings.click();
					//go to cellular settings
					WebElement cellularSettings = driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Cellular networks\")");
					cellularSettings.click();
					//switch on Cellular Data
					WebElement switchInternet = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/switchWidget\")");
					switchInternet.click();
					//Going back to main settings
					WebElement backButton = driver.findElementByAndroidUIAutomator("new UiSelector().description(\"Navigate up\")");
					backButton.click();
					//going back to app
					driver.navigate().back();
					//clicking find hotel again and checking whether their is any alert
					// Now their should not be any alert
					findHotels.click();
					alert = null;
					alert = getAlert(alert);
					// alert should be null now 
					if(alert == null){
						System.out.println("\t\tInternet - Enabled - So No Alert Popup");
						driver.quit();
					}
					else{
						System.out.println("Test case no-2 Failed");
					}
				}
				driver.quit();

		} catch (Exception ex){
			if(driver!=null)  
			driver.quit();
			System.out.println("Unexpected error occured!");
			ex.printStackTrace();
		} finally{
			System.out.println("********************************************************");
			if(driver!=null)
			driver.quit();
		}
	}

	// Function to get alert if any from the screen
	private static WebElement getAlert(WebElement alert) {
		try{
    		 alert = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/alertTitle\")");
			}
			catch (NoSuchElementException e) {
				alert = null;
			}
		return alert;
	}


	// Function to check alert message of the screen
	private static boolean getMessage(WebElement alert)
	{
		if(alert.getText().equals("You are offline"))
		return true;
			return false;
	}
}