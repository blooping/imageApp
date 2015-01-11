package cc.sgd.imageapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.jar.Manifest;

import cc.sgd.imageapp.MainActivity;
import cc.sgd.imageapp.R;
import cc.sgd.imageapp.model.ImageItem;

/**
 * Created by gdshen95 on 2015/1/9.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<String> data = new ArrayList<>();

    public GridViewAdapter(Context context, int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    public void add(String path) {
        data.add(path);
    }

    public void clear() {
        data.clear();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.textView);
            holder.image = (ImageView) row.findViewById(R.id.imageView);
            holder.position = position;
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
//        Bitmap bitmap = MainActivity.decodeSampledBitmapFromFile(data.get(position));
        new GetViewAsyncTask(position, holder, data.get(position)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        return row;
    }

    private static class GetViewAsyncTask extends AsyncTask<ViewHolder, Void, Bitmap> {

        private int mPosition;
        private ViewHolder mHolder;
        private String mPath;

        public GetViewAsyncTask(int position, ViewHolder holder, String path) {
            mPosition = position;
            mHolder = holder;
            mPath = path;
        }

        @Override
        protected Bitmap doInBackground(ViewHolder... params) {
            Bitmap bitmap = MainActivity.decodeSampledBitmapFromFile(mPath);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mHolder.position == mPosition) {
                mHolder.image.setImageBitmap(bitmap);
                mHolder.imageTitle.setText(mPath);
            }
        }
    }

    static class ViewHolder {
        ImageView image;
        TextView imageTitle;
        int position;
    }
}
