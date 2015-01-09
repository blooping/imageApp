package cc.sgd.imageapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

// todo 读取本地图片形成图库
public class MainActivity extends ActionBarActivity {

    private GridViewAdapter gridViewAdapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, i + " " + l,Toast.LENGTH_SHORT).show();
            }
        });
        gridViewAdapter.notifyDataSetChanged();
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
        File dcim = new File(path);
        File[] files = dcim.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    if(f.toString().endsWith(".jpg")) {
                        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(f));
                        imageItems.add(new ImageItem(bitmap, f.toString()));
                    }
                }
            } else {
                if(file.toString().endsWith(".jpg")) {
                    Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(file));
                    imageItems.add(new ImageItem(bitmap, file.toString()));
                }
            }
        }
        return imageItems;
    }
}
