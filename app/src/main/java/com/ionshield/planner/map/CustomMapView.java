package com.ionshield.planner.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ionshield.planner.R;
import com.ionshield.planner.database.DBC;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * TODO: document your custom view class.
 */
public class CustomMapView extends View {
 // TODO: use a default from R.colors...

    private List<ClickListener> mListeners = new ArrayList<>();

    private static final PathEffect SOLID_LINE = new PathEffect();
    private static final PathEffect DASHED_LINE = new DashPathEffect(new float[]{15, 10}, 0);

    private float mPositionX = 0;
    private float mPositionY = 0;
    private float mScaleX = 0.5f;
    private float mScaleY = 0.5f;

    private int mNodeColor = Color.YELLOW;
    private int mCurrMarkerColor = Color.RED;
    private int mPathColor = Color.GREEN;
    private int mSearchAreaColor = Color.CYAN;
    private int mSelectedNodeColor = Color.BLACK;
    private boolean mDisplaySearchArea = true;
    private boolean mDisplayMarkers = true;

    private int mNodeRadius = 5;
    private int mSelectExtraRadius = 5;
    private int mArrowLength = 20;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private Paint mPaint;

    //private int mHeight;
    //private int mWidth;
    private SQLiteDatabase mDatabase;
    private Cursor mNodes;
    private Cursor mLinks;
    private Cursor mLocations;

    private List<Integer> mPathNodeIds = new ArrayList<>();
    private List<Integer> mPathLinkIds = new ArrayList<>();
    private List<Integer> mSearchNodeIds = new ArrayList<>();
    private List<Integer> mSearchLinkIds = new ArrayList<>();
    private List<Integer> mSelectedNodeIds = new ArrayList<>();
    private int mCurrentNodeId = -1;

    private Queue<NodeData> mPathNodeQueue = new LinkedList<>();
    private Queue<LinkData> mPathLinkQueue = new LinkedList<>();
    private Queue<NodeData> mSearchNodeQueue = new LinkedList<>();
    private Queue<LinkData> mSearchLinkQueue = new LinkedList<>();

    private Set<Integer> mPathNodeIdsSet = new HashSet<>();
    private Set<Integer> mPathLinkIdsSet = new HashSet<>();
    private Set<Integer> mSearchNodeIdsSet = new HashSet<>();
    private Set<Integer> mSearchLinkIdsSet = new HashSet<>();
    private Set<Integer> mSelectedNodeIdsSet = new HashSet<>();

