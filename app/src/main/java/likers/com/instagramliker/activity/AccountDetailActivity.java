package likers.com.instagramliker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import likers.com.instagramliker.R;
import likers.com.instagramliker.extra.SharedPref;
import likers.com.instagramliker.extra.Singleton;

public class AccountDetailActivity extends AppCompatActivity {

    ImageView ig, btn;
    Toolbar tb;
    TextView toolID, userName, userFull, follower, following, media,my_bio,verified;
    Button rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        tb = (Toolbar) findViewById(R.id.abx);
        toolID = (TextView) tb.findViewById(R.id.toolbar_id);
        toolID.setText("My Details");
        btn = (ImageView) findViewById(R.id.back_btn_toolbar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        my_bio= (TextView) findViewById(R.id.my_bio);
        verified= (TextView) findViewById(R.id.verified);
        userName = (TextView) findViewById(R.id.userName);
        userFull = (TextView) findViewById(R.id.userFullName);
        follower = (TextView) findViewById(R.id.myfollowers);
        following = (TextView) findViewById(R.id.myfollowing);
        ig = (ImageView) findViewById(R.id.UserIImage);
        media = (TextView) findViewById(R.id.my_media_count);
        userName.setText(Singleton.MainUserName);
        follower.setText("Followers: " + Singleton.myFollowers);
        following.setText("Following: " + Singleton.myFollowing);
        media.setText("Total Posts: " + Singleton.MediaCount);
        userFull.setText(Singleton.MainFullName);
        Picasso.with(this).load(Singleton.userImage).placeholder(R.drawable.user_dp).error(R.drawable.user_dp).into(ig);
        my_bio.setText(Singleton.user_bio);
        verified.setText("Verified: "+Singleton.is_verfied);

        rl = (Button) findViewById(R.id.loginout);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.cleardata(AccountDetailActivity.this);
                startActivity(new Intent(AccountDetailActivity.this, SplashActivity.class));
                Toast.makeText(AccountDetailActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
