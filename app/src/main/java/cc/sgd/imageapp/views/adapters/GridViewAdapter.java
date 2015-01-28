package cc.sgd.imageapp.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import cc.sgd.imageapp.R;

/**
 * Created by gdshen95 on 2015/1/9.
 */
public class GridViewAdapter extends BaseAdapter {
    DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
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
            holder.image = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Picasso.with(context).load(new File(data.get(position))).fit().centerCrop().into(holder.image);

        return row;
    }

    //
    static class ViewHolder {
        ImageView image;
    }
}
