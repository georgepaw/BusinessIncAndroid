package company.businessinc.bathtouch.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import company.businessinc.bathtouch.DateFormatter;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Licence;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22/11/14.
 */
public class LicenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Licence> mLicences = new ArrayList<>();
    private Context mContext;

    private void generateData(){
        mLicences.add(new Licence("MaterialEditText","rengwuxian","AppCompat v21 makes it easy to use Material Design EditText in our apps, but it's so limited. If you've tried that, you know what I mean. So I wrote MaterialEditText, the EditText in Material Design, with more features that Google Material Design Spec has introduced.", "https://github.com/rengwuxian/MaterialEditText"));
        mLicences.add(new Licence("material-drawer","Heinrich Reimer","Custom drawer implementation for Material design apps.", "https://github.com/HeinrichReimer/material-drawer"));
        mLicences.add(new Licence("TextDrawable","Amulya Khare","This light-weight library provides images with letter/text like the Gmail app. It extends the Drawable class thus can be used with existing/custom/network ImageView classes. Also included is a fluent interface for creating drawables and a customizable ColorGenerator.", "https://github.com/amulyakhare/TextDrawable"));
        mLicences.add(new Licence("FloatingActionButton","Oleksandr Melnykov","Android floating action button which reacts on scrolling events. Becomes visible when an attached target is scrolled up and invisible when scrolled down.", "https://github.com/makovkastar/FloatingActionButton"));
        mLicences.add(new Licence("Material Dialogs","Aidan Follestad","A beautiful, easy-to-use, and customizable dialogs API, enabling you to use Material designed dialogs down to API 8.", "https://github.com/afollestad/material-dialogs"));
        mLicences.add(new Licence("PreferenceFragment-Compat","Germán Valencia","Unofficial PreferenceFragment compatibility layer for Android 1.6 and up.", "https://github.com/Machinarius/PreferenceFragment-Compat"));
    }

    public class ViewHolderLicence extends RecyclerView.ViewHolder{
        public TextView mLibName, mAuthor, mDescription;
        public CardView mCardView;
        public ViewHolderLicence(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.licence_item_card_view);
            mLibName = (TextView) v.findViewById(R.id.licence_item_libname);
            mAuthor = (TextView) v.findViewById(R.id.licence_item_auth);
            mDescription = (TextView) v.findViewById(R.id.licence_item_description);
        }
    }

    public LicenceAdapter(Fragment context) {
        mContext = context.getActivity();
        generateData();
    }


    @Override
    public int getItemViewType(int pos) {
        return pos == 0 ? 0 : 1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View vNoMatch = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.licence_item, parent, false);
        return new ViewHolderLicence(vNoMatch);
    }


    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderLicence v = (ViewHolderLicence) holder;

        final Licence licence = mLicences.get(position);
        v.mLibName.setText(licence.getLibName());
        v.mAuthor.setText(licence.getAuthor());
        v.mDescription.setText(licence.getDescription());
        v.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(licence.getUrl()));
                mContext.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLicences.size();
    }

}
