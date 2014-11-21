package company.businessinc.bathtouch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import company.businessinc.bathtouch.HomeCardData;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.TeamResultsData;

/**
 * Created by user on 21/11/14.
 */
public class TeamResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private TeamResultsData mDataset;

    public class ViewHolderResults extends RecyclerView.ViewHolder {
        public TextView mTeamName, mOppTeamName;

        public ViewHolderResults(View v){
            super(v);
            mTeamName = (TextView) v.findViewById(R.id.match_result_item_match_team1_name);
        }



    }

    public TeamResultsAdapter(TeamResultsData myDataset) {
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
                .inflate(R.layout.match_result_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolderResults(v);


    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        ViewHolderResults v = (ViewHolderResults) holder;
        v.mTeamName.setText(mDataset.getTeamName());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

