package kayak.project.testing;
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

public class StaticContentAndNavigationTesting {
	
	private static AndroidDriver<WebElement> driver;
	private static Properties testData;
	private static Properties capabilitiesValues;
	private static DesiredCapabilities capabilities;
	static HashMap<String,List<String>> navigationMap = new HashMap<>();
	static String[] expectedList = {"Explore", "Flight Tracker","Price Alerts", "Search", "Trips","Settings", "Directory"};
	

	
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
		try {
			driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		WebElement e = driver.findElementByAndroidUIAutomator("new UiSelector().description(\"Open navigation drawer\")");
		e.click();
		
		List<WebElement> textElements =  driver.findElements(By.className("android.widget.TextView"));
		for(WebElement te:textElements)
		{
			staticSet.add(te.getAttribute("text"));
		}
		
		for(int i =0; i <expectedList.length;i++)
		{
			if(staticSet.contains(expectedList[i]))
				System.out.println("Static Content Testing Passed "+expectedList[i]);
			else
				System.out.println("Static Content Testing Failed "+expectedList[i]);
		}
		
		driver.quit();
	}
		
		public static void menuNavigationTesting()
		{
			
			setUp();
			buildMap();
			try {
				driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			System.out.println(navigationMap);
			for(Map.Entry<String,List<String>> me : navigationMap.entrySet())
			{
				WebElement e = driver.findElementByAndroidUIAutomator("new UiSelector().description(\"Open navigation drawer\")");
				e.click();
				
				WebElement we = driver.findElementByAndroidUIAutomator("new UiSelector().text(\""+me.getKey()+"\")");
				we.click();
				
				System.out.println("clicked "+me.getKey());
				WebElement toolbar;
				
				if(me.getKey().equalsIgnoreCase("Search")||(me.getKey().equalsIgnoreCase("Directory")))
				{
					toolbar = driver.findElement(By.className("android.widget.LinearLayout")); 
				}
				else
				{
					toolbar = driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.kayak.android:id/toolbar\")");
				}	
					List<String> list = me.getValue();
					List<WebElement> textElements = toolbar.findElements(By.className("android.widget.TextView"));
					int i =0;
					for(WebElement te : textElements)
					{
						if(i<list.size())
						{
							System.out.println("-->attribute "+te.getAttribute("text"));
							if(te.getAttribute("text").equalsIgnoreCase(list.get(i)));
							i++;
						}	
					}
					System.out.println(me.getKey()+" completed");
				
			}
			driver.quit();
		}
		
		public static void main(String[] args)
		{
			menuStaticTesting();
			menuNavigationTesting();
			
		}
		
}
