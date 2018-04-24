package com.diting.voice.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.diting.voice.data.ObserverCallback;
import com.diting.voice.data.RequestApiImpl;
import com.diting.voice.data.body.Contact;
import com.diting.voice.data.body.VoiceCallInfo;
import com.diting.voice.data.observer.CommonInfoObserver;
import com.diting.voice.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPLOAD_CALL_INFO = "com.diting.voice.service.action.UPLOAD_CALL_INFO";
    private static final String ACTION_UPLOAD_CALL_CONTACT= "com.diting.voice.service.action.UPLOAD_CALL_CONTACT";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM = "com.diting.voice.service.extra.data";

    public UploadIntentService() {
        super("UploadIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
        public static void startActionCallInfo(Context context, VoiceCallInfo voiceCallInfo) {
        Intent intent = new Intent(context, UploadIntentService.class);
        intent.setAction(ACTION_UPLOAD_CALL_INFO);
        intent.putExtra(EXTRA_PARAM, voiceCallInfo);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionCallContact(Context context, ArrayList<Contact> contactList) {
        Intent intent = new Intent(context, UploadIntentService.class);
        intent.setAction(ACTION_UPLOAD_CALL_CONTACT);
        intent.putParcelableArrayListExtra(EXTRA_PARAM, contactList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if(action.equals(ACTION_UPLOAD_CALL_INFO)) {
                VoiceCallInfo voiceCallInfo = intent.getParcelableExtra(EXTRA_PARAM);
                RequestApiImpl.newInstance().saveCallInfo(voiceCallInfo, new CommonInfoObserver(callback));
            }else if(action.equals(ACTION_UPLOAD_CALL_CONTACT)){
                List<Contact> contactList = intent.getParcelableArrayListExtra(EXTRA_PARAM);
            }
        }
    }

    private ObserverCallback callback = new ObserverCallback() {
        @Override
        public void onComplete() {

        }

        @Override
        public void onNext(Object o) {
            LogUtils.d("保存成功");
        }

        @Override
        public void onNext(List<?> objects) {

        }

        @Override
        public void onError(String msg) {
            LogUtils.d("保存失败");
        }
    };


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
