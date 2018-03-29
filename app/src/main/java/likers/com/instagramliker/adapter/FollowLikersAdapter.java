package likers.com.instagramliker.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import dev.niekirk.com.instagram4android.requests.InstagramFollowRequest;
import dev.niekirk.com.instagram4android.requests.InstagramUnfollowRequest;
import dev.niekirk.com.instagram4android.requests.payload.StatusResult;
import likers.com.instagramliker.Interfaces.NotifyInterface;
import likers.com.instagramliker.R;
import likers.com.instagramliker.extra.Singleton;
import likers.com.instagramliker.model.LikerModel;

import static likers.com.instagramliker.extra.Singleton.instagram;

public class FollowLikersAdapter extends BaseAdapter {

    Context context;
    public static FollowLikersAdapter followLikerAdapterInstance;
    public static NotifyInterface notifyInterface;
    private static ArrayList<LikerModel> adapterList = new ArrayList<>();

    public FollowLikersAdapter(Context context, ArrayList<LikerModel> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
        followLikerAdapterInstance = this;
    }


    public static synchronized FollowLikersAdapter getInstance(NotifyInterface listener) {
        notifyInterface = listener;
        return followLikerAdapterInstance;
    }


    @Override

    public int getCount() {
        return adapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LikerModel model = new LikerModel();

        model = adapterList.get(position);

        FollowLikersAdapter.myholder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.likers_row, null);
            holder = new FollowLikersAdapter.myholder(convertView);
            convertView.setTag(holder);
            Log.d("VIVZ:", "creation of view");
        } else {
            holder = (FollowLikersAdapter.myholder) convertView.getTag();
            Log.d("VIVZ:", "recycling views");
        }

        holder.userName.setText(model.getUserName());
        holder.userFullname.setText(model.getFullName());
        Picasso.with(context).load(model.getUserImage()).placeholder(R.drawable.user_dp).error(R.drawable.user_dp).into(holder.user_image);


        final String myurl=model.getUserImage();
        holder.user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_mydialog);

                // Set the dialog text -- this is better done in the XML
                ImageView img = (ImageView)dialog.findViewById(R.id.image2);
                Picasso.with(context).load(myurl).into(img);


                Button save = (Button) dialog.findViewById(R.id.save_image);

                Button share = (Button) dialog.findViewById(R.id.share_image);

                Button exit = (Button) dialog.findViewById(R.id.exit_dialog);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // dismiss();
                        Toast.makeText(context, "Exit", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setFormat(PixelFormat.RGBA_8888);
            }
        });

        if (model.isChecking()) {
            //if person is friend
            holder.follow_btn.setVisibility(View.INVISIBLE);
            holder.unfollow_btn.setVisibility(View.VISIBLE);

        } else {
            //if person is stranger
            holder.unfollow_btn.setVisibility(View.INVISIBLE);
            holder.follow_btn.setVisibility(View.VISIBLE);

        }

        holder.unfollow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnfollowPerson unfollow = new UnfollowPerson(adapterList.get(position).getUserPk(), position);
                unfollow.execute();
                Toast.makeText(context, "Unfollowed successfully", Toast.LENGTH_SHORT).show();

            }
        });

        holder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowPerson followperson = new FollowPerson(adapterList.get(position).getUserPk(), position);
                followperson.execute();
                Toast.makeText(context, "Followed successfully", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }


    class myholder {
        TextView userName, userFullname;
        ImageView user_image;
        Button follow_btn, unfollow_btn;

        myholder(View v) {
            userName = (TextView) v.findViewById(R.id.u_name);
            user_image = (ImageView) v.findViewById(R.id.i_user);
            userFullname = (TextView) v.findViewById(R.id.uf_name);
            follow_btn = (Button) v.findViewById(R.id.follow_button);
            unfollow_btn = (Button) v.findViewById(R.id.unfollow_button);
        }
    }

    ProgressDialog progress;

    class FollowPerson extends AsyncTask<String, Void, Boolean> {
        long userPK;
        int position;

        public FollowPerson(long userPK, int pos) {
            this.userPK = userPK;
            this.position = pos;
        }


        @Override
        protected Boolean doInBackground(String... params) {
            boolean status = false;
            try {

                LikerModel model = new LikerModel();
                StatusResult followRequest = instagram.sendRequest(new InstagramFollowRequest(userPK));
                System.out.println("JOKO: " + followRequest);
                if (followRequest.getStatus().equals("ok")) {
                    status = true;
                    adapterList.get(position).setChecking(true);

                    Singleton.FOLLOWING_PK.add(userPK);
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
                    progress = new ProgressDialog(context);
                    progress.setTitle("Following");
                    progress.setMessage("Please wait...");
                    progress.setCancelable(false);
                }
                progress.show();
            } catch (Exception e) {
                System.out.println("VIVZ: Exception- " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
                System.out.println("Progress dismissed");
            }
            if (aBoolean) {
                FollowLikersAdapter.followLikerAdapterInstance.notifyDataSetChanged();
                if(notifyInterface!=null)
                {
                    notifyInterface.followUnfollowUpdate();
                }
            }
        }

    }


    class UnfollowPerson extends AsyncTask<Object, Object, Boolean> {
        long userPK;
        int post;

        public UnfollowPerson(long userPK, int pos) {
            this.userPK = userPK;
            this.post = pos;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progress == null) {
                    progress = new ProgressDialog(context);
                    progress.setTitle("Unfollowing");
                    progress.setMessage("Please wait...");
                    progress.setCancelable(false);
                }
                progress.show();
            } catch (Exception e) {
                System.out.println("VIVZ: Exception- " + e.getMessage());
            }
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            boolean status = false;
            try {
                LikerModel model = new LikerModel();
                StatusResult unfollowRequest = instagram.sendRequest(new InstagramUnfollowRequest(userPK));
                System.out.println(unfollowRequest);
                if (unfollowRequest.getStatus().equals("ok")) {
                    status = true;
                    adapterList.get(post).setChecking(false);

                    Singleton.FOLLOWING_PK.remove(userPK);
                }
            } catch (IOException e) {
                System.out.println(e);
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
                FollowLikersAdapter.followLikerAdapterInstance.notifyDataSetChanged();
                if(notifyInterface!=null)
                {
                    notifyInterface.followUnfollowUpdate();
                }
            }
        }
    }
}
