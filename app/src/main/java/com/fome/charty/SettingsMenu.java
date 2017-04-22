package com.fome.charty;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SettingsMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        Button twitterButton = (Button) findViewById(R.id.twitterButton);
        twitterButton.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        Drawable background = twitterButton.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor("#55acee"));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor("#55acee"));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor("#55acee"));
        }

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("https://twitter.com/Alex_Fome");
            }
        });

        Button facebookButton = (Button) findViewById(R.id.facebookButton);
        facebookButton.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        background = facebookButton.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor("#3b5998"));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor("#3b5998"));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor("#3b5998"));
        }

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("https://www.facebook.com/alex.fome.9");
            }
        });

        LinearLayout likeButton = (LinearLayout) findViewById(R.id.likeButton);
        background = likeButton.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("https://play.google.com/store/apps/details?id=com.fome.charty");
            }
        });

        TextView heart = (TextView) findViewById(R.id.heart);
        heart.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));

        TextView envelope = (TextView) findViewById(R.id.email);
        envelope.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        background = envelope.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
        }

        envelope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail ();
            }
        });

        TextView textView = (TextView) findViewById(R.id.developedBy);
        textView.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.ABEL));
        textView = (TextView) findViewById(R.id.AlexFome);
        textView.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.ABEL));
    }

    void openURL (String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    void sendEmail () {
        String[] TO = {"alxfome@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your message");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SettingsMenu.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
