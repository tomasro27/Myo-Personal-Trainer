package hackatbrown.com.myopersonaltrainer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;


public class WorkoutFragment extends Fragment {

    private ListView workoutList;
    private CustomAdapter mAdapter;
    private ArrayList<String> exerciseNames;
    private Toolbar toolbar;

    private TextView workoutTitle;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_workout, container, false);

        // = (Toolbar) v.findViewByI
        // d(R.id.toolbar_actionbar);

        //set toolbar appearance

        //for crate home button

//        ActionBarActivity activity = (ActionBarActivity) getActivity();
//        toolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
//        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mAdapter = new CustomAdapter(getActivity().getApplicationContext());

        Log.d("WorkoutFragment", "getArguments()");
        Bundle bundle = getArguments();

        String string = bundle.getString("workoutName");


        workoutTitle = (TextView) v.findViewById(R.id.workoutTitle);
        workoutTitle.setText(string);

        workoutList = (ListView) v.findViewById(R.id.workoutList);


        exerciseNames = (ArrayList)((MyApp)getActivity().getApplicationContext()).workoutsMap.get(string);

        for(int i = 0; i < exerciseNames.size(); ++i)
        {
            mAdapter.addItem(exerciseNames.get(i), 0);
        }

        workoutList.setAdapter(mAdapter);





        return v;
    }


}
