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
import likers.com.instagramliker.activity.FollowLikers;
import likers.com.instagramliker.extra.Singleton;
import likers.com.instagramliker.model.FeedModel;




public class UserFeedAdapter extends BaseAdapter {

    Context context;
    public static UserFeedAdapter userFeedAdapterInstance;
    ArrayList<FeedModel> adapterList = new ArrayList<>();

    public UserFeedAdapter(Context context, ArrayList<FeedModel> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
        userFeedAdapterInstance=this;
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
        FeedModel model;
        model = adapterList.get(position);

        myholder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_row_feed, null);
            holder = new myholder(convertView);
            convertView.setTag(holder);
            Log.d("VIVZ:", "creation of view");
        } else {
            holder = (myholder) convertView.getTag();
            Log.d("VIVZ:", "recycling views");
        }

        holder.total_like.setText("" + model.getLike_count() + "");
        Picasso.with(context).load(model.getImageUrl()).placeholder(R.drawable.user_dp).error(R.drawable.user_dp).into(holder.user_image);
        final String myurl=model.getImageUrl();
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


        holder.showliker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.mediaPk=adapterList.get(position).getMediaPk();
                System.out.println(Singleton.mediaPk);
                context.startActivity(new Intent(context, FollowLikers.class));
            }
        });
        return convertView;
    }


    class myholder {
        TextView total_like;
        ImageView user_image;
        Button showliker;
        myholder(View v) {
            total_like = (TextView) v.findViewById(R.id.user_likes);
            user_image = (ImageView) v.findViewById(R.id.user_image);
            showliker = (Button) v.findViewById(R.id.show_feed_btn);
        }
    }
}
