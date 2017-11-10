# Android UI Test Helper

A library for making Android UI testing a little easier, which includes retrying failed tests, taking screenshots on failure, and an extended set of test lifecycle annotations.

## Configuring Your Test Class

To configure your test class you will need to do 3 simple things:
- Annotate your test class with `@UiTestOptions`
- Add the L4TestRule as an `@Rule`
- Pass the instance of the test class to the L4TestRule

_Example:_

```java
@UiTestOptions
public class BaseUiTest {
    @Rule
    public L4TestRule rule = new L4TestRule(MainActivity.class, this);
}
```

### @UiTestOptions

Currently, there are 3 parameters that `@UiTestOptions` accepts:

- `retryCount`
  - Tells `L4TestRule` how many times tests should be rerun when they are failing.
- `screenshotOnFailure`
  - Tells `L4TestRule` if screenshots should be taken when tests fail. 
- `screenshotSavePath`
  - Defines where screenshots should be stored on the device under test.

#### retryCount

This parameter accepts positive integers. If `retryCount` is not passed or the `@UiTestOptions` annotation is not present `retryCount` will default to 0. This means that your test will only run once, there will be no retrying on failure.

_Example:_

```java
// A test suite configured for retrying tests 4 times on failure
@UiTestOptions(retryCount = 4)
public class BaseUiTest {
    @Rule
    public L4TestRule rule = new L4TestRule(MainActivity.class, this);
    
    // This test will run 5 times before logging the failure. 
    // 1st run + 4 retries = 5
    @Test
    public void failingTest() {
        Assert.fail();
    }
    
    // This test will only run once and then pass
    @Test
    public void passingTest() {
    
    }
}
```

#### screenshotOnFailure
This parameter accepts a boolean and defaults to false. You must explicitly enable failure screenshots. We don't want your test devices' disk space filling up with images without you knowing it. Screenshots will be taken on every failure, so if you have a single failing test that is configured to retry 3 times that means you will end up with 4 screenshots for that single test. Each screenshot is appended with the retry count (e.g., "TestClass.testMethod-retry2.jpg").

_Example:_

```java
// A test suite configured for taking screenshots on failure
@UiTestOptions(screenshotOnFailure = true)
public class BaseUiTest {
    @Rule
    public L4TestRule rule = new L4TestRule(MainActivity.class, this);
    
    // A screenshot will be taken for this test just before the activity is closed.
    @Test
    public void failingTest() {
        Assert.fail();
    }
    
    // No screenshots will be taken for this test since it passes.
    @Test
    public void passingTest() {
    
    }
}
```

#### screenshotSavePath

This parameter accepts a String and defaults to "sdcard/Pictures/test-screenshots/" when not provided. This is only useful when screenshots are enabled. 

_Example:_

```java
// A test suite configured with a custom screenshot save path
@UiTestOptions(screenshotOnFailure = true, screenShotSavePath = "sdcard/my-apps-name-screenshots/")
public class BaseUiTest {
    @Rule
    public L4TestRule rule = new L4TestRule(MainActivity.class, this);
    
    // A screenshot will be taken for this test since it's failing.
    // The screenshot will be saved on the device at "sdcard/my-apps-name-screenshots/".
    @Test
    public void failingTest() {
        Assert.fail();
    }
}
```
  
## Additional Test Lifecycle Hooks

This library also provides some additional annotations (similar to `@Before` and `@After`) that hook into the test run lifecycle:

- `@BeforeActivityLaunched`
  - Called just before the activity is launched.
- `@AfterActivityLaunched`
  - Called immediately after the activity is launched.
- `@BeforeTestLoop`
  - Only applies if tests are configured to retry when failed. Only called once before the test runs the first time.
- `@AfterTestLoop`
  - Only applies if tests are configured to retry when failed. Only called once after the test runs for the last time.
- `@BeforeActivityFinished`
  - Called just before the activities owned by the application are finished.
- `@AfterActivityFinished`
  - Called once all activities owned by the application are finished. 
  
## License

    Copyright 2017 L4 Digital LLC. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
         
         http://www.apache.org/licenses/LICENSE-2.0
         
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.