package com.osapp.appwidget.wg.wgContact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ContactReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction()+"";
        if(action.equals("CALL")){
            String phone = intent.getStringExtra("number");
            Intent intentCALL = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phone));
            intentCALL.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentCALL);
        }else if(action.equals("SMS")){
            Uri uri = Uri.parse("smsto:"+intent.getStringExtra("number"));
            Intent intentSMS = new Intent(Intent.ACTION_SENDTO, uri);
            intentSMS.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentSMS);
        }

    }
}
