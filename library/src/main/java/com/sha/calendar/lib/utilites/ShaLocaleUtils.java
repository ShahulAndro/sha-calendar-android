/*
 * Copyright 2018 Shahul Hameed Shaik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sha.calendar.lib.utilites;

import android.support.v4.text.TextUtilsCompat;

import java.util.Locale;

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

public class ShaLocaleUtils {

    public static boolean isRTL(Locale locale){
        final int directionality = Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
        if (TextUtilsCompat.getLayoutDirectionFromLocale(locale) == 1) {
            return true;
        } else if(directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT) {
            return true;
        } else if (directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC){
            return true;
        } else {
            return false;
        }
    }

    public static int rotateYForViewGroup() {
        return 180;
    }

    public static int rotateYForView() {
        return 180;
    }
}
