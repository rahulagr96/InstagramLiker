package likers.com.instagramliker.activity;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.niekirk.com.instagram4android.requests.InstagramGetMediaLikersRequest;
import dev.niekirk.com.instagram4android.requests.InstagramUserFeedRequest;
import dev.niekirk.com.instagram4android.requests.payload.InstagramFeedItem;
import dev.niekirk.com.instagram4android.requests.payload.InstagramFeedResult;
import dev.niekirk.com.instagram4android.requests.payload.InstagramGetMediaLikersResult;
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary;
import likers.com.instagramliker.R;
import likers.com.instagramliker.adapter.FollowLikersAdapter;
import likers.com.instagramliker.adapter.UserFeedAdapter;
import likers.com.instagramliker.extra.Singleton;
import likers.com.instagramliker.model.FeedModel;
import likers.com.instagramliker.model.LikerModel;

import static likers.com.instagramliker.extra.Singleton.instagram;

public class FollowLikers extends AppCompatActivity {
    ListView listView;
    ImageView btn;
    boolean localController = false;
    FollowLikersAdapter adapter;
    private static ViewGroup viewGroup;
    ProgressBar progressBar;
    Toolbar tb;
    TextView toolID,tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feeds);

        tb= (Toolbar) findViewById(R.id.back_toolbar);
        toolID= (TextView) tb.findViewById(R.id.toolbar_id);
        tt= (TextView) findViewById(R.id.user_pro_name);
        tt.setVisibility(View.GONE);
        toolID.setText("Liked Users");

        init2();
        listView = (ListView) findViewById(R.id.user_feed_listview);

        addFooterView();
        viewGroup.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),
                PorterDuff.Mode.MULTIPLY);

        Singleton.MY_LIKERS.clear();
        adapter = new FollowLikersAdapter(this, Singleton.MY_LIKERS);
        listView.setAdapter(adapter);

       new LikerAsync().execute();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addFooterView() {
        LayoutInflater inflater = getLayoutInflater();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, listView, false);
        listView.addFooterView(viewGroup);
    }

    public void init2() {
        Toolbar tb1 = (Toolbar) findViewById(R.id.back_toolbar);
        btn = (ImageView) findViewById(R.id.back_btn_toolbar);
    }
    ProgressDialog progress;

    class LikerAsync extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progress == null) {
                    progress = new ProgressDialog(FollowLikers.this);
                    progress.setTitle("Likers Data");
                    progress.setMessage("Please wait while fetching data...");
                    progress.setCancelable(false);
                }
                progress.show();
            } catch (Exception e) {
                System.out.println("VIVZ: Exception- " + e.getMessage());
            }
        }


        @Override
        protected Boolean doInBackground(String[] params) {
            boolean status = false;
            try {
                InstagramGetMediaLikersResult instLikerRequest = instagram.sendRequest(new InstagramGetMediaLikersRequest(Singleton.mediaPk));

                if (instLikerRequest.getStatus().equals("ok")) {
                    status = true;

                    for (InstagramUserSummary feedsItems : instLikerRequest.getUsers()) {
                        LikerModel likerModel = new LikerModel();
                        likerModel.setUserPk(feedsItems.getPk());
                        likerModel.setUserName(feedsItems.getUsername());
                        likerModel.setFullName(feedsItems.getFull_name());
                        likerModel.setUserImage(feedsItems.getProfile_pic_url());

                        int localvar = Singleton.FOLLOWING_PK.indexOf(feedsItems.getPk());

                        if(localvar == -1)
                        {
                            likerModel.setChecking(false);
                        }
                        else
                        {
                            likerModel.setChecking(true);
                        }
                        Singleton.MY_LIKERS.add(likerModel);
                    }
                    System.out.println("MY LIKERS SIZE: " + Singleton.MY_LIKERS.size());
                }
            } catch (IOException e) {
                e.printStackTrace();
                status = false;
                System.out.println("VIVZ: Exception occurs" + e);
            }
            return status;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
            if (Singleton.MY_LIKERS.size() <= 0) {
                Toast.makeText(FollowLikers.this, "Sorry! No one liked this post", Toast.LENGTH_LONG).show();
                finish();
            }
            if (aBoolean) {
               FollowLikersAdapter.followLikerAdapterInstance.notifyDataSetChanged();
            }
        }
    }



}
