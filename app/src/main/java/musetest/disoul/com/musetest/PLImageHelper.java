package musetest.disoul.com.musetest;

import android.content.Context;
import android.content.res.Resources;

import com.panoramagl.PLImage;
import com.panoramagl.utils.PLUtils;

/**
 * Created by DiSoul on 2015/4/21.
 */
public class PLImageHelper {
    private PLImageHelper() {
    }

    public static PLImage fromResourceIndex(final Context ctx, final int idx){
        final Resources res = ctx.getResources();
        final String resName = "image" + idx;
        final int resId = res.getIdentifier(resName, "raw", ctx.getPackageName());

        return PLImage.imageWithBitmap(PLUtils.getBitmap(ctx, resId));
    }
}
