package likers.com.instagramliker.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import likers.com.instagramliker.R;
import likers.com.instagramliker.extra.CommonClass;
import likers.com.instagramliker.extra.SharedPref;
import likers.com.instagramliker.extra.Singleton;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPref.getData(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (CommonClass.isNetworkAvailable(getApplicationContext())) {
                    if (Singleton.userName != null) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));

                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                } else {
                  alertBox();

                }

            }
        }, 2000);
    }

    void alertBox(){

        new AlertDialog.Builder(this)
                .setTitle("Closing application")
                .setMessage("No Internet Access")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SplashActivity.this, "Check network and try again", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).show();

    }
}
