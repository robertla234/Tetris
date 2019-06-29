package com.example.assignment3.views;

import java.util.concurrent.TimeUnit;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import java.util.Random;

import com.example.assignment3.R;

import java.util.ArrayList;

public class CustomView extends View {
    private int score = 0;
    private Rect sideBox;
    private Paint scoreDisp;

    private int randomHolder[];

    private int divisionNum = 10;


    private int isThread = 0; //boolean for only running one extra thread
    private int playAreaWidth = 788;
    private int DEFAULT_SQUARE_SIZE = playAreaWidth / divisionNum;

    private Rect mBackdrop;
    private Paint mPaintdrop;

    private Rect mRectArray[];
    private Rect hRectArray[];
    private Paint mPaintSquare;
    private Paint hPaintSquare;
    private Paint mPaintBorder;
    private Paint mPaintSave;
    private int mSquareSize;

    public ArrayList<Rect> mSavedRect;
    private DisplayMetrics metrics;

    public CustomView(Context context) {
        super(context);

        init(null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        mRectArray = new Rect[4];
        hRectArray = new Rect[4];
        for (int i = 0; i < mRectArray.length; i++){
            Rect newwRect = new Rect();
            Rect newRect = new Rect();
            hRectArray[i] = newwRect;
            mRectArray[i] = newRect;
        }
        mPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
        hPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSave = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSavedRect = new ArrayList<Rect>();

        randomHolder = new int[2];

        mBackdrop = new Rect();
        mPaintdrop = new Paint(Paint.ANTI_ALIAS_FLAG);

        sideBox = new Rect();
        scoreDisp = new Paint();
        scoreDisp.setColor(Color.WHITE);
        scoreDisp.setStyle(Paint.Style.FILL);
        scoreDisp.setTextSize(100);

        //mPaintCircle = new Paint();
        //mPaintCircle.setAntiAlias(true);
        //mPaintCircle.setColor(Color.parseColor("#00ccff"));

        if (set == null)
            return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomView);

        mSquareSize = ta.getDimensionPixelSize(R.styleable.CustomView_square_size, DEFAULT_SQUARE_SIZE);

        mPaintSave.setColor(Color.DKGRAY);
        mPaintSquare.setStyle(Paint.Style.FILL);
        hPaintSquare.setStyle(Paint.Style.FILL);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setColor(Color.GRAY);
        mPaintBorder.setStrokeWidth(6);
        mPaintdrop.setColor(Color.BLACK);

        ta.recycle();
    }

    public void shapeRotateRight(){
        //pivot block is always block#1
        boolean canRotate = true;
        int RectValues[][] = new int[4][4];

        int pivotLeft = mRectArray[1].left;
        int pivotTop = mRectArray[1].top;
        //System.out.println("pivotLeft is : " + pivotLeft + "     pivottop is : " + pivotTop);
        int rightRotateMatrix[] = new int[4];
        rightRotateMatrix[0] = 0;
        rightRotateMatrix[1] = -1;
        rightRotateMatrix[2] = 1;
        rightRotateMatrix[3] = 0;
        int blockBTurnedLeft, blockBTurnedTop, newLocLeft, newLocTop;
        int rVectorTop, rVectorLeft, tVectorTop, tVectorLeft;
        for (int i = 0; i < mRectArray.length; i++){
            if (i != 1) {
                //System.out.println("i is: " + i);
                blockBTurnedLeft = mRectArray[i].left;
                blockBTurnedTop = mRectArray[i].top;
                //System.out.println("BlockturnLEft is : " + blockBTurnedLeft + "     blockbturntop is : " + blockBTurnedTop);
                rVectorTop = blockBTurnedTop - pivotTop;
                rVectorLeft = blockBTurnedLeft - pivotLeft;
                tVectorTop = (rightRotateMatrix[0] * rVectorTop) + (rightRotateMatrix[2] * rVectorLeft);
                tVectorLeft = (rightRotateMatrix[1] * rVectorTop) + (rightRotateMatrix[3] * rVectorLeft);
                //System.out.println("rVectorTop is: " + rVectorTop + "      rVectorLeft is: " + rVectorLeft);
                //System.out.println("tVectorTop is: " + tVectorTop + "      tVectorLeft is: " + tVectorLeft);
                newLocTop = pivotTop + tVectorTop;
                newLocLeft = pivotLeft + tVectorLeft;
                for (int a = 0; a < mSavedRect.size(); a++){
                    if (newLocLeft == (mSavedRect.get(a)).left && newLocTop == (mSavedRect.get(a)).top){
                        canRotate = false;
                    }
                }
                if (newLocLeft < 0 || newLocLeft + mSquareSize > playAreaWidth || newLocTop + mSquareSize > getHeight() || newLocTop < 0){
                    canRotate = false;
                }
                RectValues[i][0] = newLocLeft;
                RectValues[i][1] = newLocTop;
                RectValues[i][2] = newLocLeft + mSquareSize;
                RectValues[i][3] = newLocTop + mSquareSize;
            }
        }
        for (int i = 0; i < mRectArray.length; i++){
            if (i != 1 && canRotate == true) {
                mRectArray[i].left = RectValues[i][0];
                mRectArray[i].top = RectValues[i][1];
                mRectArray[i].right = RectValues[i][2];
                mRectArray[i].bottom = RectValues[i][3];
            }
        }
        postInvalidate();
    }

