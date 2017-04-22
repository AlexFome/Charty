package com.fome.charty.models;

/**
 * Created by Alex on 07.02.2017.
 */
public class Option {

    public String mainText;
    public int mainIcon;
    public String additionalText;

    public boolean isIcon;

    public Option (String mainText, String additionalText) {
        this.mainText = mainText;
        this.additionalText = additionalText;
        isIcon = false;
    }

    public Option (int mainIcon, String additionalText) {
        this.mainIcon = mainIcon;
        this.additionalText = additionalText;
        isIcon = true;
    }

}
