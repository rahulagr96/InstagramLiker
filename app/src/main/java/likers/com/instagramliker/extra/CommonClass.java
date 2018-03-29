package likers.com.instagramliker.extra;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by GLB-335 on 12/21/2017.
 */

public class CommonClass
{
    //Checking if internet in Available or not
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if (info != null) {

                if (info.getState() == NetworkInfo.State.CONNECTED) {

                    return true;
                }

            }
        }
        return false;
    }
}