    public CustomMapView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomMapView, defStyle, 0);

        mPositionX = a.getFloat(R.styleable.CustomMapView_mapPositionX, mPositionX);
        mPositionY = a.getFloat(R.styleable.CustomMapView_mapPositionY, mPositionY);
        mScaleX = a.getFloat(R.styleable.CustomMapView_mapScaleX, mScaleX);
        mScaleY = a.getFloat(R.styleable.CustomMapView_mapScaleY, mScaleY);

        mNodeColor = a.getColor(R.styleable.CustomMapView_nodeColor, mNodeColor);
        mCurrMarkerColor = a.getColor(R.styleable.CustomMapView_currMarkerColor, mCurrMarkerColor);
        mPathColor = a.getColor(R.styleable.CustomMapView_pathColor, mPathColor);
        mSearchAreaColor = a.getColor(R.styleable.CustomMapView_searchAreaColor, mSearchAreaColor);
        mSelectedNodeColor = a.getColor(R.styleable.CustomMapView_selectedNodeColor, mSelectedNodeColor);

        mDisplaySearchArea = a.getBoolean(R.styleable.CustomMapView_displaySearchArea, mDisplaySearchArea);
        mDisplayMarkers = a.getBoolean(R.styleable.CustomMapView_displayMarkers, mDisplayMarkers);

        mNodeRadius = a.getDimensionPixelSize(R.styleable.CustomMapView_nodeRadius, mNodeRadius);
        mSelectExtraRadius = a.getDimensionPixelSize(R.styleable.CustomMapView_selectionExtraRadius, mSelectExtraRadius);
        mArrowLength = a.getDimensionPixelSize(R.styleable.CustomMapView_arrowLength, mArrowLength);

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    double absX = screenToAbsoluteX(motionEvent.getX());
                    double absY = screenToAbsoluteY(motionEvent.getY());
                    int nodeId = -1;

                    double rad = mNodeRadius + mSelectExtraRadius;
                    double absRad = rad * (mScaleX + mScaleY) / 2;
                    Cursor nodes = mDatabase.rawQuery("SELECT * FROM " + DBC.Nodes.TABLE_NAME
                            + " WHERE ("
                            + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " - " + "?)" + " * "
                            + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " - " + "?)" + " + "
                            + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " - " + "?)" + " * "
                            + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " - " + "?)" + ") <= (? * ?)"
                            + " ORDER BY ("
                            + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " - " + "?)" + " * "
                            + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " - " + "?)" + " + "
                            + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " - " + "?)" + " * "
                            + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " - " + "?)" + ") ASC;",
                            new String[]{String.valueOf(absX), String.valueOf(absX),
                                    String.valueOf(absY), String.valueOf(absY),
                                    String.valueOf(absRad), String.valueOf(absRad),
                                    String.valueOf(absX), String.valueOf(absX),
                                    String.valueOf(absY), String.valueOf(absY)});

                    nodes.moveToFirst();
                    if (!nodes.isAfterLast()) {
                        nodeId = nodes.getInt(nodes.getColumnIndexOrThrow(BaseColumns._ID));
                    }
                    nodes.close();

                    for (ClickListener l : mListeners) {
                        l.onClick(absX, absY, nodeId);
                    }
                    //Toast.makeText(getContext(), absX + "; " + absY + "; " + nodeId, Toast.LENGTH_SHORT).show();
                    performClick();
                }
                return true;
            }
        });
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(Color.RED);
        mTextWidth = mTextPaint.measureText("Test");

        //Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        //mTextHeight = fontMetrics.bottom;

        mPaint.setColor(mNodeColor);
    }

    /*@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }*/

    @Override
    public void invalidate() {
        if (mLinks != null) mLinks.close();
        mLinks = null;
        if (mNodes != null) mNodes.close();
        mNodes = null;
        if (mLocations != null) mLocations.close();
        mLocations = null;
        super.invalidate();
    }

    private void query() {
        if (mDatabase != null) {
            String nodeQuery = "SELECT * FROM " + DBC.Nodes.TABLE_NAME + " WHERE (" + DBC.Nodes.COORDINATE_X + " BETWEEN ? AND ?) AND " + BaseColumns._ID +
                    " IN (SELECT " + BaseColumns._ID + " FROM " + DBC.Nodes.TABLE_NAME + " WHERE " + DBC.Nodes.COORDINATE_Y + " BETWEEN ? AND ?)";

            String nodeIdQuery = "SELECT " + BaseColumns._ID + " FROM " + DBC.Nodes.TABLE_NAME + " WHERE (" + DBC.Nodes.COORDINATE_X + " BETWEEN ? AND ?) AND " + BaseColumns._ID +
                    " IN (SELECT " + BaseColumns._ID + " FROM " + DBC.Nodes.TABLE_NAME + " WHERE " + DBC.Nodes.COORDINATE_Y + " BETWEEN ? AND ?)";

            mNodes = mDatabase.rawQuery(nodeQuery + ";",
                    new String[]{String.valueOf(leftAbsolute()), String.valueOf(rightAbsolute()), String.valueOf(bottomAbsolute()), String.valueOf(topAbsolute())});

            mLinks = mDatabase.rawQuery("SELECT " + DBC.Links.TABLE_NAME + ".*, " +
                    DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.NAME +", " +
                    DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.DESC +", " +
                    DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.COLOR +", " +
                    DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.SPEED +", " +
                    DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.WIDTH +", " +
                    DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.ENABLED +", " +
                    "f." + DBC.Nodes.COORDINATE_X + " AS from_x, " +
                    "f." + DBC.Nodes.COORDINATE_Y + " AS from_y, " +
                    "t." + DBC.Nodes.COORDINATE_X + " AS to_x, " +
                    "t." + DBC.Nodes.COORDINATE_Y + " AS to_y " +
                    " FROM " + DBC.Links.TABLE_NAME +
                    " JOIN " + DBC.Nodes.TABLE_NAME + " f ON f." + DBC.Nodes._ID + "=" +
                    DBC.Links.TABLE_NAME + "." + DBC.Links.FROM_NODE_ID +
                    " JOIN " + DBC.Nodes.TABLE_NAME + " t ON t." + DBC.Nodes._ID + "=" +
                    DBC.Links.TABLE_NAME + "." + DBC.Links.TO_NODE_ID +
                    " JOIN " + DBC.LinkTypes.TABLE_NAME + " ON (" +
                    DBC.Links.TABLE_NAME + "." + DBC.Links.LINK_TYPE_ID + "=" +
                    DBC.LinkTypes.TABLE_NAME + "." + BaseColumns._ID + " AND " +
                    DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.ENABLED + "<>" + "0" + ") WHERE " +
                    DBC.Links.FROM_NODE_ID + " IN (" + nodeIdQuery + ") OR " + DBC.Links.TO_NODE_ID + " IN (" + nodeIdQuery + ")" +
                    " ORDER BY " + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.WIDTH + " DESC" +
                    ";",
                    new String[]{String.valueOf(leftAbsolute()), String.valueOf(rightAbsolute()), String.valueOf(bottomAbsolute()), String.valueOf(topAbsolute()),
                            String.valueOf(leftAbsolute()), String.valueOf(rightAbsolute()), String.valueOf(bottomAbsolute()), String.valueOf(topAbsolute())});

            mLocations = mDatabase.rawQuery("SELECT " + DBC.Locations.TABLE_NAME + ".*, " +
                    DBC.Types.TABLE_NAME + "." + DBC.Types.COLOR + " AS " + DBC.Types.COLOR + ", " +
                    DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " AS x, " +
                    DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " AS y " +
                    " FROM " + DBC.Locations.TABLE_NAME +
                    " JOIN " + DBC.Types.TABLE_NAME + " ON " +
                    DBC.Types.TABLE_NAME + "." + DBC.Types._ID + "=" +
                    DBC.Locations.TABLE_NAME + "." + DBC.Locations.TYPE_ID +
                    " JOIN " + DBC.Nodes.TABLE_NAME + " ON " +
                    DBC.Nodes.TABLE_NAME + "." + DBC.Nodes._ID + "=" +
                    DBC.Locations.TABLE_NAME + "." + DBC.Locations.NODE_ID +
                    " WHERE " + DBC.Nodes.TABLE_NAME + "." + BaseColumns._ID +
                    " IN (" + nodeIdQuery + ");",
                    new String[]{String.valueOf(leftAbsolute()), String.valueOf(rightAbsolute()), String.valueOf(bottomAbsolute()), String.valueOf(topAbsolute())});
        }
    }

    public void moveBy(double portsX, double portsY) {
        mPositionX += portsX * widthAbsolute();
        mPositionY += portsY * heightAbsolute();
        invalidate();
    }

    public void zoomIn(double scaleX, double scaleY) {
        mScaleX *= (1 - scaleX);
        mScaleY *= (1 - scaleY);
        invalidate();
    }

    public void zoomOut(double scaleX, double scaleY) {
        mScaleX /= (1 - scaleX);
        mScaleY /= (1 - scaleY);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        if (mNodes == null || mLinks == null) {
            query();
        }
        mPaint.setStrokeCap(Paint.Cap.BUTT);

        mPathNodeQueue.clear();
        mPathLinkQueue.clear();
        mSearchNodeQueue.clear();
        mSearchLinkQueue.clear();

        mPathNodeIdsSet.clear();
        mPathNodeIdsSet.addAll(mPathNodeIds);
        mPathLinkIdsSet.clear();
        mPathLinkIdsSet.addAll(mPathLinkIds);
        mSearchNodeIdsSet.clear();
        mSearchNodeIdsSet.addAll(mSearchNodeIds);
        mSearchLinkIdsSet.clear();
        mSearchLinkIdsSet.addAll(mSearchLinkIds);
        mSelectedNodeIdsSet.clear();
        mSelectedNodeIdsSet.addAll(mSelectedNodeIds);

        //Draw links
        if (mLinks != null) {
            mPaint.setPathEffect(SOLID_LINE);
            for (mLinks.moveToFirst(); !mLinks.isAfterLast(); mLinks.moveToNext()) {

                int id = mLinks.getInt(mLinks.getColumnIndexOrThrow(BaseColumns._ID));
                int fromId = mLinks.getInt(mLinks.getColumnIndexOrThrow(DBC.Links.FROM_NODE_ID));
                int toId = mLinks.getInt(mLinks.getColumnIndexOrThrow(DBC.Links.TO_NODE_ID));
                int typeId = mLinks.getInt(mLinks.getColumnIndexOrThrow(DBC.Links.LINK_TYPE_ID));
                double fromX = mLinks.getDouble(mLinks.getColumnIndexOrThrow("from_x"));
                double fromY = mLinks.getDouble(mLinks.getColumnIndexOrThrow("from_y"));
                double toX = mLinks.getDouble(mLinks.getColumnIndexOrThrow("to_x"));
                double toY = mLinks.getDouble(mLinks.getColumnIndexOrThrow("to_y"));
                int color = mLinks.getInt(mLinks.getColumnIndexOrThrow(DBC.LinkTypes.COLOR));
                int width = mLinks.getInt(mLinks.getColumnIndexOrThrow(DBC.LinkTypes.WIDTH));
                double modifier = mLinks.getDouble(mLinks.getColumnIndexOrThrow(DBC.Links.MODIFIER));
                double speed = mLinks.getDouble(mLinks.getColumnIndexOrThrow(DBC.LinkTypes.SPEED));

                double fromSx = absoluteToScreenX(fromX);
                double fromSy = absoluteToScreenY(fromY);
                double toSx = absoluteToScreenX(toX);
                double toSy = absoluteToScreenY(toY);

                if (mPathLinkIdsSet.contains(id)) {
                    mPathLinkQueue.add(new LinkData(id, fromId, toId, typeId, modifier, fromX, fromY, toX, toY, color, width, speed));
                    continue;
                }
                if (mDisplaySearchArea && mSearchLinkIdsSet.contains(id)) {
                    mSearchLinkQueue.add(new LinkData(id, fromId, toId, typeId, modifier, fromX, fromY, toX, toY, color, width, speed));
                    continue;
                }


                mPaint.setColor(0xFF000000 + color);
                mPaint.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics()));
                canvas.drawLine((float)fromSx, (float)fromSy, (float)toSx, (float)toSy, mPaint);

                mPaint.setStrokeCap(Paint.Cap.ROUND);
                fillArrow(mPaint, canvas, (float)fromSx, (float)fromSy, (float)toSx, (float)toSy, mNodeRadius);
                mPaint.setStrokeCap(Paint.Cap.BUTT);
            }
        }

        //Links in searched area
        while (!mSearchLinkQueue.isEmpty()) {
            LinkData link = mSearchLinkQueue.remove();

            double fromSx = absoluteToScreenX(link.fromX);
            double fromSy = absoluteToScreenY(link.fromY);
            double toSx = absoluteToScreenX(link.toX);
            double toSy = absoluteToScreenY(link.toY);

            mPaint.setPathEffect(SOLID_LINE);
            mPaint.setColor(0xFF000000 + link.color);
            mPaint.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, link.width, getResources().getDisplayMetrics()));
            canvas.drawLine((float)fromSx, (float)fromSy, (float)toSx, (float)toSy, mPaint);

            mPaint.setPathEffect(DASHED_LINE);
            mPaint.setColor(0x8F000000 + mSearchAreaColor);
            mPaint.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, link.width, getResources().getDisplayMetrics()));
            canvas.drawLine((float)fromSx, (float)fromSy, (float)toSx, (float)toSy, mPaint);

            mPaint.setPathEffect(SOLID_LINE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            fillArrow(mPaint, canvas, (float)fromSx, (float)fromSy, (float)toSx, (float)toSy, mNodeRadius);
            mPaint.setStrokeCap(Paint.Cap.BUTT);
        }

        //Links in path
        while (!mPathLinkQueue.isEmpty()) {
            LinkData link = mPathLinkQueue.remove();

            double fromSx = absoluteToScreenX(link.fromX);
            double fromSy = absoluteToScreenY(link.fromY);
            double toSx = absoluteToScreenX(link.toX);
            double toSy = absoluteToScreenY(link.toY);

            mPaint.setPathEffect(SOLID_LINE);
            mPaint.setColor(0xFF000000 + link.color);
            mPaint.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, link.width, getResources().getDisplayMetrics()));
            canvas.drawLine((float)fromSx, (float)fromSy, (float)toSx, (float)toSy, mPaint);

            mPaint.setPathEffect(DASHED_LINE);
            mPaint.setColor(0xAF000000 + mPathColor);
            mPaint.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, link.width + 1, getResources().getDisplayMetrics()));
            canvas.drawLine((float)fromSx, (float)fromSy, (float)toSx, (float)toSy, mPaint);

            mPaint.setPathEffect(SOLID_LINE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            fillArrow(mPaint, canvas, (float)fromSx, (float)fromSy, (float)toSx, (float)toSy, mNodeRadius);
            mPaint.setStrokeCap(Paint.Cap.BUTT);

        }

        //Draw nodes
        if (mNodes != null) {
            mPaint.setColor(mNodeColor);
            synchronized (mNodes) {
                for (mNodes.moveToFirst(); !mNodes.isAfterLast(); mNodes.moveToNext()) {
                    int id = mNodes.getInt(mNodes.getColumnIndexOrThrow(BaseColumns._ID));
                    double x = mNodes.getDouble(mNodes.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_X));
                    double y = mNodes.getDouble(mNodes.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_Y));
                    double sx = absoluteToScreenX(x);
                    double sy = absoluteToScreenY(y);

                    if (mSelectedNodeIdsSet.contains(id)) {
                        mPaint.setColor(mSelectedNodeColor);
                        canvas.drawCircle((float) sx, (float) sy, mNodeRadius + 5, mPaint);
                    }

                    if (mPathNodeIdsSet.contains(id)) {
                        mPaint.setColor(mPathColor);
                    } else if (mDisplaySearchArea && mSearchNodeIdsSet.contains(id)) {
                        mPaint.setColor(mSearchAreaColor);
                    } else {
                        mPaint.setColor(mNodeColor);
                    }
                    canvas.drawCircle((float) sx, (float) sy, mNodeRadius, mPaint);

                    if (mDisplayMarkers && id == mCurrentNodeId) {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_my_location_white_48dp);
                        drawable.setTint(0xFF000000 + mCurrMarkerColor);
                        drawable.setBounds((int) Math.round(sx - mNodeRadius * 2.5), (int) Math.round(sy - mNodeRadius * 2.5), (int) Math.round(sx + mNodeRadius * 2.5), (int) Math.round(sy + mNodeRadius * 2.5));
                        drawable.draw(canvas);
                    }
                }
            }
        }

        //Draw locations
        if (mDisplayMarkers && mLocations != null) {
            for (mLocations.moveToFirst(); !mLocations.isAfterLast(); mLocations.moveToNext()) {
                int id = mLocations.getInt(mLocations.getColumnIndexOrThrow(BaseColumns._ID));
                int color = mLocations.getInt(mLocations.getColumnIndexOrThrow(DBC.Types.COLOR));
                double x = mLocations.getDouble(mLocations.getColumnIndexOrThrow("x"));
                double y = mLocations.getDouble(mLocations.getColumnIndexOrThrow("y"));
                double sx = absoluteToScreenX(x);
                double sy = absoluteToScreenY(y);

                Drawable drawable = getResources().getDrawable(R.drawable.ic_place_white_48dp);
                drawable.setTint(0xFF000000 + color);
                drawable.setBounds((int) Math.round(sx - mNodeRadius * 2.5), (int) Math.round(sy - mNodeRadius * 5), (int) Math.round(sx + mNodeRadius * 2.5), (int) Math.round(sy));
                drawable.draw(canvas);
            }
        }


        // Draw the text.
        /*canvas.drawText("Test",
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);*/

        // Draw the example drawable on top of the text.
        /*if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }*/
    }

    private void fillArrow(Paint paint, Canvas canvas, float x0, float y0, float x1, float y1, float offset) {
        //paint.setStyle(Paint.Style.STROKE);

        int arrowHeadLength = mArrowLength;
        int arrowHeadAngle = 45;
        float[] linePts = new float[] {x1 - arrowHeadLength, y1, x1, y1};
        float[] linePts2 = new float[] {x1, y1, x1, y1 + arrowHeadLength};
        Matrix rotateMat = new Matrix();

        //get the center of the line
        float centerX = x1;
        float centerY = y1;

        //set the angle
        double a = Math.atan2(y1 - y0, x1 - x0);
        double angle = a * 180 / Math.PI + arrowHeadAngle;

        float offsetX = (float)(Math.cos(a) * offset);
        float offsetY = (float)(Math.sin(a) * offset);

        //rotate the matrix around the center
        rotateMat.setRotate((float) angle, centerX, centerY);
        rotateMat.mapPoints(linePts);
        rotateMat.mapPoints(linePts2);

        canvas.drawLine(linePts[0] - offsetX, linePts[1] - offsetY, linePts[2] - offsetX, linePts[3] - offsetY, paint);
        canvas.drawLine(linePts2[0] - offsetX, linePts2[1] - offsetY, linePts2[2] - offsetX, linePts2[3] - offsetY, paint);
    }

    private int contentHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    private int contentWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private double widthAbsolute() {
        return contentWidth() * mScaleX;
    }

    private  double heightAbsolute() {
        return contentHeight() * mScaleY;
    }

    private double leftAbsolute() {
        return mPositionX - contentWidth() * 0.5 * mScaleX;
    }

    private double rightAbsolute() {
        return mPositionX + contentWidth() * 0.5 * mScaleX;
    }

    private double topAbsolute() {
        return mPositionY + contentHeight() * 0.5 * mScaleY;
    }

    private double bottomAbsolute() {
        return mPositionY - contentHeight() * 0.5 * mScaleY;
    }

    private double screenToAbsoluteX(double screen) {
        double ah = screen / (double)contentWidth();
        return leftAbsolute() * (1 - ah) + rightAbsolute() * ah;
    }

    private double screenToAbsoluteY(double screen) {
        double av = 1 - (screen / (double)contentHeight());
        return bottomAbsolute() * (1 - av) + topAbsolute() * av;
    }

    private Pair<Double, Double> screenToAbsolute(Pair<Double, Double> screen) {
        double ah = screen.first / (double)contentWidth();
        double av = 1 - (screen.second / (double)contentHeight());
        return new Pair<>(leftAbsolute() * (1 - ah) + rightAbsolute() * ah, bottomAbsolute() * (1 - av) + topAbsolute() * av);
    }

    private double absoluteToScreenX(double absolute) {
        double ah = (absolute - leftAbsolute()) / (rightAbsolute() - leftAbsolute());
        return ah * contentWidth();
    }

    private double absoluteToScreenY(double absolute) {
        double av = 1 - ((absolute - bottomAbsolute()) / (topAbsolute() - bottomAbsolute()));
        return av * contentHeight();
    }

    private Pair<Double, Double> absoluteToScreen(Pair<Double, Double> absolute) {
        double ah = (absolute.first - leftAbsolute()) / (rightAbsolute() - leftAbsolute());
        double av = 1 - ((absolute.second - bottomAbsolute()) / (topAbsolute() - bottomAbsolute()));
        return new Pair<>((ah * contentWidth()), (av * contentHeight()));
    }


    public float getPositionX() {
        return mPositionX;
    }

    public void setPositionX(float positionX) {
        this.mPositionX = positionX;
    }

    public float getPositionY() {
        return mPositionY;
    }

    public void setPositionY(float positionY) {
        this.mPositionY = positionY;
    }

    public float getMapScaleX() {
        return mScaleX;
    }

    public void setMapScaleX(float scaleX) {
        this.mScaleX = scaleX;
    }

    public float getMapScaleY() {
        return mScaleY;
    }

    public void setMapScaleY(float scaleY) {
        this.mScaleY = scaleY;
    }

    public int getNodeColor() {
        return mNodeColor;
    }

    public void setNodeColor(int nodeColor) {
        this.mNodeColor = nodeColor;
    }

    public int getCurrMarkerColor() {
        return mCurrMarkerColor;
    }

    public void setCurrMarkerColor(int currMarkerColor) {
        this.mCurrMarkerColor = currMarkerColor;
    }

    public int getPathColor() {
        return mPathColor;
    }

    public void setPathColor(int pathColor) {
        this.mPathColor = pathColor;
    }

    public int getSearchAreaColor() {
        return mSearchAreaColor;
    }

    public void setSearchAreaColor(int searchAreaColor) {
        this.mSearchAreaColor = searchAreaColor;
    }

    public boolean isDisplaySearchArea() {
        return mDisplaySearchArea;
    }

    public void setDisplaySearchArea(boolean displaySearchArea) {
        this.mDisplaySearchArea = displaySearchArea;
    }

    public boolean isDisplayMarkers() {
        return mDisplayMarkers;
    }

    public void setDisplayMarkers(boolean displayMarkers) {
        if (this.mDisplayMarkers == displayMarkers) return;
        this.mDisplayMarkers = displayMarkers;
        invalidate();
    }

    public Cursor getNodes() {
        return mNodes;
    }

    public void setNodes(Cursor nodes) {
        this.mNodes = nodes;
    }

    public int getNodeRadius() {
        return mNodeRadius;
    }

    public void setNodeRadius(int nodeRadius) {
        this.mNodeRadius = nodeRadius;
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    public int getSelectedNodeColor() {
        return mSelectedNodeColor;
    }

    public void setSelectedNodeColor(int selectedNodeColor) {
        this.mSelectedNodeColor = selectedNodeColor;
    }

    public int getSelectExtraRadius() {
        return mSelectExtraRadius;
    }

    public void setSelectExtraRadius(int selectExtraRadius) {
        this.mSelectExtraRadius = selectExtraRadius;
    }

    public List<Integer> getPathNodeIds() {
        return mPathNodeIds;
    }

    public void setPathNodeIds(List<Integer> pathNodeIds) {
        this.mPathNodeIds = pathNodeIds;
    }

    public List<Integer> getPathLinkIds() {
        return mPathLinkIds;
    }

    public void setPathLinkIds(List<Integer> pathLinkIds) {
        this.mPathLinkIds = pathLinkIds;
    }

    public List<Integer> getSearchNodeIds() {
        return mSearchNodeIds;
    }

    public void setSearchNodeIds(List<Integer> searchNodeIds) {
        this.mSearchNodeIds = searchNodeIds;
    }

    public List<Integer> getSearchLinkIds() {
        return mSearchLinkIds;
    }

    public void setSearchLinkIds(List<Integer> searchLinkIds) {
        this.mSearchLinkIds = searchLinkIds;
    }

    public List<Integer> getSelectedNodeIds() {
        return mSelectedNodeIds;
    }

    public void setSelectedNodeId(int id) {
        if (mSelectedNodeIds == null) mSelectedNodeIds = new ArrayList<>();
        mSelectedNodeIds.clear();
        mSelectedNodeIds.add(id);
        invalidate();
    }

    public void setSelectedNodeIds(List<Integer> selectedNodeIds) {
        this.mSelectedNodeIds = selectedNodeIds;
    }

    public int getCurrentNodeId() {
        return mCurrentNodeId;
    }

    public void setCurrentNodeId(int currentNodeId) {
        if (this.mCurrentNodeId == currentNodeId) return;
        this.mCurrentNodeId = currentNodeId;
        invalidate();
    }

    public int getArrowLength() {
        return mArrowLength;
    }

    public void setArrowLength(int arrowLength) {
        this.mArrowLength = mArrowLength;
    }

    public interface ClickListener {
        void onClick(double absX, double absY, int nodeId);
    }

    public void addClickListener(ClickListener l) {
        mListeners.add(l);
    }
}
