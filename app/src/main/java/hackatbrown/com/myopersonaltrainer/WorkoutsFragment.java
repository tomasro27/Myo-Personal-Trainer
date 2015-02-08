package hackatbrown.com.myopersonaltrainer;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_workouts, container, false);

        workoutsList = (ListView) v.findViewById(R.id.workoutsList);


            mAdapter = new CustomAdapter(getActivity().getApplicationContext());



            mAdapter.addSectionHeaderItem("Beginner Level");

            mAdapter.addItem("Full Body Variation 1", R.drawable.beginner);
            ArrayList workout = new ArrayList();
            workout.add("Dumbbell Biceps Curl");
            workout.add("Dumbell Raise (Front)");
            workout.add("Dumbbell Triceps kickback");

           ((MyApp) getActivity().getApplicationContext()).workoutsMap.put("Full Body Variation 1", workout);


            mAdapter.addItem("Full Body Variation 2", R.drawable.beginner);
            mAdapter.addSectionHeaderItem("Intermidiate Level");
            mAdapter.addItem("Back and Biceps", R.drawable.ic_myo);
            mAdapter.addItem("Chest and Triceps", R.drawable.ic_myo);
            mAdapter.addSectionHeaderItem("Advanced Level");
            mAdapter.addItem("Shorcut to Size", R.drawable.arnold);
            mAdapter.addItem("Shorcut to Shredd", R.drawable.arnold);

//            for (int i = 1; i < 30; i++) {
//                mAdapter.addItem("Row Item #" + i);
//                if (i % 4 == 0) {
//                    mAdapter.addSectionHeaderItem("Section #" + i);
//                }
//            }

            workoutsList.setAdapter(mAdapter);


            workoutsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Fragment fragment = new WorkoutFragment();

                    Bundle args = new Bundle();

                    args.putString("workoutName", "Full Body Variation 1");
                    fragment.setArguments(args);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.content_fragment , fragment, "workout");
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.commit();
                }
            });


        return v;
    }


}
