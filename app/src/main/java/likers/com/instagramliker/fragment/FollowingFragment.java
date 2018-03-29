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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowingRequest;
import dev.niekirk.com.instagram4android.requests.payload.InstagramGetUserFollowersResult;
import dev.niekirk.com.instagram4android.requests.payload.InstagramUser;
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary;
import likers.com.instagramliker.R;
import likers.com.instagramliker.activity.MainActivity;
import likers.com.instagramliker.adapter.FollowerAdapter;
import likers.com.instagramliker.adapter.FollowingAdapter;
import likers.com.instagramliker.extra.Singleton;
import likers.com.instagramliker.model.UserModel;

import static likers.com.instagramliker.R.*;
import static likers.com.instagramliker.extra.Singleton.instagram;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    FollowingAdapter fa;
    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(layout.fragment_following, container, false);

        ImageButton ib = (ImageButton) rootView.findViewById(id.refesh_following);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    RefreshTask rt = new RefreshTask();
                rt.execute();
            }
        });

        ListView lv = (ListView) rootView.findViewById(R.id.following_list);
        fa = new FollowingAdapter(getContext(), Singleton.MY_FOLLOWING);
        lv.setAdapter(fa);
        return rootView;
    }

    ProgressDialog progress;

    private class RefreshTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean status = false;
            try {

                List<InstagramUserSummary> following = new ArrayList<>();
                String nextMaxId = null;


                    InstagramGetUserFollowersResult Following = instagram.sendRequest(new InstagramGetUserFollowingRequest(Singleton.MainUserID));
                    if(Following.getStatus().equals("ok")) {
                        status=true;
                        following.addAll(Following.getUsers());
                        System.out.println("HIVZ:" + Following);

                        Singleton.MY_FOLLOWING.clear();
                        Singleton.FOLLOWING_PK.clear();
                        for (InstagramUserSummary user_sum : following) {
                            System.out.println("users:" + user_sum);
                            System.out.println("user names:" + user_sum.getUsername());
                            UserModel model1 = new UserModel();
                            model1.setUserName(user_sum.getUsername());
                            model1.setFullName(user_sum.getFull_name());
                            model1.setUserID(user_sum.getPk());
                            Singleton.FOLLOWING_PK.add(user_sum.getPk());
                            model1.setUserImage(user_sum.getProfile_pic_url());
                            Singleton.MY_FOLLOWING.add(model1);
                        }
                    }
                System.out.println("MY FOLLOWING: "+Singleton.MY_FOLLOWING.size());
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
                    progress.setTitle("Refreshing Following");
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
                System.out.println("On Post");
                Singleton.myFollowing=Singleton.MY_FOLLOWING.size();
                MainActivity.userDataInDrawer();
                fa.notifyDataSetChanged();

            }
        }

    }
}
