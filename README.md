# CriticalPermissionsHelper

## Sample Repository
https://github.com/ltrudu/CriticalPermissionsHelper-Sample


## Description
A wrapper to easily grant critical permissions to your application running on Zebra's Android devices with Android >= A11

Available permissions are:

```txt
#1. Access Notifications:	
Controls permission to access Notifications on the device.			

#2. Package Usage Stats:	
Controls permission to access app usage statistics for the device.			

#3. System Alert Window:	
Controls permission to use the System Alert Window, which allows one app to draw its window(s) over another.			

#4. Get AppOps Stats:	
Controls permission to access app operations statistics, used to determine the resources being used by apps on the 

#5. Battery Stats:	
Controls permission to access battery statistics for the device.			

#6. Manage External Storage:	
Controls management of USB and/or SD card storage media attached to the device.	
```

This wrapper will use the EMDK with MX's AccessMgr feature to grant, deny or verify (WIP) critical permissions.

## Implementation
To use this helper on Zebra Android devices running Android 10 or higher, first declare a new permission in your AndroidManifest.xml
The EMDK permission is compulsary, the others are depending on your needs.

```xml
    <uses-permission android:name="com.symbol.emdk.permission.EMDK" />

    <uses-permission android:name="android.permission.ACCESS_NOTIFICATIONS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.BATTERY_STATS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.GET_APP_OPS_STATS"
        tools:ignore="ProtectedPermissions" />
```

Sample AdroidManifest.xml:
```xml
?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.zebra.criticalpermissionshelpersample">
    <!--> TODO: Add these permissions to your manifest </-->
    <uses-permission android:name="com.symbol.emdk.permission.EMDK" />

    <uses-permission android:name="android.permission.ACCESS_NOTIFICATIONS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.BATTERY_STATS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.GET_APP_OPS_STATS"
        tools:ignore="ProtectedPermissions" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <!--> TODO: Add uses-library com.symbol.emdk to your manifest </-->
        <uses-library android:name="com.symbol.emdk" />
        <activity android:name="com.zebra.criticalpermissionshelpersample.MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```
Update your project build.graddle file to add jitpack repository
```text
        maven { url 'https://jitpack.io' }        
```
Sample project build.gradle
```text
        buildscript {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:7.2.1'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        maven { url 'https://jitpack.io' }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}       
```

Finally, add CriticalPermissionsHelper dependency to your application build.graddle file:
```text
        implementation 'com.github.ltrudu:CriticalPermissionsHelper:0.1'        
```

Sample application build.graddle:
```text
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:28.0.0'
    testImplementation 'junit:junit:4.13'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    implementation 'com.github.ltrudu:CriticalPermissionsHelper:0.1'
}
```

Now you can use the following snippet codes to grant permissions.

The type of permissions you can grant:
```java
EPermissionType.ACCESS_NOTIFICATIONS
EPermissionType.PACKAGE_USAGE_STATS
EPermissionType.SYSTEM_ALERT_WINDOW
EPermissionType.GET_APP_OPS_STATS
EPermissionType.BATTERY_STATS
EPermissionType.MANAGE_EXTERNAL_STORAGE
```

Snippet code to grant a permission:
```java
private void enablePermission(final Context aContext, final EPermissionType aPermissionType)
{
    // Enable permission
    CriticalPermissionsHelper.grantPermission(aContext, aPermissionType, new IResultCallbacks() {
        @Override
        public void onSuccess(String message, String resultXML) {
            Log.d(TAG, aPermissionType.toString() + " granted with success.");
        }

        @Override
        public void onError(String message, String resultXML) {
            Log.d(TAG, "Error granting " + aPermissionType.toString() + " permission.\n" + message);
        }

        @Override
        public void onDebugStatus(String message) {
            Log.d(TAG, "Debug Grant Permission " + aPermissionType.toString() + ": " + message);
        }
    });
  }
```

Snippet code to disable a permission:
```java
private void disablePermission(final Context aContext, final EPermissionType aPermissionType)
{
    // Enable permission
    CriticalPermissionsHelper.denyPermission(aContext, aPermissionType, new IResultCallbacks() {
        @Override
        public void onSuccess(String message, String resultXML) {
            Log.d(TAG, aPermissionType.toString() + " granted with success.");
        }

        @Override
        public void onError(String message, String resultXML) {
            Log.d(TAG, "Error granting " + aPermissionType.toString() + " permission.\n" + message);
        }

        @Override
        public void onDebugStatus(String message) {
            Log.d(TAG, "Debug Grant Permission " + aPermissionType.toString() + ": " + message);
        }
    });
  }
```

The verifyPermission method is a work in progress.
Check the sample for more information.

