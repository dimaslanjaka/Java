package com.dimaslanjaka.tools.Helpers.permission;

/*
 * Android Helpers
 *
 * Copyright (c) 2017 Dani Mahardhika
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

import android.Manifest;

public abstract class PermissionCode {
    final public static int PERMISSION_REQUEST_CODE = 1;
    final public static int STORAGE = 1;
    final public static int LOCATION = 2;
    final public static String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS
    };
    final public static int RC_CAMERA_PERM = 123;
    final public static int RC_LOCATION_CONTACTS_PERM = 124;
}
