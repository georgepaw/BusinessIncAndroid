package company.businessinc.bathtouch;


import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import company.businessinc.bathtouch.adapters.AvailablePlayersAdapter;
import company.businessinc.bathtouch.data.DataStore;

/**
 * Created by user on 30/01/15.
 */
public class AvailablePlayersFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private View mLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private AvailablePlayersAdapter mAdapter;
    private int matchID;
    private boolean hasBeenPlayed;


    public static AvailablePlayersFragment newInstance(int matchID, boolean hasBeenPlayed) {
        AvailablePlayersFragment fragment = new AvailablePlayersFragment();
        Bundle args = new Bundle();

        args.putInt("matchID", matchID);
        args.putBoolean("hasBeenPlayed", hasBeenPlayed);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            matchID = bundle.getInt("matchID");
            hasBeenPlayed = bundle.getBoolean("hasBeenPlayed");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_team_roster, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_roster_recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        //Adapter loads the data fror the leagues
        mAdapter = new AvailablePlayersAdapter(getActivity(), matchID, hasBeenPlayed);
        mRecyclerView.setAdapter(mAdapter);


        //add stikcy header decoration
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
//        mRecyclerView.addItemDecoration(headersDecor);
//        mRecyclerView.addItemDecoration(new DividerDecoration(getActivity().getBaseContext()));


        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) mLayout.findViewById(R.id.swipeRefreshAvailabilites);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DataStore.getInstance(getActivity()).refreshMatchAvailabilities();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                },3000);
            }
        });


        return mLayout;
    }


    public class DividerDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        private Drawable mDivider;

        public DividerDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
        }

        private int getOrientation(RecyclerView parent) {
            LinearLayoutManager layoutManager;
            try {
                layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            } catch (ClassCastException e) {
                throw new IllegalStateException("DividerDecoration can only be used with a " +
                        "LinearLayoutManager.", e);
            }
            return layoutManager.getOrientation();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            if (getOrientation(parent) == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (getOrientation(parent) == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }
}
