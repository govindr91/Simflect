package codetagging.simflect;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import codetagging.simflect.models.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimManager simManager = SimManager.getInstance();
        Log.d(TAG, "Start " + Calendar.getInstance().getTimeInMillis());
        simManager.getPhoneDetails(this, new SimManager.setOnPhoneDetailsFetched() {
            @Override
            public void onFetched(PhoneDetails phoneDetails) {
                Log.d(TAG, "End " + Calendar.getInstance().getTimeInMillis());
                Gson gson = new Gson();
                Log.d(TAG, "Success - " + gson.toJson(phoneDetails).toString());
            }
        });
        printTelephonyManagerMethodNamesForThisDevice(this);
    }
    public static ArrayList<String> printTelephonyManagerMethodNamesForThisDevice(Context context) {

        ArrayList<String> methodNames = new ArrayList<>();
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;
        try {
            telephonyClass = Class.forName(telephony.getClass().getName());
            Method[] methods = telephonyClass.getMethods();
            for (int idx = 0; idx < methods.length; idx++) {
                System.out.println("\n" + methods[idx]);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return methodNames;
    }
}
