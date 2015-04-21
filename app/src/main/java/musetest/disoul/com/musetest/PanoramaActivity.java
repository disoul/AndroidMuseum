package musetest.disoul.com.musetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.PLView;

import javax.microedition.khronos.opengles.GL10;

import de.greenrobot.event.EventBus;


public class PanoramaActivity extends PLView {

    private final int MAX_IMG_ID = 14;
    private final int MIN_IMG_ID = 2;
    private final String TAG = "PanoramaActivity";

    private int nextImgId = 2;
    private int prevImgId = 0;
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

        nextPanorama();
    }

    private void nextPanorama(){
        panorama.setImage(gl10, PLImageHelper.fromResourceIndex(this, nextImgId));
        prevImgId = nextImgId;
        nextImgId++;
    }

    private  void prevPanorama(){
        panorama.setImage(gl10, PLImageHelper.fromResourceIndex(this, prevImgId));
        nextImgId--;
        prevImgId--;
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

        ImageView nextStep = (ImageView)findViewById(R.id.nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.post(new NextImageEvent());
            }
        });

        ImageView prevStep = (ImageView)findViewById(R.id.prevStep);
        prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.post(new PrevImageEvent());
            }
        });
    }

    public void onEventMainThread(final NextImageEvent e) {
        Log.d(TAG, "CLICK");
        if (nextImgId > MAX_IMG_ID) return;
        nextPanorama();
    }

    public void onEventMainThread(final PrevImageEvent e){
        if (prevImgId < MIN_IMG_ID) return;
        prevPanorama();
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

    public static class PrevImageEvent {

    }
}
