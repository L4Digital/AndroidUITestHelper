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
import com.l4digital.androidtest.rule.TestTryCounter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.l4digital.androidtest.exception.L4AndroidTestLibException.ExceptionCode.TEST_TRY_COUNTER_INVALID_INCREMENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestTryCounterTests {

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();

    @Test
    public void incrementingBy0ThrowsException() {
        mExpectedException.expect(L4AndroidTestLibException.class);
        mExpectedException.expectMessage(TEST_TRY_COUNTER_INVALID_INCREMENT.message);
        TestTryCounter counter = new TestTryCounter();
        counter.incrementBy(0);
    }

    @Test
    public void incrementingByNegativeIntThrowsException() {
        mExpectedException.expect(L4AndroidTestLibException.class);
        mExpectedException.expectMessage(TEST_TRY_COUNTER_INVALID_INCREMENT.message);
        TestTryCounter counter = new TestTryCounter();
        counter.incrementBy(-10);
    }

    @Test
    public void incrementingIncrementsByOne() {
        TestTryCounter counter = new TestTryCounter();
        counter.increment();
        assertThat("Count should be 1", counter.getCount(), is(1));
    }

    @Test
    public void incrementingByPositiveInt() {
        TestTryCounter counter = new TestTryCounter();
        counter.increment();
        counter.incrementBy(4);
        assertThat("Count should be 6", counter.getCount(), is(5));
    }
}
