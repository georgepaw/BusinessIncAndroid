package company.businessinc.bathtouch;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link company.businessinc.bathtouch.LeagueTableFragment.LeagueTableCallbacks} interface
 * to handle interaction events.
 * Use the {@link company.businessinc.bathtouch.LeagueTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMapFragment extends Fragment {

    private static final String TAG = "MapFragment";


    private SupportMapFragment fragment;
    private GoogleMap map;

    private Float mLongitude, mLatitude;
    private String mAddress, mPostCode, mPlace;

    public static MyMapFragment newInstance(float longitude, float latitude, String address, String postcode, String place) {
        MyMapFragment fragment = new MyMapFragment();
        Bundle args = new Bundle();
        args.putFloat("longitude", longitude);
        args.putFloat("latitude", latitude);
        args.putString("address", address);
        args.putString("postcode", postcode);
        args.putString("place", place);
        fragment.setArguments(args);
        return fragment;
    }

    public MyMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get the data for map
        mLatitude = 0f;
        mLongitude = 0f;
        mAddress = "";
        mPostCode = "";
        if (getArguments() != null) {
            mLatitude = getArguments().getFloat("latitude");
            mLongitude = getArguments().getFloat("longitude");
            mAddress = getArguments().getString("address");
            mPostCode = getArguments().getString("postcode");
            mPlace = getArguments().getString("place");
        }

        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapView);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapView, fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            map.addMarker(new MarkerOptions().position(new LatLng(mLatitude, mLongitude)).title(mPlace).snippet(mAddress)).showInfoWindow();
            // For zooming automatically to the Dropped PIN Location
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 15.0f));
        }
    }
}
