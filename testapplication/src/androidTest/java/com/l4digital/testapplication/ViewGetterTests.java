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
import android.widget.TextView;

import com.l4digital.androidtest.helper.ViewGetter;

import org.junit.Test;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.l4digital.androidtest.util.L4Matchers.modalMatcher;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.instanceOf;

public class ViewGetterTests extends BaseTestActivityTests {

    @Test
    public void failingToMatchViewShouldNotFailTest() {
        ViewGetter viewGetter = new ViewGetter(withId(Integer.MAX_VALUE));
        View nullView = viewGetter.getView();
        assertNull("View should be null", nullView);
    }

    @Test
    public void shouldFindViewInDefaultRoot() {
        ViewGetter viewGetter = new ViewGetter(withId(R.id.view));
        View view = viewGetter.getView();
        assertNotNull("View should not be null", view);
        assertThat("View should be the correct type", view, instanceOf(TextView.class));
    }

    @Test
    public void shouldFindViewInModalRoot() {
        testRule.getActivity().openContextMenu();
        ViewGetter viewGetter = new ViewGetter(withText("TEST_OPTION"), modalMatcher());
        View view = viewGetter.getView();
        assertNotNull("View should not be null", view);
        assertThat("View should be the correct type", view, instanceOf(TextView.class));
    }
}
