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

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.telephony.TelephonyManager;

import junit.framework.Assert;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.isOneOf;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DeviceHelper {

    public static void assertAnimationsAreDisabled() {
        Assert.assertEquals("Animator duration should be disabled.", 0f,
                DeviceHelper.getAnimatorDurationScale(InstrumentationRegistry.getContext()));
        assertEquals("Transition animations should be disabled.", 0f,
                DeviceHelper.getTransitionAnimationScale(InstrumentationRegistry.getContext()));
        assertEquals("Window animations should be disabled.", 0f,
                DeviceHelper.getWindowAnimationScale(InstrumentationRegistry.getContext()));
    }

    // Galaxy SII 4.0.4 does not have this option and will cause this assert to fail.
    public static void assertStayAwakeDevOptionEnabled() {
        int value = DeviceHelper.getStayOnWhilePluggedInValue(InstrumentationRegistry.getContext());
        int batteryPlugged = BatteryManager.BATTERY_PLUGGED_USB
                | BatteryManager.BATTERY_PLUGGED_AC;

        @SuppressLint("InlinedApi")
        int batteryPluggedAll = BatteryManager.BATTERY_PLUGGED_USB
                | BatteryManager.BATTERY_PLUGGED_AC
                | BatteryManager.BATTERY_PLUGGED_WIRELESS;

        assertThat("\"Stay awake\" dev option should be enabled.", value, isOneOf(batteryPlugged,
                batteryPluggedAll));
    }

    public static boolean hasSim() {
        TelephonyManager telMgr = (TelephonyManager) InstrumentationRegistry.getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (telMgr == null) {
            return false;
        }

        int simState = telMgr.getSimState();

        return simState == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * http://stackoverflow.com/questions/24757522/respect-animator-duration-scale-in-own-animation/31996851#31996851
     *
     * @param context app context
     * @return duration scale
     */
    private static float getAnimatorDurationScale(Context context) {
        final float animatorDurationScale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            animatorDurationScale = Settings.Global.getFloat(
                    context.getContentResolver(),
                    Settings.Global.ANIMATOR_DURATION_SCALE,
                    0);
        } else {
            //noinspection deprecation
            animatorDurationScale = Settings.System.getFloat(
                    context.getContentResolver(),
                    Settings.System.ANIMATOR_DURATION_SCALE,
                    0);
        }

        return animatorDurationScale;
    }

    /**
     * http://stackoverflow.com/questions/24757522/respect-animator-duration-scale-in-own-animation/31996851#31996851
     *
     * @param context app context
     * @return animation scale
     */
    private static float getTransitionAnimationScale(Context context) {
        final float transitionAnimationScale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            transitionAnimationScale = Settings.Global.getFloat(
                    context.getContentResolver(),
                    Settings.Global.TRANSITION_ANIMATION_SCALE,
                    0);
        } else {
            //noinspection deprecation
            transitionAnimationScale = Settings.System.getFloat(
                    context.getContentResolver(),
                    Settings.System.TRANSITION_ANIMATION_SCALE,
                    0);
        }

        return transitionAnimationScale;
    }

    /**
     * http://stackoverflow.com/questions/24757522/respect-animator-duration-scale-in-own-animation/31996851#31996851
     *
     * @param context app context
     * @return animation scale
     */
    private static float getWindowAnimationScale(Context context) {
        final float windowAnimationScale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowAnimationScale = Settings.Global.getFloat(
                    context.getContentResolver(),
                    Settings.Global.WINDOW_ANIMATION_SCALE,
                    0);
        } else {
            //noinspection deprecation
            windowAnimationScale = Settings.System.getFloat(
                    context.getContentResolver(),
                    Settings.System.WINDOW_ANIMATION_SCALE,
                    0);
        }

        return windowAnimationScale;
    }

    /**
     * http://stackoverflow.com/questions/20130358/setting-the-stay-awake-while-plugged-in-option-programatically-in-android
     *
     * @param context app context
     */
    private static int getStayOnWhilePluggedInValue(Context context) {
        int value = 0;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                value = Settings.Global.getInt(context.getContentResolver(),
                        Settings.Global.STAY_ON_WHILE_PLUGGED_IN);
            } else {
                //noinspection deprecation
                value = Settings.System.getInt(context.getContentResolver(),
                        Settings.System.STAY_ON_WHILE_PLUGGED_IN);
            }
        } catch (Settings.SettingNotFoundException e) {
            fail("Unable to find \"Stay awake\" developer option.");
        }

        return value;
    }
}