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

package com.l4digital.androidtest.screenshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScreenshotHelper {

    public static String createScreenshotNameWithTimeStamp(String displayName) {
        String dateFormat = "yyyyMMdd_HHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
        String formattedDate = formatter.format(new Date());
        return String.format(
                "%s.%s.jpeg",
                displayName,
                formattedDate
        );
    }
}
