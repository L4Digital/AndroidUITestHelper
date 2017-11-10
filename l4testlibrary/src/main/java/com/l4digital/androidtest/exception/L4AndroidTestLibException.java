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

package com.l4digital.androidtest.exception;

public class L4AndroidTestLibException extends RuntimeException {

    public L4AndroidTestLibException(ExceptionCode code) {
        super(code.message);
    }

    public L4AndroidTestLibException(ExceptionCode code, Throwable t) {
        super(code.message, t);
    }

    public enum ExceptionCode {
        BITMAP_WRITE_FAILURE("Unable to write bitmap to file"),
        INVALID_RETRY_COUNT("Retry count cannot be less than 0"),
        TEST_TRY_COUNTER_INVALID_INCREMENT("Cannot increment TestTryCounter by less than 1"),
        MISSING_VIEWS("There are no views to create a screenshot from");

        public String message;

        ExceptionCode(String message) {
            this.message = message;
        }
    }
}
