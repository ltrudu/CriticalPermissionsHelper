package com.zebra.criticalpermissionshelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.Base64;

/**
 * Device Identifiers Sample
 *
 * Original CriticalPermissionHelper Code
 *          - Trudu Laurent
 *          - https://github.com/ltrudu/DeviceIdentifiersWrapper-Sample
 *
 *  (c) Zebra 2022
 */

class GrantContentPermission extends AsyncTask<Object, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Object... objects) {
        Context context = (Context) objects[0];
        Uri uri = (Uri) objects[1];
        IResultCallbacks idiResultCallbacks = (IResultCallbacks) objects[2];
        registerFolderForAccess(context, uri, idiResultCallbacks);
        return true;
    }

    private static void registerFolderForAccess(Context context, Uri folderOrFileURI, IResultCallbacks callbackInterface)
    {
        String profileName = "AccessMgr-1";
        String profileData = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
            String path = context.getApplicationInfo().sourceDir;
            final String strName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            final String strVendor = packageInfo.packageName;
            Signature sig = CriticalPermissionsHelper.apkCertificate;

            // Let's check if we have a custom certificate
            if(sig == null)
            {
                // Nope, we will get the first apk signing certificate that we find
                // You can copy/paste this snippet if you want to provide your own
                // certificate
                // TODO: use the following code snippet to extract your custom certificate if necessary
                Signature[] arrSignatures = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    arrSignatures = packageInfo.signingInfo.getApkContentsSigners();
                }
                if(arrSignatures == null || arrSignatures.length == 0)
                {
                    if(callbackInterface != null)
                    {
                        callbackInterface.onError("Error : Package has no signing certificates... how's that possible ?", null);
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
            String encoded = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                encoded = Base64.getEncoder().encodeToString(rawCert);
            }

            profileData =
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                            "<characteristic type=\"Profile\">" +
                            "<parm name=\"ProfileName\" value=\"" + profileName + "\"/>" +
                            "<characteristic type=\"AccessMgr\" version=\"9.2\">" +
                            "<parm name=\"OperationMode\" value=\"1\" />" +
                            "<parm name=\"ServiceAccessAction\" value=\"4\" />" +
                            "<parm name=\"ServiceIdentifier\" value=\"" + folderOrFileURI + "\" />" +
                            "<parm name=\"CallerPackageName\" value=\"" + context.getPackageName().toString() + "\" />" +
                            "<parm name=\"CallerSignature\" value=\"" + encoded + "\" />" +
                            "</characteristic>"+
                            "</characteristic>";
            ProfileManagerCommand profileManagerCommand = new ProfileManagerCommand(context);
            profileManagerCommand.execute(profileData, profileName, callbackInterface);
            //}
        } catch (Exception e) {
            e.printStackTrace();
            if(callbackInterface != null)
            {
                callbackInterface.onError("Error on profile: " + profileName + "\nError:" + e.getLocalizedMessage() + "\nProfileData:" + profileData, null);
            }
        }
    }


    private static void getURIValue(Cursor cursor, Uri uri, IResultCallbacks resultCallbacks)
    {
        while (cursor.moveToNext()) {
            if (cursor.getColumnCount() == 0)
            {
                //  No data in the cursor.  I have seen this happen on non-WAN devices
                String errorMsg = "Error: " + uri + " does not exist on this device";
                resultCallbacks.onDebugStatus(errorMsg);
            }
            else{
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    try {
                        @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(i)));
                        resultCallbacks.onSuccess(data, null);
                        cursor.close();
                        return;
                    }
                    catch (Exception e)
                    {
                        resultCallbacks.onDebugStatus(e.getLocalizedMessage());
                    }
                }
            }
        }
        cursor.close();
        resultCallbacks.onError("Data not found in Uri:" + uri, null);
    }
}