package com.zebra.criticalpermissionshelper;

public enum EPermissionType
{
    ACCESS_NOTIFICATIONS(           "android.permission.ACCESS_NOTIFICATIONS"           ),
    PACKAGE_USAGE_STATS(            "android.permission.PACKAGE_USAGE_STATS"            ),
    SYSTEM_ALERT_WINDOW(            "android.permission.SYSTEM_ALERT_WINDOW"            ),
    GET_APP_OPS_STATS(              "android.permission.GET_APP_OPS_STATS"              ),
    BATTERY_STATS(                  "android.permission.BATTERY_STATS"                  ),
    MANAGE_EXTERNAL_STORAGE(        "android.permission.MANAGE_EXTERNAL_STORAGE"        ),
    BIND_NOTIFICATION_LISTENER(     "android.permission.BIND_NOTIFICATION_LISTENER"     ),
    READ_LOGS(                      "android.permission.READ_LOGS"                      ),
    ALL_DANGEROUS_PERMISSIONS(      "ALL_DANGEROUS_PERMISSIONS"                         );

    String stringContent = "";
    EPermissionType(String stringContent)
    {
        this.stringContent = stringContent;
    }

    @Override
    public String toString() {
        return stringContent;
    }

    public static EPermissionType fromString(String permissionType)
    {
        switch(permissionType)
        {
            case "android.permission.ACCESS_NOTIFICATIONS":
                return ACCESS_NOTIFICATIONS;
            case "android.permission.PACKAGE_USAGE_STATS":
                return PACKAGE_USAGE_STATS;
            case "android.permission.SYSTEM_ALERT_WINDOW":
                return SYSTEM_ALERT_WINDOW;
            case "android.permission.GET_APP_OPS_STATS":
                return GET_APP_OPS_STATS;
            case "android.permission.BATTERY_STATS":
                return BATTERY_STATS;
            case "android.permission.MANAGE_EXTERNAL_STORAGE":
                return MANAGE_EXTERNAL_STORAGE;
            case "android.permission.BIND_NOTIFICATION_LISTENER":
                return BIND_NOTIFICATION_LISTENER;
            case "android.permission.READ_LOGS":
                return READ_LOGS;
            case "ALL_DANGEROUS_PERMISSIONS":
                return ALL_DANGEROUS_PERMISSIONS;
            default:
                return null;
        }
    }
}