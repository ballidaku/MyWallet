package ballidaku.mywallet.commonClasses;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sharanpalsingh on 01/04/17.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration
{

    String TAG= GridSpacingItemDecoration.class.getSimpleName();

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge)
    {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        int position = parent.getChildAdapterPosition(view); // item position

        int column = position % spanCount; // item column

        if (includeEdge)
        {
//            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)


            outRect.right = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount)
            { // top edge
               // outRect.top = spacing;
            }

            if (position == 0)
            {
                 outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom

//            Log.e(TAG,"Position "+position+" Dimen L : "+outRect.left +" R : "+outRect.right+" T : "+outRect.top+" B : "+outRect.bottom);
        }
        else
        {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount)
            {
                outRect.top = spacing; // item top
            }
        }


    }
}



