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

import com.l4digital.androidtest.exception.L4AndroidTestLibException;

import static com.l4digital.androidtest.exception.L4AndroidTestLibException.ExceptionCode.TEST_TRY_COUNTER_INVALID_INCREMENT;

/**
 * int wrapper used for tracking the current number of test tries inside L4TestRule.
  */
public class TestTryCounter {
    private int internalCounter;

    public void increment() {
        internalCounter++;
    }

    public void incrementBy(int value) {
        if (value < 1) {
            throw new L4AndroidTestLibException(TEST_TRY_COUNTER_INVALID_INCREMENT);
        }
        internalCounter += value;
    }

    public int getCount() {
        return internalCounter;
    }
}
