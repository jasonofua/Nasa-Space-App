package com.example.dell.helpie.Fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.dell.helpie.R;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cha extends Fragment {
    private FancyButton search;
    // Our handle to Nearby Connections
    private ConnectionsClient connectionsClient;
    private TextInputLayout messageTil;
    private EditText edtMessage;
    private FloatingActionButton fabSend;
    private String opponentEndpointId;
    private ProgressDialog dialog;
    private RecyclerView chatrv;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String NAME = "name";
    private RelativeLayout con;

    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private String codeName;


    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                 //   opponentChoice = GameChoice.valueOf(new String(payload.asBytes(), UTF_8));
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    if (update.getStatus() == PayloadTransferUpdate.Status.SUCCESS ) {
                        //finishRound();
                    }
                }
            };

    // Callbacks for finding other devices
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                 //   Log.i(TAG, "onEndpointFound: endpoint found, connecting");
                    dialog.setMessage("Person Found Connecting to "+info.getEndpointName());
                    connectionsClient.requestConnection(codeName, endpointId, connectionLifecycleCallback);
                }

                @Override
                public void onEndpointLost(String endpointId) {}
            };

    // Callbacks for connections to other devices
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                 //   Log.i(TAG, "onConnectionInitiated: accepting connection");
                    connectionsClient.acceptConnection(endpointId, payloadCallback);

                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {

                        opponentEndpointId = endpointId;
                        connectionsClient.stopDiscovery();
                        connectionsClient.stopAdvertising();
                        Toast.makeText(getContext(), "Successfully Connected", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        search.setVisibility(View.GONE);
                        con.setVisibility(View.VISIBLE);

                    } else {
                     //   Log.i(TAG, "onConnectionResult: connection failed");
                        dialog.setMessage("Could not connect try again...");
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                 //   Log.i(TAG, "onDisconnected: disconnected from the opponent");
                  //  resetGame();
                }
            };


    public Cha() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_cha, container, false);
        connectionsClient = Nearby.getConnectionsClient(getContext());
        dialog = new ProgressDialog(getContext());
        SharedPreferences prefs = getContext().getSharedPreferences(MyPREFERENCES, getContext().MODE_PRIVATE);
        codeName = prefs.getString(NAME, "");
        con = (RelativeLayout)root.findViewById(R.id.container);
        fabSend = (FloatingActionButton)root.findViewById(R.id.chatSendButton);
        messageTil = (TextInputLayout)root.findViewById(R.id.tilMessage);
        edtMessage = (EditText)root.findViewById(R.id.messageEdit);


        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });



        search = (FancyButton)root.findViewById(R.id.btnSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               findOpponent(v);
            }
        });

        return root;
    }

    private void sendMessage() {
        String message = edtMessage.getText().toString().trim();

        if (message.isEmpty()){
            Toast.makeText(getContext(), "enter a message to send", Toast.LENGTH_SHORT).show();
            return;
        }


    }


    /** Starts looking for other players using Nearby Connections. */
    private void startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startDiscovery(
               getContext().getPackageName(), endpointDiscoveryCallback,
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build());
    }

    /** Broadcasts our presence using Nearby Connections so other players can find us. */
    private void startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startAdvertising(
                codeName, getContext().getPackageName(), connectionLifecycleCallback,
                new AdvertisingOptions.Builder().setStrategy(STRATEGY).build());
    }
    /** Disconnects from the opponent and reset the UI. */
    public void disconnect(View view) {
        connectionsClient.disconnectFromEndpoint(opponentEndpointId);

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!hasPermissions(getContext(), REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
    }

    @Override
    public void onStop() {
        connectionsClient.stopAllEndpoints();
        super.onStop();
    }

    /** Returns true if the app was granted all the permissions. Otherwise, returns false. */
    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /** Handles user acceptance (or denial) of our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }

    }

    public void findOpponent(View view) {
        dialog.setMessage("Searching For People Around.... Please wait");
        dialog.show();
        startAdvertising();
        startDiscovery();
        search.setEnabled(false);
    }


}
