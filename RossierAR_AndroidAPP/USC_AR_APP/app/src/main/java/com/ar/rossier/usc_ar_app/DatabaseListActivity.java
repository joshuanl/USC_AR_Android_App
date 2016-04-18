package com.ar.rossier.usc_ar_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.rossier.usc_ar_app.dBase.DatabaseContent;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An activity representing a list of Databases. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DatabaseDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class DatabaseListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private String userEmail;
    private String userID;
    private String userPW;
    final private Firebase mFBD = new Firebase("https://usc-rossier-ar.firebaseio.com");
    private DatabaseContent DBC;
    private User mUser;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_list);

        userEmail = LoginActivity.userEmail;
        userID = User.getUserIDName(userEmail);
        userPW = LoginActivity.userPW;
        Firebase.setAndroidContext(this);
        Firebase userREF = mFBD.child("users").child(userID);
        Log.d("DBListActivity", "userREF created to user: " + userID);

        Query queryRef = mFBD.orderByChild("idname");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String prevChild){
                Map<String,Object> value = (Map<String, Object>) snapshot.getValue();

                String name1 = String.valueOf(value.get("iduser"));
                Log.d("QUERY", snapshot.getKey().toString() + "is" + value.get("iduser").toString());
                if (name1.equals("iduser")){
                    System.out.println("Name" + value.get("iduser"));
                }
                else{
                    System.out.println("its is null");
                }

            }
            @Override
            public void onChildChanged(DataSnapshot var1, String var2){}


            @Override
            public void onChildRemoved(DataSnapshot var1){}

            @Override
            public void onChildMoved(DataSnapshot var1, String var2){}

            @Override
            public void onCancelled(FirebaseError var1){}
        });

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Map<String, Object> lastLogin = new HashMap<String, Object>();
        lastLogin.put("lastLogin", dateFormat.format(date));
        userREF.updateChildren(lastLogin);
        Log.d("DBListActivity", "updated last login date");
        if(mUser == null){
            Log.d("DBListActivity", "mUser is null");
        }
        if(DBC == null){
            Log.d("DBListActivity", " DBC read failed.  DBC is null");
        }
        //userRef.
        View recyclerView = findViewById(R.id.database_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.database_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DBC.ITEMS));
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DatabaseList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ar.rossier.usc_ar_app/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DatabaseList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ar.rossier.usc_ar_app/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DatabaseContent.DBItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DatabaseContent.DBItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.database_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final int pos = position;
            final Firebase userRef = mFBD.child("users").child(userID);
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).tag);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(DatabaseDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        DatabaseDetailFragment fragment = new DatabaseDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.database_detail_container, fragment)
                                .commit();
                        holder.mItem = mValues.get(pos);
                        if (holder.mItem.blankItem) {
                            final Dialog dialog = new Dialog(DatabaseListActivity.this);
                            dialog.setTitle("Add New Database");
                            dialog.setContentView(R.layout.dialog_add_db_layout);
                            dialog.show();

                            final EditText secretET = (EditText) findViewById(R.id.secretETF);
                            final EditText keyET = (EditText) findViewById(R.id.keyETF);
                            final EditText nameET = (EditText) findViewById(R.id.dbNameETF);
                            final EditText tagET = (EditText) findViewById(R.id.tagETF);
                            Button addButton = (Button) findViewById(R.id.addButton);
                            Button cancelButton = (Button) findViewById(R.id.cancelButton);

                            addButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    String secret = secretET.getText().toString();
                                    String key = keyET.getText().toString();
                                    String name = nameET.getText().toString();
                                    String tag = tagET.getText().toString();

                                    //check for empty values
                                    if (secret == null) {
                                        Toast.makeText(getApplicationContext(), "No entry given for 'Secret'", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (key == null) {
                                        Toast.makeText(getApplicationContext(), "No entry given for 'Key'", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (name == null) {
                                        name = "No Name";
                                    } else if (name.trim().length() == 0) {
                                        name = "No Name";
                                    }
                                    if (tag == null) {
                                        tag = "No Tag";
                                    } else if (tag.trim().length() == 0) {
                                        tag = "No Tag";
                                    }
                                    //end of checking for empty values
                                    mValues.add(new DatabaseContent.DBItem(name, secret, key, false, tag));
                                    userRef.child("DBContent").setValue(DBC);
                                    setContentView(R.layout.activity_database_list);
                                    //need to add to user
                                }
                            });
                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });
                        }//end of if item clicked is a blank item

                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DatabaseDetailActivity.class);
                        intent.putExtra(DatabaseDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DatabaseContent.DBItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
