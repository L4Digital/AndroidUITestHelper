/*
 * Copyright 2017 L4 Digital LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.l4digital.androidtest;

import com.l4digital.androidtest.rule.TestRuleConfiguration;
import com.l4digital.androidtest.annotation.config.UiTestOptions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;

public class TestRuleConfigurationTests {

    private static final String mockScreenshotSavePath = "sdcard/somewhere/else";

    @UiTestOptions
    private class MockClass_NoConfig {
    }

    @UiTestOptions(screenshotOnFailure = true)
    private class MockClass_ScreenshotOnFailureEnabled {
    }

    @UiTestOptions(screenShotSavePath = "")
    private class MockClass_EmptyScreenshotSavePath {
    }

    @UiTestOptions(screenShotSavePath = mockScreenshotSavePath)
    private class MockClass_CustomScreenshotSavePath {
    }

    @Test
    public void screenshotOnFailure_defaultWithoutAnnotation() {
        TestRuleConfiguration config = new TestRuleConfiguration(this);
        String failureMessage = String.format(
                "Screenshot on failure default setting should be %s",
                UiTestOptions.DEFAULT_SCREENSHOT_ON_FAILURE
        );
        assertThat(
                failureMessage,
                config.shouldTakeScreenshotOnFailure(),
                equalTo(UiTestOptions.DEFAULT_SCREENSHOT_ON_FAILURE)
        );
    }

    @Test
    public void screenshotOnFailure_defaultWithAnnotation() {
        TestRuleConfiguration config = new TestRuleConfiguration(new MockClass_NoConfig());
        String failureMessage = String.format(
                "Screenshot on failure default setting should be %s",
                UiTestOptions.DEFAULT_SCREENSHOT_ON_FAILURE
        );
        assertThat(
                failureMessage,
                config.shouldTakeScreenshotOnFailure(),
                equalTo(UiTestOptions.DEFAULT_SCREENSHOT_ON_FAILURE)
        );
    }

    @Test
    public void screenshotOnFailure_enabledWithAnnotation() {
        TestRuleConfiguration config = new TestRuleConfiguration(new MockClass_ScreenshotOnFailureEnabled());
        String failureMessage = String.format(
                "Screenshot on failure setting should be %s",
                true
        );
        assertThat(
                failureMessage,
                config.shouldTakeScreenshotOnFailure(),
                equalTo(true)
        );

    }

    @Test
    public void screenshotSavePath_DefaultWithoutAnnotation() {
        TestRuleConfiguration config = new TestRuleConfiguration(this);
        String failureMessage = String.format(
                "Screenshot save path should be %s",
                UiTestOptions.DEFAULT_SCREENSHOT_SAVE_PATH
        );
        assertThat(
                failureMessage,
                config.getScreenshotSaveDir(),
                equalTo(UiTestOptions.DEFAULT_SCREENSHOT_SAVE_PATH)
        );
    }

    @Test
    public void screenshotSavePath_DefaultWithAnnotation() {
        TestRuleConfiguration config = new TestRuleConfiguration(new MockClass_NoConfig());
        String failureMessage = String.format(
                "Screenshot save path should be %s",
                UiTestOptions.DEFAULT_SCREENSHOT_SAVE_PATH
        );
        assertThat(
                failureMessage,
                config.getScreenshotSaveDir(),
                equalTo(UiTestOptions.DEFAULT_SCREENSHOT_SAVE_PATH)
        );
    }

    @Test
    public void screenshotSavePath_Custom() {
        TestRuleConfiguration config = new TestRuleConfiguration(new MockClass_CustomScreenshotSavePath());
        String failureMessage = String.format(
                "Screenshot save path should be %s",
                mockScreenshotSavePath
        );
        assertThat(
                failureMessage,
                config.getScreenshotSaveDir(),
                equalTo(mockScreenshotSavePath)
        );
    }

    @Test
    public void screenshotSavePath_Empty() {
        TestRuleConfiguration config = new TestRuleConfiguration(new MockClass_EmptyScreenshotSavePath());
        assertThat(
                "Screenshot save path should be empty",
                config.getScreenshotSaveDir(),
                isEmptyString()
        );
    }
}