package likers.com.instagramliker.activity;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import dev.niekirk.com.instagram4android.requests.InstagramUserFeedRequest;
import dev.niekirk.com.instagram4android.requests.payload.InstagramFeedItem;
import dev.niekirk.com.instagram4android.requests.payload.InstagramFeedResult;
import likers.com.instagramliker.R;
import likers.com.instagramliker.adapter.UserFeedAdapter;
import likers.com.instagramliker.extra.Singleton;
import likers.com.instagramliker.model.FeedModel;

import static likers.com.instagramliker.extra.Singleton.instagram;

public class UserFeeds extends AppCompatActivity {

    ListView listView;
    ImageView btn;
    boolean localController = false;
    UserFeedAdapter adapter;
    private static ViewGroup viewGroup;
    ProgressBar progressBar;
    Toolbar tb;
    TextView toolID,user_pro_name;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feeds);

        tb= (Toolbar) findViewById(R.id.back_toolbar);
        toolID= (TextView) tb.findViewById(R.id.toolbar_id);
        toolID.setText("User Feeds");
        user_pro_name = (TextView) findViewById(R.id.user_pro_name);
        user_pro_name.setText(Singleton.pro_user_name);

        init2();
        listView = (ListView) findViewById(R.id.user_feed_listview);

        addFooterView();
        viewGroup.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),
                PorterDuff.Mode.MULTIPLY);

        Singleton.USER_POSTS.clear();
        adapter = new UserFeedAdapter(this, Singleton.USER_POSTS);
        listView.setAdapter(adapter);

        new UserFeedsAsync().execute();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean loadmode = firstVisibleItem + visibleItemCount >= totalItemCount;
                if (loadmode == false) {
                    localController = true;
                }
                if (loadmode) {
                    System.out.println("LOADMODEE:" + loadmode + " " + Singleton.maxID + " " + localController);
                    if (Singleton.maxID != null) {
                        if (localController) {
                            localController = false;
                            System.out.println("PRINTING: " + localController);
                            UserPagination userpg = new UserPagination();
                            userpg.execute();
                        }
                    }
                }
            }
        });
    }

    private void addFooterView() {
        LayoutInflater inflater = getLayoutInflater();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.progress_layout, listView, false);
        listView.addFooterView(viewGroup);
    }

    public void init2() {
        btn = (ImageView) findViewById(R.id.back_btn_toolbar);
    }

    ProgressDialog progress;

    class UserFeedsAsync extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progress == null) {
                    progress = new ProgressDialog(UserFeeds.this);
                    progress.setTitle("User Feeds");
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
                List<InstagramFeedItem> instagramFeedItems = new ArrayList<>();

                InstagramFeedResult instagramFeedResult = instagram.sendRequest(new InstagramUserFeedRequest(Singleton.feedUserPk, null, 0));
                if (instagramFeedResult.getStatus().equals("ok")) {
                    status = true;
                    instagramFeedItems = instagramFeedResult.getItems();
                    Singleton.maxID = instagramFeedResult.getNext_max_id();

                    for (InstagramFeedItem feedsItems : instagramFeedItems) {
                        FeedModel feedsModel = new FeedModel();
                        feedsModel.setLike_count(feedsItems.like_count);
                        feedsModel.setMedia_type(feedsItems.getMedia_type());
                        feedsModel.setMediaId(feedsItems.getCode());
                        feedsModel.setMediaPk(feedsItems.getPk());
                        if (feedsItems.getImage_versions2() != null) {
                            feedsModel.setImageUrl(feedsItems.getImage_versions2().getCandidates().get(1).getUrl() + "");
                        }
                        Singleton.USER_POSTS.add(feedsModel);
                    }
                    System.out.println("USER FEED SIZE: " + Singleton.USER_POSTS.size());
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
            if (Singleton.USER_POSTS.size() <= 0) {
                Toast.makeText(UserFeeds.this, "Sorry! This is a Private User", Toast.LENGTH_LONG).show();
                finish();
            }
            if (aBoolean) {
                UserFeedAdapter.userFeedAdapterInstance.notifyDataSetChanged();
            }
        }
    }

    class UserPagination extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewGroup.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean status = false;
            try {
                List<InstagramFeedItem> instagramFeedItems = new ArrayList<>();

                InstagramFeedResult instagramFeedResult = instagram.sendRequest(new InstagramUserFeedRequest(Singleton.feedUserPk, Singleton.maxID, 0));
                if (instagramFeedResult.getStatus().equals("ok")) {
                    status = true;
                    instagramFeedItems = instagramFeedResult.getItems();
                    Singleton.maxID = instagramFeedResult.getNext_max_id();

                    for (InstagramFeedItem feedsItems : instagramFeedItems) {
                        FeedModel feedsModel = new FeedModel();
                        feedsModel.setLike_count(feedsItems.like_count);
                        feedsModel.setMedia_type(feedsItems.getMedia_type());
                        feedsModel.setMediaId(feedsItems.getCode());
                        feedsModel.setMediaPk(feedsItems.getPk());
                        if (feedsItems.getImage_versions2() != null) {
                            feedsModel.setImageUrl(feedsItems.getImage_versions2().getCandidates().get(1).getUrl() + "");
                        }
                        Singleton.USER_POSTS.add(feedsModel);
                    }
                    System.out.println("USER FEED SIZE: " + Singleton.USER_POSTS.size());
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
            viewGroup.setVisibility(View.INVISIBLE);
            if (aBoolean) {
                UserFeedAdapter.userFeedAdapterInstance.notifyDataSetChanged();
            }
        }
    }
}
