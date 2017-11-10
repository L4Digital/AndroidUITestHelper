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

package com.l4digital.androidtest.util;

import android.support.test.espresso.Root;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.matcher.RootMatchers.DEFAULT;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.isSystemAlertWindow;
import static org.hamcrest.Matchers.anyOf;

public class L4Matchers {

    public static Matcher<Root> modalMatcher() {
        return anyOf(
                isDialog(),
                isMenuPopup(),
                isPlatformPopup(),
                isSystemAlertWindow(),
                DEFAULT
        );
    }

    private static TypeSafeMatcher<Root> isMenuPopup() {
        return new TypeSafeMatcher<Root>() {

            @Override
            protected boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                return type == WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is menu popup");
            }
        };
    }
}
