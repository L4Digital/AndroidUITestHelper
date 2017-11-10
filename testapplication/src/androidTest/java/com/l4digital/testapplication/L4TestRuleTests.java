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

import com.l4digital.testapplication.activity.TestActivity;
import com.l4digital.androidtest.annotation.config.UiTestOptions;
import com.l4digital.androidtest.rule.L4TestRule;
import com.l4digital.androidtest.rule.TestTryCounter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static com.l4digital.testapplication.L4TestRuleTests.retryTestCount;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@UiTestOptions(retryCount = retryTestCount)
public class L4TestRuleTests {
    static final int retryTestCount = 4;
    // Max tries = mandatory run + number of retries.
    private int maxTestTries = 1 + retryTestCount;
    private TestTryCounter testTryCounter = new TestTryCounter();
    private L4TestRule<TestActivity> l4Rule = new L4TestRule<>(
            TestActivity.class,
            this,
            testTryCounter
    );
    private int passAtCount;
    private boolean hasPassed = false;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testPassesImmediately() throws Throwable {
        passAtCount = 1;
        evaluate();
        assertThat(
                "Test should have run once",
                testTryCounter.getCount(),
                equalTo(passAtCount)
        );
        assertTrue("Test should have passed", hasPassed);
    }

    @Test
    public void testPassesSecondTime() throws Throwable {
        passAtCount = 2;
        evaluate();
        assertThat(
                "Test should have run twice",
                testTryCounter.getCount(),
                equalTo(passAtCount)
        );
        assertTrue("Test should have passed", hasPassed);
    }

    @Test
    public void testPassesLastTime() throws Throwable {
        passAtCount = maxTestTries;
        evaluate();
        assertThat(
                "Test should have run maximum number of times",
                testTryCounter.getCount(),
                equalTo(passAtCount)
        );
        assertTrue("Test should have passed", hasPassed);
    }

    @Test
    public void testRunsMaxTimesWhenFailing() throws Throwable {
        // Setting this above max tries since we want this to fail.
        passAtCount = maxTestTries + 1;
        evaluate();
        assertThat(
                "Test should have run the maximum number of times",
                testTryCounter.getCount(),
                equalTo(maxTestTries)
        );
        assertFalse("Test should have failed", hasPassed);
    }

    private Statement getMockStatement() {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (testTryCounter.getCount() < passAtCount) {
                    fail();
                }
            }
        };
    }

    private Description getMockDescription() {
        return Description.createTestDescription(getClass(), "someTest");
    }

    private void evaluate() throws Throwable {
        try {
            Statement statement = l4Rule.apply(getMockStatement(), getMockDescription());
            statement.evaluate();
            hasPassed = true;
        } catch (RuntimeException e) {
            hasPassed = false;
        }
    }
}
