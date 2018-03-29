package likers.com.instagramliker.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import likers.com.instagramliker.R;



@SuppressLint("ValidFragment")
public class Mydialog extends DialogFragment {

    String myurl;

    public Mydialog(String myurl) {
        this.myurl=myurl;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_mydialog, container, false);
        Dialog dialog = new Dialog(getContext());


// Set the title
        dialog.setTitle("Large View");

// inflate the layout
        dialog.setContentView(R.layout.activity_mydialog);

        // Set the dialog text -- this is better done in the XML
        ImageView img = (ImageView)dialog.findViewById(R.id.image2);
        Picasso.with(getContext()).load(myurl).into(img);


        Button save = (Button) dialog.findViewById(R.id.save_image);

        Button share = (Button) dialog.findViewById(R.id.share_image);

        Button exit = (Button) dialog.findViewById(R.id.exit_dialog);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

// Display the dialog
        dialog.show();

        return root;
    }
}
