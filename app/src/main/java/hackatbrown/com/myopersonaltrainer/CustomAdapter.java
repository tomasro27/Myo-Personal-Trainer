package hackatbrown.com.myopersonaltrainer;

/**
 * Created by tomasrodriguez on 2/7/15.
 */
import java.util.ArrayList;
import java.util.TreeSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class CustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private ArrayList<Integer> drawables = new ArrayList<Integer>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;
    private Context context;
    private TextView textView;
    private ImageView imageView;

    public CustomAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item,  int drawableId) {
        mData.add(item);
        drawables.add(drawableId);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        drawables.add(0);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        int rowType = getItemViewType(position);


            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.workout_row_layout, null);
                    textView = (TextView) convertView.findViewById(R.id.workoutName);

                    imageView = (ImageView) convertView.findViewById(R.id.workoutThumbnail);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(context.getResources(), drawables.get(position), options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    options.inSampleSize = Helper.calculateInSampleSize(options, 200, 200);

                    // Decode bitmap with inSampleSize set
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawables.get(position), options);

                    imageView.setImageBitmap(bitmap);

                    textView.setText(mData.get(position));


                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.snippet_item2, null);
                    textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    textView.setText(mData.get(position));
                    break;
            }





        return convertView;
    }



}
