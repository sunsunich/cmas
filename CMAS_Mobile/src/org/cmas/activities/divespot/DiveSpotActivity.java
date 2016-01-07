package org.cmas.activities.divespot;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.cmas.BaseBeanContainer;
import org.cmas.PermissionChecker;
import org.cmas.R;
import org.cmas.activities.SecureActivity;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.service.divespot.DiveSpotService;
import org.cmas.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on Jan 02, 2016
 *
 * @author Alexander Petukhov
 */
public class DiveSpotActivity extends SecureActivity
        implements GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback {

    /*
 * Define a request code to send to Google Play services
 * This code is returned in Activity.onActivityResult
 */
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied;

    private GoogleMap map;

    private GoogleApiClient mGoogleApiClient;

    private PermissionChecker permissionChecker;

    private DiveSpotService diveSpotService;

    private Map<String, DiveSpot> markerSpotMap;

    private ListView listView;

    public DiveSpotActivity() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dive_spot);

        permissionChecker = BaseBeanContainer.getInstance().getPermissionChecker();
        diveSpotService = BaseBeanContainer.getInstance().getDiveSpotService();
        markerSpotMap = new HashMap<>();

        listView = (ListView) findViewById(R.id.dive_spots_holder);
        listView.setAdapter(new DiveSpotAdapter(new ArrayList<DiveSpot>(), this));
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        );

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (map != null && permissionChecker.requestMapsPermissions(this)) {
            // Access to the location has been granted to the app.
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setAllGesturesEnabled(true);
            map.setInfoWindowAdapter(new DiveSpotInfoWindowAdapter(this));
            map.setOnInfoWindowClickListener(this);

            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                @Override
                public void onMapLongClick(LatLng latLng) {
                    showNewDiveSpotMarker(latLng);
                }
            });

            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                    showDiveSpotsOnMap(bounds);
                }
            });
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        showDiveSpotEditDialog(marker);
    }

    private void showNewDiveSpotMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dive_site_map_icon));
        Marker marker = map.addMarker(
                markerOptions
        );

        showDiveSpotEditDialog(marker);
    }

    private void showDiveSpotEditDialog(final Marker marker) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dive_spot_edit_dialog);
        final EditText spotNameEditText = (EditText) dialog.findViewById(R.id.spot_name);

        Button deleteBtn = (Button) dialog.findViewById(R.id.delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSpot(marker, dialog);
            }
        });

        Button saveBnt = (Button) dialog.findViewById(R.id.save);
        saveBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spotName = spotNameEditText.getText().toString();
                if (StringUtil.isTrimmedEmpty(spotName)) {
                    deleteSpot(marker, dialog);
                }
                DiveSpot diveSpot = markerSpotMap.get(marker.getId());
                boolean isNew = diveSpot == null;
                if (isNew) {
                    diveSpot = new DiveSpot();
                    markerSpotMap.put(marker.getId(), diveSpot);
                }
                diveSpot.setName(spotName);
                LatLng position = marker.getPosition();
                diveSpot.setLatitude(position.latitude);
                diveSpot.setLongitude(position.longitude);

                diveSpotService.addDiveSpot(DiveSpotActivity.this, diveSpot, isNew);

                marker.setTitle(spotName);
                marker.showInfoWindow();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void deleteSpot(Marker marker, DialogInterface dialog) {
        DiveSpot diveSpot = markerSpotMap.remove(marker.getId());
        if (diveSpot != null) {
            diveSpotService.deleteDiveSpot(this, diveSpot);
        }
        marker.remove();
        dialog.dismiss();
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    private void showDiveSpotsOnMap(LatLngBounds bounds) {
        List<DiveSpot> diveSpots = diveSpotService.getInMapBounds(this, bounds);
        listView.setAdapter(
                new DiveSpotAdapter(diveSpots, this)
        );
        for (DiveSpot diveSpot : diveSpots) {
            if (!markerSpotMap.values().contains(diveSpot)) {
                Marker marker = map.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(diveSpot.getLatitude(), diveSpot.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dive_site_map_icon))
                                .title(diveSpot.getName())
                );
                markerSpotMap.put(marker.getId(), diveSpot);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != PermissionChecker.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (permissionChecker.isMapsPermissionsGranted(permissions, grantResults)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17.0F);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    /*
     * Google Play services can resolve some errors it detects.
     * If the error has a resolution, try sending an Intent to
     * start a Google Play services activity that can resolve
     * error.
     */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            /*
            * Thrown if Google Play services canceled the original
            * PendingIntent
            */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                Log.e(e.getMessage(), getClass().getName(), e);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
//        PermissionUtils.PermissionDeniedDialog
//                .newInstance(true).show(getActivity().getSupportFragmentManager(), "dialog");
    }
}
