package com.periyar.reg13bce0722.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.Arrays;

// Extend the class from BroadcastReceiver to listen when there is a incoming call
public class CallBarring extends BroadcastReceiver
{
    // This String will hold the incoming phone number
    private String number;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
            return;

            // Else, try to do some action
        else
        {
            // Fetch the number of incoming call
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            // Check, whether this is a member of "Black listed" phone numbers stored in the database

if(lafCache.lob && lafCache.proxy) {
    // If yes, invoke the method
   if(!Arrays.asList(lafCache.CalledNo).contains(number)) {
        try {

            lafCache.CalledNo[lafCache.count++] = number;
            if(lafCache.smsfeed) {
                 SmsManager smsManager = SmsManager.getDefault();
              smsManager.sendTextMessage(number, null, "Sorry ! I'm in Library. If its urgent, TEXT ME!", null, null);

            }
        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }
    disconnectPhoneItelephony(context);

    return;
}
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void disconnectPhoneItelephony(Context context)
    {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
          //  telephonyService.silenceRinger();


            telephonyService.endCall();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

