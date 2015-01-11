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

    private GridViewAdapter gridViewAdapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        gridView = (GridView) findViewById(R.id.gridView);
//        gridViewAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
//        gridView.setAdapter(gridViewAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ImageItem imageItem = (ImageItem) adapterView.getItemAtPosition(i);
//                Toast.makeText(MainActivity.this, imageItem.getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        gridViewAdapter.notifyDataSetChanged();
        gridView = (GridView) findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(this, R.layout.row_grid);
        gridView.setAdapter(gridViewAdapter);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList getData() {
        final ArrayList imageItems = new ArrayList();
        // 获取每个图片的文件名以及Bitmap数据

        String path = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        int length = 96;
        File dcim = new File(path);
        File[] files = dcim.listFiles();
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
        options.inSampleSize = 4;
        for (File file : files) {
            if (!file.isDirectory()) {
                if (file.toString().endsWith(".jpg")) {
                    Bitmap bitmap = decodeSampledBitmapFromFile(file.toString());
                    imageItems.add(new ImageItem(bitmap, file.getName()));
                }
            }
        }
        return imageItems;
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
        return decodeSampledBitmapFromFile(filePath, 512, 384);
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
                        publishProgress(file.toString());
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            gridViewAdapter.add(values[0]);
//            gridViewAdapter.notifyDataSetChanged();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
//            gridView = (GridView) findViewById(R.id.gridView);
//            gridViewAdapter = new GridViewAdapter(MainActivity.this, R.layout.row_grid, list);
//            gridView.setAdapter(gridViewAdapter);
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    ImageItem imageItem = (ImageItem) adapterView.getItemAtPosition(i);
//                    Toast.makeText(MainActivity.this, imageItem.getTitle(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            gridViewAdapter.notifyDataSetChanged();
            gridViewAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }
}
