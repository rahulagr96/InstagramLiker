package likers.com.instagramliker.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowersRequest;
import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowingRequest;
import dev.niekirk.com.instagram4android.requests.payload.InstagramGetUserFollowersResult;
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary;
import likers.com.instagramliker.R;
import likers.com.instagramliker.activity.MainActivity;
import likers.com.instagramliker.adapter.FollowerAdapter;
import likers.com.instagramliker.extra.Singleton;
import likers.com.instagramliker.model.UserModel;

import static likers.com.instagramliker.extra.Singleton.instagram;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment {
    FollowerAdapter faa;

    public FollowersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_followers, container, false);
        ImageButton ib = (ImageButton) root.findViewById(R.id.refesh_followers);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshTask rtt = new RefreshTask();
                rtt.execute();
            }
        });

        ListView lv = (ListView) root.findViewById(R.id.followers_list);
        FollowerAdapter faa = new FollowerAdapter(getContext(), Singleton.MY_FOLLOWERS);
        lv.setAdapter(faa);
        return root;
    }

    ProgressDialog progress;

    private class RefreshTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean status = false;
            try {

                List<InstagramUserSummary> users = new ArrayList<InstagramUserSummary>();

                String nextFollowersMaxId = null;
                InstagramGetUserFollowersResult Followers = instagram.sendRequest(new InstagramGetUserFollowersRequest(Singleton.MainUserID));
                users.addAll(Followers.getUsers());
                if (Followers.getStatus().equals("ok")) {
                    status=true;
                    Singleton.MY_FOLLOWERS.clear();

                    for (InstagramUserSummary user_sum : users) {
                        UserModel model1 = new UserModel();
                        model1.setUserName(user_sum.getUsername());
                        model1.setFullName(user_sum.getFull_name());
                        model1.setUserID(user_sum.getPk());
                        model1.setUserImage(user_sum.getProfile_pic_url());
                        Singleton.MY_FOLLOWERS.add(model1);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            return status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progress == null) {
                    progress = new ProgressDialog(getActivity());
                    progress.setTitle("Refreshing Followers");
                    progress.setMessage("Please wait...");
                    progress.setCancelable(false);
                }
                progress.show();
            } catch (Exception e) {
                System.out.println("Exception " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
            if (aBoolean) {
                Singleton.myFollowers =  Singleton.MY_FOLLOWERS.size();
                MainActivity.userDataInDrawer();
                faa.notifyDataSetChanged();
            }
        }

    }
}
