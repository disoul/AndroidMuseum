package musetest.disoul.com.musetest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DiSoul on 2015/4/20.
 */
public class CityMappingData {

    public static final Map<Integer,String>CITY_COLOR_MAP = new HashMap<>();
    static {
        CITY_COLOR_MAP.put(0xffff0000,"wuxi");
    }

    private CityMappingData(){

    }
}
