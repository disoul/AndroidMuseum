package musetest.disoul.com.musetest;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ProvinceMapActivity extends ActionBarActivity {

    private static final String ARG_PROVINCE_NAME = "musetest.com.disoul.musetest.PROVINCE_NAME";

    @InjectView(R.id.provinceImage)
    ImageView provinceImage;

    private String provinceName;

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