    public void shapeRotateLeft(){
        //pivot block is always block#1
        boolean canRotate = true;
        int RectValues[][] = new int[4][4];

        int pivotLeft = mRectArray[1].left;
        int pivotTop = mRectArray[1].top;
        //System.out.println("pivotLeft is : " + pivotLeft + "     pivottop is : " + pivotTop);
        int leftRotateMatrix[] = new int[4];
        leftRotateMatrix[0] = 0;
        leftRotateMatrix[1] = 1;
        leftRotateMatrix[2] = -1;
        leftRotateMatrix[3] = 0;
        int blockBTurnedLeft, blockBTurnedTop, newLocLeft, newLocTop;
        int rVectorTop, rVectorLeft, tVectorTop, tVectorLeft;
        for (int i = 0; i < mRectArray.length; i++){
            if (i != 1) {
                //System.out.println("i is: " + i);
                blockBTurnedLeft = mRectArray[i].left;
                blockBTurnedTop = mRectArray[i].top;
                //System.out.println("BlockturnLEft is : " + blockBTurnedLeft + "     blockbturntop is : " + blockBTurnedTop);
                rVectorTop = blockBTurnedTop - pivotTop;
                rVectorLeft = blockBTurnedLeft - pivotLeft;
                tVectorTop = (leftRotateMatrix[0] * rVectorTop) + (leftRotateMatrix[2] * rVectorLeft);
                tVectorLeft = (leftRotateMatrix[1] * rVectorTop) + (leftRotateMatrix[3] * rVectorLeft);
                //System.out.println("rVectorTop is: " + rVectorTop + "      rVectorLeft is: " + rVectorLeft);
                //System.out.println("tVectorTop is: " + tVectorTop + "      tVectorLeft is: " + tVectorLeft);
                newLocTop = pivotTop + tVectorTop;
                newLocLeft = pivotLeft + tVectorLeft;
                for (int a = 0; a < mSavedRect.size(); a++){
                    if (newLocLeft == (mSavedRect.get(a)).left && newLocTop == (mSavedRect.get(a)).top){
                        canRotate = false;
                    }
                }
                if (newLocLeft < 0 || newLocLeft + mSquareSize > playAreaWidth || newLocTop + mSquareSize > getHeight() || newLocTop < 0){
                    canRotate = false;
                }
                RectValues[i][0] = newLocLeft;
                RectValues[i][1] = newLocTop;
                RectValues[i][2] = newLocLeft + mSquareSize;
                RectValues[i][3] = newLocTop + mSquareSize;
            }
        }
        for (int i = 0; i < mRectArray.length; i++){
            if (i != 1 && canRotate == true) {
                    mRectArray[i].left = RectValues[i][0];
                    mRectArray[i].top = RectValues[i][1];
                    mRectArray[i].right = RectValues[i][2];
                    mRectArray[i].bottom = RectValues[i][3];
            }
        }
        postInvalidate();
    }

