package likers.com.instagramliker.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import likers.com.instagramliker.R;
import likers.com.instagramliker.extra.CommonClass;
import likers.com.instagramliker.extra.Singleton;
import likers.com.instagramliker.extra.SharedPref;
import likers.com.instagramliker.model.UserModel;

import static likers.com.instagramliker.extra.Singleton.instagram;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText name,pass;
    Button b;
    ImageView pass_vis,pass_hide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText) findViewById(R.id.edittext_username);
        pass = (EditText) findViewById(R.id.edittext_password);

        pass_hide= (ImageView) findViewById(R.id.hide_pass);
        pass_hide.setOnClickListener(this);

        pass_vis= (ImageView) findViewById(R.id.show_pass);
        pass_vis.setOnClickListener(this);

        b = (Button) findViewById(R.id.login_button);
        b.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if (v==pass_vis)
        {
            if (pass.getText().length() > 0) {
                pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                pass_hide.setVisibility(View.VISIBLE);
                pass_vis.setVisibility(View.INVISIBLE);
                pass.setSelection(pass.length());
            }
        }
        else if (v==pass_hide)
        {
            if (pass.getText().length() > 0) {
                pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                pass_vis.setVisibility(View.VISIBLE);
                pass_hide.setVisibility(View.INVISIBLE);
                pass.setSelection(pass.length());
            }
        }
        else if (v==b)
        {
            validate();

        }
    }

    private void validate()
    {
        if (name.getText()!=null && !name.getText().toString().isEmpty())
        {
            if(pass.getText()!=null && !pass.getText().toString().isEmpty())
            {
                if(CommonClass.isNetworkAvailable(getApplicationContext()))
                {
                    loginTask loginobj= new loginTask(name.getText().toString().trim(),pass.getText().toString().trim());
                    loginobj.execute();
                }
                else
                {
                    Toast.makeText(this, "No Internet Access", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                pass.setError("Password is empty");
            }
        }
        else
        {
            name.setError("Username is empty");
        }
    }


    class loginTask extends AsyncTask<String,Void,Boolean>
    {

        String username,pass;
        public loginTask(String username, String password) {
            this.username=username;
            this.pass=password;
        }

        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(LoginActivity.this);
            progress.setTitle("Authenticating");
            progress.setMessage("Please wait while logging in");
            progress.setCancelable(false);
            progress.show();
        }



        @Override
        protected Boolean doInBackground(String... params) {

            boolean status=false;
            try {
                instagram = Instagram4Android.builder().username(username).password(pass).build();
                instagram.setup();
                instagram.login();
                if(instagram.isLoggedIn())
                {
                    InstagramSearchUsernameResult result = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
                    if(result.getStatus().equals("ok"))
                    {
                        status=true;
                        InstagramUser user = result.getUser();
                        UserModel model = new UserModel();

                        model.setFullName(user.getFull_name());
                        model.setUserImage(user.getProfile_pic_url());
                        model.setUserID(user.getPk());
                        Singleton.MainUserID=user.getPk();
                        Singleton.MainUserName=user.getUsername();
                        Singleton.MediaCount=user.media_count;
                        Singleton.MainFullName=user.getFull_name();
                        model.setMyFollowers(user.getFollower_count());
                        model.setMyFollowing(user.getFollowing_count());
                        model.setUserName(user.getUsername());
                        System.out.println("Password "+ pass);
                        model.setUserPass(pass);
                        SharedPref.savingDataToShared(model,LoginActivity.this);
                        SharedPref.getData(LoginActivity.this);

                        List<InstagramUserSummary> following = new ArrayList<>();
                        String nextMaxId = null;

                        //Getting all Following List
                        while (true)
                        {

                            InstagramGetUserFollowersResult Following = instagram.sendRequest(new InstagramGetUserFollowingRequest(user.getPk(), nextMaxId));

                            following.addAll(Following.getUsers());

                            nextMaxId = Following.getNext_max_id();

                            if (nextMaxId == null) {
                                break;
                            }
                        }
                        Singleton.MY_FOLLOWING.clear();
                        Singleton.FOLLOWING_PK.clear();
                        for(InstagramUserSummary user_sum :following)
                        {
                            UserModel model1 = new UserModel();

                            model1.setUserName(user_sum.getUsername());
                            model1.setFullName(user_sum.getFull_name());
                            model1.setUserID(user_sum.getPk());
                            Singleton.FOLLOWING_PK.add(user_sum.getPk());
                            model1.setUserImage(user_sum.getProfile_pic_url());
                            Singleton.MY_FOLLOWING.add(model1);
                        }
                        //following part ended


                        List<InstagramUserSummary> users = new ArrayList<InstagramUserSummary>();
                        String nextFollowersMaxId = null;
                        //Getting all followers list List
                        while (true)
                        {

                            InstagramGetUserFollowersResult Followers = instagram.sendRequest(new InstagramGetUserFollowersRequest(user.getPk(), nextFollowersMaxId));
                            users.addAll(Followers.getUsers());

                            nextFollowersMaxId = Followers.getNext_max_id();


                            if (nextFollowersMaxId == null) {
                                break;
                            }
                        }
                        Singleton.MY_FOLLOWERS.clear();

                        for(InstagramUserSummary user_sum :users)
                        {
                            UserModel model1 = new UserModel();

                            model1.setUserName(user_sum.getUsername());
                            model1.setFullName(user_sum.getFull_name());
                            model1.setUserID(user_sum.getPk());
                            model1.setUserImage(user_sum.getProfile_pic_url());
                            Singleton.MY_FOLLOWERS.add(model1);
                        }

                    }
                }
            } catch (IOException e) {
                System.out.println("VIVZ:"+e.getMessage());
                status=false;
            }
            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progress!=null && progress.isShowing())
            {
                progress.dismiss();
            }
            if(aBoolean)
            {
                Singleton.checking=true;
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            else
            {
                Toast.makeText(LoginActivity.this, "Please verify your details", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
