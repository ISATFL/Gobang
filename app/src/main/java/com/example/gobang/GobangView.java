package com.example.gobang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorLong;

public class GobangView extends View implements View.OnTouchListener {
    private boolean isWhite=true;
    private boolean isGameover=false;
    private int[][] chessArray;
    private Paint paint;

    private Bitmap whiteChess;
    private Bitmap blackChess;
    private Rect rect;
    private float length;
    private int grid=15;
    private float prewidth;
    private float offset;
    private GobangCallback callback;

    private int white_wins;
    private int black_wins;

    private boolean isUser=true;
    private int user_Chess=WHITE_CHESS;


    public static final int WHITE_CHESS=1;
    public static final int BLACK_CHESS=2;
    public static final int NO_CHESS=0;
    public static final int WHITE_WIN=11;
    public static final int BLACK_WIN=12;
    public static final int NO_WIN=10;


    public GobangView(Context context) {
        this(context,null);
    }

    public GobangView(Context context, AttributeSet attrs) {
       this(context,attrs,0);
    }

    public GobangView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        chessArray=new int[grid][grid];
        whiteChess= BitmapFactory.decodeResource(context.getResources(),R.drawable.white_chess);
        blackChess=BitmapFactory.decodeResource(context.getResources(),R.drawable.black_chess);
        white_wins=0;
        black_wins=0;
        rect=new Rect();
        setOnTouchListener(this);
        for(int i=0;i<grid;i++){
            for(int j=0;j<grid;j++){
                chessArray[i][j]=0;
            }
        }



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        int len=width>height?height:width;
        setMeasuredDimension(len,len);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        length=getWidth()>getHeight()?getHeight():getWidth();
        prewidth=length/grid;
        offset=prewidth/2;
        for(int i=0;i<grid;i++){
            float start=i*prewidth+offset;
            canvas.drawLine(offset,start,length-offset,start,paint);
            canvas.drawLine(start,offset,start,length-offset,paint);

        }
        for(int i=0;i<grid;i++){
            for(int j=0;j<grid;j++){
                float rectx=offset+i*prewidth;
                float recty=offset+j*prewidth;
                rect.set((int )(rectx-offset),(int )(recty-offset),(int )(rectx+offset),(int)(recty+offset));
                switch (chessArray[i][j]){
                    case WHITE_CHESS:
                        canvas.drawBitmap(whiteChess,null,rect,paint);
                        break;
                    case BLACK_CHESS:
                        canvas.drawBitmap(blackChess,null,rect,paint);
                        break;

                }
            }
        }
    }

    public void check(){
        int chess=isWhite?BLACK_CHESS:WHITE_CHESS;
        boolean isFull=true;
        for(int i=0;i<grid;i++){
            for(int j=0;j<grid;j++){
                if(chessArray[i][j]!=BLACK_CHESS&&chessArray[i][j]!=WHITE_CHESS){
                    isFull=false;
                }
                if(chessArray[i][j]==chess){
                    if(Success(i,j)){
                        isGameover=true;
                        if(callback!=null){
                            if(chess==WHITE_CHESS){
                                white_wins++;
                            }
                            else{
                                black_wins++;
                            }
                            callback.Gameover(chess==WHITE_CHESS?WHITE_WIN:BLACK_WIN);
                        }
                        return;
                    }
                }
            }
        }
        if(isFull){
            isGameover=true;
            if(callback!=null){
                callback.Gameover(NO_WIN);
            }
        }
    }
    public boolean Success(int x,int y){
        if(x+4<grid){
            if(chessArray[x][y]==chessArray[x+1][y]&&chessArray[x][y]==chessArray[x+2][y]&&chessArray[x][y]==chessArray[x+3][y]&&chessArray[x][y]==chessArray[x+4][y]){
                return true;
            }
        }
        if (y + 4 < grid) {
            if (chessArray[x][y] == chessArray[x][y + 1] && chessArray[x][y] == chessArray[x][y + 2]
                    && chessArray[x][y] == chessArray[x][y + 3] && chessArray[x][y] == chessArray[x][y + 4]) {
                return true;
            }
        }
        if (y + 4 < grid && x + 4 < grid) {
            if (chessArray[x][y] == chessArray[x + 1][y + 1] && chessArray[x][y] == chessArray[x + 2][y + 2]
                    && chessArray[x][y] == chessArray[x + 3][y + 3] && chessArray[x][y] == chessArray[x + 4][y + 4]) {
                return true;
            }
        }
        if (y - 4 > 0 && x + 4 < grid) {
            if (chessArray[x][y] == chessArray[x + 1][y - 1] && chessArray[x][y] == chessArray[x + 2][y - 2]
                    && chessArray[x][y] == chessArray[x + 3][y - 3] && chessArray[x][y] == chessArray[x + 4][y - 4]) {
                return true;
            }
        }
        return false;

    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!isGameover){
                    float downx=motionEvent.getX();
                    float downy=motionEvent.getY();
                    if(downx>=offset/2&&downx<=length-offset/2&&downy>=offset/2&&downy<=length-offset/2){
                        int x=(int)(downx/prewidth);
                        int y=(int)(downy/prewidth);
                        if(chessArray[x][y]!=WHITE_CHESS&&chessArray[x][y]!=BLACK_CHESS){

                            chessArray[x][y] = isWhite ? WHITE_CHESS : BLACK_CHESS;
                            isWhite=!isWhite;
                            postInvalidate();
                            check();
                            if(callback!=null){
                                callback.ChangeGamer(isWhite);
                            }
                        }

                    }
                }else {
                    Toast.makeText(getContext(),"游戏结束",Toast.LENGTH_SHORT).show();

                }
                break;
        }
        return false;
    }
    public void restart(){
        isGameover=false;

        for(int i=0;i<grid;i++){
            for(int j=0;j<grid;j++){
                chessArray[i][j]=0;
            }
        }
        postInvalidate();
    }
    public void setCallback(GobangCallback gobangCallback){
        this.callback=gobangCallback;
    }
    public int getWhite_wins(){
        return white_wins;
    }
    public int getBlack_wins(){
        return black_wins;
    }
}
