package musetest.disoul.com.musetest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTouch;

public class ChinaMapActivity extends ActionBarActivity {

    private static final String TAG = "ChinaMapActivity";

    @InjectView(R.id.window)
    View window;

    @InjectView(R.id.imageMask)
    ImageView imageMask;

    Bitmap maskBitmap;

    public static void launch(final Context ctx){
        Intent intent = new Intent(ctx,ChinaMapActivity.class);
        ctx.startActivity(intent);
    }

    @OnTouch(R.id.window)
    protected boolean onTouch(final View v, final MotionEvent event) {
        Log.v(TAG, "onTouch: view=" + v + ", event=" + event);

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                final float fracX = event.getX() / v.getWidth();
                final float fracY = event.getY() / v.getHeight();
                Log.v(TAG, "(fracX, fracY) = (" + fracX + ", " + fracY + ")");

                final int touchX = (int) (maskBitmap.getWidth() * fracX);
                final int touchY = (int) (maskBitmap.getHeight() * fracY);
                Log.v(TAG, "(x, y) = (" + touchX + ", " + touchY + ")");

                final int touchColor = maskBitmap.getPixel(touchX, touchY);

                final String provinceName = ProvinceMappingData.PROVINCE_COLOR_MAP.get(touchColor
                );
                if (provinceName == null) {
                    Log.d(TAG, "color " + Integer.toHexString(touchColor) + " not defined");
                    return false;
                }

                Log.i(TAG, "province clicked: " + provinceName);

                ProvinceMapActivity.launch(this, provinceName);
                this.finish();
                return true;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_china_map);
        ButterKnife.inject(this);

        initMaskBitmap();
        // initDpiFactor();
    }

    private void initMaskBitmap() {
        //imageMask.setDrawingCacheEnabled(true);
        imageMask.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        imageMask.layout(0, 0, imageMask.getMeasuredWidth(), imageMask.getMeasuredHeight());
        //imageMask.buildDrawingCache();
        //maskBitmap = Bitmap.createBitmap(imageMask.getDrawingCache());
        //imageMask.setDrawingCacheEnabled(false);
        maskBitmap = loadBitmapFromView(imageMask);
    }

    // private void initDpiFactor() {
    //     // XXX: what the fxxk
    //     dpiFactor = getResources().getDisplayMetrics().density / 2;
    //     Log.d(TAG, "dpiFactor = " + dpiFactor);
    // }

    private static Bitmap loadBitmapFromView(final View v) {
        final int w = v.getMeasuredWidth();
        final int h = v.getMeasuredHeight();
        Log.v(TAG, "loading " + w + "x" + h + " bitmap");

        final Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        final Canvas c = new Canvas(b);
        //v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_china_map, menu);
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
}
