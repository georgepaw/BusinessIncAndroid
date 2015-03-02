package company.businessinc.bathtouch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import company.businessinc.bathtouch.adapters.LeagueTableAdapter;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.LeagueTeam;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link company.businessinc.bathtouch.LeagueTableFragment.LeagueTableCallbacks} interface
 * to handle interaction events.
 * Use the {@link LeagueTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeagueFragment extends Fragment {

    private static final String TAG = "LeagueTableFragment";
    public static final String ARG_OBJECT = "object";

    private LeagueCallbacks mCallbacks;
    private View mLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<LeagueTeam> mLeagueTeams;
    private Integer mLeagueID;

    public static LeagueFragment newInstance(int leagueID) {
        LeagueFragment fragment = new LeagueFragment();
        Bundle args = new Bundle();
        args.putInt("leagueID", leagueID);
        fragment.setArguments(args);
        return fragment;
    }

    public LeagueFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //get the data from the league activity on what league to display
        mLeagueID = 0;
        if (getArguments() != null) {
            mLeagueID = getArguments().getInt("leagueID");
        }


        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_league, container, false);

        //set up the header for the league colors
        setupLeagueHeader();

        //set up the recycler view
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.fragment_league_recycle);

//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        int userTeamId = DataStore.getInstance(getActivity().getApplicationContext()).getUserTeamID();


//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
//                .getBaseContext(),
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        selectItem(position);
//                    }
//                }));

        //Adapter loads the data fror the leagues
        mAdapter = new LeagueTableAdapter(getActivity(), mLeagueID, userTeamId);
        mRecyclerView.setAdapter(mAdapter);
        return mLayout;
    }

    public void selectItem(int position) {
        if (mCallbacks != null) {
            mCallbacks.onLeagueItemSelected(position);
        }
//        Intent intent = new Intent(getActivity(), MatchActivity.class);
//        Bundle args = new Bundle();
//        args.putString("teamOneName", mLeagueTeams.get(position).);

    }

    public void setupLeagueHeader(){
        int userColor = DataStore.getInstance(getActivity().getBaseContext()).getUserTeamColorPrimary();
        TextView t = (TextView) mLayout.findViewById(R.id.fragment_league_header_team_number);
        t.setTextColor(userColor);
        t = (TextView)mLayout.findViewById(R.id.fragment_league_header_team_name);
        t.setTextColor(userColor);
        t = (TextView)mLayout.findViewById(R.id.fragment_league_header_team_draw);
        t.setTextColor(userColor);
        t = (TextView)mLayout.findViewById(R.id.fragment_league_header_team_lose);
        t.setTextColor(userColor);
        t = (TextView)mLayout.findViewById(R.id.fragment_league_header_team_points);
        t.setTextColor(userColor);
        t = (TextView)mLayout.findViewById(R.id.fragment_league_header_team_won);
        t.setTextColor(userColor);
        View v = mLayout.findViewById(R.id.fragment_league_header_item_divider);
        v.setBackgroundColor(userColor);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (LeagueCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface LeagueCallbacks {

        public void onLeagueItemSelected(int position);
    }
}
