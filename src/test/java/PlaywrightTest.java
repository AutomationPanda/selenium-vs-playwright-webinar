import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightTest {

    private static Playwright playwright;
    private static Browser browser;

    private BrowserContext context;
    private Page page;

    @BeforeAll
    public static void startBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()

                // Uncomment the following options in order to watch the browser as tests run:
//                        .setHeadless(false)
//                        .setSlowMo(1000)
        );
    }

    @AfterAll
    public static void closeBrowser() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    public void startPage() {
        context = browser.newContext(

                // Uncomment the following options to record videos for each test:
//                new Browser.NewContextOptions()
//                        .setRecordVideoDir(Paths.get("videos/"))
        );

        page = context.newPage();
    }

    @AfterEach
    public void closePage() {
        context.close();
    }

    @Test
    public void round01_testLogin() {
        page.navigate("https://demo.applitools.com/");

        page.fill("id=username", "andy");
        page.fill("id=password", "panda<3");
        page.click("id=log-in");

        assertThat(page.locator(".element-header >> nth=0")).hasText("Financial Overview");
    }

    @Test
    public void round02_testDropdown() {
        page.navigate("https://kitchen.applitools.com/ingredients/select");
        page.selectOption("id=spices-select-single", "ginger");
        assertThat(page.locator("id=spices-select-single")).hasValue("ginger");
    }

    @Test
    public void round03_testFileUpload() {
        Path pic = Paths.get("src", "test", "resources", "pic.jpg");
        page.navigate("https://kitchen.applitools.com/ingredients/file-picker");
        page.setInputFiles("id=photo-upload", pic);
    }

    @Test
    public void round04_testIframe() {
        page.navigate("https://kitchen.applitools.com/ingredients/iframe");
        assertThat(page.frameLocator("id=the-kitchen-table").locator("id=fruits-vegetables")).isVisible();
    }

    @Test
    public void round05_testExplicitWaiting() {
        page.navigate("https://automationbookstore.dev/");
        page.fill("id=searchBar", "testing");

        /*
         Explicitly waiting for the hidden elements to attach to the DOM is not required.
         Waiting for the non-hidden elements to reach a count of 1 inherently covers it.
         However, the code below shows how to explicitly wait for the elements to hide:

            page.locator("li.ui-screen-hidden >> nth=0").waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.HIDDEN)
                            .setTimeout(5000));
        */

        assertThat(page.locator("li:not(.ui-screen-hidden)")).hasCount(1,
                new LocatorAssertions.HasCountOptions().setTimeout(5000));
    }

    @Test
    public void round06_testAcceptAlert() {
        page.navigate("https://kitchen.applitools.com/ingredients/alert");
        page.onDialog(Dialog::accept);
        page.click("id=alert-button");

        // By default, Playwright accepts all alerts.
        // For this test, we could comment out `page.onDialog(...)`.
    }

    @Test
    public void round06_testDismissAlert() {
        page.navigate("https://kitchen.applitools.com/ingredients/alert");
        page.onDialog(Dialog::dismiss);
        page.click("id=confirm-button");
    }

    @Test
    public void round06_testAnswerPrompt() {
        page.navigate("https://kitchen.applitools.com/ingredients/alert");
        page.onDialog(dialog -> dialog.accept("nachos"));
        page.click("id=prompt-button");
    }

    @Test
    public void round07_testNewTab() {
        page.navigate("https://kitchen.applitools.com/ingredients/links");
        Page newTab = context.waitForPage(
                () -> page.click("id=button-the-kitchen-table"));
        assertThat(newTab.locator("id=fruits-vegetables")).isVisible();
    }

//    @Test
//    public void round08_testApi() {
//        // API testing will come to Playwright for Java in 1.18
//        // Below is a snippet for API testing in Python with pytest:
//
////        def test_kitchen_api_recipes(context):
////
////            response = context.request.get('https://kitchen.applitools.com/api/recipes')
////            assert response.ok
////
////            body = response.json()
////            assert body['time'] > 0
////            assert len(body['data']) > 0
//    }

    @Test
    public void round09_testScreenshots() {
        page.navigate("https://kitchen.applitools.com/ingredients/table");
        page.click("id=column-button-name");

        // Full page
        page.screenshot(
                new Page.ScreenshotOptions()
                        .setPath(Paths.get("screenshots", "fullPage.png"))
                        .setFullPage(true));

        // Individual element
        page.querySelector("id=fruits-vegetables").screenshot(
                new ElementHandle.ScreenshotOptions()
                        .setPath(Paths.get("screenshots", "individualElement.png")));
    }
}
