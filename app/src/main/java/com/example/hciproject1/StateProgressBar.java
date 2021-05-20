package com.example.hciproject1;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

import androidx.core.content.ContextCompat;

import com.example.hciproject1.utils.FontManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StateProgressBar extends View {

    public enum StateNumber {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);
        private int value;

        StateNumber(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static final int MIN_STATE_NUMBER = 1;
    private static final int MAX_STATE_NUMBER = 5;

    private static final String STATE_SIZE_KEY = "mStateSize";
    private static final String STATE_LINE_THICKNESS_KEY = "mStateLineThickness";
    private static final String STATE_NUMBER_TEXT_SIZE_KEY = "mStateNumberTextSize";
    private static final String STATE_DESCRIPTION_SIZE_KEY = "mStateDescriptionSize";

    private static final String MAX_STATE_NUMBER_KEY = "mMaxStateNumber";
    private static final String CURRENT_STATE_NUMBER_KEY = "mCurrentStateNumber";

    private static final String ANIM_START_DELAY_KEY = "mAnimStartDelay";
    private static final String ANIM_DURATION_KEY = "mAnimDuration";

    private static final String DESC_TOP_SPACE_DECREMENTER_KEY = "mDescTopSpaceDecrementer";
    private static final String DESC_TOP_SPACE_INCREMENTER_KEY = "mDescTopSpaceIncrementer";

    private static final String BACKGROUND_COLOR_KEY = "mBackgroundColor";
    private static final String FOREGROUND_COLOR_KEY = "mForegroundColor";
    private static final String STATE_NUMBER_BACKGROUND_COLOR_KEY = "mStateNumberBackgroundColor";
    private static final String STATE_NUMBER_FOREGROUND_COLOR_KEY = "mStateNumberForegroundColor";

    private static final String CURRENT_STATE_DESC_COLOR_KEY = "mCurrentStateDescriptionColor";
    private static final String STATE_DESC_COLOR_KEY = "mStateDescriptionColor";

    private static final String CHECK_STATE_COMPLETED_KEY = "mCheckStateCompleted";

    private static final String ENABLE_ALL_STATES_COMPLETED_KEY = "mEnableAllStatesCompleted";

    private static final String JUSTIFY_MULTILINE_DESC_KEY = "mJustifyMultilineDescription";

    private static final String DESCRIPTION_LINE_SPACING_KEY = "mDescriptionLinesSpacing";

    private static final String END_CENTER_X_KEY = "mEndCenterX";
    private static final String START_CENTER_X_KEY = "mStartCenterX";
    private static final String ANIM_START_X_POS_KEY = "mAnimStartXPos";
    private static final String ANIM_END_X_POS_KEY = "mAnimEndXPos";
    private static final String IS_CURRENT_ANIM_STARTED_KEY = "mIsCurrentAnimStarted";
    private static final String ANIMATE_TO_CURRENT_PROGRESS_STATE_KEY = "mAnimateToCurrentProgressState";
    private static final String INSTANCE_STATE = "saved_instance";

    private ArrayList<String> mStateDescriptionData = new ArrayList<String>();

    private float mStateRadius;
    private float mStateSize;
    private float mStateLineThickness;
    private float mStateNumberTextSize;
    private float mStateDescriptionSize;

    /**
     * width of one cell = stageWidth/noOfStates
     */
    private float mCellWidth;

    private float mCellHeight;

    /**
     * next cell(state) from previous cell
     */
    private float mNextCellWidth;

    /**
     * center of first cell(state)
     */
    private float mStartCenterX;

    /**
     * center of last cell(state)
     */
    private float mEndCenterX;

    private int mMaxStateNumber;
    private int mCurrentStateNumber;

    protected int mAnimStartDelay;
    protected int mAnimDuration;

    private float mSpacing;

    private float mDescTopSpaceDecrementer;
    private float mDescTopSpaceIncrementer;

    private static final float DEFAULT_TEXT_SIZE = 15f;
    private static final float DEFAULT_STATE_SIZE = 25f;

    /**
     * Paints for drawing
     */
    private Paint mStateNumberForegroundPaint;
    private Paint mStateCheckedForegroundPaint;
    private Paint mStateNumberBackgroundPaint;
    private Paint mBackgroundPaint;
    private Paint mForegroundPaint;
    private Paint mCurrentStateDescriptionPaint;
    private Paint mStateDescriptionPaint;

    private int mBackgroundColor;
    private int mForegroundColor;
    private int mStateNumberBackgroundColor;
    private int mStateNumberForegroundColor;
    private int mCurrentStateDescriptionColor;
    private int mStateDescriptionColor;

    /**
     * animate inner line to current progress state
     */
    private Animator mAnimator;

    /**
     * tracks progress of line animator
     */
    private float mAnimStartXPos;
    private float mAnimEndXPos;

    private boolean mIsCurrentAnimStarted;

    private static final String EMPTY_SPACE_DESCRIPTOR = "";

    private boolean mIsDescriptionMultiline;
    private int mMaxDescriptionLine;
    private float mDescriptionLinesSpacing;

    private static final String STATE_DESCRIPTION_LINE_SEPARATOR = "\n";

    private boolean mJustifyMultilineDescription;

    private boolean mAnimateToCurrentProgressState;
    private boolean mEnableAllStatesCompleted;
    private boolean mCheckStateCompleted;

    private Typeface mCheckFont;

    public StateProgressBar(Context context) {
        this(context, null, 0);
    }

    public StateProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
        initializePainters();
        updateCheckAllStatesValues(mEnableAllStatesCompleted);

    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        initStateProgressBar(context);

        mStateDescriptionSize = convertSpToPixel(mStateDescriptionSize);
        mStateLineThickness = convertDpToPixel(mStateLineThickness);
        mSpacing = convertDpToPixel(mSpacing);

        mCheckFont = FontManager.getTypeface(context);

        if (attrs != null) {

            final TypedArray a = context.obtainStyledAttributes(attrs, com.example.hciproject1.R.styleable.StateProgressBar, defStyle, 0);

            mBackgroundColor = a.getColor(com.example.hciproject1.R.styleable.StateProgressBar_spb_stateBackgroundColor, mBackgroundColor);
            mForegroundColor = a.getColor(com.example.hciproject1.R.styleable.StateProgressBar_spb_stateForegroundColor, mForegroundColor);
            mStateNumberBackgroundColor = a.getColor(com.example.hciproject1.R.styleable.StateProgressBar_spb_stateNumberBackgroundColor, mStateNumberBackgroundColor);
            mStateNumberForegroundColor = a.getColor(com.example.hciproject1.R.styleable.StateProgressBar_spb_stateNumberForegroundColor, mStateNumberForegroundColor);
            mCurrentStateDescriptionColor = a.getColor(com.example.hciproject1.R.styleable.StateProgressBar_spb_currentStateDescriptionColor, mCurrentStateDescriptionColor);
            mStateDescriptionColor = a.getColor(com.example.hciproject1.R.styleable.StateProgressBar_spb_stateDescriptionColor, mStateDescriptionColor);

            mCurrentStateNumber = a.getInteger(com.example.hciproject1.R.styleable.StateProgressBar_spb_currentStateNumber, mCurrentStateNumber);
            mMaxStateNumber = a.getInteger(com.example.hciproject1.R.styleable.StateProgressBar_spb_maxStateNumber, mMaxStateNumber);

            mStateSize = a.getDimension(com.example.hciproject1.R.styleable.StateProgressBar_spb_stateSize, mStateSize);
            mStateNumberTextSize = a.getDimension(com.example.hciproject1.R.styleable.StateProgressBar_spb_stateTextSize, mStateNumberTextSize);
            mStateDescriptionSize = a.getDimension(com.example.hciproject1.R.styleable.StateProgressBar_spb_stateDescriptionSize, mStateDescriptionSize);
            mStateLineThickness = a.getDimension(com.example.hciproject1.R.styleable.StateProgressBar_spb_stateLineThickness, mStateLineThickness);

            mCheckStateCompleted = a.getBoolean(com.example.hciproject1.R.styleable.StateProgressBar_spb_checkStateCompleted, mCheckStateCompleted);
            mAnimateToCurrentProgressState = a.getBoolean(com.example.hciproject1.R.styleable.StateProgressBar_spb_animateToCurrentProgressState, mAnimateToCurrentProgressState);
            mEnableAllStatesCompleted = a.getBoolean(com.example.hciproject1.R.styleable.StateProgressBar_spb_enableAllStatesCompleted, mEnableAllStatesCompleted);

            mDescTopSpaceDecrementer = a.getDimension(com.example.hciproject1.R.styleable.StateProgressBar_spb_descriptionTopSpaceDecrementer, mDescTopSpaceDecrementer);
            mDescTopSpaceIncrementer = a.getDimension(com.example.hciproject1.R.styleable.StateProgressBar_spb_descriptionTopSpaceIncrementer, mDescTopSpaceIncrementer);

            mAnimDuration = a.getInteger(com.example.hciproject1.R.styleable.StateProgressBar_spb_animationDuration, mAnimDuration);
            mAnimStartDelay = a.getInteger(com.example.hciproject1.R.styleable.StateProgressBar_spb_animationStartDelay, mAnimStartDelay);

            mMaxDescriptionLine = a.getInteger(com.example.hciproject1.R.styleable.StateProgressBar_spb_maxDescriptionLines, mMaxDescriptionLine);

            mDescriptionLinesSpacing = a.getDimension(com.example.hciproject1.R.styleable.StateProgressBar_spb_descriptionLinesSpacing, mDescriptionLinesSpacing);

            mJustifyMultilineDescription = a.getBoolean(com.example.hciproject1.R.styleable.StateProgressBar_spb_justifyMultilineDescription, mJustifyMultilineDescription);

            if (!mAnimateToCurrentProgressState) {
                stopAnimation();
            }

            resolveStateSize();
            validateLineThickness(mStateLineThickness);
            validateStateNumber(mCurrentStateNumber);

            mStateRadius = mStateSize / 2;

            a.recycle();

        }

    }


    private void initializePainters() {

        mBackgroundPaint = setPaintAttributes(mStateLineThickness, mBackgroundColor);
        mForegroundPaint = setPaintAttributes(mStateLineThickness, mForegroundColor);

        mStateNumberForegroundPaint = setPaintAttributes(mStateNumberTextSize, mStateNumberForegroundColor, mCheckFont);
        mStateCheckedForegroundPaint = setPaintAttributes(mStateNumberTextSize, mStateNumberForegroundColor, mCheckFont);

        mStateNumberBackgroundPaint = setPaintAttributes(mStateNumberTextSize, mStateNumberBackgroundColor, mCheckFont);
        mCurrentStateDescriptionPaint = setPaintAttributes(mStateDescriptionSize, mCurrentStateDescriptionColor, mCheckFont);

        mStateDescriptionPaint = setPaintAttributes(mStateDescriptionSize, mStateDescriptionColor, mCheckFont);

    }

    private void validateLineThickness(float lineThickness) {
        float halvedStateSize = mStateSize / 2;

        if (lineThickness > halvedStateSize) {
            mStateLineThickness = halvedStateSize;
        }
    }

    private void validateStateSize() {
        if (mStateSize <= mStateNumberTextSize) {
            mStateSize = mStateNumberTextSize + mStateNumberTextSize / 2;
        }
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        mBackgroundPaint.setColor(mBackgroundColor);
        invalidate();
    }

    private void resolveStateLineThickness() {
        validateLineThickness(mStateLineThickness);
        mBackgroundPaint.setStrokeWidth(mStateLineThickness);
        mForegroundPaint.setStrokeWidth(mStateLineThickness);
        invalidate();
    }

    private void resolveMaxStateNumber() {
        validateStateNumber(mCurrentStateNumber);
        updateCheckAllStatesValues(mEnableAllStatesCompleted);
        invalidate();
    }

    private void resetStateSizeValues() {

        resolveStateSize();

        mStateNumberForegroundPaint.setTextSize(mStateNumberTextSize);
        mStateNumberBackgroundPaint.setTextSize(mStateNumberTextSize);
        mStateCheckedForegroundPaint.setTextSize(mStateNumberTextSize);

        mStateRadius = mStateSize / 2;

        validateLineThickness(mStateLineThickness);

        mBackgroundPaint.setStrokeWidth(mStateLineThickness);
        mForegroundPaint.setStrokeWidth(mStateLineThickness);
        requestLayout();
    }

    private void resolveStateDescriptionSize() {
        mCurrentStateDescriptionPaint.setTextSize(mStateDescriptionSize);
        mStateDescriptionPaint.setTextSize(mStateDescriptionSize);
        requestLayout();
    }

    public void checkStateCompleted(boolean checkStateCompleted) {
        mCheckStateCompleted = checkStateCompleted;
        invalidate();
    }

    public void setAllStatesCompleted(boolean enableAllStatesCompleted) {
        mEnableAllStatesCompleted = enableAllStatesCompleted;
        updateCheckAllStatesValues(mEnableAllStatesCompleted);
        invalidate();
    }

    private void updateCheckAllStatesValues(boolean enableAllStatesCompleted) {
        if (enableAllStatesCompleted) {
            mCheckStateCompleted = true;
            mCurrentStateNumber = mMaxStateNumber;
            mStateDescriptionPaint.setColor(mCurrentStateDescriptionPaint.getColor());
        } else {
            mStateDescriptionPaint.setColor(mStateDescriptionPaint.getColor());
        }
    }

    public void enableAnimationToCurrentState(boolean animateToCurrentProgressState) {
        this.mAnimateToCurrentProgressState = animateToCurrentProgressState;

        if (mAnimateToCurrentProgressState && mAnimator == null) {
            if (super.getVisibility() == VISIBLE) {
                startAnimator();
            }
        }

        invalidate();

    }

    private void validateStateNumber(int stateNumber) {
        if (stateNumber > mMaxStateNumber) {
            throw new IllegalStateException("State number (" + stateNumber + ") cannot be greater than total number of states " + mMaxStateNumber);
        }
    }

    public void setDescriptionTopSpaceIncrementer(float spaceIncrementer) {
        mDescTopSpaceIncrementer = spaceIncrementer;
        requestLayout();
    }

    private void updateDescriptionMultilineStatus(boolean multiline) {
        mIsDescriptionMultiline = multiline;
    }

    private Paint setPaintAttributes(float strokeWidth, int color) {
        Paint paint = setPaintAttributes(color);
        paint.setStrokeWidth(strokeWidth);
        return paint;
    }

    private Paint setPaintAttributes(float textSize, int color, Typeface typeface) {
        Paint paint = setPaintAttributes(color);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        return paint;
    }

    private Paint setPaintAttributes(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        return paint;
    }

    private void initStateProgressBar(Context context) {

        mBackgroundColor = ContextCompat.getColor(context, com.example.hciproject1.R.color.background_color);
        mForegroundColor = ContextCompat.getColor(context, com.example.hciproject1.R.color.foreground_color);
        mStateNumberBackgroundColor = ContextCompat.getColor(context, com.example.hciproject1.R.color.background_text_color);
        mStateNumberForegroundColor = ContextCompat.getColor(context, com.example.hciproject1.R.color.foreground_text_color);
        mCurrentStateDescriptionColor = ContextCompat.getColor(context, com.example.hciproject1.R.color.foreground_color);
        mStateDescriptionColor = ContextCompat.getColor(context, com.example.hciproject1.R.color.background_text_color);

        mStateSize = 0.0f;
        mStateLineThickness = 4.0f;
        mStateNumberTextSize = 0.0f;
        mStateDescriptionSize = 15f;

        mMaxStateNumber = StateNumber.FIVE.getValue();
        mCurrentStateNumber = StateNumber.ONE.getValue();

        mSpacing = 4.0f;

        mDescTopSpaceDecrementer = 0.0f;
        mDescTopSpaceIncrementer = 0.0f;

        mDescriptionLinesSpacing = 0.0f;

        mCheckStateCompleted = false;
        mAnimateToCurrentProgressState = false;
        mEnableAllStatesCompleted = false;

        mAnimStartDelay = 100;
        mAnimDuration = 40000;

        mJustifyMultilineDescription = false;
    }

    private void resolveStateSize() {
        resolveStateSize(mStateSize != 0, mStateNumberTextSize != 0);
    }

    private void resolveStateSize(boolean isStateSizeSet, boolean isStateTextSizeSet) {
        if (!isStateSizeSet && !isStateTextSizeSet) {
            mStateSize = convertDpToPixel(DEFAULT_STATE_SIZE);
            mStateNumberTextSize = convertSpToPixel(DEFAULT_TEXT_SIZE);

        } else if (isStateSizeSet && isStateTextSizeSet) {
            validateStateSize();

        } else if (!isStateSizeSet) {
            mStateSize = mStateNumberTextSize + mStateNumberTextSize / 2;

        } else {
            mStateNumberTextSize = mStateSize - (mStateSize * 0.375f);
        }

    }

    private void drawCircles(Canvas canvas, Paint paint, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            canvas.drawCircle(mCellWidth * (i + 1) - (mCellWidth / 2), mCellHeight / 2, mStateRadius, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCellWidth = getWidth() / mMaxStateNumber;
        mNextCellWidth = mCellWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawState(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getDesiredHeight();
        int width = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(width, height);

        mCellHeight = getCellHeight();

    }

    private int getDesiredHeight() {
        if (mStateDescriptionData.isEmpty()) {
            return (int) (2 * mStateRadius) + (int) (mSpacing);
        } else {
            if (checkForDescriptionMultiLine(mStateDescriptionData)) {
                return (int) (2 * mStateRadius) + (int) (selectMaxDescriptionLine(mMaxDescriptionLine) * (1.3 * mStateDescriptionSize)) + (int) (mSpacing) - (int) (mDescTopSpaceDecrementer) + (int) (mDescTopSpaceIncrementer) + (int) mDescriptionLinesSpacing;

            } else {
                return (int) (2 * mStateRadius) + (int) (1.3 * mStateDescriptionSize) + (int) (mSpacing) - (int) (mDescTopSpaceDecrementer) + (int) (mDescTopSpaceIncrementer);
            }
        }
    }

    private int getCellHeight() {
        return (int) (2 * mStateRadius) + (int) (mSpacing);
    }

    private boolean checkForDescriptionMultiLine(ArrayList<String> stateDescriptionData) {
        boolean isMultiLine;
        for (String stateDescription : stateDescriptionData) {
            isMultiLine = stateDescription.contains(STATE_DESCRIPTION_LINE_SEPARATOR);
            if (isMultiLine) {
                updateDescriptionMultilineStatus(true);
                return true;
            }
        }
        return false;
    }

    private int getMaxDescriptionLine(List<String> stateDescriptionData) {
        int maxLine = 1;
        for (String stateDescription : stateDescriptionData) {
            int lineSize = stateDescription.split(STATE_DESCRIPTION_LINE_SEPARATOR).length;
            maxLine = lineSize > maxLine ? lineSize : maxLine;
        }
        mMaxDescriptionLine = maxLine;
        return maxLine;
    }

    private int selectMaxDescriptionLine(int maxLine) {
        return maxLine > 1 ? maxLine : getMaxDescriptionLine(mStateDescriptionData);
    }

    private void drawState(Canvas canvas) {

        setAnimatorStartEndCenterX();
        drawCurrentStateJoiningLine(canvas);

        if (mEnableAllStatesCompleted) {
            drawBackgroundCircles(canvas);
            drawForegroundCircles(canvas);
            drawLines(canvas, mForegroundPaint, 0, mMaxStateNumber);
        } else {
            drawBackgroundLines(canvas);
            drawBackgroundCircles(canvas);
            drawForegroundCircles(canvas);
            drawForegroundLines(canvas);
        }

        drawStateNumberText(canvas, mMaxStateNumber);
        drawStateDescriptionText(canvas);
    }

    private void drawBackgroundCircles(Canvas canvas) {

        int startIndex = mCurrentStateNumber;
        int endIndex = mMaxStateNumber;

        drawCircles(canvas, mBackgroundPaint, startIndex, endIndex);
    }

    private void drawForegroundCircles(Canvas canvas) {

        int startIndex = 0;
        int endIndex = mCurrentStateNumber;

        drawCircles(canvas, mForegroundPaint, startIndex, endIndex);
    }

    private void drawBackgroundLines(Canvas canvas) {
        int startIndex = mCurrentStateNumber - 1;
        int endIndex = mMaxStateNumber;

        drawLines(canvas, mBackgroundPaint, startIndex, endIndex);
    }

    private void drawForegroundLines(Canvas canvas) {
        int startIndex = 0;
        int endIndex = mCurrentStateNumber - 1;

        drawLines(canvas, mForegroundPaint, startIndex, endIndex);
    }

    private void drawLines(Canvas canvas, Paint paint, int startIndex, int endIndex) {

        float startCenterX;
        float endCenterX;

        float startX;
        float stopX;


        if (endIndex > startIndex) {

            startCenterX = mCellWidth / 2 + mCellWidth * startIndex;

            endCenterX = mCellWidth * endIndex - (mCellWidth / 2);

            startX = startCenterX + (mStateRadius * 0.75f);
            stopX = endCenterX - (mStateRadius * 0.75f);

            canvas.drawLine(startX, mCellHeight / 2, stopX, mCellHeight / 2, paint);

        }

    }

    private void setAnimatorStartEndCenterX() {
        if (mCurrentStateNumber > MIN_STATE_NUMBER && mCurrentStateNumber < MAX_STATE_NUMBER + 1) {
            final int count = mCurrentStateNumber - 1;
            for (int i = 0; i < count; i++) {

                if (i == 0) {
                    mStartCenterX = mNextCellWidth - (mCellWidth / 2);
                } else {
                    mStartCenterX = mEndCenterX;
                }

                mNextCellWidth += mCellWidth;
                mEndCenterX = mNextCellWidth - (mCellWidth / 2);
            }
        } else {
            resetStateAnimationData();
        }
    }

    private void drawCurrentStateJoiningLine(Canvas canvas) {
        if (mAnimateToCurrentProgressState) {
            animateToCurrentState(canvas);
        } else {
            drawLineToCurrentState(canvas);
        }
    }

    private void drawLineToCurrentState(Canvas canvas) {

        canvas.drawLine(mStartCenterX, mCellHeight / 2, mEndCenterX, mCellHeight / 2, mForegroundPaint);

        mNextCellWidth = mCellWidth;

        stopAnimation();
    }

    private void animateToCurrentState(Canvas canvas) {
        if (!mIsCurrentAnimStarted) {
            mAnimStartXPos = mStartCenterX;
            mAnimEndXPos = mAnimStartXPos;
            mIsCurrentAnimStarted = true;
        }

        if (mAnimEndXPos < mStartCenterX || mStartCenterX > mEndCenterX) {
            stopAnimation();
            enableAnimationToCurrentState(false);
            invalidate();
        } else if (mAnimEndXPos <= mEndCenterX) {
            canvas.drawLine(mStartCenterX, mCellHeight / 2, mAnimEndXPos, mCellHeight / 2, mForegroundPaint);
            canvas.drawLine(mAnimEndXPos, mCellHeight / 2, mEndCenterX, mCellHeight / 2, mBackgroundPaint);
            mAnimStartXPos = mAnimEndXPos;
        } else {
            canvas.drawLine(mStartCenterX, mCellHeight / 2, mEndCenterX, mCellHeight / 2, mForegroundPaint);
        }
        mNextCellWidth = mCellWidth;
    }

    private void drawStateDescriptionText(Canvas canvas) {

        int xPos;
        int yPos;
        Paint innerPaintType;

        if (!mStateDescriptionData.isEmpty()) {

            for (int i = 0; i < mStateDescriptionData.size(); i++) {
                if (i < mMaxStateNumber) {
                    innerPaintType = selectDescriptionPaint(mCurrentStateNumber, i);
                    xPos = (int) (mNextCellWidth - (mCellWidth / 2));


                    if (mIsDescriptionMultiline && mMaxDescriptionLine > 1) {
                        String stateDescription = mStateDescriptionData.get(i);
                        int nextLineCounter = 0;
                        int newXPos = 0;
                        String[] stateDescriptionLines = stateDescription.split(STATE_DESCRIPTION_LINE_SEPARATOR);

                        for (String line : stateDescriptionLines) {
                            nextLineCounter = nextLineCounter + 1;

                            if (mJustifyMultilineDescription && nextLineCounter > 1) {
                                newXPos = getNewXPosForDescriptionMultilineJustification(stateDescriptionLines[0], line, innerPaintType, xPos);
                            }

                            if (nextLineCounter <= mMaxDescriptionLine) {
                                yPos = (int) (mCellHeight + (nextLineCounter * mStateDescriptionSize) - mSpacing - mDescTopSpaceDecrementer + mDescTopSpaceIncrementer + (nextLineCounter > 1 ? (mDescriptionLinesSpacing * (nextLineCounter - 1)) : 0));//mSpacing = mStateNumberForegroundPaint.getTextSize()
                                canvas.drawText(line, newXPos == 0 ? xPos : newXPos, yPos, innerPaintType);
                            }

                        }

                    } else {
                        yPos = (int) (mCellHeight + mStateDescriptionSize - mSpacing - mDescTopSpaceDecrementer + mDescTopSpaceIncrementer);//mSpacing = mStateNumberForegroundPaint.getTextSize()
                        canvas.drawText(mStateDescriptionData.get(i), xPos, yPos, innerPaintType);
                    }

                    mNextCellWidth += mCellWidth;
                }
            }

        }

        mNextCellWidth = mCellWidth;
    }

    private int getNewXPosForDescriptionMultilineJustification(String firstLine, String nextLine, Paint paint, int xPos) {

        float firstLineWidth = paint.measureText(firstLine);
        float nextLineWidth = paint.measureText(nextLine);

        float newXPos;
        float widthDiff;

        if (firstLineWidth > nextLineWidth) {

            widthDiff = firstLineWidth - nextLineWidth;
            newXPos = xPos - widthDiff / 2;

        } else if (firstLineWidth < nextLineWidth) {

            widthDiff = nextLineWidth - firstLineWidth;
            newXPos = xPos + widthDiff / 2;

        } else {
            newXPos = xPos;
        }

        return Math.round(newXPos);

    }

    private Paint selectDescriptionPaint(int currentState, int statePosition) {

        if (statePosition + 1 == currentState) {
            return mCurrentStateDescriptionPaint;
        } else {
            return mStateDescriptionPaint;
        }
    }

    private void resolveStateDescriptionDataSize(ArrayList<String> stateDescriptionData) {

        final int stateDescriptionDataSize = stateDescriptionData.size();
        if (stateDescriptionDataSize < mMaxStateNumber) {
            for (int i = 0; i < mMaxStateNumber - stateDescriptionDataSize; i++) {
                stateDescriptionData.add(stateDescriptionDataSize + i, EMPTY_SPACE_DESCRIPTOR);
            }
        }
    }

    public void setStateDescriptionData(String[] stateDescriptionData) {
        mStateDescriptionData = new ArrayList<>(Arrays.asList(stateDescriptionData));

        resolveStateDescriptionDataSize(mStateDescriptionData);

        requestLayout();
    }

    private void resetStateAnimationData() {
        if (mStartCenterX > 0 || mStartCenterX < 0) mStartCenterX = 0;
        if (mEndCenterX > 0 || mEndCenterX < 0) mEndCenterX = 0;
        if (mAnimEndXPos > 0 || mAnimEndXPos < 0) mAnimEndXPos = 0;
        if (mIsCurrentAnimStarted) mIsCurrentAnimStarted = false;
    }

    private void drawStateNumberText(Canvas canvas, int noOfCircles) {

        int xPos;
        int yPos;
        Paint innerPaintType;
        boolean isChecked;

        for (int i = 0; i < noOfCircles; i++) {

            innerPaintType = selectPaintType(mCurrentStateNumber, i, mCheckStateCompleted);

            xPos = (int) (mCellWidth * (i + 1) - (mCellWidth / 2));

            yPos = (int) ((mCellHeight / 2) - ((innerPaintType.descent() + innerPaintType.ascent()) / 2));

            isChecked = isCheckIconUsed(mCurrentStateNumber, i);

            if (mCheckStateCompleted && isChecked) {
                canvas.drawText(getContext().getString(com.example.hciproject1.R.string.check_icon), xPos, yPos, innerPaintType);
            } else {
                canvas.drawText(String.valueOf(i + 1), xPos, yPos, innerPaintType);
            }
        }

    }

    private Paint selectPaintType(int currentState, int statePosition, boolean checkStateCompleted) {

        Paint foregroundPaint = mStateNumberForegroundPaint;
        Paint backgroundPaint = mStateNumberBackgroundPaint;

        if (checkStateCompleted) {
            return applyCheckStateCompletedPaintType(currentState, statePosition, true);
        } else {

            if ((statePosition + 1 == currentState) || (statePosition + 1 < currentState)) {
                return foregroundPaint;
            } else {
                return backgroundPaint;
            }
        }

    }

    private Paint applyCheckStateCompletedPaintType(int currentState, int statePosition, boolean checkStateCompleted) {
        if (checkStateCompleted(currentState, statePosition, checkStateCompleted)) {
            return mStateCheckedForegroundPaint;
        } else if (statePosition + 1 == currentState) {
            return mStateNumberForegroundPaint;
        } else {
            return mStateNumberBackgroundPaint;
        }
    }

    private boolean checkStateCompleted(int currentState, int statePosition, boolean checkStateCompleted) {

        return (mEnableAllStatesCompleted || statePosition + 1 < currentState) && checkStateCompleted;

    }

    private boolean isCheckIconUsed(int currentState, int statePosition) {

        return mEnableAllStatesCompleted || statePosition + 1 < currentState;
    }

    private void startAnimator() {
        mAnimator = new Animator();
        mAnimator.start();
    }

    private void stopAnimation() {
        if (mAnimator != null) {
            mAnimator.stop();
        }
    }

    private class Animator implements Runnable {
        private Scroller mScroller;
        private boolean mRestartAnimation = false;

        public Animator() {
            mScroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
        }

        public void run() {
            if (mAnimator != this) return;

            if (mRestartAnimation) {
                mScroller.startScroll(0, (int) mStartCenterX, 0, (int) mEndCenterX, mAnimDuration);

                mRestartAnimation = false;
            }

            boolean scrollRemains = mScroller.computeScrollOffset();

            mAnimStartXPos = mAnimEndXPos;
            mAnimEndXPos = mScroller.getCurrY();

            if (scrollRemains) {
                invalidate();
                post(this);
            } else {
                stop();
                enableAnimationToCurrentState(false);
            }

        }

        public void start() {
            mRestartAnimation = true;
            postDelayed(this, mAnimStartDelay);
        }

        public void stop() {
            removeCallbacks(this);
            mAnimator = null;
        }

    }

    private float convertDpToPixel(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale;
    }

    private float convertSpToPixel(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (super.getVisibility() == VISIBLE) {
            startAnimator();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();

        super.onDetachedFromWindow();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            startAnimator();
        } else if (visibility == GONE){
            resetStateAnimationData();
            stopAnimation();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(END_CENTER_X_KEY, this.mEndCenterX);
        bundle.putFloat(START_CENTER_X_KEY, this.mStartCenterX);

        bundle.putFloat(ANIM_START_X_POS_KEY, this.mAnimStartXPos);
        bundle.putFloat(ANIM_END_X_POS_KEY, this.mAnimEndXPos);

        bundle.putBoolean(IS_CURRENT_ANIM_STARTED_KEY, this.mIsCurrentAnimStarted);
        bundle.putBoolean(ANIMATE_TO_CURRENT_PROGRESS_STATE_KEY, this.mAnimateToCurrentProgressState);


        bundle.putFloat(STATE_SIZE_KEY, this.mStateSize);
        bundle.putFloat(STATE_LINE_THICKNESS_KEY, this.mStateLineThickness);
        bundle.putFloat(STATE_NUMBER_TEXT_SIZE_KEY, this.mStateNumberTextSize);
        bundle.putFloat(STATE_DESCRIPTION_SIZE_KEY, this.mStateDescriptionSize);

        bundle.putInt(MAX_STATE_NUMBER_KEY, this.mMaxStateNumber);
        bundle.putInt(CURRENT_STATE_NUMBER_KEY, this.mCurrentStateNumber);
        bundle.putInt(ANIM_START_DELAY_KEY, this.mAnimStartDelay);
        bundle.putInt(ANIM_DURATION_KEY, this.mAnimDuration);

        bundle.putFloat(DESC_TOP_SPACE_DECREMENTER_KEY, this.mDescTopSpaceDecrementer);
        bundle.putFloat(DESC_TOP_SPACE_INCREMENTER_KEY, this.mDescTopSpaceIncrementer);

        bundle.putFloat(DESCRIPTION_LINE_SPACING_KEY, this.mDescriptionLinesSpacing);

        bundle.putInt(BACKGROUND_COLOR_KEY, this.mBackgroundColor);
        bundle.putInt(FOREGROUND_COLOR_KEY, this.mForegroundColor);
        bundle.putInt(STATE_NUMBER_BACKGROUND_COLOR_KEY, this.mStateNumberBackgroundColor);
        bundle.putInt(STATE_NUMBER_FOREGROUND_COLOR_KEY, this.mStateNumberForegroundColor);

        bundle.putInt(CURRENT_STATE_DESC_COLOR_KEY, this.mCurrentStateDescriptionColor);
        bundle.putInt(STATE_DESC_COLOR_KEY, this.mStateDescriptionColor);

        bundle.putBoolean(CHECK_STATE_COMPLETED_KEY, this.mCheckStateCompleted);

        bundle.putBoolean(ENABLE_ALL_STATES_COMPLETED_KEY, this.mEnableAllStatesCompleted);

        bundle.putBoolean(JUSTIFY_MULTILINE_DESC_KEY, this.mJustifyMultilineDescription);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            mEndCenterX = bundle.getFloat(END_CENTER_X_KEY);

            mStartCenterX = bundle.getFloat(START_CENTER_X_KEY);

            mAnimStartXPos = bundle.getFloat(ANIM_START_X_POS_KEY);

            mAnimEndXPos = bundle.getFloat(ANIM_END_X_POS_KEY);

            mIsCurrentAnimStarted = bundle.getBoolean(IS_CURRENT_ANIM_STARTED_KEY);

            mAnimateToCurrentProgressState = bundle.getBoolean(ANIMATE_TO_CURRENT_PROGRESS_STATE_KEY);

            mStateNumberTextSize = bundle.getFloat(STATE_NUMBER_TEXT_SIZE_KEY);
            mStateSize = bundle.getFloat(STATE_SIZE_KEY);
            resetStateSizeValues();

            mStateLineThickness = bundle.getFloat(STATE_LINE_THICKNESS_KEY);
            resolveStateLineThickness();

            mStateDescriptionSize = bundle.getFloat(STATE_DESCRIPTION_SIZE_KEY);
            resolveStateDescriptionSize();

            mMaxStateNumber = bundle.getInt(MAX_STATE_NUMBER_KEY);
            mCurrentStateNumber = bundle.getInt(CURRENT_STATE_NUMBER_KEY);
            resolveMaxStateNumber();

            mAnimStartDelay = bundle.getInt(ANIM_START_DELAY_KEY);

            mAnimDuration = bundle.getInt(ANIM_DURATION_KEY);

            mDescTopSpaceDecrementer = bundle.getFloat(DESC_TOP_SPACE_DECREMENTER_KEY);

            mDescTopSpaceIncrementer = bundle.getFloat(DESC_TOP_SPACE_INCREMENTER_KEY);

            mDescriptionLinesSpacing = bundle.getFloat(DESCRIPTION_LINE_SPACING_KEY);

            setDescriptionTopSpaceIncrementer(mDescTopSpaceIncrementer); // call requestLayout

            mBackgroundColor = bundle.getInt(BACKGROUND_COLOR_KEY);

            mForegroundColor = bundle.getInt(FOREGROUND_COLOR_KEY);

            mStateNumberBackgroundColor = bundle.getInt(STATE_NUMBER_BACKGROUND_COLOR_KEY);

            mStateNumberForegroundColor = bundle.getInt(STATE_NUMBER_FOREGROUND_COLOR_KEY);

            mCurrentStateDescriptionColor = bundle.getInt(CURRENT_STATE_DESC_COLOR_KEY);

            mStateDescriptionColor = bundle.getInt(STATE_DESC_COLOR_KEY);

            mJustifyMultilineDescription = bundle.getBoolean(JUSTIFY_MULTILINE_DESC_KEY);

            initializePainters();

            checkStateCompleted(bundle.getBoolean(CHECK_STATE_COMPLETED_KEY)); // call invalidate

            setAllStatesCompleted(bundle.getBoolean(ENABLE_ALL_STATES_COMPLETED_KEY));

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));

            return;
        }
        super.onRestoreInstanceState(state);
    }

}
