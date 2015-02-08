package hackatbrown.com.myopersonaltrainer;

import android.app.Activity;
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

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;

import com.thalmic.myo.scanner.ScanActivity;

public class MyoFragment extends Fragment {

    TextView _myoTextView;
    TextView _exerciseTextView;

    Activity _activity;
    Button connectButton;
    int _exerciseCount = 0;
    int _exercise = -1;

    int delay = 0;
    boolean bottom = false;
    boolean top = false;

    private DeviceListener mListener = new AbstractDeviceListener() {
        boolean fist = false;
        long oldtime = 0;
        Myo _myo;

        @Override
        public void onConnect(Myo myo, long timestamp) {
            //Toast.makeText(mContext, "Myo Connected!", Toast.LENGTH_SHORT).show();
            _myo = myo;
            Log.d("pose", "connected");
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            //Toast.makeText(mContext, "Myo Disconnected!", Toast.LENGTH_SHORT).show();
            _myo = null;
            Log.d("pose", "disconnected");
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {

//            if (timestamp - oldtime < 1000) {
//                return;
//            }
//            Log.i("pose", "Pose: " + pose);

            oldtime = timestamp;
            switch (pose) {
                case UNKNOWN:
                    fist = false;
                    break;
                case FIST:
                    if (!fist) {
                        _myo.notifyUserAction();
                        fist = true;
                    }
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

            if (delay >= 2) {
                Log.i("orientation", "pitch :" + pitch + " fist: " + fist);
                delay = 0;
            } else {
                delay++;
            }

            if (!fist) {
                return;
            }
            switch(_exercise) {
                case ExerciseVariables.BICEP_CURLS:
                    if (pitch >= 40) {
                        bottom = true;
                    } else if (pitch <= -40) {
                        top = true;
                    }

                    if (pitch <= -40 && top && bottom) {
                        _exerciseCount++;
                        updateCount();
                        top = false;
                        bottom = false;
                    }
                case ExerciseVariables.DELTOID_RAISE:
                    if (pitch <= -65) {
                        bottom = true;
                    } else if (pitch >= -5) {
                        top = true;
                    }

                    if (pitch >= -5 && top && bottom) {
                        _exerciseCount++;
                        updateCount();
                        top = false;
                        bottom = false;
                    }
            }
        }

//        public void onAccelerometerData (Myo myo, long timestamp, Vector3 accel) {
//            double x = accel.x();
//            double y = accel.y();
//            double z = accel.z();
//
//            Log.i("accel", "x: "+ x + " y: " + y + " z: " + z);
//        }

    };



    public MyoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _activity = this.getActivity();
        Hub hub = Hub.getInstance();
        if (!hub.init(_activity)) {
            Log.e("test", "Could not initialize the Hub.");
            _activity.finish();
            return;
        }
        hub.setLockingPolicy(Hub.LockingPolicy.NONE);

        hub.addListener(mListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_myo, container, false);

        _myoTextView = (TextView) v.findViewById(R.id.myoTextView);
        _myoTextView.setText("Count: " + _exerciseCount);

        _exerciseTextView = (TextView) v.findViewById(R.id.exerciseTextView);
        _exerciseTextView.setText("Exercise: None");

        // myo connect button
        connectButton = (Button) v.findViewById(R.id.button_connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(v);
            }
        });

        // bicep curl button
        connectButton = (Button) v.findViewById(R.id.bicep_curls_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBicepCurls();
            }
        });

        // bench press button
        connectButton = (Button) v.findViewById(R.id.deltoid_raise_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDeltoidRaise();
            }
        });

        return v;
    }

    public void connect(View view){
        Context context = _activity.getApplicationContext();
        Intent intent = new Intent(context, ScanActivity.class);
        startActivity(intent);
        Log.d("test",  "done with scan activity");
    }

    public void updateCount() {
        _myoTextView = (TextView)  _activity.findViewById(R.id.myoTextView);
        _myoTextView.setText("Count: " + _exerciseCount);
    }

    public void doBicepCurls() {
        bottom = false;
        top = false;
        _exerciseCount = 0;
        _exercise = ExerciseVariables.BICEP_CURLS;
        _exerciseTextView.setText("Exercise: Bicep Curls");
    }

    public void doDeltoidRaise() {
        bottom = false;
        top = false;
        _exerciseCount = 0;
        _exercise = ExerciseVariables.DELTOID_RAISE;
        _exerciseTextView.setText("Exercise: Deltoid Raise");
    }

    public void doTricepsKickback() {
        bottom = false;
        top = false;
        _exerciseCount = 0;
        _exercise = ExerciseVariables.TRICEPS_KICKBACK;
        _exerciseTextView.setText("Exercise: Triceps Kickback");
    }

    // set other options for other exercises

}
