package com.zebra.criticalpermissionshelper;

public enum EPermissionType
{
    ACCESS_NOTIFICATIONS(       "android.permission.ACCESS_NOTIFICATIONS"),
    PACKAGE_USAGE_STATS(        "android.permission.PACKAGE_USAGE_STATS"),
    SYSTEM_ALERT_WINDOW(        "android.permission.SYSTEM_ALERT_WINDOW"),
    GET_APP_OPS_STATS(          "android.permission.GET_APP_OPS_STATS"),
    BATTERY_STATS(              "android.permission.BATTERY_STATS"),
    MANAGE_EXTERNAL_STORAGE(    "android.permission.MANAGE_EXTERNAL_STORAGE"),
    BIND_NOTIFICATION_LISTENER(    "android.permission.BIND_NOTIFICATION_LISTENER"),
    READ_LOGS(    "android.permission.READ_LOGS"),
    ALL_DANGEROUS( "ALL_DANGEROUS_PERMISSIONS");

    String stringContent = "";
    EPermissionType(String stringContent)
    {
        this.stringContent = stringContent;
    }

    @Override
    public String toString() {
        return stringContent;
    }
}