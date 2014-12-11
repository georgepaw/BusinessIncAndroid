package company.businessinc.bathtouch.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import company.businessinc.bathtouch.R;
import company.businessinc.dataModels.League;
import company.businessinc.endpoints.LeagueList;
import company.businessinc.endpoints.LeagueListInterface;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements LeagueListInterface {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context) {


        prepareListData();
        this._context = context;
        this._listDataHeader = new ArrayList<String>();
        this._listDataChild = new HashMap<String, List<String>>();

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navigation_drawer_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navigation_drawer_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }


    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    /*
     * Preparing the list data
     * Executes api call before firing the call back
     */
    private void prepareListData() {

        new LeagueList(this).execute();

    }

    @Override
    public void leagueListCallback(List<League> data) {

//        listDataHeader = new ArrayList<String>();
//        listDataChild = new HashMap<String, List<String>>();



        // Adding child data
        _listDataHeader.add("Home");
        _listDataHeader.add("League Tables");
        _listDataHeader.add("Team List");

        List<String> home = new ArrayList<String>();
        home.add("Home");

        List<String> leagueTables = new ArrayList<String>();
        for(League l: data){
            leagueTables.add(l.getLeagueName());
        }

        List<String> teamList = new ArrayList<String>();
        teamList.add("CompSci Vipers");
        teamList.add("TeamB");
        teamList.add("TeamC");
        teamList.add("TeamD");
        teamList.add("TeamE");

        _listDataChild.put(_listDataHeader.get(0), home);
        _listDataChild.put(_listDataHeader.get(1), leagueTables);
        _listDataChild.put(_listDataHeader.get(2), teamList);
    }



}