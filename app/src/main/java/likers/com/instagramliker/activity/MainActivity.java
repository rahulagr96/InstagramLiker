package likers.com.instagramliker.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.niekirk.com.instagram4android.Instagram4Android;
import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowersRequest;
import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowingRequest;
import dev.niekirk.com.instagram4android.requests.InstagramSearchUsernameRequest;
import dev.niekirk.com.instagram4android.requests.payload.InstagramGetUserFollowersResult;
import dev.niekirk.com.instagram4android.requests.payload.InstagramSearchUsernameResult;
import dev.niekirk.com.instagram4android.requests.payload.InstagramUser;
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary;
import likers.com.instagramliker.Interfaces.NotifyInterface;
import likers.com.instagramliker.R;
import likers.com.instagramliker.adapter.FollowLikersAdapter;
import likers.com.instagramliker.adapter.FollowerAdapter;
import likers.com.instagramliker.adapter.FollowingAdapter;
import likers.com.instagramliker.extra.CommonClass;
import likers.com.instagramliker.extra.SharedPref;
import likers.com.instagramliker.extra.Singleton;
import likers.com.instagramliker.fragment.MainFragment;
import likers.com.instagramliker.model.UserModel;

import static likers.com.instagramliker.extra.Singleton.instagram;

public class MainActivity extends AppCompatActivity implements NotifyInterface{

