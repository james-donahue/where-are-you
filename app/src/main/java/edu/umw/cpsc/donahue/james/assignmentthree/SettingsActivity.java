package edu.umw.cpsc.donahue.james.assignmentthree;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setupAppBar();

        String username = DataManager.readUsername(this);
        EditText usernameEditText = (EditText) findViewById(R.id.username);
        usernameEditText.setText(username);
    }

    /*public void testServer(View view) {
        EditText arbitrary = (EditText) findViewById(R.id.arbitrary);
        String text = arbitrary.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://cpsc470amobiledevelopment.appspot.com/echo?name=JamesDonahue&echo=" + text;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setMessage(response);
                        builder.setPositiveButton("OK", SettingsActivity.this);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast toast = Toast.makeText(SettingsActivity.this, "Something went wrong", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }*/

    public void updateUsername(View v) {
        EditText usernameEditText = (EditText) findViewById(R.id.username);
        String username = usernameEditText.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://cpsc470amobiledevelopment.appspot.com/messaging?type=register&id=" +
                username + "&token=crn-goxOkOo:APA91bHGhXqNCN-NuGQL7FCizP-DD6_4yzZq64onbl3Wi72Y-2pIbxI_U29ygqmTXAgTIYAG2TXvOL6k_E9QBUalMg8LcOvIX4ISGulJNhbebMgmBhUcIOq0JWNB67RUA4zRESucGrM3";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setMessage(response);
                        builder.setPositiveButton("OK", SettingsActivity.this);
                        AlertDialog dialog = builder.create();
                        dialog.show();*/
                        Log.d("SettingsActivity", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //mTextView.setText("That didn't work!");
                        Toast toast = Toast.makeText(SettingsActivity.this, "Something went wrong", Toast.LENGTH_SHORT);
                        toast.show();
                }
            });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        DataManager.saveUsername(this, username);
    }


    public void signOut(View v) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Toast toast = Toast.makeText(this, "You are not signed in", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder().build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

            googleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
        }
    }
}