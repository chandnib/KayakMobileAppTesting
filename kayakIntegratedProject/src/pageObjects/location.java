package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class location {
WebDriver driver;
	
	public location(WebDriver dr) {
		driver = dr;
	}
	
	
	
	public void timelapse(){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean assertlocation(String place){
		timelapse();
		//Assert.assertEquals("2 Rooms", ((WebElement) By.id("com.kayak.android:id/roomsText")).getText());
		return driver.findElement(By.xpath("//android.widget.LinearLayout[2]/android.widget.TextView")).getText().equals(place);
	}
}
