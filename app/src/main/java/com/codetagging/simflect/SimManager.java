package com.codetagging.simflect;

import android.content.Context;

import com.codetagging.simflect.builder.SimflectBuilder;
import com.codetagging.simflect.models.*;

/**
 * Created by govind on 23/05/16.
 */
public class SimManager {

    private static SimManager simManager;
    private PhoneDetails phoneDetails;
    private setOnPhoneDetailsFetched setOnPhoneDetailsFetchedListener;

    public static SimManager getInstance() {
        if (simManager == null) {
            simManager = new SimManager();
        }
        return simManager;
    }

    public interface setOnPhoneDetailsFetched {
        void onFetched(PhoneDetails phoneDetails);
    }

    public void getPhoneDetails(Context context, setOnPhoneDetailsFetched setOnPhoneDetailsFetched) {
        phoneDetails = new PhoneDetails();
        setOnPhoneDetailsFetchedListener = setOnPhoneDetailsFetched;
        SimflectBuilder.buildDetail(context, setOnPhoneDetailsFetched);
    }

}
