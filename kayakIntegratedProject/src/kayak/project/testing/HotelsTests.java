package kayak.project.testing;
import static org.junit.Assert.*;

import org.junit.Test;

import org.testng.annotations.AfterMethod;


import io.appium.java_client.android.AndroidDriver;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import pageObjects.DateSelection;

import pageObjects.guests;

import pageObjects.location;


public class HotelsTests {
	

	 
	   AndroidDriver androidDriver;
	
	
	InputStream inputStream;
	private Properties prop = new Properties();
	String propFileName = "login.properties";
	private static Properties capabilitiesValues;

		// Read from properties file
		@BeforeMethod public void getPropValues() throws IOException{
		try {

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}


		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
	}
	
	
	

	  WebDriver driver;
	    public static Long sleepBetweenOperations = 4000L;

	    @BeforeMethod public void setUp() throws FileNotFoundException, IOException {
	        //Set up desired capabilities and pass the Android app-activity and app-package to Appium
	        File app = new File("/Users/chimawarade/Desktop/testingproject/kayak.apk");
	        capabilitiesValues  = new Properties();
	    	capabilitiesValues.load(new FileInputStream("testdata/capabilities.properties"));
	    	DesiredCapabilities capabilities=DesiredCapabilities.android();
	    	
	    	capabilities.setCapability(CapabilityType.BROWSER_NAME,"");
	    	capabilities.setCapability("deviceName", capabilitiesValues.getProperty("deviceName"));
	    	capabilities.setCapability("platformVersion", capabilitiesValues.getProperty("platformVersion"));
	    	capabilities.setCapability("appPackage", "com.kayak.android");
	    	capabilities.setCapability("appActivity", "com.kayak.android.Splash");

		    driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

	        /*driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
	        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);*/
	    }
	    
	    
	    @Test
	    public void testRoomsperGuests() throws InterruptedException{
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("com.kayak.android:id/closeBtn")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("android:id/button1")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("com.kayak.android:id/roomsGuestsRow")).click();
	    	Thread.sleep(3000);
	    	for(int i=0;i<6;i++){
	    	   driver.findElement(By.id("com.kayak.android:id/incrementGuests")).click();
	    	   Thread.sleep(3000);
	    	}
	    	guests gp = new guests(driver);
	    	gp.assertRoomsPerGuests();
	    }
	    			    
	    
	   
	    @Test
	    public void validSelection() throws InterruptedException{
	    	Thread.sleep(10000);
	    	driver.findElement(By.id("com.kayak.android:id/closeBtn")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("android:id/button1")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("com.kayak.android:id/datesRow")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.xpath("//android.widget.TextView[@text='29']")).click();
	    	//driver.findElement(By.linkText("29")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.xpath("//android.widget.TextView[@text='30']")).click();
	    	//driver.findElement(By.linkText("30")).click();
	    	Thread.sleep(3000);
	    	//Nov 29 – Nov 30
	    	String duration = driver.findElement(By.id("com.kayak.android:id/text")).getText();
	    	 String pattern = "(.*)29 – (.*)30";

	        // Create a Pattern object
	        Pattern r = Pattern.compile(pattern);

	        // Now create matcher object.
	        Matcher m = r.matcher(duration);
	        if (m.matches( )) {
	          
	        }else {
	           System.out.println("NO MATCH");
	        }
	        DateSelection l = new DateSelection(driver);
	    	l.assertDateSelection(m);
	     }
	    	
    
	    
	    @Test
	    public void locationTracker() throws InterruptedException{
	    	Thread.sleep(10000);
	    	driver.findElement(By.id("com.kayak.android:id/closeBtn")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("android:id/button1")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("com.kayak.android:id/destinationLayout")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("com.kayak.android:id/smartySearchText")).sendKeys("San Jose, CA");
	    	Thread.sleep(3000);
	    	String place = driver.findElement(By.xpath("//android.widget.LinearLayout[2]/android.widget.TextView")).getText();
	    	driver.findElement(By.xpath("//android.widget.LinearLayout[2]/android.widget.TextView")).click();
	    	driver.findElement(By.id("com.kayak.android:id/destinationLayout")).click();
	    	Thread.sleep(3000);
	    	
	    	location l = new location(driver);
	    	l.assertlocation(place);
	   }
	    
	 
	    @AfterMethod 
	    public void teardown() {
	        //close the app
	        driver.quit();
	    }
	    

}
