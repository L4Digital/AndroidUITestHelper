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

package com.l4digital.androidtest.util;

public class StringUtil {

    public static String stackTraceToString(Throwable t) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(t.toString());
        stringBuilder.append("\n");
        for (StackTraceElement stackTraceLine : t.getStackTrace()) {
            stringBuilder.append("\t\tat ");
            stringBuilder.append(stackTraceLine.toString());
            stringBuilder.append("\n");
        }

        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public static String removePackagePrefix(String className) {
        // The following regex (".*\\..*\\.") is removing the package name that is prefixed to the
        // class name to make the output more human readable.
        return className.replaceAll(".*\\..*\\.", "");
    }
}
