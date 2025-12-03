Here is a **clean, professional, production-quality README.md** tailored specifically for your **MultiBank Selenium Automation Framework** (POM + TestNG + Maven + Jenkins + Selenium Grid support).
It follows best practices and is suitable for GitHub, portfolio, interviews, and CI documentation.

---

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

### **Jenkins Reports**

Automatically archived as build artifacts.

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

* Publish JUnit report: `**/surefire-reports/*.xml`
* Archive artifacts: `/target/**`
* Email notification on failure

---

# **Known Issues / Notes**

* Firefox driver compatibility needs verification for Jenkins Windows nodes.
* Some UI elements require scroll + FluentWait due to lazy loading.
* App Store buttons may be covered by the logo → JS fallback used.

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
