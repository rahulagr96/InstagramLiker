package likers.com.instagramliker.model;

/**
 * Created by GLB-335 on 12/22/2017.
 */

public class FeedModel
{
    private long mediaPk;
    private String mediaId;
    private int media_type;
    private int like_count;
    private String imageUrl;

    @Override
    public String toString() {
        return "FeedModel{" +
                "mediaPk=" + mediaPk +
                ", mediaId='" + mediaId + '\'' +
                ", media_type=" + media_type +
                ", like_count=" + like_count +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public long getMediaPk() {
        return mediaPk;
    }

    public void setMediaPk(long mediaPk) {
        this.mediaPk = mediaPk;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public int getMedia_type() {
        return media_type;
    }

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



}
