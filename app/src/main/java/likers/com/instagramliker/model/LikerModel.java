package likers.com.instagramliker.model;

/**
 * Created by GLB-335 on 12/27/2017.
 */

public class LikerModel {

    private long mediaPk;
    private long userPk;
    private String userName;
    private String fullName;
    private String userImage;

    public boolean isChecking() {
        return checking;
    }

    public void setChecking(boolean checking) {
        this.checking = checking;
    }

    private boolean checking;

    @Override
    public String toString() {
        return "LikerModel{" +
                "mediaPk=" + mediaPk +
                ", userPk=" + userPk +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userImage='" + userImage + '\'' +
                '}';
    }

    public long getUserPk() {
        return userPk;
    }

    public void setUserPk(long userPk) {
        this.userPk = userPk;
    }

    public long getMediaPk() {
        return mediaPk;
    }

    public void setMediaPk(long mediaPk) {
        this.mediaPk = mediaPk;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
