package cc.sgd.imageapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import cc.sgd.imageapp.adapter.GridViewAdapter;
import cc.sgd.imageapp.model.ImageItem;

public class MainActivity extends ActionBarActivity {
    // todo 使用图片缓存来提升速度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, R.layout.row_grid);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String path = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
            }
        });
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(gridViewAdapter);
        bitmapWorkerTask.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "您点击了设置", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight
    ) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options) {
        return calculateInSampleSize(options, 512, 384);
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    public static Bitmap decodeSampledBitmapFromFile(String filePath) {
        return decodeSampledBitmapFromFile(filePath, 320, 240);
    }


    private class BitmapWorkerTask extends AsyncTask<Void, String, Void> {
        private File targetDirector;
        GridViewAdapter gridViewAdapter;

        public BitmapWorkerTask(GridViewAdapter gridViewAdapter) {
            this.gridViewAdapter = gridViewAdapter;
        }

        @Override
        protected void onPreExecute() {
            String path = Environment.getExternalStorageDirectory().toString() + "/DCIM";
            targetDirector = new File(path);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {

            File[] files = targetDirector.listFiles();
            for (File file : files) {
                if (!file.isDirectory()) {
                    if (file.toString().endsWith(".jpg")) {
                        publishProgress(file.getAbsolutePath());
                    }
                } else {
                    if(!file.toString().endsWith(".thumbnails"))
                    for (File f : file.listFiles()) {
                        if (f.toString().endsWith(".jpg")) {
                            publishProgress(f.getAbsolutePath());
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            gridViewAdapter.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            gridViewAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }
}
