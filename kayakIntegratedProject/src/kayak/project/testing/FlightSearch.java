package kayak.project.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import io.appium.java_client.android.AndroidDriver;
import java.util.Properties;

public class FlightSearch{

	public AndroidDriver driver;
	Properties property=new Properties();
	String from, fromCode, to, toCode, expectedMonth,expectedMonthYear, expectedDate, expectedtravellers, expectedcabinClass;
	@BeforeTest
    public void prepareAndroidForAppium() throws MalformedURLException, InterruptedException {
        File appDir = new File("C:/Users/ritik/Selenium_projects/Appium_Test/app/");
        File app = new File(appDir, "com.kayak.android_2016-11-21.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device","Android");

        //mandatory capabilities
        capabilities.setCapability("deviceName","emulator-5554");
        capabilities.setCapability("platformName","Android");
        //capabilities.setCapability("appPackage", "com.kayak.android");
     	//capabilities.setCapability("appActivity","com.facebook.FacebookActivity");

        //other caps
        capabilities.setCapability("app", app.getAbsolutePath());
        driver =  new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        
        try {
			property.load(new FileInputStream("testData/searchData.properties"));
		} catch (IOException e) {
			System.out.println("Unable to load testData file");
		}
	}
    
    @Test
    public void search() throws InterruptedException
    {
    	//Wait for the homepage to open (Flights tab should be visible)
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Flights")));     
        driver.findElement(By.name("Flights")).click();
        
        fromCode = property.getProperty("fromCode1");
        from = property.getProperty("fromAirport1");
        System.out.println("From:" +from +"  "+ fromCode);
        toCode = property.getProperty("toCode1");
        to = property.getProperty("toAirport1");
        expectedMonthYear = property.getProperty("monthYear1");
        expectedMonth = property.getProperty("month1");
        expectedDate = property.getProperty("date1");
        expectedtravellers = property.getProperty("travellers1");
        expectedcabinClass = property.getProperty("class1");
        
        //wait for the flight search section to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.kayak.android:id/searchTypeSpinner")));  
        driver.findElement(By.id("com.kayak.android:id/searchTypeSpinner")).click();
        driver.findElement(By.name("One-way")).click();
        
        
        //Select "from" field and enter value
        driver.findElement(By.id("com.kayak.android:id/originLayout")).click();
        driver.findElement(By.id("com.kayak.android:id/smartySearchText")).sendKeys(fromCode);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text='San Jose, CA']")));  
        driver.findElement(By.xpath("//android.widget.TextView[@text='"+from+"']")).click();
        
        //Select "to" field and enter value
        driver.findElement(By.id("com.kayak.android:id/destinationLayout")).click();
        driver.findElement(By.id("com.kayak.android:id/smartySearchText")).sendKeys(toCode);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text='New York, NY']")));  
        driver.findElement(By.xpath("//android.widget.TextView[@text='"+to+"']")).click();
        
        //Select date
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.kayak.android:id/datesRow")));  
        driver.findElement(By.id("com.kayak.android:id/datesRow")).click();
        if(driver.findElements(By.name(expectedMonthYear)).size()>0)
        {
        	 driver.findElement(By.name(expectedDate)).click();
        }
        else
        {
        driver.scrollTo(expectedMonthYear);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(expectedMonthYear)));   
        driver.findElement(By.name(expectedDate)).click();
        }
        
        //select number of passengers
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.kayak.android:id/passengersCabinClassRow")));
        System.out.println("TEXT IN PAS:" +driver.findElement(By.id("com.kayak.android:id/passengersCabinClassRow")).getText());
        
        driver.findElement(By.id("com.kayak.android:id/passengersCabinClassRow")).click();
        
        WebElement adultRow = driver.findElement(By.id("com.kayak.android:id/flight_ptc_traveler_adults"));
        WebElement plus = adultRow.findElement(By.id("com.kayak.android:id/ptc_increment"));
        plus.click();
        
        driver.findElement(By.id("com.kayak.android:id/ptc_done")).click();
             
       //click on "Find Flights"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.kayak.android:id/searchButton"))); 
        driver.findElement(By.id("com.kayak.android:id/searchButton")).click();
    }

    @Test
    public void verifyTitle() throws InterruptedException
    {
        String title = driver.findElement(By.className("android.widget.TextView")).getText();
        System.out.println("Title" +title);
        String expectedTitle = property.getProperty("title1");
        System.out.println("Expected title:" +expectedTitle);
        if(title.equalsIgnoreCase(expectedTitle))
        {
        	System.out.println("Correct flights returned");
        }
        else
        	System.out.println("Incorrect flights returned");   
        
        Assert.assertEquals(title, expectedTitle);
    }

    @Test
    public void verifyDates() throws InterruptedException
    {
        
        String dates = driver.findElement(By.id("com.kayak.android:id/dates")).getText();
        String expectedDates = expectedMonth + " " +expectedDate;
        if(dates.equals(expectedDates))
        {
        	System.out.println("Correct dates returned");
        }
        else
        	System.out.println("Incorrect dates returned");
           
        Assert.assertEquals(dates,expectedDates);
    }

    @Test
    public void verifyTravellers() throws InterruptedException
    {
        
        String travellers = driver.findElement(By.id("com.kayak.android:id/travelers")).getText();
        
        if(travellers.equals(expectedtravellers))
        {
        	System.out.println("Correct passengers returned");
        }
        else
        	System.out.println("Incorrect passengers returned");
        
        Assert.assertEquals(travellers, expectedtravellers);
    }
    
    @Test
    public void verifyCabinClass() throws InterruptedException
    {
        
        String cabinClass = driver.findElement(By.id("com.kayak.android:id/cabinClass")).getText();
        
        if(cabinClass.equals("Economy"))
        {
        	System.out.println("Correct cabin class returned");
        }
        else
        	System.out.println("Incorrect cabin class returned");
     
        Assert.assertEquals(cabinClass, "Economy");
    }
    
    @Test
    public void verifySearchType() throws InterruptedException
    {
        
        String searchType = driver.findElement(By.id("com.kayak.android:id/searchType")).getText();
        
        if(searchType.equals("One-way"))
        {
        	System.out.println("Correct search type returned");
        }
        else
        	System.out.println("Incorrect search type returned");
        
        Assert.assertEquals(searchType, "One-way");
    }
}