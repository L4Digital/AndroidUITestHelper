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

package com.l4digital.androidtest.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class AnnotationProcessor {

    private Object mReceivingObject;
    private Class<? extends Annotation> mAnnotationClass;

    public AnnotationProcessor(Object receivingObject) {
        mReceivingObject = receivingObject;
    }

    public void invokeAnnotatedMethod(Class<? extends Annotation> annotationClass) {
        mAnnotationClass = annotationClass;
        Method[] methods = mReceivingObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationClass)) {
                invokeMethod(method);
            }
        }
    }

    private void invokeMethod(Method method) {
        try {
            assertMethodIsPublic(method);
            assertMethodIsNotStatic(method);
            method.invoke(mReceivingObject);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertMethodIsPublic(Method method) {
        if (Modifier.isPrivate(method.getModifiers())) {
            String message = String.format(
                    "Methods annotated with %s must not be private",
                    mAnnotationClass.getName()
            );
            throw new RuntimeException(message);
        }
    }

    private void assertMethodIsNotStatic(Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            String message = String.format(
                    "Methods annotated with %s cannot be static",
                    mAnnotationClass.getName()
            );
            throw new RuntimeException(message);
        }
    }
}
