package com.source.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.hand_cards) EditText _handCards;
    @BindView(R.id.community_cards) EditText _communityCards;
    @BindView(R.id.num_players) EditText _numPlayers;
    @BindView(R.id.btn_login) Button _loginButton;

    public static String handCards = null;
    public static String communityCards = null;
    public static String numPlayers = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                onLoginSuccess();
            }
        });


    }

    public void login() {
        Log.d(TAG, "Login");
        _loginButton.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    public void onLoginSuccess() {

        handCards = _handCards.getText().toString();
        communityCards = _communityCards.getText().toString();
        numPlayers = _numPlayers.getText().toString();

        _loginButton.setEnabled(true);
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);
    }


}
