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

import android.view.View;

import org.junit.Test;

import static com.l4digital.androidtest.helper.AppHelper.getDecorView;
import static com.l4digital.androidtest.helper.AppHelper.getModalView;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;

public class L4MatchersTests extends BaseTestActivityTests {

    private View mDecorView;
    private View mModalView;

    @Test
    public void contextMenuViewShouldBeSmallerThanDecorView() {
        // Opening the context menu gives us a decor view and a modal view to test.
        testRule.getActivity().openContextMenu();
        mDecorView = getDecorView();
        mModalView = getModalView();
        assertNotNull("Decor view should not be null", mDecorView);
        assertNotNull("Modal view should not be null", mModalView);
        assertThat(
                "Decor view should be larger than the modal view",
                mDecorView.getWidth() + mDecorView.getHeight(),
                greaterThan(mModalView.getWidth() + mModalView.getHeight())
        );
    }

    @Test
    public void modalViewAndDecorViewShouldNotMatchIfBothPresent() {
        testRule.getActivity().openContextMenu();
        mDecorView = getDecorView();
        mModalView = getModalView();
        assertNotNull("Decor view should not be null", mDecorView);
        assertNotNull("Modal view should not be null", mModalView);
        assertThat(
                "Modal view and decor view should not be the same if both are displayed.",
                mDecorView,
                not(equalTo(mModalView))
        );
    }

    @Test
    public void missingModalViewShouldReturnNull() {
        mModalView = getModalView();
        assertThat("Modal view should be null", mModalView, equalTo(null));
    }
}
