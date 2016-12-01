package kayak.project.testing;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.ScreenOrientation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class CheckOrientation {

	private static RemoteWebDriver driver;
	private static Properties capabilitiesValues;
	private DesiredCapabilities capabilities;

	/**
	 * Initial Set up to set the capabilities and load the mobile app.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Before
	public void setUp() throws FileNotFoundException, IOException {

		try {
			capabilities = DesiredCapabilities.android();
			capabilitiesValues = new Properties();
			capabilitiesValues.load(new FileInputStream("testdata/capabilities.properties"));
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "");

			// Set android deviceName desired capability. Set it Android
			// Emulator.
			capabilities.setCapability("deviceName", "TA44909IZI");

			// Set your emulator's android version.
			capabilities.setCapability("platformVersion", "5.1");

			// Set android platformName desired capability. It's Android in our
			// case
			// here.
			capabilities.setCapability("platformName", "Android");

			// Set android appPackage desired capability.
			capabilities.setCapability("appPackage", "com.kayak.android");

			// Set android appActivity desired capability.
			capabilities.setCapability("appActivity", "com.facebook.FacebookActivity");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test1 : Check if the number of eleemnts in the landscape as well as
	 * portrait mode are same.
	 */
	@Test
	public void test1CheckIfAllElementsExist() {
		try {
			System.out.println("**************************************************");
			System.out.println("             Test Case No: 1\n");
			// Created object of RemoteWebDriver will all set capabilities.
			driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			closeButton();
			clickLooksGoodBtn();
			checkIfHomeScreen();
			System.out.println("--Current screen orientation Is : " + ((AndroidDriver) driver).getOrientation());
			checkIfAllElementsExists();
			((AndroidDriver) driver).rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE);
			System.out.println("--Current screen orientation Is : " + ((AndroidDriver) driver).getOrientation());
			//closeUpdateButton();
			checkIfAllElementsExists();
			System.out.println("             Result: Test Case No: 1 Passed\n"
					+ "**************************************************\n");
		} catch (Exception ex) {
			System.out.println("Unexpected error occured!");
			System.out.println("********************\n" + "             Test Case No: 1\n" + "             Result: Test Case Failed\n"
					+ "********************\n");
			ex.printStackTrace();
		}
	}

	/**
	 * Check if the data is persisted when we change the orientation.
	 */
	@Test
	public void test2CheckIfDataisPersisted() {
		try {
			// closeUpdateButton();
			// System.out.println("Test 3 --Current screen orientation Is : " +
			// ((AndroidDriver) driver).getOrientation());
			System.out.println("**************************************************");
			System.out.println("             Test Case No: 2\n");
			((AndroidDriver) driver).rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT);

			String roomGuestslayout = "com.kayak.android:id/roomsGuestsRow";
			WebElement roomGuestsElement = driver.findElementById(roomGuestslayout);

			if (roomGuestsElement.isDisplayed()) {
				roomGuestsElement.click();
				// increment rooms
				String incrementrooms = "com.kayak.android:id/incrementRooms";
				WebElement incrementroomsElement = driver.findElementById(incrementrooms);
				if (incrementroomsElement.isDisplayed()) {
					incrementroomsElement.click();
//					System.out.println("Increment rooms clicked");
				} else {
					System.out.println(" Increment Rooms Element Textview is Hidden");
				}
				// Increment Guests value

				String incrementGuests = "com.kayak.android:id/incrementGuests";
				WebElement incrementGuestsElement = driver.findElementById(incrementGuests);
				if (incrementGuestsElement.isDisplayed()) {
					incrementGuestsElement.click();
				} else {
					System.out.println(" Increment Guests Element Textview is Hidden");
				}

				// Click OK after incrementing values
				String okbtn = "android:id/button1";
				WebElement okbtnElement = driver.findElementById(okbtn);
//				System.out.println("OK clicked");
				okbtnElement.click();

				// Get the values just SET
				String originalroomguestvalue = getRoomGuestValues();
				System.out.println("Original room-guest Value: " + originalroomguestvalue);
				// Now change the orientation.
				System.out.println("Now changing the screen orientation");
				org.openqa.selenium.ScreenOrientation currorientation = ((AndroidDriver) driver).getOrientation();
				switch (currorientation) {
				case PORTRAIT:
					((AndroidDriver) driver).rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE);
					Thread.sleep(5000);
				case LANDSCAPE:
					((AndroidDriver) driver).rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT);
					Thread.sleep(5000);
				}

				String rotatedroomguestvalue = getRoomGuestValues();
				System.out.println("Rotated room-guest Value: " + rotatedroomguestvalue);

				// now extract the values to check if they are retained.
				Assert.assertEquals(originalroomguestvalue, rotatedroomguestvalue);
			} else {
				System.out.println("Room guests Textview is Hidden\n");
			}
			System.out.println("             Result: Test Case No: 2 Passed\n"
					+ "**************************************************\n");
		} catch (Exception e) {
			System.out.println("********************\n" + "             Test Case No: 2\n" + "             Result: Test Case Failed\n"
					+ "********************\n");
			System.out.println("Error in Test2 : CheckIfDataisPersisted");
			e.printStackTrace();
		}

	}

	/**
	 * Quit the driver after use.
	 */
	@After
	public void closureActivities() {
		try {
			driver.quit();
		} catch (Exception ex) {
			System.out.println("Error while quitting the driver ");
			ex.printStackTrace();
		}
	}

	/**
	 * Helper method. Close the update-apk button dialog.
	 */
	public void closeUpdateButton() {
		try {
			ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
			exec.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					WebElement latrBtn = driver.findElement(By.id("android:id/button2"));
					if (latrBtn.isDisplayed()) {
						WebElement laterBtn = driver.findElement(By.id("android:id/button2"));
						laterBtn.click();
					} else {
						System.out.println("Later button not found");
					}
				}
			}, 0, 4, TimeUnit.SECONDS);
		} catch (Exception e) {
			System.out.println("Error in closeUpdateButton");
			e.printStackTrace();
		}

	}

	/**
	 * Helper method.Check if all the elements exists in landscape as well as
	 * portrait method.
	 * 
	 * @throws NoSuchElementException
	 */
	private void checkIfAllElementsExists() throws NoSuchElementException {
		try {
			HashMap<String, Boolean> elements = new HashMap<String, Boolean>();
			String drawerlayout = "com.kayak.android:id/navigation_drawer_activity_drawer_layout";
			WebElement drawerElement = driver.findElementById(drawerlayout);
			if (drawerElement.isDisplayed()) {
				elements.put("navigation_drawer", true);
			} else {
				elements.put("navigation_drawer", false);
			}

			String toolbarlayout = "com.kayak.android:id/toolbar";
			WebElement toolbarElement = driver.findElementById(toolbarlayout);
			if (toolbarElement.isDisplayed()) {
				elements.put("toolbar", true);
			} else {
				elements.put("toolbar", false);
			}

			String destinationlayout = "com.kayak.android:id/destinationLayout";
			WebElement destinationElement = driver.findElementById(destinationlayout);
			if (destinationElement.isDisplayed()) {
				elements.put("destinationLayout", true);
			} else {
				elements.put("destinationLayout", false);
			}

			String dateslayout = "com.kayak.android:id/datesRow";
			WebElement datesElement = driver.findElementById(dateslayout);
			if (datesElement.isDisplayed()) {
				elements.put("datesRow", true);
			} else {
				elements.put("datesRow", false);
			}

			String roomGuestslayout = "com.kayak.android:id/roomsGuestsRow";
			WebElement roomGuestsElement = driver.findElementById(roomGuestslayout);
			if (roomGuestsElement.isDisplayed()) {
				elements.put("roomsGuestsRow", true);
			} else {
				elements.put("roomsGuestsRow", false);
			}

			String searchButtonlayout = "com.kayak.android:id/searchButton";
			WebElement searchButtonElement = driver.findElementById(searchButtonlayout);
			if (searchButtonElement.isDisplayed()) {
				elements.put(searchButtonElement.getText(), true);
			} else {
				elements.put(searchButtonElement.getText(), false);
			}
			// System.out.println(elements);
			for (Entry<String, Boolean> entry : elements.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
			System.out.println();
			elements.clear();
		} catch (Exception e) {
			System.out.println("Error in checkIfAllElementsExists");
			e.printStackTrace();
		}

	}

	/**
	 * Helper method. Gets the values set in the Guest and room text view.
	 * Function is called in both the orientation modes.
	 * 
	 * @return
	 */
	private String getRoomGuestValues() {
		String originalroomguestvalue = null;
		try {
			String scrollbar = "android.widget.ScrollView";
			WebElement li = driver.findElementByClassName(scrollbar);
			List<WebElement> llelements = li.findElements(By.className("android.widget.TextView"));
			for (WebElement element : llelements) {
				if (element.getAttribute("name").toString().contains("Room")) {
					originalroomguestvalue = element.getAttribute("name").toString();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error in checkIfAllElementsExists");
			e.printStackTrace();
		}
		return originalroomguestvalue;
	}

	/**
	 * Helper method. Clicks and closes the Looks Good Button seen at the
	 * beginning otf the app.
	 * 
	 * @throws NoSuchElementException
	 */
	private void clickLooksGoodBtn() throws NoSuchElementException {
		try {
			String looksGoodBtn = "android:id/button1";
			WebElement looksGoodView = driver.findElement((By.id(looksGoodBtn)));
			if (looksGoodView.isDisplayed()) {
//				System.out.println("LooksGood present");
				looksGoodView.click();
			} else {
				System.out.println("LooksGood absent");
			}
		} catch (Exception e) {
			System.out.println("Error in clickLooksGoodBtn");
			e.printStackTrace();
		}
	}

	/**
	 * Helper method. Checks if the home-screen has opened correctly as expected
	 * or not.
	 * 
	 * @throws NoSuchElementException
	 */
	private void checkIfHomeScreen() throws NoSuchElementException {
		try {
			String homeScreenToolbar = "com.kayak.android:id/toolbar";
			WebElement homeToolBar = driver.findElement((By.id(homeScreenToolbar)));
			if (homeToolBar.isDisplayed()) {
//				System.out.println("HomeScreen present");
			} else {
				System.out.println("HomeScreen absent");
			}
		} catch (Exception e) {
			System.out.println("Error in checkIfHomeScreen");
			e.printStackTrace();
		}
	}

	/**
	 * Helper method. Closes the close button seen in the main activity of the
	 * application at top left.
	 */
	private void closeButton() {
		try {
			String crossimgbtn = "com.kayak.android:id/closeBtn";
			WebElement crsbtn = null;
			try {
				crsbtn = driver.findElement((By.id(crossimgbtn)));
			} catch (NoSuchElementException nse) {
				System.out.println("No close button found. Throwing ==> " + nse);
			}
			if (crsbtn.isDisplayed()) {
//				System.out.println("Crossbtn present");
				crsbtn.click();
			} else {
				System.out.println("Crossbtn absent");
			}
		} catch (Exception e) {
			System.out.println("Error in closeButton");
			e.printStackTrace();
		}

	}
}