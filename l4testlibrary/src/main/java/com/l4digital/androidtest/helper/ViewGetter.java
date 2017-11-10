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

package com.l4digital.androidtest.helper;

import android.support.annotation.NonNull;
import android.support.test.espresso.NoMatchingRootException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.Root;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.view.View;

import org.hamcrest.Matcher;

import javax.annotation.Nullable;

import static android.support.test.espresso.Espresso.onView;

public class ViewGetter implements ViewAction {
    private final Matcher<View> mViewMatcher;
    private final Matcher<Root> mRootMatcher;
    private View mView;

    public ViewGetter(@NonNull Matcher<View> viewMatcher, Matcher<Root> rootMatcher) {
        mViewMatcher = viewMatcher;
        mRootMatcher = rootMatcher;
    }

    public ViewGetter(@NonNull Matcher<View> viewMatcher) {
        this(viewMatcher, null);
    }

    @Nullable
    public View getView() {
        try {
            performGetView();
        } catch (NoMatchingRootException | NoMatchingViewException e) {
            // Don't fail if the view isn't found. We might want this to return null
            // sometimes. For instance, if we're waiting for a view to be dismissed.
        }

        return mView;
    }

    private void performGetView() {
        ViewInteraction onView = onView(mViewMatcher);
        if (mRootMatcher != null) {
            onView.inRoot(mRootMatcher);
        }
        onView.perform(this);
    }

    @Override
    public Matcher<View> getConstraints() {
        return mViewMatcher;
    }

    @Override
    public String getDescription() {
        return "get view";
    }

    @Override
    public void perform(UiController uiController, View view) {
        if (mViewMatcher.matches(view)) {
            mView = view;
        }
    }
}
