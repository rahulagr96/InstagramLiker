package likers.com.instagramliker.extra;

import java.util.ArrayList;

import dev.niekirk.com.instagram4android.Instagram4Android;
import likers.com.instagramliker.model.FeedModel;
import likers.com.instagramliker.model.LikerModel;
import likers.com.instagramliker.model.UserModel;

import static android.R.attr.name;

/**
 * Created by GLB-335 on 12/21/2017.
 */

public class Singleton
{
    public static long MainUserID;
    public static Instagram4Android instagram;
    public static  String userName,fullName,userImage;
    public static  long userID;
    public static int myFollowers,myFollowing;
    public static ArrayList<UserModel> MY_FOLLOWING = new ArrayList<>();
    public static ArrayList<UserModel> MY_FOLLOWERS = new ArrayList<>();
    public static ArrayList<LikerModel> MY_LIKERS = new ArrayList<>();
    public static boolean checking=false;
    public static String UserPass;
    public static ArrayList<Long> FOLLOWING_PK  = new ArrayList<>();
    public static ArrayList<FeedModel> USER_POSTS = new ArrayList<>();

    public static long feedUserPk;
    public static String pro_user_name;
    public static long mediaPk;
    public static String maxID;
    public static String MainUserName;
    public static String MainFullName;
    public static int MediaCount;
    public static String user_bio;
    public static boolean is_verfied;
}
