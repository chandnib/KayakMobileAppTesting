package kayak.project.testing;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import java.io.File;
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
	

	 
	
	   WebDriver driver;
	    public static Long sleepBetweenOperations = 4000L;

	    @BeforeMethod public void setUp() throws MalformedURLException {
	        //Set up desired capabilities and pass the Android app-activity and app-package to Appium
	        File app = new File("/Users/chimawarade/Desktop/testingproject/kayak.apk");
	        DesiredCapabilities cap = new DesiredCapabilities();
	        cap.setCapability(CapabilityType.BROWSER_NAME, " ");
	        cap.setCapability("deviceName", "emulator-5556");
	        cap.setCapability("platformVersion", "5.1.1");
	        cap.setCapability("platformName", "Android");
	        cap.setCapability("app", app.getAbsolutePath());
	        cap.setCapability("appPackage", "com.kayak.android");
	        cap.setCapability("appActivity", "com.kayak.android.Splash");

		    driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
		    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

	        /*driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
	        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);*/
	    }
	    
	    //Check if the Number of Rooms increases as Guest exceeds 4 each time
	    @Test
	    public void testRoomsperGuests() throws InterruptedException{
	    	System.out.println("****************************************************************");
	    	System.out.println("Test Case No: 1");
	    	System.out.println("Test Case Details: Check if the Number of Rooms increases as Guest exceeds 4 each time");
	    	
	    	Thread.sleep(3000);
	    	//click close button
	    	driver.findElement(By.id("com.kayak.android:id/closeBtn")).click();    
	    	Thread.sleep(3000);
	    	//click looks good button on pop up
	    	driver.findElement(By.id("android:id/button1")).click();
	    	Thread.sleep(3000);
	    	//click on rooms tab
	    	driver.findElement(By.id("com.kayak.android:id/roomsGuestsRow")).click();
	    	Thread.sleep(3000);
	    	//Increment guests upto 7 guest
	    	for(int i=0;i<6;i++){
	    	   driver.findElement(By.id("com.kayak.android:id/incrementGuests")).click();
	    	   Thread.sleep(3000);
	    	}
	    	guests gp = new guests(driver);
	    	//check if the room number incremented
	    	boolean b=gp.assertRoomsPerGuests();
	    	System.out.println("Actual Result:  "+ driver.findElement(By.id("com.kayak.android:id/roomsText")).getText());
	    	System.out.println("Expected Result: 2 Rooms");
	    	System.out.print("Result: Test case");
	    	if(b){
	    		System.out.println(" Passed ");
	    	}else{
	    		System.out.println(" Failed ");
	    	}
	    	System.out.println("****************************************************************");
	    }
	    			    
	    
	   // check if the selected dates are show properly on the hotels page
	    @Test
	    public void validSelection() throws InterruptedException{
	    	System.out.println("****************************************************************");
	    	System.out.println("Test Case No: 2");
	    	System.out.println("Test Case Details: check if the selected dates are show properly on the hotels page");
	    	
	    	Thread.sleep(10000);
	    	driver.findElement(By.id("com.kayak.android:id/closeBtn")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("android:id/button1")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("com.kayak.android:id/datesRow")).click();   // click on the dates row
	    	Thread.sleep(3000);
	    	driver.findElement(By.xpath("//android.widget.TextView[@text='29']")).click();  // click on the date number e.g 29
	    	//driver.findElement(By.linkText("29")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.xpath("//android.widget.TextView[@text='30']")).click();   // // click on the date number e.g 30
	    	//driver.findElement(By.linkText("30")).click();
	    	Thread.sleep(3000);
	    	//Nov 29 – Nov 30
	    	// get the text from teh date tab
	    	String duration = driver.findElement(By.id("com.kayak.android:id/text")).getText();
	    	 String pattern = "(.*)29 – (.*)30";

	        // Create a Pattern object
	        Pattern r = Pattern.compile(pattern);

	        // Now create matcher object.
	        Matcher m = r.matcher(duration);
	       
	        DateSelection l = new DateSelection(driver);
	        //check if the from and to dates whether they are correct
	       
	    	l.assertDateSelection(m);
	    	System.out.println("Actual Result:  ");
	    	System.out.println("Expected Result: ");
	    	System.out.print("Result: Test case");
	    	 if (m.matches( )) {
		          System.out.println(" passed");
		        }else {
		           System.out.println(" failed");
		        }
	    	System.out.println("****************************************************************");
	     }
	    	
    
	    // check if the searched location shows in suggestions in search tab in location tab
	    @Test
	    public void locationTracker() throws InterruptedException{
	    	System.out.println("****************************************************************");
	    	System.out.println("Test Case No: 3 ");
	    	System.out.println("Test Case Details: check if the searched location shows in suggestions in search tab in location tab");
	    	
	    	Thread.sleep(10000);
	    	driver.findElement(By.id("com.kayak.android:id/closeBtn")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("android:id/button1")).click();
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("com.kayak.android:id/destinationLayout")).click();      // click the location tab
	    	Thread.sleep(3000);
	    	driver.findElement(By.id("com.kayak.android:id/smartySearchText")).sendKeys("San Jose, CA");//set the location
	    	Thread.sleep(3000);
	    	String place = driver.findElement(By.xpath("//android.widget.LinearLayout[2]/android.widget.TextView")).getText(); //get the location searched
	    	driver.findElement(By.xpath("//android.widget.LinearLayout[2]/android.widget.TextView")).click();
	    	driver.findElement(By.id("com.kayak.android:id/destinationLayout")).click();
	    	Thread.sleep(3000);
	    	
	    	location l = new location(driver);
	    	boolean b = l.assertlocation(place);   // check if the searched location shows in suggestions
	    	System.out.println("Actual Result: "+place);
	    	System.out.println("Expected Result: San Jose, CA ");
	    	System.out.print("Result: Test case");
	    	if(b){
	    		System.out.println(" Passed ");
	    	}else{
	    		System.out.println(" Failed ");
	    	}
	    	System.out.println("****************************************************************");
	   }
	    
	 
	    @AfterMethod 
	    public void teardown() {
	        //close the app
	        driver.quit();
	    }
	    

}
