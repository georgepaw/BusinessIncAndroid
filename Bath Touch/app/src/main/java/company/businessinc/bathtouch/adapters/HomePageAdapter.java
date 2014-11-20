package company.businessinc.bathtouch.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import company.businessinc.bathtouch.HomeCardData;
import company.businessinc.bathtouch.R;

/**
 * Created by user on 20/11/14.
 */
public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HomeCardData mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public class ViewHolderHome extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public ViewHolderHome(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.home_page_card_home);
            mTextView = (TextView) v.findViewById(R.id.home_page_card_home_header);
        }
    }

    public class ViewHolderTable extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mHeaderTextView;
        public TextView mSubHeaderTextView;
        public TextView mTeam1name;
        public TextView mTeam2name;
        public TextView mTeam3name;
        public ViewHolderTable(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.home_page_card_table);
            mHeaderTextView = (TextView) v.findViewById(R.id.home_page_card_table_header);
            mSubHeaderTextView = (TextView) v.findViewById(R.id.home_page_card_table_subHeader);
            mTeam1name = (TextView) v.findViewById(R.id.team_name1);
            mTeam2name = (TextView) v.findViewById(R.id.team_name2);
            mTeam3name = (TextView) v.findViewById(R.id.team_name3);

        }



    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomePageAdapter(HomeCardData myDataset) {
        mDataset = myDataset;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_cards_content, parent, false);
                // set the view's size, margins, paddings and layout parameters

        View vt = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_table, parent, false);


        switch (viewType){
            case 0:
                return new ViewHolderHome(v);
            case 1:
                return new ViewHolderTable(vt);
            default:
                return new ViewHolderHome(v);
        }


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(holder instanceof ViewHolderHome){
            ViewHolderHome vh = (ViewHolderHome) holder;
            vh.mTextView.setText("HOME PAGE MUTHAFUCKA");
        }
        else if (holder instanceof ViewHolderTable) {
            ViewHolderTable vht = (ViewHolderTable) holder;
            Log.d("ERR", "in viewholder table");


            vht.mHeaderTextView.setText("Bath Summer League 2015");
            vht.mSubHeaderTextView.setText("Standings of Top 3");
            vht.mTeam1name.setText(mDataset.teams.get(0).getTeamName());
            vht.mTeam2name.setText(mDataset.teams.get(1).getTeamName());
            vht.mTeam3name.setText(mDataset.teams.get(2).getTeamName());


        }
        else{
            ViewHolderHome vho = (ViewHolderHome) holder;
            vho.mTextView.setText("HOME PAGE MUTHAFUCKA");
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
