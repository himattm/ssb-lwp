package com.matthewcmckenna.smashbroslivewallpaper;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.util.LruCache;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.animation.Animation;

/**
 * Created by mattmckenna on 7/22/14.
 */
public class mWallpaperService extends WallpaperService {

    @Override // TODO -------------------
    public Engine onCreateEngine() {
        return new AnimationEngine();
    }

    // Wallpaper Engine
    protected class AnimationEngine extends Engine {

        // Animation Alterators
        private int num = 0;
        BitmapFactory.Options options;
        private boolean touched = false;
        private GestureDetector gestureDetector;
        //private boolean touchEnabled;
        // END Alterators

        // Caching
//        private LruCache<String, Bitmap> mMemoryCache;

        Bitmap[] bitmaps = new Bitmap[Assets.MIX_INTRO_ANIM.length];

        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                iteration();
                drawFrame(num);
            }
        };
        private boolean visible = true;
        public Bitmap backgroundImage;

        AnimationEngine() {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mWallpaperService.this);
            //touchEnabled = prefs.getBoolean("touch", false);

            // This works for images of the ame size only
            // Loads only the size of the first bitmap
            options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = true; // Only the bounds of the image are grabbed
            BitmapFactory.decodeResource(getResources(), Assets.MIX_INTRO_ANIM[num], options); // Grab first bitmap bounds
            backgroundImage = Bitmap.createBitmap(options.outWidth, options.outHeight, Bitmap.Config.RGB_565);

            // Load the actual image into the current bitmap
            options.inJustDecodeBounds = false;
            options.inBitmap = backgroundImage; // Req. API 11
            options.inSampleSize = 2; // TODO can change in settings using preferences
            BitmapFactory.decodeResource(getResources(), Assets.MIX_INTRO_ANIM[num], options);
        }


        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
//                    Log.e("onDoubleTap", e.toString());
                    touched = true;
                    return true;
                }
            });

        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            // if screen wallpaper is visible then draw the image otherwise do not draw
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            drawFrame(num);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            gestureDetector.onTouchEvent(event);
//            Log.e("NUMBERS", num + "");
//            Log.e("DoubleTap", "Touched = " + touched);
        }

        void drawFrame(int num) {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                // clear the canvas
                if (c != null) {
                    c.drawColor(Color.BLACK);
                    // draw the background image
                    c.drawBitmap(backgroundImage, (c.getWidth() - backgroundImage.getWidth()) / 2, (c.getHeight() - backgroundImage.getHeight()) / 2, null);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }

            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 0);
            }

        }

        public void iteration() {
//            Log.e("DoubleTap", "Touched Before = " + touched);
            //if (touchEnabled) {
                if (touched) {
//                Log.e("DoubleTap", "Touched entered = " + touched);
                    if (num >= Assets.MIX_INTRO_ANIM.length - 1) {
//                    Log.e("DoubleTap", "Touched restarting = " + touched);
                        this.num = 0;
                        num = 0;
                        touched = false;
                    }
                } else { // Looping
                    if (num >= 176) {
                        num = 118;
                    }
                }
//           } else { // Touch not enabled and looping
//                if (num >= 177) {
//                    num = 119;
//                }
//            }

//            backgroundImage = bitmaps[num++];

            // Reuse bitmap
            BitmapFactory.Options bitmapOptions = null;
            bitmapOptions = options;
            bitmapOptions.inBitmap = backgroundImage;
            BitmapFactory.decodeResource(getResources(), Assets.MIX_INTRO_ANIM[num++], bitmapOptions);
        }


    }


}