package com.zebra.criticalpermissionshelper;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.Base64;

public class CriticalPermissionsHelper {

    // Placeholder for custom certificate
    // Otherwise, the app will use the first certificate found with the method:
    // final Signature[] arrSignatures = packageInfo.signingInfo.getApkContentsSigners();
    // TODO: Put your custom certificate in the apkCertificate member for MX AccessMgr registering (only if necessary and if you know what you are doing)
    public static Signature apkCertificate = null;

    public static final long SEC_IN_MS = 1000;
    public static final long MIN_IN_MS = SEC_IN_MS * 60;
    public static long MAX_EMDK_TIMEOUT_IN_MS = 10 * MIN_IN_MS; // 10 minutes
    public static long WAIT_PERIOD_BEFORE_RETRY_EMDK_RETRIEVAL_IN_MS = 2 * SEC_IN_MS; // 2 seconds


    private enum EPermissionAccessAction
    {
        GRANT_PERMISSION("1"),
        DENY_PERMISSION("2"),
        ALLOW_USER_TO_CHOOSE("3"),
        VERIFY("4");

        String stringContent = "";
        EPermissionAccessAction(String stringContent)
        {
            this.stringContent = stringContent;
        }

        @Override
        public String toString() {
            return stringContent;
        }

        public EPermissionAccessAction fromString(String permissionAction)
        {
            switch(permissionAction)
            {
                case "1":
                    return GRANT_PERMISSION;
                case "2":
                    return DENY_PERMISSION;
                case "3":
                    return ALLOW_USER_TO_CHOOSE;
                case "4":
                    return VERIFY;
                default:
                    return null;
            }
        }
    };

    // MX10.0 Permissions
    // android.permission.ACCESS_NOTIFICATIONS
    // android.permission.PACKAGE_USAGE_STATS
    // android.permission.SYSTEM_ALERT_WINDOW
    // android.permission.GET_APP_OPS_STATS
    // android.permission.BATTERY_STATS

    // MX10.4 Permissions
    // android.permission.MANAGE_EXTERNAL_STORAGE

    // MX11.5 Permissions
    // android.permission.BIND_NOTIFICATION_LISTENER

    // MX11.9 Permissions
    // android.permission.READ_LOGS

    // MX13.1 Permissions
    // All dangerous permissions
    // List of Dangerous permissions in A13 (2023/07/01):
    //READ_CALENDAR
    //WRITE_CALENDAR
    //CAMERA
    //READ_CONTACTS
    //WRITE_CONTACTS
    //GET_ACCOUNTS
    //ACCESS_FINE_LOCATION
    //ACCESS_COARSE_LOCATION
    //RECORD_AUDIO
    //READ_PHONE_STATE
    //READ_PHONE_NUMBERS
    //CALL_PHONE
    //ANSWER_PHONE_CALLS
    //READ_CALL_LOG
    //WRITE_CALL_LOG
    //ADD_VOICEMAIL
    //USE_SIP
    //PROCESS_OUTGOING_CALLS
    //BODY_SENSORS
    //SEND_SMS
    //RECEIVE_SMS
    //READ_SMS
    //RECEIVE_WAP_PUSH
    //RECEIVE_MMS
    //READ_EXTERNAL_STORAGE
    //WRITE_EXTERNAL_STORAGE
    //ACCESS_MEDIA_LOCATION
    //ACCEPT_HANDOVER
    //ACCESS_BACKGROUND_LOCATION
    //ACTIVITY_RECOGNITION

    public static void grantPermission(Context context, EPermissionType permissionType, IResultCallbacks callbackInterface)
    {
        executeAccessMgrPermissionCommand(context, EPermissionAccessAction.GRANT_PERMISSION, permissionType, callbackInterface);
    }

    public static void denyPermission(Context context, EPermissionType permissionType, IResultCallbacks callbackInterface)
    {
        executeAccessMgrPermissionCommand(context, EPermissionAccessAction.DENY_PERMISSION, permissionType, callbackInterface);
    }

