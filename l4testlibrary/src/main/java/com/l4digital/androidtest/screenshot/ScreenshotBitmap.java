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

import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.view.View;

import com.l4digital.androidtest.exception.L4AndroidTestLibException;

import static com.l4digital.androidtest.exception.L4AndroidTestLibException.ExceptionCode.MISSING_VIEWS;

public class ScreenshotBitmap {
    private final View mDecorView;
    private final View mModalView;

    public ScreenshotBitmap(View decorView, View modalView) {
        mDecorView = decorView;
        mModalView = modalView;
    }

    public Bitmap createBitmapForScreenshot() {
        Bitmap screenshotBitmap;

        if (mDecorView != null && mModalView != null) {
            screenshotBitmap = combineViewsIntoBitmaps(mDecorView, mModalView);
        } else if (mDecorView != null) {
            screenshotBitmap = getBitmapFromView(mDecorView);
        } else if (mModalView != null) {
            screenshotBitmap = getBitmapFromView(mModalView);
        } else {
            throw new L4AndroidTestLibException(MISSING_VIEWS);
        }

        return screenshotBitmap;
    }

    @NonNull
    private Bitmap combineViewsIntoBitmaps(View decorView, View modalView) {
        Bitmap decorBitmap = getBitmapFromView(decorView);
        Bitmap modalBitmap = getBitmapFromView(modalView);
        int[] modalViewCoordinates = new int[2];
        modalView.getLocationOnScreen(modalViewCoordinates);
        Canvas layeredCanvas = new Canvas(decorBitmap);

        // Without these coordinates the modal will appear at the top, left corner
        // of the screen instead of where it should (usually center of screen).
        int modalViewX = modalViewCoordinates[0];
        int modalViewY = modalViewCoordinates[1];
        layeredCanvas.drawBitmap(modalBitmap, modalViewX, modalViewY, null);

        return decorBitmap;
    }

    @NonNull
    private Bitmap getBitmapFromView(@NonNull final View view) {
        final Bitmap[] bitmapCarrier = new Bitmap[1];
        // We cannot touch views from outside of the UI thread. There are no guarantees that this
        // will not be called from a different thread so we have to make sure that it is run on the
        // UI thread.
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(new Runnable() {

            @Override
            public void run() {
                view.setDrawingCacheEnabled(true);
                bitmapCarrier[0] = createMutableBitmap(view.getDrawingCache());
                view.setDrawingCacheEnabled(false);
            }
        });

        instrumentation.waitForIdleSync();
        return bitmapCarrier[0];
    }

    @NonNull
    private Bitmap createMutableBitmap(@NonNull Bitmap immutableBitmap) {
        return Bitmap.createBitmap(
                immutableBitmap.getWidth(),
                immutableBitmap.getHeight(),
                immutableBitmap.getConfig()
        );
    }
}