    public void shapeLeft() {
        boolean emptySpace = true, withinBoundary = true;
        for (int a = 0; a < mRectArray.length; a++) {
            for (int i = 0; i < mSavedRect.size(); i++) {
                if (mRectArray[a].top == (mSavedRect.get(i)).top) {
                    if ((mRectArray[a].right - mSquareSize) == (mSavedRect.get(i)).right) {
                        emptySpace = false;
                    }
                }
            }
            if (mRectArray[a].left - mSquareSize < 0){
                withinBoundary = false;
            }
        }
        if (withinBoundary == true && emptySpace == true) {
            for (int b = 0; b < mRectArray.length; b++){
                mRectArray[b].right = mRectArray[b].left;
                mRectArray[b].left -= mSquareSize;
            }
            //System.out.println("left is " + mRectSquare.left);
        }
        postInvalidate();
    }

    public void shapeDown() {
        boolean emptySpace = true, withinBoundary = true;
        for (int a = 0; a < mRectArray.length; a++) {
            for (int i = 0; i < mSavedRect.size(); i++) {
                if (mRectArray[a].right == (mSavedRect.get(i)).right) {
                    if ((mRectArray[a].top + mSquareSize) == (mSavedRect.get(i)).top) {
                        emptySpace = false;
                    }
                }
            }
            if (mRectArray[a].bottom + mSquareSize > getHeight()) {
                withinBoundary = false; // problem
            }
        }
        if (withinBoundary == true && emptySpace == true) {
            for (int b = 0; b < mRectArray.length; b++) {
                mRectArray[b].top += mSquareSize;
                mRectArray[b].bottom = mRectArray[b].top + mSquareSize;
            }
            //System.out.println("bottom is " + mRectSquare.bottom);
        }
        else {
            for (int c = 0; c < mRectArray.length; c++) {
                int top = mRectArray[c].top;
                int bottom = mRectArray[c].bottom;
                int right = mRectArray[c].right;
                int left = mRectArray[c].left;
                Rect sav = new Rect();
                sav.set(left, top, right, bottom);
                mSavedRect.add(sav);
            }
            mRectArray[0].top = 0;
        }
        postInvalidate();
    }

    public void shapeRight() {
        boolean emptySpace = true, withinBoundary = true;
        for (int a = 0; a < mRectArray.length; a++) {
            for (int i = 0; i < mSavedRect.size(); i++) {
                if (mRectArray[a].top == (mSavedRect.get(i)).top) {
                    if ((mRectArray[a].left + mSquareSize) == (mSavedRect.get(i)).left) {
                        emptySpace = false;
                    }
                }
            }
            if (mRectArray[a].right + mSquareSize > playAreaWidth){
                withinBoundary = false;
            }
        }
        if (withinBoundary == true && emptySpace == true) {
            for (int b = 0; b < mRectArray.length; b++) {
                mRectArray[b].left = mRectArray[b].right;
                mRectArray[b].right += mSquareSize;
            }
            //System.out.println("right is " + mRectSquare.left);
        }
        postInvalidate();
    }

