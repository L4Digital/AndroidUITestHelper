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

package com.l4digital.androidtest.screenshot;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.l4digital.androidtest.exception.L4AndroidTestLibException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.l4digital.androidtest.exception.L4AndroidTestLibException.ExceptionCode.BITMAP_WRITE_FAILURE;

public class ScreenshotFile {
    private final String mTag = getClass().getSimpleName();
    private final String mSaveDir;
    private final String mFileName;
    private final Bitmap mBitmap;
    private File mScreenshotFile;

    public ScreenshotFile(@NonNull String saveDir, @NonNull String fileName, @NonNull Bitmap bitmap) {
        mSaveDir = saveDir;
        mFileName = fileName;
        mBitmap = bitmap;
    }

    public void saveToDevice() {
        try {
            mScreenshotFile = createScreenshotFile(mFileName);
            writeBitmapToFile();
        } catch (IOException e) {
            throw new L4AndroidTestLibException(BITMAP_WRITE_FAILURE, e);
        }
    }

    private File createScreenshotFile(String fileName) {
        File directory = new File(mSaveDir);
        //noinspection ResultOfMethodCallIgnored
        directory.mkdir();
        return new File(directory, fileName);
    }

    private void writeBitmapToFile() throws IOException {
        FileOutputStream out = new FileOutputStream(mScreenshotFile);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        out.flush();
        String message = String.format("Screenshot saved to %s.", mScreenshotFile.getAbsolutePath());
        Log.i(mTag, message);
    }
}
