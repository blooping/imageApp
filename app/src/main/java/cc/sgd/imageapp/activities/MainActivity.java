package cc.sgd.imageapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

import cc.sgd.imageapp.R;
import cc.sgd.imageapp.activities.ImageDetialViewActivity;
import cc.sgd.imageapp.views.adapters.GridViewAdapter;

public class MainActivity extends ActionBarActivity {
    //TODO use content to get all picture in sdcard

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
                Intent intent = new Intent(getApplicationContext(), ImageDetialViewActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
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
        if (id == R.id.action_upload_test) {
            Toast.makeText(getApplicationContext(), "Start Upload", Toast.LENGTH_SHORT).show();
//            String filePath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/upload1.jpg";
//            HttpFileUpload httpFileUpload =  new HttpFileUpload(filePath);
//            try {
//                httpFileUpload.Send_Now();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            Toast.makeText(getApplicationContext(), "Upload completed", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Click setting", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private class BitmapWorkerTask extends AsyncTask<Void, String, Void> {
        GridViewAdapter gridViewAdapter;
        private File targetDirector;

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
                    if (!file.toString().endsWith(".thumbnails"))
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
