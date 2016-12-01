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
 * Script to test whether app ask for permission for Location
 * It will also check whether app ask to enable GPS when its
 * Disabled. And in the end when both are given it will give the
 * current location
 * */
public class RequestingEnableGPSTest {
	//Global Variables
	private static AndroidDriver<WebElement> driver;
	private static Properties capabilitiesValues;
	private static DesiredCapabilities capabilities;

	//Function to setup the project and environment
	private static void setUp() {
		try {
			capabilities = DesiredCapabilities.android();
			capabilitiesValues  = new Properties();
			capabilitiesValues.load(new FileInputStream("testdata/capabilities.properties"));
			capabilities.setCapability(CapabilityType.BROWSER_NAME,"");
			capabilities.setCapability("deviceName", capabilitiesValues.getProperty("deviceName"));
			capabilities.setCapability("platformVersion", capabilitiesValues.getProperty("platformVersion"));
			capabilities.setCapability("platformName","Android");
			capabilities.setCapability("appPackage", "com.kayak.android");
			capabilities.setCapability("appActivity", "com.kayak.android.Splash");

			
		} catch (Exception e) {
			System.out.println("Some Error occured");
		}
		
	}

	public static void main(String[] args) {

		setUp();
		
		try {
				System.out.println("********************************************************");
				System.out.println("Test Case No: 1- App Doesnt have permission for location");
				driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				// Clicking on location textbox
				WebElement whereLocationTextBox = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.kayak.android:id/displayName\")");
				whereLocationTextBox.click();
				// Extracting list of all textbox
				List<WebElement> listTextBox = driver.findElementsByClassName("android.widget.TextView");
				// Iterating over list and extracting the current location textbox
				for(WebElement textBox : listTextBox) {
					// Only for current location text
					if(textBox.getText().equals("Current location")) {
						// Clicking on current location
						textBox.click();
						// Waiting for next screen to appear
						driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
						// Extracting Alert
						WebElement alertLocation = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/alertTitle\")");
						// Extracting messsage
						System.out.println("\tAlert Appeared");
						WebElement alertMessage =   driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/message\")");
						System.out.println("\tAlert Message is - "+ alertMessage.getText());
						// Extracting ok button of alert
						WebElement okButton = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/button1\")");
						// Clicking ok button 
						okButton.click();
						System.out.println("\tClicking on Ok button");
						System.out.println("\tExpecting Permission Alert");
						// Finding Permission alert
						WebElement permissionMessage = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.android.packageinstaller:id/permission_message\")");
						if(permissionMessage!=null) {
							System.out.println("\tAlert Message is - "+permissionMessage.getText());
							// Finding allow button
							WebElement allowButton = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.android.packageinstaller:id/permission_allow_button\")");
							// Giving permission
							allowButton.click();
							System.out.println("\tAllowed Button Clicked");
							System.out.println("\tLocation Permission Granted");
							System.out.println("");
						}
						System.out.println("Test Case No: 2- Enable Gps Condition Test");
						// Enable GPS message expected
						WebElement alertEnableGpsMessage =   driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/message\")");	
						System.out.println("\tAlert Message is - "+ alertEnableGpsMessage.getText());
						// Finding yes button
						WebElement yesButton = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/button1\")");
						// Clicking on yes button to enter settings of phone
						yesButton.click();
						System.out.println("\tEnabling GPS now");
						// Finding switch button on GPS setting
						WebElement switchButtonLocation = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.android.settings:id/switch_widget\")");
						switchButtonLocation.click();
						// Going back to app
						driver.navigate().back();
						System.out.println("\tGPS enabled");
						textBox.click();
						// Getting current location textbox again
						WebElement displayTextBox = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.kayak.android:id/displayName\")");
						System.out.println("\tCurrent location after enabling GPS is -" + displayTextBox.getText());
					}
				}
				driver.quit();

		} catch (Exception ex){
			driver.quit();
			System.out.println("Unexpected error occured!");
			ex.printStackTrace();
		} finally{
			System.out.println("********************************************************");
			driver.quit();
		}
	}

	private static boolean checkInternet(WebElement alert)
	{
		if(alert.getText().equals("You are offline"))
		return true;
			return false;
	}
}