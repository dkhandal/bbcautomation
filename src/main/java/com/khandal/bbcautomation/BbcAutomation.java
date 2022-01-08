package com.khandal.bbcautomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class BbcAutomation {
	
	  // WebDriver object
	  static BbcAutomation bbcAutomation;
	  static WebDriver driver;
	  static long timeOutInSeconds = 180; //3 minute
//	  static long timeOutSecuringCC = 15000; // 15 seconds
	  static boolean isHeadlessRequired = false; // Please true if headless is required
	  static boolean isDisableFirefoxLog = true; // If this is true then will now show firefox logs otherwise will show.
	  private static String OS = System.getProperty("os.name").toLowerCase();
	  private static final String screenshotFolderPath = new File("./screenshots").getAbsolutePath();
	  // URLs
	  static String callingUrl = "https://www.bbc.com/";
	  private Workbook writeWorkbook;
	  public static final boolean IS_XLS_FILE = false;
	  public static final String FILE_NAME_WRITE_EXCEL_XLSX = "BbcAutomation.xlsx";
	  public static final String FILE_NAME_WRITE_EXCEL_XLS = "BbcAutomation.xls";
	  public static final String FILE_PATH_EXCEL = "screenshots";
	  public static final String FILE_SHEET_NAME_WRITE_EXCEL_SHEET = "Sheet1";
	  
	  public static boolean isWindows() {
	      return OS.contains("win");
	  }

	  public static boolean isMac() {
	      return OS.contains("mac");
	  }

	  public static boolean isUnix() {
	      return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
	  }

	  public static boolean isSolaris() {
	      return OS.contains("sunos");
	  }
	
	public void invokeBrowser(String mUrl) {
	    try {
	      System.out.println("Opening Browser");
	      
	      if (isWindows()) {
	          System.out.println("This is Windows");
	          System.setProperty("webdriver.gecko.driver", "gecodriver/geckodriver.exe");
	      } else if (isMac()) {
	          System.out.println("This is Mac");
	          System.setProperty("webdriver.gecko.driver", "gecodrivermac/geckodriver");
	      } else if (isUnix()) {
	          System.out.println("This is Unix or Linux");
	          System.setProperty("webdriver.gecko.driver", "gecodriver/geckodriver");
	      } else if (isSolaris()) {
	          System.out.println("This is Solaris");
	          System.setProperty("webdriver.gecko.driver", "gecodriver/geckodriver");
	      } else {
	          System.out.println("Your OS is not support!!");
	      }
	      
	      String logsTxtFile = new File(screenshotFolderPath, "logs.txt").getAbsolutePath();
	      if(isDisableFirefoxLog) { // To disable firefox log
	          System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
	          if (isUnix()) {
	        	  System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,logsTxtFile);
	          }else if (isWindows()) {
	        	  System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,logsTxtFile);
	          }else {
	        	  System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,logsTxtFile);
	          }
	      }
	      
	      if(isHeadlessRequired){ // to enable headless or not
	    	  if(driver == null) {
		          FirefoxBinary firefoxBinary = new FirefoxBinary();
		          firefoxBinary.addCommandLineOptions("--headless");
		          FirefoxOptions firefoxOptions = new FirefoxOptions();
		          //firefoxOptions.setLogLevel(FirefoxDriverLogLevel.TRACE);
		          firefoxOptions.setBinary(firefoxBinary);
	          
	        	  driver = new FirefoxDriver(firefoxOptions);
		          driver.manage().deleteAllCookies();
		          driver.manage().window().maximize();
	          }

	          System.out.println("You are running in headless mode");
	      }else{
	    	  if(driver == null) {
	    		  driver = new FirefoxDriver();
	    		  driver.manage().deleteAllCookies();
	    		  driver.manage().window().maximize();
	    	  }
	          System.out.println("You are not running in headless mode");
	      }
	      
	      driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS);
	      driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS);
	      
	      System.out.println("[Calling URL  ]---> " + mUrl);
	      driver.get(mUrl); // opening the browser with give url
	      Thread.sleep(5000); 
	      System.out.println("********************" + mUrl + "******************");
	    } catch (Exception e) {
	      System.out.println("Exception occured in invokeBrowser");
	      e.printStackTrace();
	      driver.quit();
	      System.out.println("\nTerminating/Exception Current Program due to: " + e.getLocalizedMessage());
	      System.exit(1);
	    }
	  }
	
	public static void closeBrowser() {
		driver.quit();
	}
	
	  public void readUrl() {

		    try {
		    	
		    	 //Get list of web-elements with tagName  - a
		    	 List<WebElement> allLinks = driver.findElements(By.tagName("a"));
		    	 
		    	 //Traversing through the list and printing its text along with link address
		    	 int i = 1;
		    	 for(WebElement link:allLinks){
		    		 
		    		 if(link.getAttribute("href").contains("#") || link.getAttribute("href").contains("session") || link.getAttribute("href").contains("international")) {
		    			 System.out.println("skipped content - ****************************");
		    			 i++;
		    		 }else {
			    		 String linkName = link.getText();
			    		 String linkHref = link.getAttribute("href").trim();
			    		 System.out.println(linkName + " - " + linkHref); 
			    		 
			    		 
			            	// write to excel code here
			            	try {
			            		//************** Customers sheet *************//
			            		String FILE_NAME_WRITE_EXCEL = "";
			            		if(IS_XLS_FILE) {
			            			FILE_NAME_WRITE_EXCEL = FILE_NAME_WRITE_EXCEL_XLS;
			            		}else {
			            			FILE_NAME_WRITE_EXCEL = FILE_NAME_WRITE_EXCEL_XLSX;
			            		}
			            		writeExcelUrl(FILE_PATH_EXCEL, FILE_NAME_WRITE_EXCEL, FILE_SHEET_NAME_WRITE_EXCEL_SHEET, linkHref,linkName, i);
			            		System.out.println("Data written successfully");
							} catch (Exception e) {
								e.printStackTrace();
								String STRING1 = "\nException occured while writing Customers sheet in excel and detailed message due to: " + e.getLocalizedMessage();
								break;
							}
			    		 
//			    		 bbcAutomation.invokeBrowser(linkHref);
//			    		 dependableClick(driver, By.linkText(linkHref));
//			    		 
//			    		 Thread.sleep(20000);
//			    		 
//			    		//TODO: Get Title
//				    	 String title = "";
//				    	 title = driver.getTitle();
//				    	 
//				    	 //TODO: Get Description
//				    	 String descriptionPage = "";
//				    	 WebElement descPage = isElementPresent(By.cssSelector("article__intro b-font-family-serif"));
//				    	 WebElement descPage2 = isElementPresent(By.cssSelector("ssrcss-1q0x1qg-Paragraph eq5iqo00"));
//				    	 if(descPage != null){
//				    		 descriptionPage = descPage.getText();
//				    	 }else if(descPage2 != null){
//				    		 descriptionPage = descPage2.getText();
//				    	 }else {
//				    		 descriptionPage = "";
//				    	 }
//				    	 
//				    	 JavascriptExecutor js = (JavascriptExecutor) driver; 
//				    	 js.executeScript("window.history.go(-1)");
//				    	 
//				    	 if(!linkHref.isEmpty()&& !title.isEmpty() && !descriptionPage.isEmpty()) {
//				    		 System.out.print(linkHref + " - " + title + " - " + descriptionPage);
//				    	 }
			           i++;
		    		 }
		    		 
		    		 
		    	 }

		    } catch (Exception e) {
		      e.printStackTrace();
		    }finally {
		    	closeBrowser();
			}
	  }

	  public static WebElement isElementPresent(By by) {
			//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		  	WebElement webElement = null;
		    try {
		        webElement = driver.findElement(by);
		        return webElement;
		        
		    } catch (NoSuchElementException e) {
		        return webElement;
		    }catch (Exception e){
		        return webElement;
		    }
		}

	  /**
	     * Attempts to click on an element multiple times (to avoid stale element
	     * exceptions caused by rapid DOM refreshes)
	     *
	     * @param d
	     *            The WebDriver
	     * @param by
	     *            By element locator
	     */
	    public static void dependableClick(WebDriver d, By by)
	    {
	        final int MAXIMUM_WAIT_TIME = 10;
	        final int MAX_STALE_ELEMENT_RETRIES = 5;

	        WebDriverWait wait = new WebDriverWait(d, MAXIMUM_WAIT_TIME);
	        int retries = 0;
	        while (true)
	        {
	            try
	            {
	                wait.until(ExpectedConditions.elementToBeClickable(by)).click();

	                return;
	            }
	            catch (StaleElementReferenceException e)
	            {
	                if (retries < MAX_STALE_ELEMENT_RETRIES)
	                {
	                    retries++;
	                    continue;
	                }
	                else
	                {
	                    throw e;
	                }
	            }
	        }
	    }
	    
	    public void writeExcelUrl(String filePath,String fileName,String sheetName,String href,String name,int rowCount) throws IOException{
			

	        //Create an object of File class to open xlsx file
	        File file =    new File(filePath+"//"+fileName);
	        //Create an object of FileInputStream class to read excel file
	        FileInputStream inputStream = new FileInputStream(file);
	        
	        if(IS_XLS_FILE) {
	        	if(writeWorkbook == null) {
	        		writeWorkbook = new HSSFWorkbook(inputStream); // for ".xls" file
	        	}
	        }else {
	        	if(writeWorkbook == null) {
	        		writeWorkbook = new XSSFWorkbook(inputStream); // for ".xlsx" file
	        	}
	        }
	  
		    //Read excel sheet by sheet name    
		    Sheet sheet = writeWorkbook.getSheet(sheetName);
		    
		    //Get the first row from the sheet
		    Row row = sheet.getRow(0);
		    
		    //Get the current count of rows in excel file
//		    int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
		    
		    //Create a new row and append it at last of sheet
//		    Row newRow = sheet.createRow(rowCount+1);
		    
		    Row newRow = sheet.createRow(rowCount);
		
		    //Create a loop over the cell of newly created Row
		    for(int j = 0; j < row.getLastCellNum(); j++){
		    	String columnInputValueHref = href;
		    	String columnInputValueName = name;
//		    	System.out.println("\tWriting Column " + (j+1) + " with: " + columnInputValue);
		        //Fill data in row
		        Cell cell = null;
		        if(j == 0) { // href and name
		        	cell = newRow.createCell(j,CellType.STRING);
		        	cell.setCellValue(columnInputValueHref);
		        }else if(j == 1){
		        	cell = newRow.createCell(j,CellType.STRING);
		        	cell.setCellValue(columnInputValueName);
		        }
		    }
		
		    //Close input stream
		    inputStream.close();
		    
		    //Create an object of FileOutputStream class to create write data in excel file
		    FileOutputStream outputStream = new FileOutputStream(file);
		
		    //write data in the excel file
		    writeWorkbook.write(outputStream);
		    //close output stream
		    outputStream.close();
	    }
	  
	public static void main(String[] args) {

		bbcAutomation = new BbcAutomation();
		bbcAutomation.invokeBrowser(callingUrl);
		bbcAutomation.readUrl();
		
	}

}
