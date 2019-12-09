package com.example.fengg2.realchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.media.MediaPlayer;

public class UIdesign extends AppCompatActivity {
    private chess chess;
    private TextView blue;
    private TextView yellow;
    private TextView green;
    private MediaPlayer mediaPlayer;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_design);
        update = (Button) findViewById(R.id.Update);
        blue = (TextView) findViewById(R.id.textView15);
        green = (TextView) findViewById(R.id.textView17);
        yellow = (TextView) findViewById(R.id.textView18);
        chess =(chess)findViewById(R.id.panel);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = chess.getBluescore();
                blue.setText(String.valueOf(a));
                int b = chess.getGreenscore();
                int c = chess.getYellowscore();
                green.setText(String.valueOf(c));
                yellow.setText(String.valueOf(b));
            }
        });
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.doudizhu);
        mediaPlayer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.setting){
            chess.restart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