    public static void allowUserToChoosePermission(Context context, EPermissionType permissionType, IResultCallbacks callbackInterface)
    {
        executeAccessMgrPermissionCommand(context, EPermissionAccessAction.ALLOW_USER_TO_CHOOSE, permissionType, callbackInterface);
    }

    public static void verifyPermission(final Context context, final EPermissionType permissionType, final IResultCallbacks callbackInterface)
    {
        executeAccessMgrPermissionCommand(context, EPermissionAccessAction.VERIFY, permissionType, new IResultCallbacks() {
            @Override
            public void onSuccess(String message, String resultXML) {
                // Parse resultXML to check if the permission is granted or not and bubble up the right message to
                // the user callbackInterface
                String toParse = resultXML;
                callbackInterface.onSuccess(message, resultXML);
            }

            @Override
            public void onError(String message, String resultXML) {
                // Bubble up the error message to the user callbackInterface
                callbackInterface.onError(message, resultXML);
            }

            @Override
            public void onDebugStatus(String message) {
                // Bubble up the debug status message to the user callbackInterface
                callbackInterface.onDebugStatus(message);
            }
        });
    }

    private static void executeAccessMgrPermissionCommand(Context context, EPermissionAccessAction permissionAccessAction, EPermissionType permissionType, IResultCallbacks callbackInterface) {
        String profileName = "AccessMgr-1";
        String profileData = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
            String path = context.getApplicationInfo().sourceDir;
            final String strName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            final String strVendor = packageInfo.packageName;

            // Use custom signature if it has been set by the user
            Signature sig = CriticalPermissionsHelper.apkCertificate;

            // Let's check if we have a custom certificate
            if (sig == null) {
                // Nope, we will get the first apk signing certificate that we find
                // You can copy/paste this snippet if you want to provide your own
                // certificate
                // TODO: use the following code snippet to extract your custom certificate if necessary
                final Signature[] arrSignatures = packageInfo.signingInfo.getApkContentsSigners();
                if (arrSignatures == null || arrSignatures.length == 0) {
                    if (callbackInterface != null) {
                        callbackInterface.onError("Error : Package has no signing certificates... how's that possible ?","");
                        return;
                    }
                }
                sig = arrSignatures[0];
            }

            /*
             * Get the X.509 certificate.
             */
            final byte[] rawCert = sig.toByteArray();

            // Get the certificate as a base64 string
            String encoded = Base64.getEncoder().encodeToString(rawCert);

            profileData =
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                            "<characteristic type=\"Profile\">" +
                            "<parm name=\"ProfileName\" value=\"" + profileName + "\"/>" +
                            "<characteristic type=\"AccessMgr\" version=\"11.3\">" +
                            "<parm name=\"PermissionAccessAction\" value=\"" + permissionAccessAction.toString() + "\" />" +
                            "<parm name=\"PermissionAccessPackageName\" value=\"" + context.getPackageName() + "\" />" +
                            "<parm name=\"PermissionAccessPermissionName\" value=\"" + permissionType.toString() + "\" />\n" +
                            "<parm name=\"PermissionAccessSignature\" value=\"" + encoded + "\" />" +
                            "</characteristic>" +
                            "</characteristic>";
            ProfileManagerCommand profileManagerCommand = new ProfileManagerCommand(context);
            profileManagerCommand.execute(profileData, profileName, callbackInterface);
            //}
        } catch (Exception e) {
            e.printStackTrace();
            if (callbackInterface != null) {
                callbackInterface.onError("Error on profile: " + profileName + "\nError:" + e.getLocalizedMessage() + "\nProfileData:" + profileData, "");
            }
        }
    }
}

// android.permission.ACCESS_NOTIFICATIONS
// android.permission.PACKAGE_USAGE_STATS
// android.permission.SYSTEM_ALERT_WINDOW
// android.permission.GET_APP_OPS_STATS
// android.permission.BATTERY_STATS
// android.permission.MANAGE_EXTERNAL_STORAGE


