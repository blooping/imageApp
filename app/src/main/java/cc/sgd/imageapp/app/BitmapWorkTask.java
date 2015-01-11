//package cc.sgd.imageapp.app;
//
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
//import android.widget.ImageView;
//
//import java.lang.ref.WeakReference;
//
///**
//* Created by gdshen95 on 2015/1/10.
//*/
//public class BitmapWorkTask extends AsyncTask<Integer,void,Bitmap>{
//    //WeakReference 使用与垃圾回收
//
//    private final WeakReference<ImageView> imageViewWeakReference;
//    private int data = 0;
//
//    public BitmapWorkTask(WeakReference<ImageView> imageViewWeakReference) {
//        this.imageViewWeakReference = imageViewWeakReference;
//    }
//
//    @Override
//    protected Bitmap doInBackground(Integer... params) {
//        return null;
//    }
//}
