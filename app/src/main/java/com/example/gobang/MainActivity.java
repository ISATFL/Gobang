package com.example.gobang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GobangCallback, View.OnClickListener {
    private GobangView gobangView;
    private TextView user_score_1;
    private TextView user_score_2;
    private ImageView restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gobangView=findViewById(R.id.gobang_view);
        gobangView.setCallback(this);
        user_score_1=findViewById(R.id.user_score_1);
        user_score_2=findViewById(R.id.user_score_2);
        restart=findViewById(R.id.restart_game);
        restart.setOnClickListener(this);


    }
    @Override
    public void Gameover(int winner) {
        user_score_1.setText(gobangView.getWhite_wins()+"");
        user_score_2.setText(gobangView.getBlack_wins()+"");
        switch (winner){
            case GobangView.BLACK_WIN:
                Toast.makeText(this,"黑棋胜利",Toast.LENGTH_SHORT).show();
                break;
            case GobangView.WHITE_WIN:
                Toast.makeText(this,"白棋胜利",Toast.LENGTH_SHORT).show();
                break;
            case GobangView.NO_WIN:
                Toast.makeText(this,"平局",Toast.LENGTH_SHORT).show();
                break;


        }

    }

    @Override
    public void ChangeGamer(boolean isWhite) {

    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.restart_game:
                gobangView.restart();
                break;
        }
    }
}