/*
<wap-provisioningdoc>
  <characteristic version="11.3" type="AccessMgr">
    <parm name="PermissionAccessAction" value="1" />
    <parm name="PermissionAccessPermissionName" value="android.permission.BATTERY_STATS" />
    <parm name="PermissionAccessPackageName" value="com.zebra.criticalpermissionshelper" />
    <parm name="PermissionAccessSignature" value="MzA4MjA0NDUzMDgyMDMyZGEwMDMwMjAxMDIwMjA0MDhjZjVlODkzMDBkMDYwOTJhODY0ODg2ZjcwZDAxMDEwYjA1MDAzMDgxZDIzMTIxMzAxZjA2MDM1NTA0MDYxMzE4NzI2NTZkNmY3NDY1NjM2ZjZlNzQ3MjZmNmM2MTcwNzA2YzY5NjM2MTc0Njk2ZjZlMzEyMTMwMWYwNjAzNTUwNDA4MTMxODcyNjU2ZDZmNzQ2NTYzNmY2ZTc0NzI2ZjZjNjE3MDcwNmM2OTYzNjE3NDY5NmY2ZTMxMjEzMDFmMDYwMzU1MDQwNzEzMTg3MjY1NmQ2Zjc0NjU2MzZmNmU3NDcyNmY2YzYxNzA3MDZjNjk2MzYxNzQ2OTZmNmUzMTIxMzAxZjA2MDM1NTA0MGExMzE4NzI2NTZkNmY3NDY1NjM2ZjZlNzQ3MjZmNmM2MTcwNzA2YzY5NjM2MTc0Njk2ZjZlMzEyMTMwMWYwNjAzNTUwNDBiMTMxODcyNjU2ZDZmNzQ2NTYzNmY2ZTc0NzI2ZjZjNjE3MDcwNmM2OTYzNjE3NDY5NmY2ZTMxMjEzMDFmMDYwMzU1MDQwMzEzMTg3MjY1NmQ2Zjc0NjU2MzZmNmU3NDcyNmY2YzYxNzA3MDZjNjk2MzYxNzQ2OTZmNmUzMDFlMTcwZDMxMzgzMTMyMzEzNDMwMzkzNDMzMzMzMDVhMTcwZDM0MzMzMTMyMzAzODMwMzkzNDMzMzMzMDVhMzA4MWQyMzEyMTMwMWYwNjAzNTUwNDA2MTMxODcyNjU2ZDZmNzQ2NTYzNmY2ZTc0NzI2ZjZjNjE3MDcwNmM2OTYzNjE3NDY5NmY2ZTMxMjEzMDFmMDYwMzU1MDQwODEzMTg3MjY1NmQ2Zjc0NjU2MzZmNmU3NDcyNmY2YzYxNzA3MDZjNjk2MzYxNzQ2OTZmNmUzMTIxMzAxZjA2MDM1NTA0MDcxMzE4NzI2NTZkNmY3NDY1NjM2ZjZlNzQ3MjZmNmM2MTcwNzA2YzY5NjM2MTc0Njk2ZjZlMzEyMTMwMWYwNjAzNTUwNDBhMTMxODcyNjU2ZDZmNzQ2NTYzNmY2ZTc0NzI2ZjZjNjE3MDcwNmM2OTYzNjE3NDY5NmY2ZTMxMjEzMDFmMDYwMzU1MDQwYjEzMTg3MjY1NmQ2Zjc0NjU2MzZmNmU3NDcyNmY2YzYxNzA3MDZjNjk2MzYxNzQ2OTZmNmUzMTIxMzAxZjA2MDM1NTA0MDMxMzE4NzI2NTZkNmY3NDY1NjM2ZjZlNzQ3MjZmNmM2MTcwNzA2YzY5NjM2MTc0Njk2ZjZlMzA4MjAxMjIzMDBkMDYwOTJhODY0ODg2ZjcwZDAxMDEwMTA1MDAwMzgyMDEwZjAwMzA4MjAxMGEwMjgyMDEwMTAwYTk0ZDdjMTQ5MmFmZjg1N2E3MTkyODBlOGVmY2JhMzE4NjU1OGRlMjcxYjcwZDI5YTE5YTc0NWI1MjhiMWFkODhhNzVlZDI0NThlYzM5MTJkYjlmZjY4Y2E3ZjEwOTBlNjAwMTAwODE2YTc2M2I4NDNlYTY0YmE2ZDM4MzAzYjdkNWNhNGFiN2RkMWEyMGYxMzU5ZDdjZDE4OWQxY2NmZjk3NzdkZjMzM2M3MWUwMzZjMWE5YmQ0OWE1MTM2ZjNiNThlNWU0NTUwZDBlOWJkMWY4MDQ5Y2MyMTQ4ODNiYTc5NzQ0ZTUxODc5OTIyZDdlNjQ4NzlmNzc4N2QwODc1NDUwN2IwZWY4Y2E4ZTM5YTQ5ZTQxNDcwNjYyNWNhNTkxYWU5MWZmZDY5ZTBlOTQ3M2U3Mjc2ZWRiNTFlN2M4MWFlMjVjNWI4NGYzZDk5MzY3NjEzZjZkMzg1MzFhOGU4ZTFlZDNmOWFkMTFiZDJmNDhjYWJhNWQzZjQwMmZlN2MyNDJhZjczNGYzZWVhOWQ5MWQyYmU0M2QzYjUzMzZiMzEwNjQzYTY5MmM2YTdjODFmMGIyYmVlMWY3ZTI4Yzg5YmIyY2U2ZDRmNmQwZjRmYjNjZTU3OTVlYTc2MDZkNDljMGFlNDUxYzEwYjlmYjEzNjM4NGJiYzUwOGEyOGU5M2QwMjAzMDEwMDAxYTMyMTMwMWYzMDFkMDYwMzU1MWQwZTA0MTYwNDE0MjdkODVmMzVlMmZhNDNiNjM3MDNlNmZiYzE0ZjMyYjk3ZGQzMDdiMzMwMGQwNjA5MmE4NjQ4ODZmNzBkMDEwMTBiMDUwMDAzODIwMTAxMDA1ODhmNTFlMjFjZTk2NDU1MGUzNGRlNTY0NGI2NzBlNDkwZTNmYTljMzFkNGNiMDYyYjhlNWQ3NzIxODAxNWE1NGMxNWFjNmJkYjQ5ZjcwYmRjMTQxZmE0ZWYyNDBmNmIzYzNhODUzZjA0MzJjYjZkZjg2M2ZlMWQxNGFiMGU1YzAyY2ZlMWNhOWU4NjA3MjcyZWMzZDUwZDc5YzUwNDEzZTdhMTMxYTUzZGY4MzRlZjc2MDg4ODBiNWVhYTNhNDFkOGRiMTQ2MmQwN2M1YWIxYTIzNDViYmQ1YmEzMjZmNWEzYWM2YzRlYzQ2ZGJjNWQ5NjA4YWE3YTFlZTNmMmM3OGYxMjRjOTUwNDljZTBmZGMyMDg3OTI1NzIxNDJjMDkxOTBjOTA2YjFkMWYxMDM2MTg1ZjE0NzEzNmU0ODNkZThhY2UxZTY2YmM1MWViMzEyNmFhNzYwY2ZmODkyZmY2OTRkMjc2Y2NkM2U2Njk5ODgzZTMwMmQxNzY1NTM5YzkwNmU3NGQ3YTJkNGNjOGI1MWZjYTA1MGI2Y2I0NmFlMjkzZmM3YzY1MzZmN2RlZWJlOWY4YzY5NWNhODA5OGI1MDFmNWQxZWI4NjNhODQxZTJiZDEzNGE3ZDUxODYzZTdmZTAwMDYwNjA4NWJmYTQ3ZDljZmU0Y2RlMTAwZjQxZg0KDQo=" />
  </characteristic>
</wap-provisioningdoc>
//*/