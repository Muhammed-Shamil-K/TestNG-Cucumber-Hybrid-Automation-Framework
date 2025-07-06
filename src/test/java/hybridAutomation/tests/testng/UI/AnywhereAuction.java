package hybridAutomation.tests.testng.UI;

import hybridAutomation.Pages.anywhereAction.Dashboard;
import hybridAutomation.Pages.anywhereAction.Login;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class AnywhereAuction {

    WebDriver driver = new ChromeDriver();


    @AfterClass
    public void cleanUp() {
        driver.quit();
    }

    @Test
    public void test() {
        Login loginPage =new Login(driver);
        loginPage.login("Agent");
//        Dashboard dashboard = new Dashboard(driver);
//        dashboard.selectMenuItem("Create Property Listing");
//        AddProperty addProperty = new AddProperty(driver);
//        addProperty.fillAuctionSetupForm("AutoNum", "AutoName", LocalDate.now().plusDays(5).getDayOfMonth(),
//                "07:30 pm", "agent one", "Victoria", "Sugarloaf");
//        addProperty.addPropertyPrice("12000");
//        addProperty.uploadLegalDocument("/home/qburst/Downloads/Contract document.pdf");
//        addProperty.tagDocument();
//        TagDocument tagDocument = new TagDocument(driver);
//        //tagDocument.addTags(Tag.SIGNEE_NAME, Tag.SIGN);
//        tagDocument.saveDocumentAndExit();
//        addProperty.savePropertyToDraft();
//        addProperty.exit();
//        dashboard.editProperty("AutoNum AutoName, Sugarloaf VIC 3234");
//        EditProperty editProperty = new EditProperty(driver);
//        editProperty.deleteProperty();
    }


    @Test
    public void demoTableTest() {
        Login loginPage =new Login(driver);
        loginPage.login("Agent");
        Dashboard dashboard = new Dashboard(driver);
        dashboard.selectSubMenuItem("Upcoming Auctions");
        dashboard.tableOperation();
        dashboard.selectSubMenuItem("Private Sales");
        dashboard.tableOperation2();
    }

}
