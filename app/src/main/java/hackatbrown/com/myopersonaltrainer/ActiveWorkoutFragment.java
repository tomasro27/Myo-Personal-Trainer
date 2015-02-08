package hackatbrown.com.myopersonaltrainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.scanner.ScanActivity;


public class ActiveWorkoutFragment extends Fragment {

    TextView counter;
    TextView tittle;

    MusicService musicSrv;
    boolean musicBound = false;

    float old_yaw = 0;
    boolean yaw_overflow = false;

    int delay = 0;
    boolean bottom = false;
    boolean top = false;
    Activity _activity;
    int _exerciseCount = 0;
    int _exercise = 0;
    boolean setStarted = false;




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

            if (delay >= 3) {
                Log.i("orientation", "pitch :" + pitch + " fist: " + fist);
//                Log.i("orientation", "yaw :" + yaw + " fist: " + fist);
                delay = 0;
            } else {
                delay++;
            }

            if (!fist && _exerciseCount <= 0) {
                return;
            }
            else if (fist && !setStarted)
            {
                setStarted = true;

                switch(_exercise) {
                    case ExerciseVariables.BICEP_CURLS:
                        doBicepCurls();
                        break;
                    case ExerciseVariables.DELTOID_RAISE_FRONT:
                        doDeltoidRaiseFront();
                        break;
                    case ExerciseVariables.DELTOID_RAISE_LATERAL:
                        doDeltoidRaiseLateral();
                        break;
                    case ExerciseVariables.TRICEPS_KICKBACK:
                        doTricepsKickback();
                        break;
                }

            }