    public ArrayList<Rect> FullLineNMove(ArrayList<Rect> Saved) { //checks if full line is created, deletes it, and moves everything down 1
        ArrayList<Rect> NewSaved = new ArrayList<Rect>();
        ArrayList<Integer> LowerArrayBy = new ArrayList<Integer>();
        int counter = 0;
        for (int i = 1; i + DEFAULT_SQUARE_SIZE < getHeight(); i = i + DEFAULT_SQUARE_SIZE) { //checks bottom
            for (int j = 0; j < Saved.size(); j++) {
                if ((Saved.get(j).top == i)) { // for all Rects in the row
                    counter++;
                }
            }
            if (counter != divisionNum) {
                for (int k = 0; k < Saved.size(); k++)
                    if ((Saved.get(k).top == i))
                        NewSaved.add(Saved.get(k));
            } else {
                LowerArrayBy.add(i);
                score += divisionNum;
            }
            counter = 0;
        }
        for (int i = 0; i < LowerArrayBy.size(); i++) {
            for (int h = 0; h < NewSaved.size(); h++) {
                if (NewSaved.get(h).top < LowerArrayBy.get(i)) { // change to lower limit) {// if bottom doesn't go past limit after add
                    NewSaved.get(h).top += (DEFAULT_SQUARE_SIZE);
                    NewSaved.get(h).bottom += (DEFAULT_SQUARE_SIZE);
                }
            }
        }
        return NewSaved;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //SETS black backdrop
        mBackdrop.left = 0;
        mBackdrop.top = 0;
        mBackdrop.right = playAreaWidth;
        mBackdrop.bottom = getHeight();
        canvas.drawRect(mBackdrop, mPaintdrop);
        //System.out.println("width is " + getWidth());

        //RESETS and randomly spawn new tetrominoe
        if (mRectArray[0].left == 0 || mRectArray[0].top == 0) {
            Random rand = new Random();
            int n = randomHolder[0];

            // redraws to top // I tetrominoe
            if (n == 6) {
                mRectArray[0].left = playAreaWidth / 2;
                mRectArray[0].top = 1;
                mRectArray[0].right = mRectArray[0].left + mSquareSize;
                mRectArray[0].bottom = mRectArray[0].top + mSquareSize;

                mRectArray[1].left = mRectArray[0].left;
                mRectArray[1].top = mRectArray[0].top + mSquareSize;
                mRectArray[1].right = mRectArray[0].right;
                mRectArray[1].bottom = mRectArray[0].bottom + mSquareSize;

                mRectArray[2].left = mRectArray[0].left;
                mRectArray[2].top = mRectArray[0].top + mSquareSize * 2;
                mRectArray[2].right = mRectArray[0].right;
                mRectArray[2].bottom = mRectArray[0].bottom + mSquareSize * 2;

                mRectArray[3].left = mRectArray[0].left;
                mRectArray[3].top = mRectArray[0].top + mSquareSize * 3;
                mRectArray[3].right = mRectArray[0].right;
                mRectArray[3].bottom = mRectArray[0].bottom + mSquareSize * 3;

                mPaintSquare.setColor(Color.CYAN);
            }

            // redraws to top // Z tetrominoe
            else if (n == 5) {
                mRectArray[0].left = playAreaWidth / 2;
                mRectArray[0].top = 1;
                mRectArray[0].right = mRectArray[0].left + mSquareSize;
                mRectArray[0].bottom = mRectArray[0].top + mSquareSize;

                mRectArray[1].left = mRectArray[0].left;
                mRectArray[1].top = mRectArray[0].top + mSquareSize;
                mRectArray[1].right = mRectArray[0].right;
                mRectArray[1].bottom = mRectArray[0].bottom + mSquareSize;

                mRectArray[2].left = mRectArray[0].left - mSquareSize;
                mRectArray[2].top = mRectArray[0].top;
                mRectArray[2].right = mRectArray[0].right - mSquareSize;
                mRectArray[2].bottom = mRectArray[0].bottom;

                mRectArray[3].left = mRectArray[0].left + mSquareSize;
                mRectArray[3].top = mRectArray[0].top + mSquareSize;
                mRectArray[3].right = mRectArray[0].right + mSquareSize;
                mRectArray[3].bottom = mRectArray[0].bottom + mSquareSize;

                mPaintSquare.setColor(Color.RED);
            }

            // redraws to top // S tetrominoe
            else if (n == 4) {
                mRectArray[0].left = playAreaWidth / 2;
                mRectArray[0].top = 1;
                mRectArray[0].right = mRectArray[0].left + mSquareSize;
                mRectArray[0].bottom = mRectArray[0].top + mSquareSize;

                mRectArray[1].left = mRectArray[0].left;
                mRectArray[1].top = mRectArray[0].top + mSquareSize;
                mRectArray[1].right = mRectArray[0].right;
                mRectArray[1].bottom = mRectArray[0].bottom + mSquareSize;

                mRectArray[2].left = mRectArray[0].left + mSquareSize;
                mRectArray[2].top = mRectArray[0].top;
                mRectArray[2].right = mRectArray[0].right + mSquareSize;
                mRectArray[2].bottom = mRectArray[0].bottom;

                mRectArray[3].left = mRectArray[0].left - mSquareSize;
                mRectArray[3].top = mRectArray[0].top + mSquareSize;
                mRectArray[3].right = mRectArray[0].right - mSquareSize;
                mRectArray[3].bottom = mRectArray[0].bottom + mSquareSize;

                mPaintSquare.setColor(Color.GREEN);
            }

            // redraws to top // T tetrominoe
            else if (n == 3) {
                mRectArray[0].left = playAreaWidth / 2;
                mRectArray[0].top = 1;
                mRectArray[0].right = mRectArray[0].left + mSquareSize;
                mRectArray[0].bottom = mRectArray[0].top + mSquareSize;

                mRectArray[1].left = mRectArray[0].left;
                mRectArray[1].top = mRectArray[0].top + mSquareSize;
                mRectArray[1].right = mRectArray[0].right;
                mRectArray[1].bottom = mRectArray[0].bottom + mSquareSize;

                mRectArray[2].left = mRectArray[0].left - mSquareSize;
                mRectArray[2].top = mRectArray[0].top + mSquareSize;
                mRectArray[2].right = mRectArray[0].right - mSquareSize;
                mRectArray[2].bottom = mRectArray[0].bottom + mSquareSize;

                mRectArray[3].left = mRectArray[0].left + mSquareSize;
                mRectArray[3].top = mRectArray[0].top + mSquareSize;
                mRectArray[3].right = mRectArray[0].right + mSquareSize;
                mRectArray[3].bottom = mRectArray[0].bottom + mSquareSize;

                mPaintSquare.setColor(Color.rgb(138, 43, 226));
            }

            // redraws to top // cube tetrominoe
            else if (n == 2) {
                mRectArray[0].left = playAreaWidth / 2;
                mRectArray[0].top = 1;
                mRectArray[0].right = mRectArray[0].left + mSquareSize;
                mRectArray[0].bottom = mRectArray[0].top + mSquareSize;

                mRectArray[1].left = mRectArray[0].left;
                mRectArray[1].top = mRectArray[0].top + mSquareSize;
                mRectArray[1].right = mRectArray[0].right;
                mRectArray[1].bottom = mRectArray[0].bottom + mSquareSize;

                mRectArray[2].left = mRectArray[0].left - mSquareSize;
                mRectArray[2].top = mRectArray[0].top;
                mRectArray[2].right = mRectArray[0].right - mSquareSize;
                mRectArray[2].bottom = mRectArray[0].bottom;

                mRectArray[3].left = mRectArray[0].left - mSquareSize;
                mRectArray[3].top = mRectArray[0].top + mSquareSize;
                mRectArray[3].right = mRectArray[0].right - mSquareSize;
                mRectArray[3].bottom = mRectArray[0].bottom + mSquareSize;

                mPaintSquare.setColor(Color.YELLOW);
            }

            // redraws to top // .i tetrominoe
            else if (n == 1) {
                mRectArray[0].left = playAreaWidth / 2;
                mRectArray[0].top = 1;
                mRectArray[0].right = mRectArray[0].left + mSquareSize;
                mRectArray[0].bottom = mRectArray[0].top + mSquareSize;

                mRectArray[1].left = mRectArray[0].left;
                mRectArray[1].top = mRectArray[0].top + mSquareSize;
                mRectArray[1].right = mRectArray[0].right;
                mRectArray[1].bottom = mRectArray[0].bottom + mSquareSize;

                mRectArray[2].left = mRectArray[0].left;
                mRectArray[2].top = mRectArray[0].top + mSquareSize * 2;
                mRectArray[2].right = mRectArray[0].right;
                mRectArray[2].bottom = mRectArray[0].bottom + mSquareSize * 2;

                mRectArray[3].left = mRectArray[0].left - mSquareSize;
                mRectArray[3].top = mRectArray[0].top + mSquareSize * 2;
                mRectArray[3].right = mRectArray[0].right - mSquareSize;
                mRectArray[3].bottom = mRectArray[0].bottom + mSquareSize * 2;

                mPaintSquare.setColor(Color.BLUE);
            }

            // redraws to top // L tetrominoe
            else if (n == 0) {
                mRectArray[0].left = playAreaWidth / 2;
                mRectArray[0].top = 1;
                mRectArray[0].right = mRectArray[0].left + mSquareSize;
                mRectArray[0].bottom = mRectArray[0].top + mSquareSize;

                mRectArray[1].left = mRectArray[0].left;
                mRectArray[1].top = mRectArray[0].top + mSquareSize;
                mRectArray[1].right = mRectArray[0].right;
                mRectArray[1].bottom = mRectArray[0].bottom + mSquareSize;

                mRectArray[2].left = mRectArray[0].left;
                mRectArray[2].top = mRectArray[0].top + mSquareSize * 2;
                mRectArray[2].right = mRectArray[0].right;
                mRectArray[2].bottom = mRectArray[0].bottom + mSquareSize * 2;

                mRectArray[3].left = mRectArray[0].left + mSquareSize;
                mRectArray[3].top = mRectArray[0].top + mSquareSize * 2;
                mRectArray[3].right = mRectArray[0].right + mSquareSize;
                mRectArray[3].bottom = mRectArray[0].bottom + mSquareSize * 2;

                mPaintSquare.setColor(Color.rgb(255, 165, 0));
            }

            randomHolder[0] = randomHolder[1];
            randomHolder[1] = (rand.nextInt(700)) % 7;
        }



        for (int j = 0; j < mRectArray.length; j++) {
            canvas.drawRect(mRectArray[j], mPaintSquare);
            canvas.drawRect(mRectArray[j], mPaintBorder);
        }

        if (true) {
            int f = randomHolder[0];

            // redraws to top // I tetrominoe
            if (f == 6) {
                hRectArray[0].left = (playAreaWidth + (getWidth() - playAreaWidth) / 3);
                hRectArray[0].top = 3*mSquareSize;
                hRectArray[0].right = hRectArray[0].left + mSquareSize;
                hRectArray[0].bottom = hRectArray[0].top + mSquareSize;

                hRectArray[1].left = hRectArray[0].left;
                hRectArray[1].top = hRectArray[0].top + mSquareSize;
                hRectArray[1].right = hRectArray[0].right;
                hRectArray[1].bottom = hRectArray[0].bottom + mSquareSize;

                hRectArray[2].left = hRectArray[0].left;
                hRectArray[2].top = hRectArray[0].top + mSquareSize * 2;
                hRectArray[2].right = hRectArray[0].right;
                hRectArray[2].bottom = hRectArray[0].bottom + mSquareSize * 2;

                hRectArray[3].left = hRectArray[0].left;
                hRectArray[3].top = hRectArray[0].top + mSquareSize * 3;
                hRectArray[3].right = hRectArray[0].right;
                hRectArray[3].bottom = hRectArray[0].bottom + mSquareSize * 3;

                hPaintSquare.setColor(Color.CYAN);
            }

            // redraws to top // Z tetrominoe
            else if (f == 5) {
                hRectArray[0].left = (playAreaWidth + (getWidth() - playAreaWidth) / 3);
                hRectArray[0].top = 3*mSquareSize;
                hRectArray[0].right = hRectArray[0].left + mSquareSize;
                hRectArray[0].bottom = hRectArray[0].top + mSquareSize;

                hRectArray[1].left = hRectArray[0].left;
                hRectArray[1].top = hRectArray[0].top + mSquareSize;
                hRectArray[1].right = hRectArray[0].right;
                hRectArray[1].bottom = hRectArray[0].bottom + mSquareSize;

                hRectArray[2].left = hRectArray[0].left - mSquareSize;
                hRectArray[2].top = hRectArray[0].top;
                hRectArray[2].right = hRectArray[0].right - mSquareSize;
                hRectArray[2].bottom = hRectArray[0].bottom;

                hRectArray[3].left = hRectArray[0].left + mSquareSize;
                hRectArray[3].top = hRectArray[0].top + mSquareSize;
                hRectArray[3].right = hRectArray[0].right + mSquareSize;
                hRectArray[3].bottom = hRectArray[0].bottom + mSquareSize;

                hPaintSquare.setColor(Color.RED);
            }

            // redraws to top // S tetrominoe
            else if (f == 4) {
                hRectArray[0].left = (playAreaWidth + (getWidth() - playAreaWidth) / 3);
                hRectArray[0].top = 3*mSquareSize;
                hRectArray[0].right = hRectArray[0].left + mSquareSize;
                hRectArray[0].bottom = hRectArray[0].top + mSquareSize;

                hRectArray[1].left = hRectArray[0].left;
                hRectArray[1].top = hRectArray[0].top + mSquareSize;
                hRectArray[1].right = hRectArray[0].right;
                hRectArray[1].bottom = hRectArray[0].bottom + mSquareSize;

                hRectArray[2].left = hRectArray[0].left + mSquareSize;
                hRectArray[2].top = hRectArray[0].top;
                hRectArray[2].right = hRectArray[0].right + mSquareSize;
                hRectArray[2].bottom = hRectArray[0].bottom;

                hRectArray[3].left = hRectArray[0].left - mSquareSize;
                hRectArray[3].top = hRectArray[0].top + mSquareSize;
                hRectArray[3].right = hRectArray[0].right - mSquareSize;
                hRectArray[3].bottom = hRectArray[0].bottom + mSquareSize;

                hPaintSquare.setColor(Color.GREEN);
            }

            // redraws to top // T tetrominoe
            else if (f == 3) {
                hRectArray[0].left = (playAreaWidth + (getWidth() - playAreaWidth) / 3);
                hRectArray[0].top = 3*mSquareSize;
                hRectArray[0].right = hRectArray[0].left + mSquareSize;
                hRectArray[0].bottom = hRectArray[0].top + mSquareSize;

                hRectArray[1].left = hRectArray[0].left;
                hRectArray[1].top = hRectArray[0].top + mSquareSize;
                hRectArray[1].right = hRectArray[0].right;
                hRectArray[1].bottom = hRectArray[0].bottom + mSquareSize;

                hRectArray[2].left = hRectArray[0].left - mSquareSize;
                hRectArray[2].top = hRectArray[0].top + mSquareSize;
                hRectArray[2].right = hRectArray[0].right - mSquareSize;
                hRectArray[2].bottom = hRectArray[0].bottom + mSquareSize;

                hRectArray[3].left = hRectArray[0].left + mSquareSize;
                hRectArray[3].top = hRectArray[0].top + mSquareSize;
                hRectArray[3].right = hRectArray[0].right + mSquareSize;
                hRectArray[3].bottom = hRectArray[0].bottom + mSquareSize;

                hPaintSquare.setColor(Color.rgb(138, 43, 226));
            }

            // redraws to top // cube tetrominoe
            else if (f == 2) {
                hRectArray[0].left = (playAreaWidth + (getWidth() - playAreaWidth) / 2);
                hRectArray[0].top = 3*mSquareSize;
                hRectArray[0].right = hRectArray[0].left + mSquareSize;
                hRectArray[0].bottom = hRectArray[0].top + mSquareSize;

                hRectArray[1].left = hRectArray[0].left;
                hRectArray[1].top = hRectArray[0].top + mSquareSize;
                hRectArray[1].right = hRectArray[0].right;
                hRectArray[1].bottom = hRectArray[0].bottom + mSquareSize;

                hRectArray[2].left = hRectArray[0].left - mSquareSize;
                hRectArray[2].top = hRectArray[0].top;
                hRectArray[2].right = hRectArray[0].right - mSquareSize;
                hRectArray[2].bottom = hRectArray[0].bottom;

                hRectArray[3].left = hRectArray[0].left - mSquareSize;
                hRectArray[3].top = hRectArray[0].top + mSquareSize;
                hRectArray[3].right = hRectArray[0].right - mSquareSize;
                hRectArray[3].bottom = hRectArray[0].bottom + mSquareSize;

                hPaintSquare.setColor(Color.YELLOW);
            }

            // redraws to top // .i tetrominoe
            else if (f == 1) {
                hRectArray[0].left = (playAreaWidth + (getWidth() - playAreaWidth) / 3);
                hRectArray[0].top = 3*mSquareSize;
                hRectArray[0].right = hRectArray[0].left + mSquareSize;
                hRectArray[0].bottom = hRectArray[0].top + mSquareSize;

                hRectArray[1].left = hRectArray[0].left;
                hRectArray[1].top = hRectArray[0].top + mSquareSize;
                hRectArray[1].right = hRectArray[0].right;
                hRectArray[1].bottom = hRectArray[0].bottom + mSquareSize;

                hRectArray[2].left = hRectArray[0].left;
                hRectArray[2].top = hRectArray[0].top + mSquareSize * 2;
                hRectArray[2].right = hRectArray[0].right;
                hRectArray[2].bottom = hRectArray[0].bottom + mSquareSize * 2;

                hRectArray[3].left = hRectArray[0].left - mSquareSize;
                hRectArray[3].top = hRectArray[0].top + mSquareSize * 2;
                hRectArray[3].right = hRectArray[0].right - mSquareSize;
                hRectArray[3].bottom = hRectArray[0].bottom + mSquareSize * 2;

                hPaintSquare.setColor(Color.BLUE);
            }

            // redraws to top // L tetrominoe
            else if (f == 0) {
                hRectArray[0].left = (playAreaWidth + (getWidth() - playAreaWidth) / 3);
                hRectArray[0].top = 3*mSquareSize;
                hRectArray[0].right = hRectArray[0].left + mSquareSize;
                hRectArray[0].bottom = hRectArray[0].top + mSquareSize;

                hRectArray[1].left = hRectArray[0].left;
                hRectArray[1].top = hRectArray[0].top + mSquareSize;
                hRectArray[1].right = hRectArray[0].right;
                hRectArray[1].bottom = hRectArray[0].bottom + mSquareSize;

                hRectArray[2].left = hRectArray[0].left;
                hRectArray[2].top = hRectArray[0].top + mSquareSize * 2;
                hRectArray[2].right = hRectArray[0].right;
                hRectArray[2].bottom = hRectArray[0].bottom + mSquareSize * 2;

                hRectArray[3].left = hRectArray[0].left + mSquareSize;
                hRectArray[3].top = hRectArray[0].top + mSquareSize * 2;
                hRectArray[3].right = hRectArray[0].right + mSquareSize;
                hRectArray[3].bottom = hRectArray[0].bottom + mSquareSize * 2;

                hPaintSquare.setColor(Color.rgb(255, 165, 0));
            }
        }

        for (int k = 0; k < hRectArray.length; k++){
            canvas.drawRect(hRectArray[k], hPaintSquare);
            canvas.drawRect(hRectArray[k], mPaintBorder);
        }

        mSavedRect = FullLineNMove(mSavedRect);

        if (isThread == 0) {
            Thread test = new Thread() {
                public void run() {
                    int runforever = 1;
                    try {
                        while (runforever == 1) {
                            shapeDown();
                            Thread.sleep(2000);
                        }
                    } catch (InterruptedException v) {
                        System.out.println(v);
                    }
                }
            };
            test.start();
            isThread = 1;
        }


        for (int i = 0; i < mSavedRect.size(); i++){
            canvas.drawRect(mSavedRect.get(i), mPaintSave);
            canvas.drawRect(mSavedRect.get(i), mPaintBorder);
        }

        //SCORE BOX
        sideBox.top = 1;
        sideBox.bottom = 1 + 2*DEFAULT_SQUARE_SIZE;
        sideBox.right = playAreaWidth;
        sideBox.left = getWidth();
        canvas.drawRect(sideBox, mPaintdrop);
        canvas.drawRect(sideBox, mPaintBorder);
        canvas.drawText(Integer.toString(score), playAreaWidth + (getWidth() - playAreaWidth)/3, 1 + DEFAULT_SQUARE_SIZE * 1.5f, scoreDisp);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        return value;
    }
}
