package hackatbrown.com.myopersonaltrainer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
    Activity _activity;
    Button connectButton;
    int _curl_count = 0;
    int _exercise = -1;
    MusicService musicSrv;
    boolean musicBound = false;


    private DeviceListener mListener = new AbstractDeviceListener() {
//        int _curl_count = 0;
        boolean bottom = false;
        boolean top = false;
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

            if (timestamp - oldtime < 1000) {
                return;
            }
            Log.i("pose", "Pose: " + pose);

            oldtime = timestamp;
            switch (pose) {
                case UNKNOWN:
//                    _myo.notifyUserAction();
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

            Log.i("orientation", "pitch: " + pitch + " fist: " + fist + " count: " + _curl_count);

            if (!fist) {
                return;
            }
            switch(_exercise) {
                case ExerciseVariables.BICEP_CURLS:
                    if (pitch >= 45) {
                        bottom = true;
                    } else if (pitch <= -45) {
                        top = true;
                    }

                    if (pitch <= -45 & top & bottom) {
                        _curl_count++;
                        updateCount();
                        top = false;
                        bottom = false;
                    }
                case ExerciseVariables.BENCH_PRESS:

            }
        }
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
        _myoTextView.setText("Count: " + _curl_count);

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
        connectButton = (Button) v.findViewById(R.id.bench_press_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBenchPress();
            }
        });

        connectButton = (Button) v.findViewById(R.id.play_cue_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCue();
            }
        });


        return v;
    }

    public void playCue(){
        Song s=((MyApp)getActivity().getApplicationContext()).getCued().get(0);
        ((MyApp)getActivity().getApplicationContext()).musicSrv.playSong(s);
        ((MyApp)getActivity().getApplicationContext()).musicSrv.seek(s.getCuePos());
        //Log.i("sizecue", s.getCuePos() + "");
        //MusicPlayer mp=new MusicPlayer(this.getActivity().getApplicationContext());
        //mp.playSong("@sound/cups");

    }

    public void connect(View view){
        Context context = _activity.getApplicationContext();
        Intent intent = new Intent(context, ScanActivity.class);
        startActivity(intent);
        Log.d("test",  "done with scan activity");
    }

    public void updateCount() {
        _myoTextView = (TextView)  _activity.findViewById(R.id.myoTextView);
        _myoTextView.setText("Count: " + _curl_count);
    }

    public void doBicepCurls() {
        _exercise = ExerciseVariables.BICEP_CURLS;
    }

    public void doBenchPress() {
        _exercise = ExerciseVariables.BENCH_PRESS;
    }

    // set other options for other exercises

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            Log.i("musicbound",musicBound + "");
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(((MyApp) getActivity().getApplicationContext()).getSongList());
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

}
