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

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.l4digital.androidtest.exception.ExceptionMessages;

import org.junit.runner.Description;

import java.util.Locale;

import static com.l4digital.androidtest.util.StringUtil.stackTraceToString;

public class TestFailureReporter {

    private final String mTag = getClass().getSimpleName();
    private final Description mDescription;
    private final TestTryCounter mTryCount;

    public TestFailureReporter(Description description, TestTryCounter tryCount) {
        mDescription = description;
        mTryCount = tryCount;
    }

    void failTest(@Nullable Throwable t) {
        String message = getFinalFailureMessage();
        RuntimeException e = new RuntimeException(message);
        if (t != null) {
            e.initCause(t);
        }
        throw e;
    }

    @VisibleForTesting
    public void failTest() {
        failTest(null);
    }

    private String getFinalFailureMessage() {
        return String.format(
                Locale.US,
                ExceptionMessages.FINAL_TEST_FAILURE,
                mDescription.getDisplayName(),
                mTryCount.getCount());
    }

    void reportFailure(Throwable t) {
        Log.e(mTag, getIntermediateFailureMessage(t));
    }

    private String getIntermediateFailureMessage(Throwable t) {
        return String.format(
                Locale.US,
                ExceptionMessages.INTERMEDIATE_TEST_FAILURE,
                mDescription.getDisplayName(),
                mTryCount.getCount(),
                stackTraceToString(t)
        );
    }
}
