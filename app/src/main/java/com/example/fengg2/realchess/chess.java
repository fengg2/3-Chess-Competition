package com.example.fengg2.realchess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class chess extends View {
    private int bluescore = 0;
    private int greenscore = 0;
    private int yellowscore = 0;
    private int panelwidth;
    private float lineheight;
    private int MAX_LINE=10;
    private Paint paint=new Paint();
    private Bitmap blue;
    private Bitmap green;
    private Bitmap yellow;
    private float ratio=3*1.0f/4;
    private boolean isblue=true;
    private ArrayList<Point> blueArray =new ArrayList<>();
    private ArrayList<Point> greenArray =new ArrayList<>();
    private ArrayList<Point> yellowArray =new ArrayList<>();
    private boolean gameover;
    private int countwow =0;
    private boolean whitewinner;
    private int MAXCOUNTLINE=3;

    public int getBluescore() {
        return bluescore;
    }

    public int getGreenscore() {
        return greenscore;
    }

    public int getYellowscore() {
        return yellowscore;
    }

    public chess(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(0x87000000);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        blue=BitmapFactory.decodeResource(getResources(),R.drawable.blue);
        green =BitmapFactory.decodeResource(getResources(),R.drawable.green);
        yellow = BitmapFactory.decodeResource(getResources(),R.drawable.yellow);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthsize=MeasureSpec.getSize(widthMeasureSpec);
        int widthmode=MeasureSpec.getMode(widthMeasureSpec);
        int heightsize=MeasureSpec.getSize(heightMeasureSpec);
        int heightmode=MeasureSpec.getMode(heightMeasureSpec);
        int width=Math.min(widthsize,heightsize);
        if (widthmode==MeasureSpec.UNSPECIFIED){
            width=heightsize;
        }else if (heightmode==MeasureSpec.UNSPECIFIED){
            width=widthsize;
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        panelwidth=w;
        lineheight=panelwidth*1.0f/MAX_LINE;
        int whitewidth=(int)(lineheight*ratio);
        blue = Bitmap.createScaledBitmap(blue,whitewidth,whitewidth,false);
        green = Bitmap.createScaledBitmap(green,whitewidth,whitewidth,false);
        yellow = Bitmap.createScaledBitmap(yellow, whitewidth,whitewidth, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameover) return false;
        int action=event.getAction();
        if (action==MotionEvent.ACTION_UP){
            int x=(int)event.getX();
            int y=(int)event.getY();
            Point p=getValidPoint(x,y);
            if (blueArray.contains(p)|| greenArray.contains(p)|| yellowArray.contains(p)){
                return false;
            }
            if (countwow % 3 == 0) {
                blueArray.add(p);
            } else if (countwow % 3 == 1) {
                greenArray.add(p);
            } else {
                yellowArray.add(p);
            }
            invalidate();
            countwow++;

        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int)(x/lineheight),(int)(y/lineheight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkGame();
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0; i< blueArray.size(); i++){
            Point whitepoint= blueArray.get(i);
            canvas.drawBitmap(blue,(whitepoint.x+(1-ratio)/2)*lineheight,(whitepoint.y+(1-ratio)/2)*lineheight,null);
        }
        for (int i = 0; i< greenArray.size(); i++){
            Point blackpoint= greenArray.get(i);
            canvas.drawBitmap(green,(blackpoint.x+(1-ratio)/2)*lineheight,(blackpoint.y+(1-ratio)/2)*lineheight,null);
        }
        for (int i = 0; i< yellowArray.size(); i++){
            Point yellowpoint= yellowArray.get(i);
            canvas.drawBitmap(yellow,(yellowpoint.x+(1-ratio)/2)*lineheight,(yellowpoint.y+(1-ratio)/2)*lineheight,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w=panelwidth;
        float lineHeight=lineheight;
        for (int i=0;i<MAX_LINE;i++){
            int startX=(int)(lineHeight/2);
            int endX=(int)(w-lineHeight/2);
            int y=(int)((0.5+i)*lineHeight);
            canvas.drawLine(startX,y,endX,y,paint);//horizon line
            canvas.drawLine(y,startX,y,endX,paint);//vertical line
        }
    }

    public void restart(){
        blueArray.clear();
        greenArray.clear();
        yellowArray.clear();
        gameover=false;
        whitewinner=false;
        invalidate();
    }

    private void checkGame() {
        boolean bluewin = checkThreeInLine(blueArray);
        boolean greenwin = checkThreeInLine(greenArray);
        boolean yellowwin = checkThreeInLine(yellowArray);
        if (bluewin){
            gameover=true;
            whitewinner=bluewin;
            String text= "bluewin";
            bluescore++;
            Toast.makeText(getContext(),text, Toast.LENGTH_SHORT).show();
        } else if (yellowwin){
            gameover=true;
            whitewinner=yellowwin;
            String text= "yellowwin";
            Toast.makeText(getContext(),text, Toast.LENGTH_SHORT).show();
            yellowscore++;
        } else if (greenwin){
            gameover=true;
            whitewinner=greenwin;
            String text= "greenwin";
            Toast.makeText(getContext(),text, Toast.LENGTH_SHORT).show();
            greenscore++;
        }
    }

    private boolean checkThreeInLine(List<Point> points) {
        for (Point p:points){
            int x=p.x;
            int y=p.y;
            boolean win=checkHorizontal(x,y,points);
            if (win) return true;
            win=checkVertical(x,y,points);
            if (win) return true;
            win=left(x,y,points);
            if (win) return true;
            win=right(x,y,points);
            if (win) return true;
        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAXCOUNTLINE;i++){
            if (points.contains(new Point(x-i,y))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAXCOUNTLINE) return true;
        for (int i=1;i<MAXCOUNTLINE;i++){
            if (points.contains(new Point(x+i,y))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAXCOUNTLINE) return true;
        return false;
    }
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAXCOUNTLINE;i++){
            if (points.contains(new Point(x,y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAXCOUNTLINE) return true;
        for (int i=1;i<MAXCOUNTLINE;i++){
            if (points.contains(new Point(x,y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAXCOUNTLINE) return true;
        return false;
    }
    private boolean left(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAXCOUNTLINE;i++){
            if (points.contains(new Point(x-i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAXCOUNTLINE) return true;
        for (int i=1;i<MAXCOUNTLINE;i++){
            if (points.contains(new Point(x+i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAXCOUNTLINE) return true;
        return false;
    }
    private boolean right(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAXCOUNTLINE;i++){
            if (points.contains(new Point(x+i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAXCOUNTLINE) return true;
        for (int i=1;i<MAXCOUNTLINE;i++){
            if (points.contains(new Point(x-i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAXCOUNTLINE) return true;
        return false;
    }

    private static final String INSTANCE="instance";
    private static final String INSTANCE_OVER="gameover";
    private static final String INSTANCE_BLUE="blue";
    private static final String INSTANCE_GREEN ="white";
    private static final String INSTANCE_YELLOW = "yellow";
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_OVER,gameover);
        bundle.putParcelableArrayList(INSTANCE_BLUE,blueArray);
        bundle.putParcelableArrayList(INSTANCE_GREEN,greenArray);
        bundle.putParcelableArrayList(INSTANCE_YELLOW, yellowArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            gameover = bundle.getBoolean(INSTANCE_OVER);
            blueArray = bundle.getParcelableArrayList(INSTANCE_BLUE);
            greenArray = bundle.getParcelableArrayList(INSTANCE_GREEN);
            yellowArray = bundle.getParcelableArrayList(INSTANCE_YELLOW);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
