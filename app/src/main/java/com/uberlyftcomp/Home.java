package com.uberlyftcomp;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uberlyftcomp.logger.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.uberlyftcomp.util.Loc;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, android.view.View.OnClickListener{
    private TextView tv_origin;
    private TextView tv_dest;
    private Button btn_origin;
    private Button btn_dest;
    private Button btn_comp;

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private Loc tempLoc;
    private Loc originLoc;
    private Loc destLoc;

    public static final String TAG = "UberLyftComp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Retrieve the button and textviews
        tv_origin = (TextView)findViewById(R.id.tv_origin);
        tv_dest= (TextView)findViewById(R.id.tv_dest);
        btn_origin = (Button)findViewById(R.id.btn_origin);
        btn_dest = (Button)findViewById(R.id.btn_dest);
        btn_comp = (Button)findViewById(R.id.btn_comp);

        btn_origin.setOnClickListener(this);
        btn_dest.setOnClickListener(this);
        btn_comp.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);


        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);
        updateTxtView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            tempLoc = new Loc(place.getName().toString(),place.getAddress().toString(),place.getLatLng().latitude,place.getLatLng().longitude);;

            // Format details of the place for display and show it in a TextView.
//            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                    place.getId(), place.getAddress(), place.getPhoneNumber(),
//                    place.getWebsiteUri()));
//
//            // Display the third party attributions if set.
//            final CharSequence thirdPartyAttribution = places.getAttributions();
//            if (thirdPartyAttribution == null) {
//                mPlaceDetailsAttribution.setVisibility(View.GONE);
//            } else {
//                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
//                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
//            }

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.btn_comp:
                if(originLoc == null || destLoc == null)
                    Toast.makeText(getApplicationContext(),
                            "Please input origin and destination!" , Toast.LENGTH_SHORT)
                            .show();
                else{
                    Intent i = new Intent(this,Comp_activity.class);
                    ArrayList<Loc> l = new ArrayList<Loc>();
                    l.add(originLoc);
                    l.add((destLoc));
                    i.putExtra("Loc",l);
                    startActivity(i);
                }
                break;
            case R.id.btn_dest:
                if (tempLoc!=null) {
                    destLoc = tempLoc;
                    updateTxtView();
                    mAutocompleteView.setText("");
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please select address from drop down list!" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.btn_origin:
                if (tempLoc!=null) {
                    originLoc = tempLoc;
                    updateTxtView();
                    mAutocompleteView.setText("");
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please select address from drop down list!" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    private void updateTxtView(){
        if(originLoc!=null){
            tv_origin.setText(originLoc.getAddress());
        }
        if(destLoc!=null){
            tv_dest.setText(destLoc.getAddress());
        }
    }


}
