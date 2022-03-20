package com.bl.hs.keyword.engine;

import com.bl.hs.keyword.base.Base;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class KeywordEngine {
    public WebDriver driver;
    public Properties properties;

    public static Workbook book;
    public static Sheet sheet;

    public Base base;
    public WebElement element;

    public final String SCENARIO_SHEET_PATH = "C:\\Users\\Altamash\\IdeaProjects\\KeywordDrivenHubspot\\src" +
            "\\main\\java\\com\\bl\\hs\\keyword\\scenarios\\hubspot.xlsx";

    public void startExecution(String sheetName) {

        String locatorName = null;
        String locatorValue = null;

        FileInputStream file = null;
        try {
            file = new FileInputStream((SCENARIO_SHEET_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            book = WorkbookFactory.create(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        sheet = book.getSheet(sheetName);
        int k = 0;
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            try {
                String locatorColumneValue = sheet.getRow(i + 1).getCell(k + 1).toString().trim(); //id=username
                if (!locatorColumneValue.equalsIgnoreCase("NA")) {
                    locatorName = locatorColumneValue.split("=")[0].trim(); //id
                    locatorValue = locatorColumneValue.split("=")[1].trim(); //username
                }

                String action = sheet.getRow(i + 1).getCell(k + 2).toString().trim();
                String value = sheet.getRow(i + 1).getCell(k + 3).toString().trim();

                switch (action) {
                    case "open browser":
                        base = new Base();
                        properties = base.init_properties();
                        if (value.isEmpty() || value.equals("NA")) {
                            driver = base.init_driver(properties.getProperty("browser"));
                        } else {
                            driver = base.init_driver(value);
                        }
                        break;

                    case "enter url":
                        if (value.isEmpty() || value.equals("NA")) {
                            driver.get(properties.getProperty("url"));
                        } else {
                            driver.get(value);
                        }
                        break;

                    case "quit":
                        driver.quit();
                        break;

                    default:
                        break;
                }

                switch (locatorName) {
                    case "id":
                        element = (WebElement) driver.findElements(By.id(locatorValue));
                        if (action.equalsIgnoreCase("sendkeys")) {
                            element.clear();
                            element.sendKeys(value);
                        } else if (action.equalsIgnoreCase("click")) {
                            element.click();
                        }
                        locatorName = null;
                        break;

                    case "linkText":
                        element = (WebElement) driver.findElements(By.linkText(locatorValue));
                        element.click();
                        locatorName = null;
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
            }
        }
    }
}
