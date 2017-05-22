package com.stick.mtglifecounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String SAVED_COLOR = "saved_color";

    private int life = 20;
    private int energy = 0;
    private boolean isSoundPlaying = false;
    private MediaPlayer player;
    private Color currentColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setImageClickHandler();
        updateLifeText();
        updateEnergyText();
        applySavedColor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.start_flip:
                Intent intent = new Intent(this, FlipActivity.class);
                startActivity(intent);
                return true;
            case R.id.reset_game:
                reset();
                return true;
            case R.id.pick_color:
                pickColorDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void pickColorDialog() {
        String[] displayNames = new String[Color.values().length];
        for (int i = 0; i < Color.values().length; i++) {
            displayNames[i] = Color.values()[i].getDisplayName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pick_color)
                .setItems(displayNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Color selectedColor = Color.getColorFromPosition(which);
                        setThemeColor(selectedColor);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setImageClickHandler()
    {
        ImageView manaImage = (ImageView)findViewById(R.id.mana_image);
        manaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isSoundPlaying) {
                        player.stop();
                        player.prepare();
                    } else {
                        player.start();
                    }
                    isSoundPlaying = !isSoundPlaying;
                } catch (IOException e)
                {

                }
            }
        });
    }

    public void lifeUp(View view)
    {
        this.life++;
        updateLifeText();
    }

    public void lifeDown(View view)
    {
        if (this.life > 0)
            this.life--;
        updateLifeText();
    }

    private void updateLifeText()
    {
        TextView textView = (TextView) findViewById(R.id.life_text_view);
        textView.setText(Integer.toString(life));
    }

    public void energyUp(View view)
    {
        this.energy++;
        updateEnergyText();
    }

    public void energyDown(View view)
    {
        if (this.energy > 0)
            this.energy--;
        updateEnergyText();
    }

    private void updateEnergyText()
    {
        TextView textView = (TextView) findViewById(R.id.energy_text_view);
        textView.setText(Integer.toString(energy));
    }

    public void reset()
    {
        this.life = 20;
        this.energy = 0;
        updateLifeText();
        updateEnergyText();
    }

    private void applySavedColor()
    {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        String savedColorName = sharedPreferences.getString(SAVED_COLOR, Color.BLACK.name());
        Color savedColor = Color.valueOf(savedColorName);
        setThemeColor(savedColor);
    }

    private void saveColor(Color color)
    {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SAVED_COLOR, color.name()).apply();
    }

    private void setThemeColor(Color color)
    {
        if (currentColor != null && currentColor == color)
            return;

        this.currentColor = color;

        ImageView manaImageView = (ImageView) findViewById(R.id.mana_image);
        manaImageView.setImageResource(color.getManaDrawable());

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.activity_main);
        mainLayout.setBackgroundColor(color.getBackgroundColor());

        TextView lifeTextView = (TextView) findViewById(R.id.life_text_view);
        lifeTextView.setTextColor(color.getTextColor());

        TextView energyTextView = (TextView) findViewById(R.id.energy_text_view);
        energyTextView.setTextColor(color.getTextColor());

        if (isSoundPlaying) {
            player.stop();
            isSoundPlaying = false;
        }
        player = MediaPlayer.create(getApplicationContext(), color.getSongResource());

        saveColor(color);
    }
}
