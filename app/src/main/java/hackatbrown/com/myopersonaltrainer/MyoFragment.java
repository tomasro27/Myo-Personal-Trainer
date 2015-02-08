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

import java.util.ArrayList;

public class MyoFragment extends Fragment {

    TextView _myoTextView;
    TextView _exerciseTextView;


    Button connectButton;



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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_myo, container, false);



//        // myo connect button
//        connectButton = (Button) v.findViewById(R.id.button_connect);
//        connectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                connect(v);
//            }
//        });





        return v;
    }





}
