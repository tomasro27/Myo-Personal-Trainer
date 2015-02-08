package hackatbrown.com.myopersonaltrainer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.Menu;
import android.view.MenuItem;


import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.scanner.ScanActivity;

public class MyoFragment extends Fragment {

    TextView myoTextView;
    Activity activity;
    Button connectButton;

    private DeviceListener mListener = new AbstractDeviceListener() {
        int curl_count = 0;
        boolean bottom = false;
        boolean top = false;
        boolean fist = false;
        long oldtime = 0;

        @Override
        public void onConnect(Myo myo, long timestamp) {
            //Toast.makeText(mContext, "Myo Connected!", Toast.LENGTH_SHORT).show();
            Log.d("pose", "connected");
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            //Toast.makeText(mContext, "Myo Disconnected!", Toast.LENGTH_SHORT).show();
            Log.d("pose", "disconnected");
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {

            if (timestamp - oldtime < 1000) {
                return;
            }
            Log.i("pose", "Pose: " + pose);

            oldtime = timestamp;
            switch (pose) {
                case UNKNOWN:
                    fist = false;
                    break;
                case FIST:
                    fist = true;
                    break;
                case FINGERS_SPREAD:
                    fist = false;
                    break;
                case REST:
                    fist = false;
                    break;
                case WAVE_IN:
                    fist = false;
                    break;
                case WAVE_OUT:
                    fist = false;
                    break;
            }
        }

        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));

            Log.i("orientation", "timestamp: " + timestamp + " pitch: " + pitch + " fist: " + fist + " count: " + curl_count);
//            Log.i("orientation", "yaw: " + yaw);

            if (!fist) {
                return;
            }

            if (pitch >= 45) {
                bottom = true;
            } else if (pitch <= -45) {
                top = true;
            }

            if (pitch <= -45 & top & bottom) {
                curl_count++;
                top = false;
                bottom = false;
            }
        }
    };



    public MyoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = this.getActivity();
        Hub hub = Hub.getInstance();
        if (!hub.init(activity)) {
            Log.e("test", "Could not initialize the Hub.");
            activity.finish();
            return null;
        }
        hub.setLockingPolicy(Hub.LockingPolicy.NONE);
        Log.i("test", "reaches");

        hub.addListener(mListener);
        Log.i("test", "reaches after hub");


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_myo, container, false);

        myoTextView = (TextView) v.findViewById(R.id.myoTextView);
        myoTextView.setText("MYO IS AWESOME");
        connectButton = (Button) v.findViewById(R.id.button_connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(v);
            }
        });

        return v;
    }


    public void connect(View view){
        Context context = activity.getApplicationContext();
        Intent intent = new Intent(context, ScanActivity.class);
        startActivity(intent);
        Log.d("test",  "done with scan activity");
    }

}
