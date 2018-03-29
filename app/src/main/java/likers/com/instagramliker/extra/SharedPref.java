package likers.com.instagramliker.extra;

import android.content.Context;
import android.content.SharedPreferences;

import likers.com.instagramliker.model.UserModel;

/**
 * Created by GLB-335 on 12/21/2017.
 */

public class SharedPref
{
    public static final String DEFAULT=null;
    public static void savingDataToShared(UserModel model,Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",model.getUserName());
        editor.putLong("userId",model.getUserID());
        editor.putInt("followers",model.getMyFollowers());
        editor.putString("userFullName",model.getFullName());
        editor.putInt("following",model.getMyFollowing());
        editor.putString("proURL",model.getUserImage());
        editor.putString("password",model.getUserPass());
        editor.commit();
    }

    public static void getData(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        Singleton.userName = sharedPreferences.getString("username",DEFAULT);
        Singleton.fullName =  sharedPreferences.getString("userFullName",DEFAULT);
        Singleton.userImage = sharedPreferences.getString("proURL",DEFAULT);
        Singleton.userID=sharedPreferences.getLong("userId",0);
        Singleton.myFollowers=sharedPreferences.getInt("followers",0);
        Singleton.myFollowing=sharedPreferences.getInt("following",0);
        Singleton.UserPass=sharedPreferences.getString("password",DEFAULT);
        System.out.println("PASSWORD "+Singleton.UserPass +" dasd "+sharedPreferences.getString("password",DEFAULT));
    }

    public static void cleardata(Context context)
    {
      context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE).edit().clear().commit();
        Singleton.userName = null;
        Singleton.fullName = null;
        Singleton.userImage = null;
        Singleton.userID=0;
        Singleton.myFollowers=0;
        Singleton.myFollowing=0;
        Singleton.UserPass=null;
        Singleton.MY_FOLLOWING.clear();
        Singleton.MY_FOLLOWERS.clear();
        Singleton.USER_POSTS.clear();
    }
}