    NavigationView navigationView;
    DrawerLayout homeDrawer;
    static ImageView proPic;
    static TextView userName;
    static TextView follower;
    static TextView following;
    static TextView userFullName;
    static MainActivity mainInstance;
    Button bb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainInstance = this;
        initView();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragm_container, new MainFragment()).commit();
        if (!Singleton.checking) {
            if (CommonClass.isNetworkAvailable(getApplicationContext())) {
                LoginTask loginobj = new LoginTask(Singleton.userName, Singleton.UserPass);
                loginobj.execute();
            } else {
                Toast.makeText(this, "No Internet Access", Toast.LENGTH_SHORT).show();
            }
        }

        FollowLikersAdapter.getInstance(this);
    }

    private void initView() {
        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Instagram Likers");

        homeDrawer = (DrawerLayout) findViewById(R.id.home_drawer);
        navigationView = (NavigationView) findViewById(R.id.home_navigation_view);
        navigationView.setItemIconTintList(null);
        View view = navigationView.getHeaderView(0);
      //  bb=(Button)view.findViewById(R.id.my_post);

        proPic = (ImageView) view.findViewById(R.id.profile_pic);
        userName = (TextView) view.findViewById(R.id.userName);
        following = (TextView) view.findViewById(R.id.myfollowing);
        follower = (TextView) view.findViewById(R.id.myfollowers);
        userFullName = (TextView) view.findViewById(R.id.userFullName);
        userDataInDrawer();


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, homeDrawer, tb, R.string.drawer_open, R.string.drawer_close);
        homeDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        drawerContent();

    }

    private void drawerContent() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        menuSelector(item);
                        return false;
                    }
                }
        );
    }


    private void menuSelector(MenuItem item)
    {
        homeDrawer.closeDrawers();
        switch (item.getItemId())
        {
            case R.id.log_out:
            {
                SharedPref.cleardata(MainActivity.this);
                startActivity(new Intent(MainActivity.this,SplashActivity.class));
                Toast.makeText(this, "Logged Out", Toast.LENGTH_LONG).show();
                MainActivity.this.finish();
                break;
            }

            case R.id.account_detail:
            {
                startActivity(new Intent(MainActivity.this,AccountDetailActivity.class));
                break;
            }
            case R.id.share:
            {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Please download InstagramLiker");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            }

            case R.id.exit_app:
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Confirm Exit?");
                alertDialog.setMessage("Are you sure");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Toast.makeText(getApplicationContext(), "See You Again", Toast.LENGTH_SHORT).show();
                        System.exit(1);
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Great Choice", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                break;
            }
            case R.id.my_post:
            {
                Singleton.pro_user_name= Singleton.MainUserName;
                Singleton.feedUserPk=Singleton.MainUserID;
                startActivity(new Intent(MainActivity.this, UserFeeds.class));
                break;
            }
        }
    }

    public static void userDataInDrawer() {
        Picasso.with(mainInstance).load(Singleton.userImage).placeholder(R.drawable.user_dp).error(R.drawable.user_dp).into(proPic);
        userName.setText(Singleton.userName);
        userFullName.setText(Singleton.fullName);
        follower.setText("Followers: " + Singleton.myFollowers);
        following.setText("Following: " + Singleton.myFollowing);

    }
    ProgressDialog progress;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if ( progress!=null && progress.isShowing()){
            progress.dismiss();

        }
    }

    @Override
    public void followUnfollowUpdate() {
        System.out.println("Output comes here");
        userDataInDrawer();
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        String username, pass;

         LoginTask(String username, String password) {
            this.username = username;
            this.pass = password;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progress==null){
                    progress = new ProgressDialog(MainActivity.this);
                    progress.setTitle("User Details");
                    progress.setMessage("Please wait while fetching details...");
                    progress.setCancelable(false);
                }
                progress.show();
            }
            catch (Exception e){
                System.out.println( "Exception "+e.getMessage());
            }
        }


        @Override
        protected Boolean doInBackground(String... params) {

            boolean status = false;
            try {
                instagram = Instagram4Android.builder().username(username).password(pass).build();
                instagram.setup();
                instagram.login();
                if (instagram.isLoggedIn()) {
                    InstagramSearchUsernameResult result = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
                    if (result.getStatus().equals("ok")) {
                        status = true;
                        InstagramUser user = result.getUser();
                        UserModel model = new UserModel();
                        model.setFullName(user.getFull_name());
                        model.setUserImage(user.getProfile_pic_url());
                        model.setUserID(user.getPk());
                        Singleton.MainUserID=user.getPk();
                        Singleton.MediaCount=user.media_count;
                        Singleton.MainUserName=user.getUsername();
                        Singleton.MainFullName=user.getFull_name();
                        model.setMyFollowers(user.getFollower_count());
                        model.setMyFollowing(user.getFollowing_count());
                        model.setUserName(user.getUsername());
                        model.setUserPass(Singleton.UserPass);
                        Singleton.user_bio=user.biography;
                        Singleton.is_verfied=user.is_verified;

                        SharedPref.savingDataToShared(model, MainActivity.this);
                        SharedPref.getData(MainActivity.this);

                        List<InstagramUserSummary> following = new ArrayList<>();
                        String nextMaxId = null;

                        //Getting all Following List
                        while (true) {

                            InstagramGetUserFollowersResult Following = instagram.sendRequest(new InstagramGetUserFollowingRequest(user.getPk(), nextMaxId));

                            following.addAll(Following.getUsers());

                            nextMaxId = Following.getNext_max_id();

                            if (nextMaxId == null) {
                                break;
                            }
                        }
                        Singleton.MY_FOLLOWING.clear();
                        Singleton.FOLLOWING_PK.clear();
                        for (InstagramUserSummary user_sum : following) {
                            UserModel model1 = new UserModel();
                            model1.setUserName(user_sum.getUsername());
                            //model1.setMediaCount(user.media_count);
                            model1.setFullName(user_sum.getFull_name());
                            model1.setUserID(user_sum.getPk());
                            Singleton.FOLLOWING_PK.add(user_sum.getPk());
                            model1.setUserImage(user_sum.getProfile_pic_url());
                            Singleton.MY_FOLLOWING.add(model1);
                        }
                        //following part ended


                        List<InstagramUserSummary> users = new ArrayList<InstagramUserSummary>();
                        while (true) {
                            String nextFollowersMaxId = null;
                        //Getting all followers list

                            InstagramGetUserFollowersResult Followers = instagram.sendRequest(new InstagramGetUserFollowersRequest(user.getPk(), nextFollowersMaxId));
                            users.addAll(Followers.getUsers());

                            nextFollowersMaxId = Followers.getNext_max_id();


                            if (nextFollowersMaxId == null) {
                                break;
                            }
                        }
                        Singleton.MY_FOLLOWERS.clear();

                        for (InstagramUserSummary user_sum : users) {
                            UserModel model1 = new UserModel();
                            model1.setUserName(user_sum.getUsername());
                            //model1.setMediaCount(user.media_count);
                            model1.setFullName(user_sum.getFull_name());
                            model1.setUserID(user_sum.getPk());
                            model1.setUserImage(user_sum.getProfile_pic_url());
                            Singleton.MY_FOLLOWERS.add(model1);
                        }

                    }
                }
            } catch (IOException e) {
                System.out.println("VIVZ:" + e.getMessage());
                status = false;
            }
            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
            if (aBoolean) {

                userDataInDrawer();
                if (FollowingAdapter.followingAdapterInstance != null)
                    FollowingAdapter.followingAdapterInstance.notifyDataSetChanged();
                if (FollowerAdapter.followerAdapterInstance != null)
                    FollowerAdapter.followerAdapterInstance.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Please verify your details", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
