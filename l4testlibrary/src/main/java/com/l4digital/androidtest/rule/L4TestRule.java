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

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.l4digital.androidtest.annotation.AnnotationProcessor;
import com.l4digital.androidtest.annotation.lifecycle.AfterActivityFinished;
import com.l4digital.androidtest.annotation.lifecycle.AfterActivityLaunched;
import com.l4digital.androidtest.annotation.lifecycle.AfterTestLoop;
import com.l4digital.androidtest.annotation.lifecycle.BeforeActivityFinished;
import com.l4digital.androidtest.annotation.lifecycle.BeforeActivityLaunched;
import com.l4digital.androidtest.annotation.lifecycle.BeforeTestLoop;
import com.l4digital.androidtest.screenshot.ScreenshotTaker;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static com.l4digital.androidtest.helper.AppHelper.getCurrentActivity;
import static com.l4digital.androidtest.screenshot.ScreenshotHelper.createScreenshotNameWithTimeStamp;

public class L4TestRule<T extends Activity> extends ActivityTestRule<T> {

    @SuppressWarnings("WeakerAccess")
    protected boolean mShouldPassImmediately = true; // Default. To be overridden if needed.
    private final AnnotationProcessor mAnnotationProcessor;
    private final TestRuleConfiguration mConfig;
    private final int mMaximumTries;
    private TestFailureReporter mTestFailureReporter;
    private ScreenshotTaker mScreenshotTaker;
    private TestTryCounter mTryCounter;

    public L4TestRule(Class<T> launcherActivity, Object testSuiteObject, TestTryCounter tryCounter) {
        super(launcherActivity, false, false);
        mAnnotationProcessor = new AnnotationProcessor(testSuiteObject);
        mConfig = new TestRuleConfiguration(testSuiteObject);
        // Adding 1 since we should always be running a test at least once.
        mMaximumTries = mConfig.getRetryCount() + 1;
        mTryCounter = tryCounter;
    }

    public L4TestRule(Class<T> launcherActivity, Object testSuiteObject) {
        this(launcherActivity, testSuiteObject, new TestTryCounter());
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        mTestFailureReporter = new TestFailureReporter(description, mTryCounter);
        String screenshotName = createScreenshotNameWithTimeStamp(description.getDisplayName());
        mScreenshotTaker = new ScreenshotTaker(mConfig.getScreenshotSaveDir(), screenshotName);
        return getStatement(base);
    }

    private Statement getStatement(final Statement base) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                mAnnotationProcessor.invokeAnnotatedMethod(BeforeTestLoop.class);
                startTestLoop(base);
                mAnnotationProcessor.invokeAnnotatedMethod(AfterTestLoop.class);
            }
        };
    }

    private void startTestLoop(Statement base) {
        while (mTryCounter.getCount() < mMaximumTries) {
            mTryCounter.increment();
            launchActivity();
            boolean hasPassed = runTest(base);
            finishActivities();

            // Some tests might want to loop regardless of status (e.g., looping performance test).
            if (hasPassed && mShouldPassImmediately) {
                break;
            }
        }
    }

    /**
     * Runs the test and reports the status.
     *
     * @param base the statement passed in by the test runner.
     * @return true if test passes, false otherwise.
     */
    private boolean runTest(Statement base) {
        try {
            base.evaluate();
        } catch (Throwable t) {
            handleFailure(t);
            return false;
        }

        return true;
    }

    private void launchActivity() {
        mAnnotationProcessor.invokeAnnotatedMethod(BeforeActivityLaunched.class);
        launchActivity(getActivityIntent());
        mAnnotationProcessor.invokeAnnotatedMethod(AfterActivityLaunched.class);
    }

    private void finishActivities() {
        mAnnotationProcessor.invokeAnnotatedMethod(BeforeActivityFinished.class);

        // Make sure to finish the main activity. I'm not sure if this is
        // necessary with closing the current activity below, but keeping it
        // just in case. It has no negative impact.
        finishActivity(getActivity());

        // Make sure to close the current activity and not just the main
        // activity, otherwise the app will be left in an unknown state (e.g.,
        // context menus left open, left on details pages, etc.)
        finishActivity(getCurrentActivity());
        mAnnotationProcessor.invokeAnnotatedMethod(AfterActivityFinished.class);
    }

    private void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    private void handleFailure(Throwable t) {
        takeScreenshot();
        if (mTryCounter.getCount() < mMaximumTries) {
            mTestFailureReporter.reportFailure(t);
        } else {
            finishActivities();
            mTestFailureReporter.failTest(t);
        }
    }

    private void takeScreenshot() {
        if (mConfig.shouldTakeScreenshotOnFailure()) {
            mScreenshotTaker.takeScreenshot();
        }
    }
}