package musetest.disoul.com.musetest;

import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.panoramagl.PLImage;
import com.panoramagl.PLJSONLoader;
import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.PLView;
import com.panoramagl.utils.PLUtils;

import javax.microedition.khronos.opengles.GL10;

import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class PanoramaActivity extends PLView {

    private final int MAX_IMG_ID = 14;
    private final String TAG = "PanoramaActivity";

    private int nextImgId = 2;
    private int lastImgId = 0;
    private final GL10 gl10 = this.getCurrentGL();

    private PLSphericalPanorama panorama;

    private EventBus bus = EventBus.getDefault();

    public static void launch(Context ctx){
        Intent intent = new Intent(ctx,PanoramaActivity.class);
        ctx.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
        initPanorama();
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    private void initPanorama(){
        panorama = new PLSphericalPanorama();
        this.setPanorama(panorama);
        injectUI();

        updatePanorama();
    }

    private void updatePanorama(){
        panorama.setImage(gl10, PLImageHelper.fromResourceIndex(this, nextImgId));
        updateId();
    }

    private void updateId(){
        lastImgId = nextImgId;
        nextImgId++;
    }

    private void injectUI() {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        RelativeLayout container = (RelativeLayout) View.inflate(
                this,
                R.layout.activity_panorama,
                null);

        root.removeAllViews();
        container.addView(content, 0);
        root.addView(container);

        ImageButton imageButton = (ImageButton)findViewById(R.id.nextStep);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.post(new NextImageEvent());
            }
        });
    }

    public void onEventMainThread(final NextImageEvent e) {
        Log.d(TAG, "CLICK");
        if (nextImgId > MAX_IMG_ID) return;
        updatePanorama();
        updateId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_panorama, menu);
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

    public static class NextImageEvent {
    }
}
