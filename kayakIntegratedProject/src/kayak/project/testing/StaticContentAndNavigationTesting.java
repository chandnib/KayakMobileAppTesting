package kayak.project.testing;
//import required libraries
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
 * Author---Chandni Balchandani
 * This program checks the static content of the menu of this application
 * And ensures that the application navigates to appropriate activities when user clicks on a menu item
 */
public class StaticContentAndNavigationTesting {
	
	private static AndroidDriver<WebElement> driver;
	private static Properties testData;
	private static Properties capabilitiesValues;
	private static DesiredCapabilities capabilities;
	static HashMap<String,List<String>> navigationMap = new HashMap<>();
	//expected Static Content of the Menu
	static String[] expectedList = {"Explore", "Flight Tracker","Price Alerts", "Search", "Trips","Settings", "Directory"};
	

	//setup includes setting desired capabilities 
	public static void setUp() {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	//Hashmap which populates the expected Menu items and the list of elements which should be present in that menu
	public static void buildMap()
	{
		List<String> elements1 = new ArrayList<>();
		elements1.add("Hotels");
		elements1.add("Flights");
		elements1.add("Cars");
		navigationMap.put("Search",elements1 );
		List<String> elements2 = new ArrayList<>();
		elements2.add("Price Alerts");
		navigationMap.put("Price Alerts",elements2);
		List<String> elements3 = new ArrayList<>();
		elements3.add("Trips");
		navigationMap.put("Trips",elements3);
		List<String> elements4 = new ArrayList<>();
		elements4.add("My Flights");
		navigationMap.put("Flight Tracker",elements4); 
		List<String> elements5 = new ArrayList<>();
		elements5.add("Explore");
		navigationMap.put("Explore",elements5);
		List<String> elements6 = new ArrayList<>();
		elements6.add("AIRLINES");
		elements6.add("AIRPORTS");
		navigationMap.put("Directory",elements6);
		List<String> elements7 = new ArrayList<>();
		elements7.add("Settings");
		navigationMap.put("Settings", elements7);
	}
	
	public static void menuStaticTesting()
	{
		HashSet<String> staticSet = new HashSet<>();
		setUp();
		//initialization of Android driver
		try {
			driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		//opening navigation drawer
		WebElement e = driver.findElementByAndroidUIAutomator("new UiSelector().description(\"Open navigation drawer\")");
		e.click();
		
		//collect all static content
		List<WebElement> textElements =  driver.findElements(By.className("android.widget.TextView"));
		for(WebElement te:textElements)
		{
			staticSet.add(te.getAttribute("text"));
		}
		
		//check if it matches the expected list
		System.out.println("***************************************STATIC CONTENT TESTING******************************************");
		System.out.println("Started Static Content Testing");
		for(int i =0; i <expectedList.length;i++)
		{
			System.out.println("Checking for "+expectedList[i]);
			if(staticSet.contains(expectedList[i]))
			{
				System.out.println("Successful");
			}	
			else
				System.out.println("Failed");
		}
		System.out.println("********************************STATIC CONTENT TESTING PASSED*************************************************");
		//close the driver
		driver.quit();
	}
		
		public static void menuNavigationTesting()
		{
			setUp();
			//build hashmap for menu item and a list of text in it
			buildMap();
			//initializing the android driver
			try {
				driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			System.out.println("*******************************************NAVIGATION TESTING**************************************");
			System.out.println("Printing Menu Contents \n"+navigationMap);
			//for every element in Map
			for(Map.Entry<String,List<String>> me : navigationMap.entrySet())
			{
				//open navigation drawer
				WebElement e = driver.findElementByAndroidUIAutomator("new UiSelector().description(\"Open navigation drawer\")");
				e.click();
				//find menu item and click it
				WebElement we = driver.findElementByAndroidUIAutomator("new UiSelector().text(\""+me.getKey()+"\")");
				we.click();
				
				System.out.println("clicked "+me.getKey());
				WebElement toolbar;
				//different elements for Search and Directory
				if(me.getKey().equalsIgnoreCase("Search")||(me.getKey().equalsIgnoreCase("Directory")))
				{
					//get elements on the toolbar
					toolbar = driver.findElement(By.className("android.widget.LinearLayout")); 
				}
				else
				{
					//get elements on the toolbar
					toolbar = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.kayak.android:id/toolbar\")");
				}	
					List<String> list = me.getValue();
					List<WebElement> textElements = toolbar.findElements(By.className("android.widget.TextView"));
					int i =0;
					//print the text on it
					for(WebElement te : textElements)
					{
						if(i<list.size())
						{
							//text elements on toolbar
							System.out.println("-->attribute "+te.getAttribute("text"));
							if(te.getAttribute("text").equalsIgnoreCase(list.get(i)));
							i++;
						}	
					}
					System.out.println(me.getKey()+" completed");
				
			}
			System.out.println("********************************NAVIGATION TESTS SUCCESSFUL*************************************************");
			driver.quit();
		}
		
		public static void main(String[] args)
		{
			menuStaticTesting();
			menuNavigationTesting();
			
		}
		
}
