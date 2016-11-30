package kayak.project.testing;


import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CarModuleTesting {
	
	private static AndroidDriver<WebElement> driver;
	private static DesiredCapabilities capabilities;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws MalformedURLException {
		//initial set up
		capabilities = DesiredCapabilities.android();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability("deviceName", "47ae75fa");
		capabilities.setCapability("platformVersion", "5.0.2");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("appPackage", "com.kayak.android");
		capabilities.setCapability("appActivity", "com.kayak.android.Splash");

		//name of the code package
	    capabilities.setCapability("appPackage","com.kayak.android");
		capabilities.setCapability("appActivity","com.kayak.android.Splash");
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		//close the landing page
		driver.findElementById("com.kayak.android:id/closeBtn").click();
		//open cars page
		driver.findElementById("android:id/button1").click();
		driver.findElementByXPath("//android.support.v7.a.d[@index='2']").click();
		//click on find cars
		driver.findElementById("com.kayak.android:id/searchButton").click();
		//checking for current location update in search find cars page
		if(!driver.findElementsByName("San Francisco, CA").isEmpty()){
			System.out.println("Correct Location header is San Francisco, CA");
		}
		else{
			System.out.println("Incorrect Location header");
		}
		//checking for current location update in cars page
		driver.findElementByClassName("android.widget.ImageButton").click();
		WebElement location = driver.findElementById("com.kayak.android:id/displayName");
		if(location.getText().equalsIgnoreCase("San Francisco, CA")){
			System.out.println("Correct Location - San Francisco, CA");
		}
		else{
			System.out.println("Incorrect Location");
		}
		//checking for same drop off options and the layout
		if(!driver.findElementsByName("Same drop-off").isEmpty()){
			System.out.println("Same drop-off is selected");
			WebElement pickDropLayout = driver.findElementById("com.kayak.android:id/pickupDropoffLayout");
			WebElement pickDropElement = pickDropLayout.findElement(By.id("com.kayak.android:id/label"));
			if(pickDropElement.getText().equalsIgnoreCase("Pick-up/drop-off")){
				System.out.println("Pick-up/drop-off inside Same drop-off");
			}
		}
		else{
			System.out.println("Same drop-off is not selected");
		}
		//checking for drop off options
		driver.findElementByName("Same drop-off").click();
		WebElement sameDrop = driver.findElementByXPath("//android.widget.TextView[@index='0']");
		if(sameDrop.getText().equalsIgnoreCase("Same drop-off")){
			System.out.println("Same drop-off option is present");
		}
		else{
			System.out.println("Same drop-off option is not present");
		}
		WebElement diffDrop = driver.findElementByXPath("//android.widget.TextView[@index='1']");
		if(diffDrop.getText().equalsIgnoreCase("Different drop-off")){
			//checking for different drop off options and the layout
			System.out.println("Different drop-off option is present");
			driver.findElementByXPath("//android.widget.TextView[@index='1']").click();
			if(!driver.findElementsByName("Different drop-off").isEmpty()){
				System.out.println("Different drop-off is selected");
			}
			else{
				System.out.println("Different drop-off is not selected");
			}
			//checking for the pickup in Different drop-off
			WebElement pickLayout = driver.findElementById("com.kayak.android:id/pickupLayout");
			WebElement pickElement = pickLayout.findElement(By.id("com.kayak.android:id/label"));
			if(pickElement.getText().equalsIgnoreCase("Pick-up")){
				System.out.println("Pick-up inside Different drop-off");
			}
			//checking for the drop off in Different drop-off
			WebElement dropLayout = driver.findElementById("com.kayak.android:id/dropoffLayout");
			WebElement dropElement = dropLayout.findElement(By.id("com.kayak.android:id/label"));
			if(dropElement.getText().equalsIgnoreCase("Drop-off")){
				System.out.println("Drop-off inside Different drop-off");
			}
		}
		else{
			System.out.println("Different drop-off option is not present");
		}
	}

}
