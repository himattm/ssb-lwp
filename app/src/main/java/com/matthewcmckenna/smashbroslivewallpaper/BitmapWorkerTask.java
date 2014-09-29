//package com.matthewcmckenna.smashbroslivewallpaper;
//
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
//
//import java.lang.ref.WeakReference;
//
///**
// * Created by mattmckenna on 7/22/14.
// */
//class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
//    private final WeakReference<Bitmap> bitmapWeakReference;
//    private int data = 0;
//
//    public BitmapWorkerTask(Bitmap bitmap) {
//        // Use a WeakReference to ensure the Bitmap can be garbage collected
//        bitmapWeakReference = new WeakReference<Bitmap>(bitmap);
//    }
//
//    // Decode image in background.
//    @Override
//    protected Bitmap doInBackground(Integer... params) {
//        data = params[0];
//        return decodeSampledBitmapFromResource(getResources(), data, 100, 100));
//    }
//
//    // Once complete, see if ImageView is still around and set bitmap.
//    @Override
//    protected void onPostExecute(Bitmap bitmap) {
//        if (bitmapWeakReference != null && bitmap != null) {
//            final Bitmap imageView = bitmapWeakReference.get();
//            if (imageView != null) {
//                imageView.setImageBitmap(bitmap);
//            }
//        }
//    }
//}
