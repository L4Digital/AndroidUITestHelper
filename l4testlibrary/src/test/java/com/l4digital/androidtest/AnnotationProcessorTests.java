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

import com.l4digital.androidtest.annotation.AnnotationProcessor;
import com.l4digital.androidtest.annotation.lifecycle.AfterActivityFinished;
import com.l4digital.androidtest.annotation.lifecycle.BeforeActivityFinished;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AnnotationProcessorTests {

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();
    private AnnotationProcessor mAnnotationProcessor;

    @Before
    public void beforeEach() {
        mAnnotationProcessor = new AnnotationProcessor(this);
    }

    @Test
    public void staticMethodThrowsException() {
        mExpectedException.expect(RuntimeException.class);
        mAnnotationProcessor.invokeAnnotatedMethod(BeforeActivityFinished.class);
    }

    @Test
    public void privateMethodThrowsException() {
        mExpectedException.expect(RuntimeException.class);
        mAnnotationProcessor.invokeAnnotatedMethod(AfterActivityFinished.class);
    }

    @AfterActivityFinished
    private void privateMethod() {

    }

    @BeforeActivityFinished
    public static void staticMethod() {

    }
}
