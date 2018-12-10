package com.source.activities;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultsActivity extends AppCompatActivity {

    private Button nextTurn;
    String handcards = LoginActivity.handCards;
    String communityCards = LoginActivity.communityCards;
    String numPlayers = LoginActivity.numPlayers;
    static double win = 0.0;
    static double loss = 0.0;
    static double tie = 0.0;
    @BindView(R.id.textView6) TextView _winPerc;
    @BindView(R.id.textView7) TextView _losePerc;
    @BindView(R.id.textView8) TextView _tiePerc;
    @BindView(R.id.textView3) TextView _bet;


    String[] hCards = handcards.split(",");
    String[] cCards = communityCards.split(",");

    Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);
        currentActivity = this;
        final ProgressDialog progressDialog = new ProgressDialog(ResultsActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Calculating...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                    }
                }, 2500);
        nextTurn = findViewById(R.id.next_button);
        nextTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentActivity, LoginActivity.class);
                startActivity(intent);
            }
        });


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = null;
        final DecimalFormat df = new DecimalFormat("#.00");
        if (cCards.length == 3) {
            url = "https://poker-odds.p.rapidapi.com/hold-em/odds?community=" + cCards[0] + "%2C" + cCards[1] + "%2C" + cCards[2] + "&hand=" + hCards[0] + "%2C" + hCards[1] + "&players=" + numPlayers;
        } else if (cCards.length == 4) {
            url = "https://poker-odds.p.rapidapi.com/hold-em/odds?community=" + cCards[0] + "%2C" + cCards[1] + "%2C" + cCards[2] + "%2C" + cCards[3] + "&hand=" + hCards[0] + "%2C" + hCards[1] + "&players=" + numPlayers;
        } else {
            url = "https://poker-odds.p.rapidapi.com/hold-em/odds?community=" + cCards[0] + "%2C" + cCards[1] + "%2C" + cCards[2] + "%2C" + cCards[3] + "%2C" + cCards[4] + "&hand=" + hCards[0] + "%2C" + hCards[1] + "&players=" + numPlayers;
        }
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response

                        Log.d("aaaa", response.toString());
                        try {
                            Log.d("aaaa",""+response.getDouble("win"));
                            win = response.getDouble("win");
                            loss = response.getDouble("lose");
                            tie = response.getDouble("tie");
                            if (win - loss < 0.1 || tie > 0.4) {
                                _bet.setText("Call.");
                            } if(win > 0.5) {
                                _bet.setText("Bet.");
                            } else if(loss > 0.5) {
                                _bet.setText("Fold.");
                            } else {
                                _bet.setText("Call.");
                            }
                            _winPerc.setText(""+df.format(100*response.getDouble("win"))+"%");
                            _losePerc.setText(""+df.format(100*response.getDouble("lose"))+"%");
                            _tiePerc.setText(""+df.format(100*response.getDouble("tie"))+"%");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("gggg","error");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-RapidAPI-Key", "kRCZKdqb2cmsh7tMWKywgoF65D0Ep1gzKRmjsnobQude2GCQKu");
                return params;
            }
        };
        queue.add(getRequest);
    }

}
