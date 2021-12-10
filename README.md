# selenium-vs-playwright-webinar

This repository holds the code from the Applitools webinar,
[Selenium vs. Playwright: Let the Code Speak](https://applitools.com/selenium-vs-playwright-let-the-code-speak/).


## The battle

During this webinar,
[Angie Jones](https://twitter.com/techgirl1908) represented Selenium WebDriver,
while [Andrew Knight](https://twitter.com/AutomationPanda) represented Playwright.
The code battle had ten rounds total.
Each round pitched an automation coding challenge,
and each side presented their solution.
To keep comparisons apples-to-apples,
all code was written in Java within the context of a JUnit test class.
Then, after some discussion (and maybe a little smack talk),
the audience voted for the solution they thought was better.
All results were determined live by the audience.
Nothing was predetermined or planted.


## The code

This repository is set up as a Java Maven project.
It contains the complete code for all ten rounds:

* `src/test/java/PlaywrightTest.java` contains the Playwright examples
* `src/test/java/SeleniumTest.java` contains the Selenium WebDriver examples

You can build and run the tests locally for your own comparisons.

The Playwright tests are configured to use headless Chromium.
You must [install the browsers through the Playwright CLI](https://playwright.dev/java/docs/cli#install-browsers)
before running tests.

The Selenium WebDriver test are configured to use headless Chrome.
You must have Google Chrome installed
as well as the matching version of [ChromeDriver](https://chromedriver.chromium.org/home)
installed on your system path.
