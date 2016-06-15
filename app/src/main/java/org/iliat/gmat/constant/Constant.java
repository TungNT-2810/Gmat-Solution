package org.iliat.gmat.constant;

import android.graphics.Color;
import android.hardware.camera2.params.StreamConfigurationMap;

import org.iliat.gmat.R;

/**
 * Created by MrBom on 5/16/2016.
 */
public class Constant {
    //tag id
    public static final int TAG_GREY = 1;
    public static final int TAG_GREEN = 2;
    public static final int TAG_YELLOW = 3;
    public static final int TAG_RED = 4;
    public static final int TAG_STAR = 5;

    //html
    public static final String MIME_TYPE = "text/html; charset=utf-8";
    public static final String HTML_ENCODE = "UTF-8";

    //question type
    public static final String TYPE_RC = "RC";
    public static final String TYPE_CR = "CR";
    public static final String TYPE_SC = "SC";
    public static final String TYPE_Q = "Q";

    //color picker
    public static final int[] COLOR_PICKER = {Color.parseColor("#673AB7"), Color.parseColor("#2962FF"),
            Color.parseColor("#4CAF50"), Color.parseColor("#FF5722"),
            Color.parseColor("#607D8B"), Color.parseColor("#795548"), Color.parseColor("#CDDC39"), Color.parseColor("#00BFA5"),
            Color.parseColor("#0091EA"), Color.parseColor("#2962FF"), Color.parseColor("#6b3c3c"), Color.parseColor("#3c6b67"),
            Color.parseColor("#6b573c"), Color.parseColor("#6b3c56")};

    //resource for star
    public static final int[] STARS = {R.drawable.star0, R.drawable.star1, R.drawable.stars2, R.drawable.stars3};

    //header for math formula
    public static final String JS = "<html><head>"
            + "<link rel='stylesheet' href='file:///android_asset/mathscribe/jqmath-0.4.3.css'>"
            + "<script src = 'file:///android_asset/mathscribe/jquery-1.4.3.min.js'></script>"
            + "<script src = 'file:///android_asset/mathscribe/jqmath-etc-0.4.3.min.js'></script>"
            + "</head><body>";
}
