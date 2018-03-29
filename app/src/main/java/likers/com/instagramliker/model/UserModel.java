package likers.com.instagramliker.model;

public class UserModel
{


    private String userName;
    private String fullName;
    private String userImage;
    private String userPass;
    private  long userID;
    private int myFollowers,myFollowing;


    @Override
    public String toString() {
        return "UserModel{" +
                "userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userPass='" + userPass + '\'' +
                ", userID=" + userID +
                ", myFollowers=" + myFollowers +
                ", myFollowing=" + myFollowing +
                '}';
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }


    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;

    }

    public int getMyFollowers() {
        return myFollowers;
    }

    public void setMyFollowers(int myFollowers) {
        this.myFollowers = myFollowers;
    }

    public int getMyFollowing() {
        return myFollowing;
    }

    public void setMyFollowing(int myFollowing) {
        this.myFollowing = myFollowing;
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