            switch(_exercise) {
                case ExerciseVariables.BICEP_CURLS:


                    if (pitch >= 35) {
                        bottom = true;
                    } else if (pitch <= -35) {
                        top = true;
                    }

                    if (pitch <= -35 && top && bottom) {
                        updateCount();
                        top = false;
                        bottom = false;
                    }
                    break;
                case ExerciseVariables.DELTOID_RAISE_LATERAL:
                    if (pitch <= -65) {
                        bottom = true;
                    } else if (pitch >= -5) {
                        top = true;
                    }

                    if (pitch >= -5 && top && bottom) {
                        updateCount();
                        top = false;
                        bottom = false;
                    }
                    break;
                case ExerciseVariables.DELTOID_RAISE_FRONT:
                    if (pitch <= -65) {
                        bottom = true;
                    } else if (pitch >= -5) {
                        top = true;
                    }

                    if (pitch >= -5 && top && bottom) {
                        updateCount();
                        top = false;
                        bottom = false;
                    }
                    break;
                case ExerciseVariables.TRICEPS_KICKBACK:
                    if (pitch <= -75) {
                        bottom = true;
                    } else if (pitch >= -20) {
                        top = true;
                    }

                    if (pitch >= -20 && top && bottom) {
                        updateCount();
                        top = false;
                        bottom = false;
                    }
                    break;
                case ExerciseVariables.BACK_FLYES:
                    if (old_yaw == 0) {
                        old_yaw = yaw;
                        bottom = true;
                    }
                    else if (Math.abs(yaw - old_yaw) <= 10) {
                        bottom = true;
                    }

                    if ((old_yaw < -100 && yaw > 100) || (old_yaw > 100 && yaw < -100)) {
                        yaw_overflow = true;
                    }

                    if (yaw_overflow) {
                        if (Math.abs(yaw - old_yaw) >= 320 && bottom) {
                            top = true;
                        }
                    } else {
                        if (Math.abs(yaw - old_yaw) >= 40 && bottom) {
                            top = true;
                        }
                    }

                    if (top && bottom) {
                        updateCount();
                        top = false;
                        bottom = false;
                    }
                    break;

            }
        }
    };

    public ActiveWorkoutFragment() {
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
        View v = inflater.inflate(R.layout.fragment_active_workout, container, false);

        tittle = (TextView) v.findViewById(R.id.activeWorkoutTitle);
        counter = (TextView) v.findViewById(R.id.counter);

        //_myoTextView = (TextView) v.findViewById(R.id.myoTextView);
        counter.setText(Integer.toString(_exerciseCount));



//
//        while(_exerciseCount < 11)
//        {
//
//        }
//
//        doDeltoidRaiseFront();
//
//        while(_exerciseCount < 11)
//        {
//
//        }
//
//        doDeltoidRaiseLateral();
//
//        while(_exerciseCount < 11)
//        {
//
//        }
//
//        doTricepsKickback();

        //_exerciseTextView = (TextView) v.findViewById(R.id.exerciseTextView);
        //_exerciseTextView.setText("Exercise: None");






        return v;
    }



    public void playCue(){
        Song s=((MyApp)getActivity().getApplicationContext()).getCued().get(0);
        ((MyApp)getActivity().getApplicationContext()).musicSrv.playSong(s);
        ((MyApp)getActivity().getApplicationContext()).musicSrv.seek(s.getCuePos());
        //Log.i("sizecue", s.getCuePos() + "");
        //MusicPlayer mp=new MusicPlayer(this.getActivity().getApplicationContext());
        //mp.playSong(R.raw.cups);
    }



    public void updateCount() {
        _exerciseCount++;
        //_myoTextView = (TextView)  _activity.findViewById(R.id.myoTextView);
        counter.setText(Integer.toString(_exerciseCount));


        if (_exerciseCount >= 10) {
            setStarted = false;
            switch(_exercise) {
                case ExerciseVariables.BICEP_CURLS:
                    _exercise = ExerciseVariables.DELTOID_RAISE_FRONT;
                    _exerciseCount = 0;
                    break;
                case ExerciseVariables.DELTOID_RAISE_FRONT:
                    _exercise = ExerciseVariables.DELTOID_RAISE_LATERAL;
                    _exerciseCount = 0;

                    break;
                case ExerciseVariables.DELTOID_RAISE_LATERAL:
                    _exercise = ExerciseVariables.TRICEPS_KICKBACK;
                    _exerciseCount = 0;

                    break;
                case ExerciseVariables.TRICEPS_KICKBACK:
                    _exercise = -1;
                    _exerciseCount = 0;

                    EndWorkout();
                    break;
            }
        }
    }

    public void doBicepCurls() {

        bottom = false;
        top = false;
        _exerciseCount = -1;
        updateCount();
        _exercise = ExerciseVariables.BICEP_CURLS;
        tittle.setText("Bicep Curls");
        playCue();
    }

    public void doDeltoidRaiseFront() {

        bottom = false;
        top = false;
        _exerciseCount = -1;
        updateCount();
        _exercise = ExerciseVariables.DELTOID_RAISE_FRONT;
        tittle.setText("Dumbbell Raise (Front)");
    }

    public void doDeltoidRaiseLateral() {
        bottom = false;
        top = false;
        _exerciseCount = -1;
        updateCount();
        _exercise = ExerciseVariables.DELTOID_RAISE_LATERAL;
        tittle.setText("Dumbbell Raise (Lateral)");
    }

    public void doTricepsKickback() {
        bottom = false;
        top = false;
        _exerciseCount = -1;
        updateCount();
        _exercise = ExerciseVariables.TRICEPS_KICKBACK;
        tittle.setText("Exercise: Triceps Kickback");
    }

    public void doBackFlyes() {
        bottom = false;
        top = false;
        _exerciseCount = -1;
        updateCount();
        old_yaw = 0;
        _exercise = ExerciseVariables.BACK_FLYES;
        tittle.setText("Exercise: Back Flyes");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.connect) {

            Context context = getActivity().getApplicationContext();
            Intent intent = new Intent(context, ScanActivity.class);
            startActivity(intent);
            Log.d("test",  "done with scan activity");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void EndWorkout(){
        tittle.setText("Workout Completed!");
        counter.setText("Congrats!");
    }



}
