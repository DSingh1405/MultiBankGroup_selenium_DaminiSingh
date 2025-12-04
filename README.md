# **MultiBank Web Automation Framework – Selenium | Java | TestNG | Maven**

A robust, scalable, and maintainable **test automation framework** built for validating critical functionalities of the **MultiBank Web Trading Platform**.
This framework follows **Page Object Model (POM)** design pattern with **Fluent Waits**, **parallel execution**, **Jenkins CI**, and **detailed reporting**.

---

# **Key Features**

### **Page Object Model (POM)**

* Each page has its own dedicated class.
* Clean separation of locators, actions, and validations.

### **Fluent Wait Implementation**

* Custom FluentWait wrapper for stable handling of dynamic web elements.
* Avoids stale and timing-based failures.

### **Reusable BasePage**

* Scroll helpers
* Fluent waits
* Text extractors (domProperty, innerText)
* Click wrappers (scroll + JS fallback)

### **Cross-Browser Support**

Supports:

* Chrome
* Firefox *(optional / fixable)*
* Edge *(can be enabled)*

Configured through:

```xml
<parameter name="browser" value="chrome"/>
```

### **Parallel Test Execution**

Powered by TestNG:

* Parallel tests
* Parallel classes
* Parallel suites

### **Jenkins Integration**

* Pulls code from GitHub
* Runs Maven commands automatically
* Generates TestNG XML Reports + Extent Reports
* Notifies failures
* Archives artifacts

### **Extensive Modular Tests**

Includes coverage for:

* Home Page
* Navigation Menus
* Spot Trading Section
* Category Filtering
* Table Data Validation
* App Store Links
* Why Multibank Section

---

# **Project Structure**
<img width="404" height="649" alt="image" src="https://github.com/user-attachments/assets/df81a22b-2c1c-41d3-95db-a3dfe6d99384" />

# **Prerequisites**

### **Software**

* Java 17 (or higher)
* Maven 3.x
* Chrome / Firefox browsers
* WebDriverManager (auto-handles drivers)
* Jenkins (optional for CI)

### **IDE**

* IntelliJ IDEA / Eclipse

---

# **How to Run Tests Locally**

### ** Clone the Repository**

```sh
git clone https://github.com/DSingh1405/MultiBankGroup_selenium_DaminiSingh.git
cd MultiBankGroup_selenium_DaminiSingh
```

### ** Run Using Maven**

```sh
mvn clean test
```

### ** Run With TestNG Suite**

```sh
mvn clean test -DsuiteXmlFile=testng.xml
```

### ** Run Tests on a Specific Browser**

```sh
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
```

---

# **Tests Included**

### ### **Home Page Tests**

* Verify top navigation menu
* Verify context menus (Spot, Instant Buy, Panic Sell)
* Validate carousel
* Validate App Store & Play Store buttons

### **Spot Section Tests**

* Spot categories filter validation
* Trading pairs load validation
* Headers validation
* Price/High/Low/Volume data checks
* SVG indicator detection inside cells
* Pair symbol format validation (`BTC-USDT`)

### **Why MultiBank Page Tests**

* Validate CTA buttons
* Validate card titles & descriptions

---

# **Reporting**

### **TestNG Reports**

Generated under:

```
/test-output/
```

### **Extent Reports**

If enabled:

```
/ExtentReports/
```
### **Extent Report Execution Screenshot for Reference**
**Latest Local Run (04 December 2025)**

<img width="1918" height="915" alt="Extent Report_04 Dec 2025 at 140822" src="https://github.com/user-attachments/assets/44218070-df57-4c7a-8e16-110deb0cb2db" />

**Latest Local Run (04 December 2025) Chrome Category View**

<img width="1919" height="913" alt="Extent Report_ChromeCategory_04 Dec 2025 at 140822" src="https://github.com/user-attachments/assets/a773d639-9047-4e00-a6f2-bb212774f152" />

**Latest Local Run (04 December 2025) FireFox category View**

<img width="1919" height="909" alt="Extent Report_FireFoxCategory_04 Dec 2025 at 140822" src="https://github.com/user-attachments/assets/467af6b5-a3b9-4049-b3b3-e4dc30fe2943" />


### **Jenkins Reports**

Automatically archived as build artifacts.

**Latest Jenkins Snapshot (04 December 2025)**

