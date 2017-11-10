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

package com.l4digital.testapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.RawRes;
import android.support.test.InstrumentationRegistry;

import com.l4digital.androidtest.screenshot.ScreenshotFile;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

import static junit.framework.Assert.assertTrue;

// For Android 6+, these tests will only pass if android.permission.WRITE_EXTERNAL_STORAGE and
// android.permission.READ_EXTERNAL_STORAGE have been granted.
public class ScreenshotFileTests extends BaseTestActivityTests {

    private String mFileName = "test.jpg";
    private String mDirName = "sdcard";
    private File mSavedScreenshot;

    @Before
    public void beforeEach() {
        mSavedScreenshot = new File(mDirName, mFileName);
        mSavedScreenshot.delete();
        Assume.assumeFalse(
                "Assuming screenshot file does not exist",
                mSavedScreenshot.exists()
        );
    }

    @Test
    public void screenshotSavesToDevice() {
        ScreenshotFile file = new ScreenshotFile(mDirName, mFileName, getTestBitmap(R.raw.test));
        file.saveToDevice();
        assertTrue("Screenshot should be saved to device", mSavedScreenshot.exists());
    }

    private Bitmap getTestBitmap(@RawRes int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        InputStream is = targetContext.getResources().openRawResource(id);
        return BitmapFactory.decodeStream(is);
    }
}
