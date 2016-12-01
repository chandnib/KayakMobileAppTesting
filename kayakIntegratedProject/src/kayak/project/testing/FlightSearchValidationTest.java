package kayak.project.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import io.appium.java_client.android.AndroidDriver;
import java.util.Properties;

public class FlightSearchValidationTest {

	public AndroidDriver driver;
	private static Properties capabilitiesValues;
	
	Properties property = new Properties();
	String from, fromCode, to, toCode, expectedMonth, expectedMonthYear, expectedDate, expectedtravellers,
			expectedcabinClass;

	@BeforeTest
	public void prepareAndroidForAppium() throws InterruptedException, FileNotFoundException, IOException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilitiesValues.load(new FileInputStream("testdata/capabilities.properties"));
		capabilitiesValues  = new Properties();
		capabilities.setCapability("device", "Android");
		capabilities.setCapability("deviceName", capabilitiesValues.getProperty("deviceName"));
		capabilities.setCapability("platformVersion", capabilitiesValues.getProperty("platformVersion"));
		capabilities.setCapability("appPackage", "com.kayak.android");
		capabilities.setCapability("appActivity","com.facebook.FacebookActivity");
		capabilities.setCapability("platformName", "Android");

		// other capabilities
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

		// load the data file
		try {
			property.load(new FileInputStream("testData/searchData.properties"));
		} catch (IOException e) {
			System.out.println("Unable to load testData file");
		}

