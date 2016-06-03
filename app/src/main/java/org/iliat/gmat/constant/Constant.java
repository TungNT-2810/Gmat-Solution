package org.iliat.gmat.constant;

import android.graphics.Color;
import android.hardware.camera2.params.StreamConfigurationMap;

import org.iliat.gmat.R;

/**
 * Created by MrBom on 5/16/2016.
 */
public class Constant {
    public static final int TAG_GREY = 1;
    public static final int TAG_GREEN = 2;
    public static final int TAG_YELLOW = 3;
    public static final int TAG_RED = 4;
    public static final int TAG_STAR = 5;
    public static final String MIME_TYPE = "text/html; charset=utf-8";
    public static final String HTML_ENCODE = "UTF-8";
    public static final String TYPE_Q = "Q";
    public static final String TYPE_RC = "RC";
    public static final int[] COLOR_PICKER = {Color.parseColor("#2d5139"), Color.parseColor("#2d3f51"),
            Color.parseColor("#462e52"), Color.parseColor("#4e512d"), Color.parseColor("#512d2d"),
            Color.parseColor("#2e524f"), Color.parseColor("#52432e"), Color.parseColor("#522e42"), Color.parseColor("#3c6b4c"),
            Color.parseColor("#5b3c6b"), Color.parseColor("#676b3c"), Color.parseColor("#6b3c3c"), Color.parseColor("#3c6b67"),
            Color.parseColor("#6b573c"), Color.parseColor("#6b3c56")};
    public static final int[] PICTURES = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5};
    public static final int[] STARS = {R.drawable.star0, R.drawable.star1, R.drawable.stars2, R.drawable.stars3};
    public static final String js = "<html><head>"
            + "<link rel='stylesheet' href='file:///android_asset/mathscribe/jqmath-0.4.3.css'>"
            + "<script src = 'file:///android_asset/mathscribe/jquery-1.4.3.min.js'></script>"
            + "<script src = 'file:///android_asset/mathscribe/jqmath-etc-0.4.3.min.js'></script>"
            + "</head><body>";

}
