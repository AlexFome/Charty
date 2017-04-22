package com.fome.charty;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Alex on 07.02.2017.
 */
public class FontManager {

    public static final String ROOT = "fonts/";
    public static final String FONTAWESOME = ROOT + "fontawesome-webfont.ttf";
    public static final String ABEL = ROOT + "Abel-Regular.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}
