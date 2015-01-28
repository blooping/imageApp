package cc.sgd.imageapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import cc.sgd.imageapp.R;


public class ImageDetialViewActivity extends ActionBarActivity {

    public static final MediaType MEDIA_TYPE_JPG
            = MediaType.parse("image/jpg");

    private final OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detial_view);

        ImageView imageView = (ImageView) findViewById(R.id.imageDetail);
        String path = getIntent().getStringExtra("path");

        Picasso.with(this).load(new File(path)).into(imageView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_detial_view, menu);
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

    public void onClick(View view) throws Exception {
        Toast.makeText(this, "Upload start", Toast.LENGTH_LONG).show();
        String path = getIntent().getStringExtra("path");
        Log.d(getResources().getString(R.string.tag), path);
        run(path);
    }

    public void run(String filePath) throws Exception {
        File file = new File(filePath);
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"" + file.getName() + "\""),
                        RequestBody.create(MEDIA_TYPE_JPG, file))
                .build();

        Request request = new Request.Builder()
                .url("http://sgd.freeshell.ustc.edu.cn/imcloud/upload")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Log.d(getResources().getString(R.string.tag), response.body().string());
            }
        });
    }
}

