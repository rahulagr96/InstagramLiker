package likers.com.instagramliker.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
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

import java.util.ArrayList;

import likers.com.instagramliker.R;
import likers.com.instagramliker.activity.UserFeeds;
import likers.com.instagramliker.extra.Singleton;
import likers.com.instagramliker.model.UserModel;

/**
 * Created by GLB-335 on 12/21/2017.
 */

public class FollowingAdapter extends BaseAdapter {
    ArrayList<UserModel> localArray = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    public static FollowingAdapter followingAdapterInstance;

    public FollowingAdapter(Context con, ArrayList<UserModel> arrayList) {
        inflater = LayoutInflater.from(con);
        localArray = arrayList;
        context = con;
        followingAdapterInstance=this;
    }

    @Override

    public int getCount() {
        return localArray.size();
    }

    @Override
    public Object getItem(int position) {
        return localArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //convertView=inflater.inflate(R.layout.single_row,null);
        UserModel userModel = new UserModel();
        userModel = localArray.get(position);
        myholder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_row, null);
            holder = new myholder(convertView);
            convertView.setTag(holder);
            Log.d("VIVZ:", "creation of view");
        } else {
            holder = (myholder) convertView.getTag();
            Log.d("VIVZ:", "recycling views");
        }
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.pro_user_name=localArray.get(position).getUserName();
                Singleton.feedUserPk=localArray.get(position).getUserID();
                context.startActivity(new Intent(context, UserFeeds.class));
            }
        });
        Picasso.with(context).load(userModel.getUserImage()).placeholder(R.drawable.user_dp).error(R.drawable.user_dp).into(holder.userDp);
        holder.userName.setText(userModel.getUserName());
        holder.userFull.setText(userModel.getFullName());

        final String myurl=userModel.getUserImage();
        holder.userDp.setOnClickListener(new View.OnClickListener() {
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
        return convertView;
    }


    class myholder {
        TextView userName, userFull;
        ImageView userDp;
        Button b;
        myholder(View v) {
            userName = (TextView) v.findViewById(R.id.user_name);
            userFull = (TextView) v.findViewById(R.id.user_full_name);
            userDp = (ImageView) v.findViewById(R.id.img_user);
            b= (Button) v.findViewById(R.id.get_feeds);
        }
    }
}