<img width="1897" height="907" alt="Jenkins Console Snapshot_04 Dec 2025" src="https://github.com/user-attachments/assets/bd06ab19-aac7-4cb1-848f-a446f146854f" />

---

# **Design Patterns Used**

### Page Object Model (POM)

### Factory Pattern (DriverFactory)

### Singleton WebDriver Management *(optional setup)*

### Utility Classes for Waits & Extractors

### Base Test Class for setup/teardown

### Action Methods for business logic

---

# **Jenkins CI Setup**

### **1. Create a Freestyle or Pipeline job**

### **2. Add GitHub repo URL**

```
https://github.com/DSingh1405/MultiBankGroup_selenium_DaminiSingh.git
```

### **3. Set Branch**

```
*/main
```

(or your preferred branch)

### **4. Add Build Step**

```
mvn clean test
```

### **5. Post Build Actions**

* Publish report: `**/surefire-reports/*.xml`
* Archive artifacts: `/target/**`
* Email notification on failure

---
# **Implementation Limitations & Pending Enhancements**

* **Remote Execution Support (Selenium Grid / Selenoid)**
  - The framework is already designed to support RemoteWebDriver and can connect to Selenium Grid / Selenoid using a remote hub URL.
  - Currently, no Grid or Selenoid infrastructure is attached to this assignment.
  - Remote execution is handled in code and can be enabled instantly by providing:
```
Dremote=true  +  GRID_URL
```
   - For this assignment submission, execution is performed locally and on Jenkins only.

* **Cross-Browser Support (Chrome / Firefox / Edge)**
```
-Dbrowser=chrome
-Dbrowser=firefox
-Dbrowser=edge
```
    - Local system: Chrome and Firefox both run successfully.

    - Jenkins Windows node:* Tests are restricted to Chrome due to a
```
geckodriver ↔ Firefox marionette compatibility issue.
```
    - Jenkins is intentionally configured to run ensuring stability of build: 
```
mvn clean test -Dbrowser=chrome
```

* **Email Notifications (SMTP Configuration Pending)**
  - Email reporting is included in Jenkins job configuration.
  - SMTP setup is pending because Gmail requires:
    - 2-Step Verification
    - App Password
  - Due to account restrictions and time constraints, email alerts are not yet enabled.

Jenkins is ready to support SMTP once the Gmail App Password is available.
# **Known Issues / Notes/ Quick Fixes (mitigation activities)**

* Firefox driver compatibility needs verification for Jenkins Windows nodes.
- **Firefox on Jenkins (Windows node)**  
  - Locally, the framework supports both **Chrome** and **Firefox** (via the `browser` system property).  
  - On the Jenkins Windows agent, the Firefox pipeline is currently disabled due to a **geckodriver ↔ Firefox compatibility issue** (`SessionNotCreatedException: Failed to decode response from marionette`).  
  - The CI job is configured to run with `-Dbrowser=chrome` only, ensuring a stable, green pipeline while Firefox support on the node is verified and aligned.
* Some UI elements require scroll + FluentWait due to lazy loading.
- **Lazy-loaded / below-the-fold UI elements**  
  - The landing page uses **lazy loading** and components often render **below the initial viewport**.  
  - To keep the checks stable, the `BasePage` exposes a `scrollAndWaitVisible(...)` helper which:
    1. Scrolls the target element into view via JavaScript.
    2. Uses `WebDriverWait` + `ExpectedConditions.visibilityOf(...)` before assertions.  
  - All list-based visibility checks (headers, cards, Spot table, etc.) rely on this helper to avoid flakiness due to elements being off-screen.
* App Store buttons may be covered by the logo → JS fallback used.
   **App Store / Google Play buttons overlapped by header**  
  - On certain viewport sizes the responsive header (logo / mobile toggle) can partially cover the App Store buttons, causing `ElementClickInterceptedException` during a standard `.click()`.  
  - The page object methods `clickAppleAppStore()` and `clickGooglePlayStore()` first **scroll the button into view** and attempt a normal click; if the header still intercepts, they fall back to a **JavaScript click**.  
  - This keeps the tests stable while respecting the current responsive layout of the application.

---

# **Contributions**

Pull requests, bug reports, and suggestions are welcome!

---

# **Author**

**Damini Singh**
Automation Architect | QA Lead
📧 *daminisingh_1415@outlook.com*
🔗 GitHub: **DSingh1405**

---
