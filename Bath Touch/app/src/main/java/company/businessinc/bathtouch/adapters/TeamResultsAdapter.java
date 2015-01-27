package company.businessinc.bathtouch.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.ApdaterData.TeamResultsData;
import company.businessinc.dataModels.Match;

/**
 * Created by user on 21/11/14.
 */
public class TeamResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TeamResultsData mDataset;

    public class ViewHolderResults extends RecyclerView.ViewHolder {
        public TextView mTeam1Name, mTeam2Name, mTeam1Score, mTeam2Score;
        public ImageView mImageView;

        public ViewHolderResults(View v){
            super(v);
            mTeam1Name = (TextView) v.findViewById(R.id.match_result_item_match_team1_name);
            mTeam2Name = (TextView) v.findViewById(R.id.match_result_item_match_team2_name);
            mTeam1Score = (TextView) v.findViewById(R.id.match_result_item_match_team1_score);
            mTeam2Score = (TextView) v.findViewById(R.id.match_result_item_match_team2_score);
            mImageView = (ImageView) v.findViewById(R.id.match_result_item_result_image);
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
        Match match = mDataset.getMatch(position);
        ViewHolderResults v = (ViewHolderResults) holder;
        v.mTeam1Name.setText(match.getTeamOne());
        v.mTeam2Name.setText(match.getTeamTwo());
        v.mTeam1Score.setText(match.getTeamOnePoints().toString());
        v.mTeam2Score.setText(match.getTeamTwoPoints().toString());

        if(match.getTeamOne().equals(mDataset.getTeamName())){
            v.mTeam1Name.setTypeface(null, Typeface.BOLD);
            v.mTeam1Score.setTypeface(null, Typeface.BOLD);
            if(match.getTeamOnePoints() > match.getTeamTwoPoints()){
                //won, set green
//                v.mImageView.setBackgroundColor(Color.parseColor("#FF99CC00"));
                v.mImageView.setImageResource(R.drawable.ic_thumb_up);
            }
            else{
                v.mImageView.setImageResource(R.drawable.ic_thumb_down);
            }
        }
        else{
            v.mTeam2Name.setTypeface(null, Typeface.BOLD);
            v.mTeam2Score.setTypeface(null, Typeface.BOLD);
            if(match.getTeamOnePoints() > match.getTeamTwoPoints()){
                //won, set green
                v.mImageView.setImageResource(R.drawable.ic_thumb_up);
            }
            else{
                v.mImageView.setImageResource(R.drawable.ic_thumb_down);
            }
        }

        if(match.getTeamOnePoints() == match.getTeamTwoPoints()){
            v.mImageView.setBackgroundColor(Color.parseColor("#FFFFBB33"));
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

