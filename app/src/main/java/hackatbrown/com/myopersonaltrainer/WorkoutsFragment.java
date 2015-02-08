package hackatbrown.com.myopersonaltrainer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class WorkoutsFragment extends Fragment {

    private CustomAdapter mAdapter;
    private ListView workoutsList;

    public WorkoutsFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_workouts, container, false);

        workoutsList = (ListView) v.findViewById(R.id.workoutsList);


            mAdapter = new CustomAdapter(getActivity().getApplicationContext());



            mAdapter.addSectionHeaderItem("Beginner Level");
            mAdapter.addItem("Full Body Variation 1", R.drawable.ic_myo);

//            for (int i = 1; i < 30; i++) {
//                mAdapter.addItem("Row Item #" + i);
//                if (i % 4 == 0) {
//                    mAdapter.addSectionHeaderItem("Section #" + i);
//                }
//            }

            workoutsList.setAdapter(mAdapter);



        return v;
    }


}