		System.out.println("****************************************************");
		System.out.println();
		System.out.println("*******STARTING TEST: FLIGHT SEARCH VALIDATION******");
		System.out.println();
		System.out.println("****************************************************");

	}

	// input the test data in the fields and search for flights
	@Test
	public void search() throws InterruptedException {
		System.out.println("***************STARTING TEST: Search***************");

		// Wait for the homepage to open (Flights tab should be visible)
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Flights")));

		// find the Flights tab
		driver.findElement(By.name("Flights")).click();

		// extract the data to be entered from the data file
		fromCode = property.getProperty("fromCode1");
		from = property.getProperty("fromAirport1");
		toCode = property.getProperty("toCode1");
		to = property.getProperty("toAirport1");
		expectedMonthYear = property.getProperty("monthYear1");
		expectedMonth = property.getProperty("month1");
		expectedDate = property.getProperty("date1");
		expectedtravellers = property.getProperty("travellers1");
		expectedcabinClass = property.getProperty("class1");

		// Print the test data
		System.out.println("TEST DATA:");
		System.out.println("Depart From: " + from);
		System.out.println("Arriving At: " + to);
		System.out.println("Date: " + expectedDate + ", " + expectedMonthYear);
		System.out.println("Number of travelers: " + expectedtravellers);
		System.out.println("Cabin class: " + expectedcabinClass);

		// wait for the flight search section to load
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.kayak.android:id/searchTypeSpinner")));

		// find the search type dropdown and click on it
		driver.findElement(By.id("com.kayak.android:id/searchTypeSpinner")).click();

		// find the search type fronm the dropdown and click on it
		driver.findElement(By.name("One-way")).click();

		// Select "from" field and enter value
		driver.findElement(By.id("com.kayak.android:id/originLayout")).click();
		driver.findElement(By.id("com.kayak.android:id/smartySearchText")).sendKeys(fromCode);
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text='San Jose, CA']")));
		driver.findElement(By.xpath("//android.widget.TextView[@text='" + from + "']")).click();

		// Select "to" field and enter value
		driver.findElement(By.id("com.kayak.android:id/destinationLayout")).click();
		driver.findElement(By.id("com.kayak.android:id/smartySearchText")).sendKeys(toCode);
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text='New York, NY']")));
		driver.findElement(By.xpath("//android.widget.TextView[@text='" + to + "']")).click();

		// Obtain the screen size for scrolling
		Dimension size = driver.manage().window().getSize();
		// System.out.println("SCREEN SIZE:" +size);
		int height = size.height;
		int startx = size.width / 2;
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.height * 0.20);
		// System.out.println("Height" +height+ "Startx:" +startx+ "starty"
		// +starty+ "endy" +endy);

		// Select date
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.kayak.android:id/datesRow")));

		// find the date field and click on it
		driver.findElement(By.id("com.kayak.android:id/datesRow")).click();

		// check if the month to be entered is present on screen

		// if found select the date
		if (driver.findElements(By.name(expectedMonthYear)).size() > 0) {
			// System.out.println("MONTH FOUND");
			driver.findElement(By.name(expectedDate)).click();
		} else {
			// scroll till the expected month appears on screen
			while (driver.findElements(By.name(expectedMonthYear)).size() == 0) {
				// System.out.println("Swiping");
				driver.swipe(startx, starty, startx, endy, 2000);
				// driver.scrollTo(expectedMonthYear);
			}

			// click on the date
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(expectedMonthYear)));
			driver.findElement(By.name(expectedDate)).click();
		}
		// select number of passengers
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.id("com.kayak.android:id/passengersCabinClassRow")));

		// find the cabin class field and click on it
		driver.findElement(By.id("com.kayak.android:id/passengersCabinClassRow")).click();

		// find the plus icon to increment the number of travelers
		WebElement adultRow = driver.findElement(By.id("com.kayak.android:id/flight_ptc_traveler_adults"));
		WebElement plus = adultRow.findElement(By.id("com.kayak.android:id/ptc_increment"));
		plus.click();

		// find and click on the done button
		driver.findElement(By.id("com.kayak.android:id/ptc_done")).click();

		// click on "Find Flights"
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.kayak.android:id/searchButton")));
		driver.findElement(By.id("com.kayak.android:id/searchButton")).click();

		System.out.println("Search results returned!");
		System.out.println("Test case result: PASSED");
		System.out.println("***************ENDING TEST: Search***************");
		System.out.println();

	}

	// Test to verify that the correct title is displayed based on the search
	@Test
	public void verifyTitle() throws InterruptedException {

		System.out.println("******STARTING TEST: Verify Search Result title******");

		// extract the title of the current screen
		String title = driver.findElement(By.className("android.widget.TextView")).getText();
		String expectedTitle = property.getProperty("title1");

		// print the expected and actual title
		System.out.println("Expected title:" + expectedTitle);
		System.out.println("Actual title" + title);

		// if the actual title is same as expected title, test case passes
		if (title.equalsIgnoreCase(expectedTitle)) {
			System.out.println("Correct flights returned for: " + title);
			System.out.println("Test case result: PASSED");
		} else {
			System.out.println("Incorrect flights returned");
			System.out.println("Test case result: FAILED");
		}

		System.out.println("******ENDING TEST: Verify Search Result title******");
		System.out.println();

		// if the actual title is same as expected title, test case passes
		Assert.assertEquals(title, expectedTitle);
	}

	// Test to verify that the correct dates are displayed based on the search
	@Test
	public void verifyDates() throws InterruptedException {

		System.out.println("*********STARTING TEST: Verify Result Dates*********");

		// extract the dates displayed in the search results
		String dates = driver.findElement(By.id("com.kayak.android:id/dates")).getText();
		String expectedDates = expectedMonth + " " + expectedDate;

		// print the expected and actual dates
		System.out.println("Expected dates:" + expectedDates);
		System.out.println("Actual dates" + dates);

		// if the actual dates are same as expected dates, test case passes
		if (dates.equals(expectedDates)) {
			System.out.println("Correct dates returned: " + dates);
			System.out.println("Test case result: PASSED");
		} else {
			System.out.println("Incorrect dates returned Expected:" + expectedDates + " Got " + dates);
			System.out.println("Test case result: FAILED");
		}

		System.out.println("*********ENDING TEST: Verify Result Dates*********");
		System.out.println();

		// if the actual dates are same as expected dates, test case passes
		Assert.assertEquals(dates, expectedDates);
	}

	// Test to verify that the correct number of travelers are displayed based
	// on the search
	@Test
	public void verifyTravellers() throws InterruptedException {
		System.out.println("*********STARTING TEST: Verify Travelers*********");

		// extract the number of travelers displayed in the search results
		String travellers = driver.findElement(By.id("com.kayak.android:id/travelers")).getText();

		// print the expected and actual travelers
		System.out.println("Expected travelers:" + expectedtravellers);
		System.out.println("Actual travelers: " + travellers);

		// if the actual number of travelers are same as expected number of
		// travelers, test case passes
		if (travellers.equals(expectedtravellers)) {
			System.out.println("Correct passengers returned " + travellers);
			System.out.println("Test case result: PASSED");
		} else {
			System.out.println("Incorrect passengers returned Expected:" + expectedtravellers + " Got " + travellers);
			System.out.println("Test case result: FAILED");
		}

		System.out.println("*********ENDING TEST: Verify Cabin Class*********");
		System.out.println();

		// if the actual number of travelers are same as expected number of
		// travelers, test case passes
		Assert.assertEquals(travellers, expectedtravellers);
	}

	// Test to verify that the correct cabin class is displayed based on the
	// search
	@Test
	public void verifyCabinClass() throws InterruptedException {
		System.out.println("*********STARTING TEST: Verify Cabin Class*********");

		// extract the Cabin class displayed in the search results
		String cabinClass = driver.findElement(By.id("com.kayak.android:id/cabinClass")).getText();

		// print the expected and actual cabin class
		System.out.println("Expected cabin class: Economy");
		System.out.println("Actual cabin class: " + cabinClass);

		// if the cabin class displayed in the search result is same as expected
		// cabin class, test case passes
		if (cabinClass.equals("Economy")) {
			System.out.println("Correct cabin class returned: Economy");
			System.out.println("Test case result: PASSED");
		} else {
			System.out.println("Incorrect cabin class returned Expected: Economy Got " + cabinClass);
			System.out.println("Test case result: FAILED");
		}
		System.out.println("*********ENDING TEST: Verify Cabin Class*********");
		System.out.println();

		// if the cabin class displayed in the search result is same as expected
		// cabin class, test case passes
		Assert.assertEquals(cabinClass, "Economy");
	}

	// Test to verify that the correct search type is displayed based on the
	// search
	@Test
	public void verifySearchType() throws InterruptedException {
		System.out.println("*********STARTING TEST: Verify Search Type*********");

		// extract the search type displayed in the search results
		String searchType = driver.findElement(By.id("com.kayak.android:id/searchType")).getText();

		// print the expected and actual search types
		System.out.println("Expected search type: One-way");
		System.out.println("Actual search type: " + searchType);

		// if the search type displayed in the search result is same as expected
		// search type, test case passes
		if (searchType.equals("One-way")) {
			System.out.println("Correct search type returned: One-way");
			System.out.println("Test case result: PASSED");
		} else {
			System.out.println("Incorrect search type returned Expected: One-way Got " + searchType);
			System.out.println("Test case result: FAILED");
		}
		System.out.println("*********ENDING TEST: Verify Search Type*********");
		System.out.println();

		// if the search type displayed in the search result is same as expected
		// search type, test case passes
		Assert.assertEquals(searchType, "One-way");
	}
}