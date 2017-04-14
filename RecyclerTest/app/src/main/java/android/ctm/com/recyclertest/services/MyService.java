package android.ctm.com.recyclertest.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.ctm.com.recyclertest.Consts.INTENT_MESSAGE;
import static android.ctm.com.recyclertest.Consts.INTENT_PARAM_SUCCESS;
import static android.ctm.com.recyclertest.Consts.PARAM_IN_MSG;


public class MyService extends IntentService {

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String msg = intent.getStringExtra(PARAM_IN_MSG);
        Log.e("MyService", msg);

        sendBroadcast(true);
    }

    private void sendBroadcast (boolean success){
        Intent intent = new Intent(INTENT_MESSAGE); //put the same message as in the filter you used in the activity when registering the receiver
        intent.putExtra(INTENT_PARAM_SUCCESS, success);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}

