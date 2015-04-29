package baidu.com.ndkdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "NDKDemo";
    private static final boolean DEBUG = true;

    private static final int MSG_GET_NATIVESTR = 1001;

    private Button mButton;
    private Context mContext;
    private Client mClient;
    private String mNativeStr;
    private NativeMethod mNativeMethod;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_NATIVESTR:
                    Toast.makeText(mContext, mNativeStr, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
    }

    private void initData() {
        mContext = this;
        mClient = Client.getInstance(mContext);
        mNativeMethod = new NativeMethod();
    }

    @Override
    public void onClick(View v) {
        if (v == mButton) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mNativeMethod.callJavaCode();
                    try {
                        mNativeStr = mClient.getNativeString(true);
                        mHandler.sendEmptyMessage(MSG_GET_NATIVESTR);
                    } catch (RemoteException e) {
                        if (DEBUG) {
                            Log.d(TAG, "getNativeString RemoteException");
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
