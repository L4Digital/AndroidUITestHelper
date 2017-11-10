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

package com.l4digital.androidtest.rule;

import com.l4digital.androidtest.annotation.config.UiTestOptions;
import com.l4digital.androidtest.exception.L4AndroidTestLibException;

import static com.l4digital.androidtest.exception.L4AndroidTestLibException.ExceptionCode.INVALID_RETRY_COUNT;

public class TestRuleConfiguration {

    private UiTestOptions mOptions;
    private final int mRetryCount;
    private final boolean mShouldTakeScreenshotOnFailure;
    private final String mScreenshotSaveDir;

    public TestRuleConfiguration(Object testSuiteObject) {
        mOptions = testSuiteObject.getClass().getAnnotation(UiTestOptions.class);
        if (mOptions != null) {
            mRetryCount = internalGetRetryCount();
            mShouldTakeScreenshotOnFailure = mOptions.screenshotOnFailure();
            mScreenshotSaveDir = mOptions.screenShotSavePath();
        } else {
            // In case the UiTestOptions annotation is not used defaults are still provided.
            mRetryCount = UiTestOptions.DEFAULT_RETRY_COUNT;
            mShouldTakeScreenshotOnFailure = UiTestOptions.DEFAULT_SCREENSHOT_ON_FAILURE;
            mScreenshotSaveDir = UiTestOptions.DEFAULT_SCREENSHOT_SAVE_PATH;
        }
    }

    private int internalGetRetryCount() {
        int retryCount = mOptions.retryCount();
        if (retryCount < 0) {
            throw new L4AndroidTestLibException(INVALID_RETRY_COUNT);
        }

        return retryCount;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public boolean shouldTakeScreenshotOnFailure() {
        return mShouldTakeScreenshotOnFailure;
    }

    public String getScreenshotSaveDir() {
        return mScreenshotSaveDir;
    }
}
