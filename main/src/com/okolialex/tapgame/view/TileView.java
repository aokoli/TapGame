package com.okolialex.tapgame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.okolialex.tapgame.android.R;

/**
 * This class is a View-variant designed for manipulating "tiles/icons" or other 
 * drawables, and displaying them on the UI.
 * Sub-classers of this class (TapGameView) can model the tile presentation 
 * however they choose. The function of this class will simply be to render
 * the tile presentation. 
 * 
 */
public class TileView extends View {

    /**
     * Parameters controlling the size of the tiles and their range within view. Width/Height are in
     * pixels, and Drawables will be scaled to fit to these dimensions. X/Y Tile Counts are the
     * number of tiles that will be drawn.
     */
    protected static int mTileSize;

    protected static int mXTileCount;
    protected static int mYTileCount;

    protected static int mXOffset;  
    protected static int mYOffset; 

    private final Paint mPaint = new Paint();

    /**
     * A hash that maps integer handles specified by the subclasser to the drawable that will be
     * used for that reference
     */
    private Bitmap[] mTileArray;

    /**
     * A two-dimensional array of integers in which the number represents the index of the tile that
     * should be drawn at that locations
     */
    protected int[][] mTileGrid;  

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);
        mTileSize = a.getDimensionPixelSize(R.styleable.TileView_tileSize, 50);

        a.recycle();
    }

    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);
        mTileSize = a.getDimensionPixelSize(R.styleable.TileView_tileSize, 50);

        a.recycle();

    }

    /**
     * Resets all tiles to 0 (empty)
     * 
     */
    public void clearTiles() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                setTile(0, x, y);
            }
        }
    }

    /**
     * Function to set the specified Drawable as the tile for a particular integer key.
     *
     * @param key
     * @param tile
     */
    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSize, mTileSize);
        tile.draw(canvas);

        mTileArray[key] = bitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < mXTileCount; x += 1) {
            for (int y = 0; y < mYTileCount; y += 1) {
                if (mTileGrid[x][y] > 0) {
                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]], mXOffset + x * mTileSize, 
                            mYOffset + y * mTileSize, mPaint);
                }
            }
        }

    }

    /**
     * Rests the internal array of Bitmaps used for drawing tiles, and sets the maximum index of
     * tiles to be inserted
     *
     * @param tilecount
     */

    public void resetTiles(int tilecount) {
        mTileArray = new Bitmap[tilecount];
    }

    /**
     * Used to indicate that a particular tile (set with loadTile and referenced by an integer)
     * should be drawn at the given x/y coordinates during the next invalidate/draw cycle.
     * 
     * @param tileindex
     * @param x
     * @param y
     */
    public void setTile(int tileindex, int x, int y) {
        mTileGrid[x][y] = tileindex;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXTileCount = (int) Math.floor(w / mTileSize);  
        mYTileCount = (int) Math.floor(h / mTileSize);

        mXOffset = ((w - (mTileSize * mXTileCount)) / 2);  
        mYOffset = ((h - (mTileSize * mYTileCount)) / 2);

        mTileGrid = new int[mXTileCount][mYTileCount];
        setTouchedRanges(); 
        clearTiles();
    }
    
    
    /*
     * Establish the touch ranges for all click-able cells
     */
    private int[] mXTouchRanges;
    private int[] mYTouchRanges;
    
    private void setTouchedRanges() {
    	
    	mXTouchRanges = new int[mXTileCount];
    	mYTouchRanges = new int[mYTileCount];
    	
        for (int i = 0; i < mXTouchRanges.length; i++) {
        	mXTouchRanges[i] = i * mTileSize;
		}
        
        for (int i = 0; i < mYTouchRanges.length; i++) {
        	mYTouchRanges[i] = i * mTileSize;
		}
    }
    
    
    /*
     * Set touch cell
     */
    protected int touchedCellX = -1;
    protected int touchedCellY = -1;				        /* In pixels*/
    public void setTouchedCellCoords(int touchedPixelX, int touchedPixelY){
    	setTouchedRanges(); // TODO Remove. 
    	    	
    	int cellX = -1;
        int cellY = -1;	
        
    	for (int column = 0; column < mXTouchRanges.length - 1; column ++) {
			if (mXTouchRanges[column] <= touchedPixelX && mXTouchRanges[column + 1] >= touchedPixelX){
				cellX = column;
				break;
			}
			
			if (cellX == -1){
				cellX = mXTouchRanges.length - 1;
			}
		}
    	
    	
    	for (int row = 0; row < mYTouchRanges.length - 1; row ++) {
			if (mYTouchRanges[row] <= touchedPixelY && mYTouchRanges[row + 1] >= touchedPixelY){
				cellY = row;
				break;
			}
		}
    	
    	if (cellY == -1){
			cellY = mYTouchRanges.length - 1;
		}
    	
    //	System.out.println("Coords of TOUCHED_CELL: " + cellX + "," + cellY);  // TODO Debugging.
    	
    	this.touchedCellX = cellX;
    	this.touchedCellY = cellY;
    	
    }
    				// TODO Change "Cell" to "Tile" in this class.
    public void setTouchedCellX(int touchedCellX){ // This setter is mainly for debugging.
    	this.touchedCellX = touchedCellX;
    }
    
    public void setTouchedCellY(int touchedCellY) { 
		this.touchedCellY = touchedCellY;
	}
    
    public int getTouchedCellX() {
		return touchedCellX;
	}
    
    public int getTouchedCellY() {
		return touchedCellY;
	}
    
    public int getXTileCount() {
    	return mXTileCount;
    }
    
    public int getYTileCount() {
    	return mYTileCount;
    }

}
