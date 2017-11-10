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

package com.l4digital.androidtest.helper;

import android.app.Activity;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.view.View;
import android.view.Window;

import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import static android.support.test.espresso.matcher.RootMatchers.DEFAULT;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static com.l4digital.androidtest.util.L4Matchers.modalMatcher;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@SuppressWarnings({"WeakerAccess", "unused"})
public class AppHelper {

    // Activity

    public static Activity getCurrentActivity() {

        final ActivityLifecycleMonitor monitor = ActivityLifecycleMonitorRegistry.getInstance();
        final Activity[] activity = new Activity[1];

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {

            public void run() {
                Collection resumedActivities = monitor.getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    activity[0] = (Activity) resumedActivities.iterator().next();
                }
            }
        });

        return activity[0];
    }

    public static Activity waitForActivity(final Class<? extends Activity> activityClass, int timeout, TimeUnit unit, boolean shouldAssert) {

        final Activity[] activity = new Activity[1];

        waitForCondition(new Condition() {

            @Override
            public boolean isSatisfied() {
                activity[0] = getCurrentActivity();
                return activity[0] != null && activity[0].getClass().equals(activityClass);
            }
        }, timeout, unit);

        if (shouldAssert) {
            assertNotNull("Current activity should not be null.", activity[0]);
            assertEquals("Should be in expected activity.", activityClass, activity[0].getClass());
        }

        return activity[0];
    }

    // View

    @Nullable
    public static View getDecorView() {
        // This is certain to capture the activity window, if it exists. An instance where this might
        // not exist is if this method were called after the activity has already finished.
        Activity mainActivity = getCurrentActivity();

        if (mainActivity != null) {
            Window window = mainActivity.getWindow();
            return window.getDecorView();
        }
        return null;
    }

    @Nullable
    public static View getModalView() {
        View modalView = getView(
                isRoot(),
                modalMatcher()
        );
        // The above modalMatcher() will match to the decor view if a modal view is not present.
        // This is a hack, but required to make the above check not take 1+ minutes due to the
        // hardcoded backoff in Espresso's RootViewPicker. If the modal view matches the decor view
        // then we know there's no modal.
        return modalView.equals(getDecorView()) ? null : modalView;
    }

    public static View getView(final Matcher<View> matcher) {
        return getView(matcher, DEFAULT);
    }

    public static View getView(final Matcher<View> viewMatcher, final Matcher<Root> rootMatcher) {
        ViewGetter viewGetter = new ViewGetter(viewMatcher, rootMatcher);
        return viewGetter.getView();
    }

    public static boolean waitForView(final Matcher<View> matcher, int timeout, TimeUnit unit) {
        return waitForCondition(new Condition() {

            @Override
            public boolean isSatisfied() {
                return getView(matcher) != null;
            }
        }, timeout, unit);
    }

    // Condition

    public static boolean waitForCondition(Condition condition, int timeout, TimeUnit unit) {
        int timeoutInMillis = (int) unit.toMillis(timeout);
        long endTime = SystemClock.uptimeMillis() + timeoutInMillis;

        while (SystemClock.uptimeMillis() < endTime) {

            if (condition.isSatisfied()) {
                return true;
            }
        }

        return false;
    }

    public interface Condition {

        boolean isSatisfied();
    }
}