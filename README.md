# Sauce Demo Mobile – Appium POM Framework
# Sauce Demo Mobile – Appium POM Framework

A professional **Page Object Model (POM)** test automation framework
for the **Sauce Demo** React Native Android application.

Built with: **Appium 2 · TestNG · Java 11 · Gradle (Groovy DSL)**

---

## Project Structure

```
SauceDemoMobileFramework/
├── app/src/main/
│   ├── java/com/saucedemo/
│   │   ├── base/           – BaseTest (driver setup) + BasePage (common element actions)
│   │   ├── pages/          – Page Object classes (LoginPage, ProductPage, CartPage, MenuPage)
│   │   ├── tests/          – Test classes (LoginTest, CartTest, LogoutTest, ProductTest)
│   │   ├── listeners/      – 8 TestNG Listeners
│   │   └── utils/          – Helper utilities (config, driver, waits, screenshots, video)
│   └── resources/
│       ├── config.properties          – All configuration (device, credentials, timeouts)
│       └── testng-suites/
│           ├── MasterSuite.xml        – All tests
│           ├── SmokeSuite.xml         – Smoke tests only
│           ├── RegressionSuite.xml    – Full regression
│           └── LoginSuite.xml         – Login tests only
├── reports/
│   ├── extent-report/ExtentReport.html  – HTML report (generated after run)
│   ├── screenshots/passed/ + failed/    – PNG screenshots
│   ├── videos/passed/ + failed/         – MP4 recordings
│   └── logs/execution.log               – Text execution log
├── build.gradle      – Groovy DSL
├── settings.gradle
└── gradle.properties
```

---

## Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 11+ | https://adoptium.net |
| Android Studio | Latest | https://developer.android.com/studio |
| Appium Server | 2.x | `npm install -g appium` |
| Appium UiAutomator2 Driver | Latest | `appium driver install uiautomator2` |
| Android Emulator | API 34 (Android 14) | Android Studio AVD Manager |
| Node.js | 18+ | https://nodejs.org |

---

## Step-by-Step Setup

### 1 – Clone / Open the project
Open Android Studio → **File → Open** → select this folder.
Wait for Gradle sync to complete.

### 2 – Start the Android Emulator
In Android Studio → **Device Manager** → Start **emulator-5554** (Pixel 6 / API 34).

### 3 – Install the Sauce Demo APK
Download from: https://github.com/saucelabs/my-demo-app-rn/releases
```
adb install MyDemoAppRN.apk
```

### 4 – Start Appium Server
Open a terminal and run:
```
appium
```
Appium will listen on http://127.0.0.1:4723

### 5 – Run the tests

#### From Android Studio Terminal:
```bash
# Run all tests (Master Suite)
./gradlew test

# Run only Smoke tests (fast sanity check)
./gradlew smokeSuite

# Run full Regression
./gradlew regressionSuite

# Run Login Suite only
./gradlew loginSuite

# Run Master Suite explicitly
./gradlew masterSuite
```

#### From Android Studio:
- Right-click any `testng-suites/*.xml` → **Run**
- Or right-click any test class → **Run**

---

## Test Cases

### Login Tests (`LoginTest.java`)
| ID | Name | Type | Expected Result |
|----|------|------|-----------------|
| TC_LOGIN_01 | Valid credentials | Positive | Home screen loads ✅ |
| TC_LOGIN_02 | Invalid password | Negative | Error shown / login rejected ✅ |
| TC_LOGIN_03 | Invalid email | Negative | Error shown / login rejected ✅ |
| TC_LOGIN_04 | Blank credentials | Negative | Validation error / login rejected ✅ |

> **Negative tests:** A negative test PASSES when the app correctly REJECTS bad input.
> If the error message is shown, the assertion is true → TestNG marks PASS.

### Cart Tests (`CartTest.java`)
| ID | Name | Type |
|----|------|------|
| TC_CART_01 | Add product to cart | Smoke |
| TC_CART_02 | Cart item listed | Regression |
| TC_CART_03 | Remove item from cart | Regression |

### Product Tests (`ProductTest.java`)
| ID | Name | Type |
|----|------|------|
| TC_PRODUCT_01 | Product listing displayed | Smoke |
| TC_PRODUCT_02 | Product detail screen opens | Regression |
| TC_PRODUCT_03 | Quantity counter increment | Regression |

### Logout Tests (`LogoutTest.java`)
| ID | Name | Type |
|----|------|------|
| TC_LOGOUT_01 | Successful logout | Smoke |
| TC_LOGOUT_02 | Logout option visible when logged in | Regression |

---

## The 8 Listeners

| # | Listener | Purpose |
|---|----------|---------|
| 1 | `ExtentReportListener` | Generates HTML report (Spark dark theme) |
| 2 | `ScreenshotListener` | Screenshots on pass and fail |
| 3 | `RetryListener` | Retries flaky tests (1 retry by default) |
| 4 | `VideoRecordingListener` | Stops screen recording, saves MP4 |
| 5 | `TestLogListener` | Writes timestamped logs to execution.log |
| 6 | `SuiteListener` | Prints suite start/end banners + summary |
| 7 | `TestTimingListener` | Measures test duration, flags slow tests |
| 8 | `NegativeTestListener` | Adds context to negative test results |

---

## Reports

After every run, open:
```
reports/extent-report/ExtentReport.html
```
The report includes:
- Pass/Fail status per test
- Screenshots embedded inline
- Video recording path
- System info (device, platform, tester)
- Test execution time

---

## Configuration

Edit `app/src/main/resources/config.properties` to change:
- Device name / UDID
- App package / activity
- Login credentials
- Wait timeouts
- Retry count

---

## Test Groups / Tags

| Group | When to use |
|-------|-------------|
| `smoke` | Quick sanity check after every build |
| `regression` | Full test pass before a release |
| `login` | Only login-related tests |
| `negative` | Tests that verify error handling |
| `cart` | Shopping cart tests |
| `product` | Product page tests |
| `logout` | Logout tests |

---

## Credentials (from config.properties)

| Credential | Username | Password |
|------------|----------|----------|
| Valid | bob@example.com | 10203040 |
| Invalid | invalid@example.com | wrongpassword |

---

## Troubleshooting

**"No such driver" error** → Run: `appium driver install uiautomator2`

**"App not found" error** → Ensure the APK is installed: `adb shell pm list packages | grep saucelabs`

**Device not found** → Start emulator first, then confirm: `adb devices`

**Port 4723 already in use** → Kill existing Appium: `pkill -f appium` or restart terminal

**Gradle sync fails** → File → Invalidate Caches and Restart in Android Studio
