package company.businessinc.bathtouch.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;

public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private static final String TAG = "ExpandableListAdapter";
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    //headers ID's
    private int HOME = -1;
    private int MYLEAGUES = -1;
    private int ALLLEAGUES = -1;

    public ExpandableListAdapter(Context context) {
        this._context = context;
        this._listDataHeader = new ArrayList<String>();
        this._listDataChild = new HashMap<String, List<String>>();
        prepareListData();
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
        notifyDataSetInvalidated();
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


    public void updateMyLeagues(List<String> data){
        _listDataChild.remove(_listDataHeader.get(MYLEAGUES));
        _listDataChild.put(_listDataHeader.get(MYLEAGUES), data);

        notifyDataSetChanged();
    }

    public void updateAllLeagues(List<String> data){
        _listDataChild.remove(_listDataHeader.get(ALLLEAGUES));
        _listDataChild.put(_listDataHeader.get(ALLLEAGUES), data);

        notifyDataSetChanged();
    }
    /*
     * Preparing the list data
     * Executes api call before firing the call back
     */
    private void prepareListData() {
        int id = 0;
        _listDataHeader.add("Home");
        List<String> child = new ArrayList<>();
        child.add("Home");
        HOME = id;
        _listDataChild.put(_listDataHeader.get(HOME), child);
        id++;

        if(DataStore.getInstance(_context).isUserLoggedIn()){
            _listDataHeader.add("My Leagues");
            child = new ArrayList<>();
            MYLEAGUES = id;
            id++;
        }

        _listDataHeader.add("All Leagues");
        child = new ArrayList<>();
        ALLLEAGUES = id;
        _listDataChild.put(_listDataHeader.get(ALLLEAGUES), child);
        id++;

        notifyDataSetChanged();
    }

}