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

import android.graphics.Bitmap;
import android.view.View;

import com.l4digital.androidtest.exception.L4AndroidTestLibException;
import com.l4digital.androidtest.screenshot.ScreenshotBitmap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.l4digital.androidtest.exception.L4AndroidTestLibException.ExceptionCode.MISSING_VIEWS;
import static com.l4digital.androidtest.helper.AppHelper.getDecorView;
import static com.l4digital.androidtest.helper.AppHelper.getModalView;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SuppressWarnings("ConstantConditions")
public class ScreenshotBitmapTests extends BaseTestActivityTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void beforeEach() {
        testRule.getActivity().openContextMenu();
    }

    @Test
    public void bitmapSizeShouldMatchDecorView_ModalViewIsPresent() {
        View decorView = getDecorView();
        View modalView = getModalView();
        ScreenshotBitmap util = new ScreenshotBitmap(decorView, modalView);
        Bitmap bitmap = util.createBitmapForScreenshot();
        assertThat("Decor view should not be null", decorView, notNullValue());
        assertThat("Bitmap should be same width as decor view", bitmap.getWidth(), is(decorView.getWidth()));
        assertThat("Bitmap should be same height as decor view", bitmap.getHeight(), is(decorView.getHeight()));
    }

    @Test
    public void bitmapSizeShouldMatchDecorView_ModalViewNotPresent() {
        View decorView = getDecorView();
        ScreenshotBitmap util = new ScreenshotBitmap(decorView, null);
        Bitmap bitmap = util.createBitmapForScreenshot();
        assertThat("Decor view should not be null", decorView, notNullValue());
        assertThat("Bitmap should be same width as decor view", bitmap.getWidth(), is(decorView.getWidth()));
        assertThat("Bitmap should be same height as decor view", bitmap.getHeight(), is(decorView.getHeight()));
    }

    @Test
    public void bitmapSizeShouldMatchModalView_DecorViewNotPresent() {
        View modalView = getModalView();
        ScreenshotBitmap util = new ScreenshotBitmap(null, modalView);
        Bitmap bitmap = util.createBitmapForScreenshot();
        assertThat("Decor view should not be null", modalView, notNullValue());
        assertThat("Bitmap should be same width as decor view", bitmap.getWidth(), is(modalView.getWidth()));
        assertThat("Bitmap should be same height as decor view", bitmap.getHeight(), is(modalView.getHeight()));
    }

    @Test
    public void missingViewsShouldThrowException() {
        expectedException.expect(L4AndroidTestLibException.class);
        expectedException.expectMessage(MISSING_VIEWS.message);
        ScreenshotBitmap util = new ScreenshotBitmap(null, null);
        util.createBitmapForScreenshot();
    }
}
