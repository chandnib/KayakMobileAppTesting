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

public class RequestingEnableGPSTest {

	private static AndroidDriver<WebElement> driver;
	private static Properties testData;
	private static Properties capabilitiesValues;
	private static DesiredCapabilities capabilities;

	private static void setUp() {
		try {
			capabilities = DesiredCapabilities.android();
			capabilitiesValues  = new Properties();
			capabilitiesValues.load(new FileInputStream("testdata/capabilities.properties"));
			capabilities.setCapability(CapabilityType.BROWSER_NAME,"");
			capabilities.setCapability("deviceName", capabilitiesValues.getProperty("deviceName"));
			capabilities.setCapability("platformVersion", capabilitiesValues.getProperty("platformVersion"));
			capabilities.setCapability("platformName","Android");

			testData = new Properties();
			testData.load(new FileInputStream("testdata/flightData.properties"));
			capabilities.setCapability("appPackage", "com.kayak.android");
			capabilities.setCapability("appActivity", "com.kayak.android.Splash");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	 //Get all keys from the properties file
	  public static HashMap<String,String> getAllKeys() {
	    Set<Object> keys = testData.keySet();
	    HashMap<String,String> airLineVsFlightNo = new HashMap<>();
	    
	    for(Object key: keys) {
	    	String keyStr = (String)key;
	    	String value = testData.getProperty(keyStr);
	    	airLineVsFlightNo.put(keyStr, value);
	    }
	    return airLineVsFlightNo;
	  }

	public static void main(String[] args) {

		setUp();
		
		try {
				driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				// Clicking on location textbox
				WebElement whereLocationTextBox = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.kayak.android:id/displayName\")");
				whereLocationTextBox.click();
				// Extracting currentlocation textbox
				List<WebElement> listTextBox = driver.findElementsByClassName("android.widget.TextView");
				
				for(WebElement textBox : listTextBox) {
					if(textBox.getText().equals("Current location")) {
						textBox.click();
						driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
						WebElement alertLocation = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/alertTitle\")");
						WebElement alertMessage =   driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/message\")");
						System.out.println("Alert Message is - "+ alertMessage.getText());
						WebElement okButton = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/button1\")");
						okButton.click();
						WebElement permissionMessage = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.android.packageinstaller:id/permission_message\")");
						if(permissionMessage!=null) {
							WebElement allowButton = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.android.packageinstaller:id/permission_allow_button\")");
							allowButton.click();
						}
						WebElement alertEnableGpsMessage =   driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/message\")");	
						System.out.println("Alert Message is - "+ alertEnableGpsMessage.getText());
						WebElement yesButton = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/button1\")");
						yesButton.click();
						WebElement switchButtonLocation = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.android.settings:id/switch_widget\")");
						switchButtonLocation.click();
						driver.navigate().back();
						textBox.click();
						WebElement displayTextBox = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.kayak.android:id/displayName\")");
						System.out.println("Current location is -" + displayTextBox.getText());
					}
				}
				driver.quit();

		} catch (Exception ex){
			driver.quit();
			System.out.println("Unexpected error occured!");
			ex.printStackTrace();
		} finally{
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