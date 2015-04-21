package musetest.disoul.com.musetest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTouch;


public class ProvinceMapActivity extends ActionBarActivity {

    private static final String ARG_PROVINCE_NAME = "musetest.com.disoul.musetest.PROVINCE_NAME";

    @InjectView(R.id.provinceImage)
    ImageView provinceImage;

    @InjectView(R.id.provinceMask)
    ImageView provinceMask;

    private Bitmap maskBitmap;

    private String provinceName;

    private final String TAG = "ProvinceMapActivity";

    public static void launch(final Context ctx, final String provinceName) {
        Intent intent = new Intent(ctx, ProvinceMapActivity.class);
        intent.putExtra(ARG_PROVINCE_NAME, provinceName);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_map);
        ButterKnife.inject(this);

        final Intent intent = getIntent();
        provinceName = intent.getStringExtra(ARG_PROVINCE_NAME);

        provinceImage.setImageDrawable(getProvinceImageDrawable(provinceName));
        provinceMask.setImageDrawable(getProvinceImageDrawable(provinceName + "_mask"));
        initMaskBitmap();
    }

    @OnTouch(R.id.window)
    boolean onTouch(final View v, final MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                final float fracX = event.getX() / v.getWidth();
                final float fracY = event.getY() / v.getHeight();

                final int touchX = (int) (maskBitmap.getWidth() * fracX);
                final int touchY = (int) (maskBitmap.getHeight() * fracY);

                final int touchColor = maskBitmap.getPixel(touchX, touchY);

                Log.d(TAG,"touchcolor"+Integer.toHexString(touchColor));

                final String cityName = CityMappingData.CITY_COLOR_MAP.get(touchColor);
                if (cityName == null) {
                    return false;
                }
                Log.d(TAG,"cityname = "+cityName);
                PanoramaActivity.launch(this);
                this.finish();
                return true;
        }

        return true;
    }

    private void initMaskBitmap() {
        //provinceMask.setDrawingCacheEnabled(true);
        provinceMask.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        provinceMask.layout(0, 0, provinceMask.getMeasuredWidth(), provinceMask.getMeasuredHeight());
        //provinceMask.buildDrawingCache();
        //maskBitmap = Bitmap.createBitmap(provinceMask.getDrawingCache());
        //provinceMask.setDrawingCacheEnabled(false);
        maskBitmap = loadBitmapFromView(provinceMask);
    }

    private static Bitmap loadBitmapFromView(final View v) {
        final int w = v.getMeasuredWidth();
        final int h = v.getMeasuredHeight();

        final Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        final Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    @SuppressWarnings("deprecation")
    private Drawable getProvinceImageDrawable(final String provinceName) {
        final int resId = getResources().getIdentifier("province_img_" + provinceName, "drawable", getPackageName());
        return getResources().getDrawable(resId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_province_map, menu);
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
