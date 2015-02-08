package hackatbrown.com.myopersonaltrainer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class WorkoutFragment extends Fragment {

    private ListView workoutList;
    private CustomAdapter mAdapter;
    private ArrayList<String> exerciseNames;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_workout, container, false);

        workoutList = (ListView) v.findViewById(R.id.workoutList);

        mAdapter = new CustomAdapter(getActivity().getApplicationContext());

        Log.d("WorkoutFragment", "getArguments()");
        Bundle bundle = getArguments();

        String string = bundle.getString("workoutName");
        exerciseNames = (ArrayList)((MyApp)getActivity().getApplicationContext()).workoutsMap.get(string);

        for(int i = 0; i < exerciseNames.size(); ++i)
        {
            mAdapter.addItem(exerciseNames.get(i), 0);
        }

        workoutList.setAdapter(mAdapter);

        return v;
    }

}
