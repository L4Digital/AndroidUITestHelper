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

import com.l4digital.androidtest.exception.ExceptionMessages;
import com.l4digital.androidtest.rule.TestFailureReporter;
import com.l4digital.androidtest.rule.TestTryCounter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.Description;

public class TestFailureReporterTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private TestTryCounter testTryCounter;

    @Before
    public void beforeEach() {
        testTryCounter = new TestTryCounter();
    }

    @Test
    public void failingTestReportsCorrectMessageAndTestCount() {
        int tryCount = 5;
        String fakeTestName = "testSomething";
        Description testDescription = Description.createTestDescription(getClass(), fakeTestName);
        TestFailureReporter reporter = new TestFailureReporter(testDescription, testTryCounter);
        testTryCounter.incrementBy(tryCount);

        expectedException.expect(RuntimeException.class);
        String displayName = getExpectedFailureMessage(testDescription, tryCount);
        expectedException.expectMessage(displayName);
        reporter.failTest();
    }

    private String getExpectedFailureMessage(Description description, int tryCount) {
        return String.format(
                ExceptionMessages.FINAL_TEST_FAILURE,
                description.getDisplayName(),
                tryCount
        );
    }
}
