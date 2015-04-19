package musetest.disoul.com.musetest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DiSoul on 2015/4/20.
 */
public final class ProvinceMappingData {

    public static final Map<Integer, String> PROVINCE_COLOR_MAP = new HashMap<Integer, String>();

    static {
        PROVINCE_COLOR_MAP.put(0xffc32f2e, "jiangsu");
        PROVINCE_COLOR_MAP.put(0xff4c0e0e, "anhui");
        PROVINCE_COLOR_MAP.put(0xffbff15d, "shandong");
    }

    private ProvinceMappingData() {
    }
}
