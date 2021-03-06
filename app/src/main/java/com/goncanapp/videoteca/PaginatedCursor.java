package com.goncanapp.videoteca;

import android.database.AbstractCursor;
import android.database.Cursor;

/**
 * Created by Alberto on 02/07/2017.
 */

public class PaginatedCursor extends AbstractCursor {

    private static final int PAGE_SIZE = 10;
    private static final int PAGE_THRESHOLD = PAGE_SIZE / 2;
    private final Cursor mCursor;
    private final int mRowCount;
    private final boolean[] mCachedRows;
    private final String[] mColumnNames;
    private final int mColumnCount;
    private final int[] mColumnTypes;
    private final byte[][][] mByteArrayDataCache;
    private final float[][] mFloatDataCache;
    private final int[][] mIntDataCache;
    private final String[][] mStringDataCache;
    private final int[] mByteArrayCacheIndexMap;
    private final int[] mFloatCacheIndexMap;
    private final int[] mIntCacheIndexMap;
    private final int[] mStringCacheIndexMap;
    private int mByteArrayCacheColumnSize;
    private int mFloatCacheColumnSize;
    private int mIntCacheColumnSize;
    private int mStringCacheColumnSize;
    private int mLastCachePosition;

    public PaginatedCursor(Cursor cursor) {
        super();
        mCursor = cursor;
        mRowCount = mCursor.getCount();
        mCachedRows = new boolean[mRowCount];
        mColumnNames = mCursor.getColumnNames();
        mColumnCount = mCursor.getColumnCount();
        mColumnTypes = new int[mColumnCount];
        mByteArrayCacheColumnSize = 0;
        mFloatCacheColumnSize = 0;
        mIntCacheColumnSize = 0;
        mStringCacheColumnSize = 0;
        mByteArrayCacheIndexMap = new int[mColumnCount];
        mFloatCacheIndexMap = new int[mColumnCount];
        mIntCacheIndexMap = new int[mColumnCount];
        mStringCacheIndexMap = new int[mColumnCount];
        mCursor.moveToFirst();
        for (int i = 0; i < mColumnCount; i++) {
            int type = mCursor.getType(i);
            mColumnTypes[i] = type;
            switch (type) {
                case Cursor.FIELD_TYPE_BLOB:
                    mByteArrayCacheIndexMap[i] = mByteArrayCacheColumnSize++;
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    mFloatCacheIndexMap[i] = mFloatCacheColumnSize++;
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    mIntCacheIndexMap[i] = mIntCacheColumnSize++;
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    mStringCacheIndexMap[i] = mStringCacheColumnSize++;
                    break;
            }
        }
        mByteArrayDataCache = mByteArrayCacheColumnSize >
                0 ? new byte[mRowCount][][] : null;
        mFloatDataCache = mFloatCacheColumnSize>0 ?
                new float[mRowCount][] : null;
        mIntDataCache = mIntCacheColumnSize>0 ? new int[mRowCount][] : null;
        mStringDataCache = mStringCacheColumnSize > 0 ?
                new String[mRowCount][] : null;
        for (int i = 0; i < mRowCount; i++) {
            mCachedRows[i] = false;
            if (mByteArrayDataCache != null) {
                mByteArrayDataCache[i] =
                        new byte[mByteArrayCacheColumnSize][];
            }
            if (mFloatDataCache != null) {
                mFloatDataCache[i] = new float[mFloatCacheColumnSize];
            }
            if (mIntDataCache != null) {
                mIntDataCache[i] = new int[mIntCacheColumnSize];
            }
            if (mStringDataCache != null) {
                mStringDataCache[i] = new String[mStringCacheColumnSize];
            }
        }
        loadCacheStartingFromPosition(0);
    }

    private void loadCacheStartingFromPosition(int index) {
        mCursor.moveToPosition(index);
        for (int row = index; row < (index + PAGE_SIZE) && row < mRowCount;
             row++) {
            if (!mCachedRows[row]) {
                for (int col = 0; col < mColumnCount; col++) {
                    switch (mCursor.getType(col)) {
                        case Cursor.FIELD_TYPE_BLOB:
                            mByteArrayDataCache[row][mByteArrayCacheIndexMap[col]]
                                    = mCursor.getBlob(col);
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            mFloatDataCache[row][mFloatCacheIndexMap[col]]
                                    = mCursor.getFloat(col);
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            mIntDataCache[row][mIntCacheIndexMap[col]]
                                    = mCursor.getInt(col);
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            mStringDataCache[row][mStringCacheIndexMap[col]]
                                    = mCursor.getString(col);
                            break;
                    }
                }
                mCachedRows[row] = true;
            }
            mCursor.moveToNext();
        }
        mLastCachePosition = Math.min(index + PAGE_SIZE, mRowCount) - 1;
    }

    @Override
    public boolean onMove(int oldPosition, int newPosition) {
        if ((newPosition - oldPosition) != 1 ||
                (newPosition + PAGE_THRESHOLD) <= mLastCachePosition) {
            loadCacheStartingFromPosition(newPosition);
        }
        return true;
    }

    @Override
    public int getType(int column) {
        return mColumnTypes[column];
    }

    @Override
    public int getCount() {
        return mRowCount;
    }

    @Override
    public String[] getColumnNames() {
        return mColumnNames;
    }

    @Override
    public String getString(int column) {
        return mStringDataCache[mPos][mStringCacheIndexMap[column]];
    }

    @Override
    public short getShort(int column) {
        return (short) mIntDataCache[mPos][mIntCacheIndexMap[column]];
    }

    @Override
    public int getInt(int column) {
        return mIntDataCache[mPos][mIntCacheIndexMap[column]];
    }

    @Override
    public long getLong(int column) {
        return mIntDataCache[mPos][mIntCacheIndexMap[column]];
    }

    @Override
    public float getFloat(int column) {
        return mFloatDataCache[mPos][mFloatCacheIndexMap[column]];
    }

    @Override
    public double getDouble(int column) {
        return mFloatDataCache[mPos][mFloatCacheIndexMap[column]];
    }

    @Override
    public byte[] getBlob(int column) {
        return mByteArrayDataCache[mPos][mByteArrayCacheIndexMap[column]];
    }

    @Override
    public boolean isNull(int column) {
        return mColumnTypes[column] == Cursor.FIELD_TYPE_NULL;
    }
}