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

import com.l4digital.androidtest.exception.L4AndroidTestLibException;
import com.l4digital.androidtest.rule.TestRuleConfiguration;
import com.l4digital.androidtest.annotation.config.UiTestOptions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.l4digital.androidtest.exception.L4AndroidTestLibException.ExceptionCode.INVALID_RETRY_COUNT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TestRuleConfiguration_RetryCountTests {
    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();
    private static final int sPositiveRetryCount = 1;
    private static final int sNegativeRetryCount = -1;

    @UiTestOptions
    private class MockAnnotatedTestClass {
    }

    @UiTestOptions(retryCount = sPositiveRetryCount)
    private class MockTestClassWithPositiveRetries {
    }

    @UiTestOptions(retryCount = sNegativeRetryCount)
    private class MockTestClassWithNegativeRetries {
    }

    @Test
    public void retryCount_defaultValueWithoutAnnotation() {
        TestRuleConfiguration config = new TestRuleConfiguration(this);
        String failureMessage = String.format(
                "Retry count default setting should be %s",
                UiTestOptions.DEFAULT_RETRY_COUNT
        );
        assertThat(
                failureMessage,
                config.getRetryCount(),
                equalTo(UiTestOptions.DEFAULT_RETRY_COUNT)
        );
    }

    @Test
    public void retryCount_defaultValueWithAnnotation() {
        TestRuleConfiguration config = new TestRuleConfiguration(new MockAnnotatedTestClass());
        String failureMessage = String.format(
                "Retry count default setting should be %s",
                UiTestOptions.DEFAULT_RETRY_COUNT
        );
        assertThat(
                failureMessage,
                config.getRetryCount(),
                equalTo(UiTestOptions.DEFAULT_RETRY_COUNT)
        );
    }

    @Test
    public void retryCount_setToPositiveCountByAnnotation() {
        TestRuleConfiguration config = new TestRuleConfiguration(new MockTestClassWithPositiveRetries());
        String failureMessage = String.format(
                "Retry count setting should be %s",
                sPositiveRetryCount
        );
        assertThat(
                failureMessage,
                config.getRetryCount(),
                equalTo(sPositiveRetryCount)
        );
    }

    @Test
    public void retryCount_setToNegativeCountByAnnotation() {
        mExpectedException.expect(L4AndroidTestLibException.class);
        mExpectedException.expectMessage(is(INVALID_RETRY_COUNT.message));
        new TestRuleConfiguration(new MockTestClassWithNegativeRetries());
    }
}
