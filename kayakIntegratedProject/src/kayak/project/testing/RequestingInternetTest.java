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

public class RequestingInternetTest {

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
				//System.out.println("Airline: " + airLine + " FlightNo: " + flightNo);
				WebElement findHotels = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.kayak.android:id/searchButton\")");
				findHotels.click();
				
				// Check if their is any alert
         		WebElement alert = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/alertTitle\")");
				if(checkInternet(alert)) {
					System.out.println("you are offline");
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
					//clicking find hotel again
					findHotels.click();
					if(!checkInternet(alert)) {
						System.out.println("Internet working");
					}
					if(alert!=null) {
						WebElement cancelButton = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/button2\")");
						System.out.println("new error is for "+ alert.getText() );
						cancelButton.click();
					}
/*					WebElement alert2 = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/alertTitle\")");
					android:id/button2*/
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