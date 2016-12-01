package kayak.project.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class LocalizationCurrencyTest {
	WebDriver driver;
	 @Test
	public void setUp() throws Exception {
		// Created object of DesiredCapabilities class.
		DesiredCapabilities capabilities = new DesiredCapabilities();
		// Set android deviceName desired capability. Set it Android Emulator.
		capabilities.setCapability("deviceName", "emulator-5554");
		// Set browserName desired capability. It's Android in our case here.
		// Set android platformVersion desired capability. Set your emulator's
		// android version.
		capabilities.setCapability("platformVersion", "5.1");
		// Set android platformName desired capability. It's Android in our case
		// here.
		capabilities.setCapability("platformName", "Android");
		// Set android appPackage desired capability. It is
		// com.android.calculator2 for calculator application.
		// Set your application's appPackage if you are using any other app.
		capabilities.setCapability("appPackage", "com.kayak.android");
		// Set android appActivity desired capability. It is
		// com.android.calculator2.Calculator for calculator application.
		// Set your application's appPackage if you are using any other app.
		capabilities.setCapability("appActivity", "com.facebook.FacebookActivity");
		// Created object of RemoteWebDriver will all set capabilities.
		// Set appium server address and port number in URL string.
		// It will launch calculator app in emulator.
		driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		Properties prop = new Properties();
		//load properties file
		File file = new File("testData/inputData.properties");
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
				try {
					//Properties file loaded successfully!!
					prop.load(fileInput);
				} catch (IOException e) {
					e.printStackTrace();
				}
		//This hashmap stores countries and their currencies
		HashMap<String, String> countryCurrency = new HashMap<String, String>();
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		//Code for handling Update Kayak site pop up after every 4 seconds
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
		  @Override
		  public void run() {
			  //Finding Update Later Button by id
		   WebElement latrBtn=driver.findElement(By.id("android:id/button2"));
		   if(latrBtn.isDisplayed())
		   {
			   //Click the later button
			   WebElement laterBtn=driver.findElement(By.id("android:id/button2"));
			   laterBtn.click();
		   }
		   else{
			   System.out.println("Later button not found");
		   }
		  }
		}, 0, 4, TimeUnit.SECONDS);
        //Make the driver wait for 100 milliseconds
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		try
		{
		WebElement closebtn=driver.findElement(By.id("com.kayak.android:id/closeBtn"));
		//Close button is clicked
		closebtn.click();
		}
		catch(NoSuchElementException e)
		{
		}
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		try
		{
			//Looks good button in the pop up is clicked
			WebElement looksGoodbtn=driver.findElement(By.id("android:id/button1"));
			looksGoodbtn.click();
		}
		catch(NoSuchElementException e)
		{
		}
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		try
		{
			//Flight tab button is clicked
		WebElement flightTab=driver.findElement(By.className("android.support.v7.a.d"));
		flightTab.click();
		}
		catch(NoSuchElementException e){

		}
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		try{
			//Navigation bar button is clicked
		WebElement navigationBtn=driver.findElement(By.name("Open navigation drawer"));
		navigationBtn.click();
		}
		catch(NoSuchElementException e)
		{
		}
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		try{
			WebElement scrollViewImage=driver.findElement(By.id("com.kayak.android:id/navigation_drawer_activity_navigation_view"));
			//List of Linear Layout is retrieved from the navigation bar
			List<WebElement> childElements=scrollViewImage.findElements(By.className("android.widget.LinearLayout"));
			//Seventh element is clicked from the navigation bar list which is the Settings button
			WebElement mainElement = childElements.get(7);
			WebElement settingsLink=mainElement.findElement(By.id("com.kayak.android:id/navigation_drawer_row_text"));
			settingsLink.click();
			}
			catch(NoSuchElementException e)
			{
				
			}
		
		try{
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			try{
				//Site text field is clicked to select country site from the list
				WebElement siteElement=driver.findElement(By.name("Site"));
				siteElement.click();
				}
				catch(NoSuchElementException e)
				{
					
				}
			//Creating a set of properties which is a set of country kayak sites
			Set<String> propertyNames = prop.stringPropertyNames();
			for (String Property : propertyNames) {
				//Country specific kayak sites and their currency is added to the hashmap
				//Hashmap is generated
				 countryCurrency.put(Property, prop.getProperty(Property));
				 }
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			int testCaseNo=1;
			for(String Property : propertyNames) {
				System.out.println("************************************");
				System.out.println("Test Case No: "+testCaseNo);
			WebElement listKayakSites=driver.findElement(By.id("android:id/select_dialog_listview"));
			//Getting list of Linear layouts for kayak sites
			List<WebElement> listCountries=listKayakSites.findElements(By.className("android.widget.LinearLayout"));
		//Selecting kayak site for teh country
			System.out.println("Property name is "+Property);
			WebElement selectedElement=driver.findElement(By.name(Property));
			System.out.println("\tCountry site selected: "+selectedElement.getText());
			//Selecting teh country name
			selectedElement.click();
			WebElement scrollView=driver.findElement(By.id("com.kayak.android:id/settingsFragment"));
			//Getting list of Linear layouts for kayak sites
			List<WebElement> listFields=scrollView.findElements(By.className("android.widget.LinearLayout"));
			//Selecting the currency field which is the 4th Linear Layout in that page
			WebElement currencyField=listFields.get(4);
			WebElement currencyFieldValue=currencyField.findElement(By.id("com.kayak.android:id/textvalue"));
			Iterator<Entry<String, String>>iterator=countryCurrency.entrySet().iterator();
			while(iterator.hasNext()){
				  final Entry<String, String>next=iterator.next();
				  if(next.getKey().equals(selectedElement.getText()))
				  {
					  System.out.println("\tCurrency found - "+currencyFieldValue.getText());
					  if(next.getValue().equals(currencyFieldValue.getText()))
							  {
						  System.out.println("\tOutput: Currency for the selected country site is valid");
							  }
					  else
					  {
						  System.out.println("\tBug: Currency for the selected country site does not match the expected value");;
					  }
				  }
				}
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			try{
				WebElement siteElement=driver.findElement(By.name("Site"));
				siteElement.click();
				}
				catch(NoSuchElementException e)
				{
					
				}
			testCaseNo++;
			}	
		}
			catch(NoSuchElementException e)
			{
				System.out.println("Exception thrown: Country link not found");
			}
		driver.quit();
}
	
}
