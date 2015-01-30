package company.businessinc.bathtouch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 30/01/15.
 */
public class AvailablePlayersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private View mLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    public static AvailablePlayersFragment newInstance() {
        AvailablePlayersFragment fragment = new AvailablePlayersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_team_roster, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_roster_recycle );

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);



        //Adapter loads the data fror the leagues
        mAdapter = new AvailablePlayersAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return mLayout;
    }

    /*
    Adapter to dipslay the available players in the recycler view
     */
    public class AvailablePlayersAdapter extends RecyclerView.Adapter {

        public class ViewHolderPlayer extends RecyclerView.ViewHolder{

            public ViewHolderPlayer(View itemView) {
                super(itemView);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.team_roster_available_player_item, parent, false);
            return new ViewHolderPlayer(v);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }


}
