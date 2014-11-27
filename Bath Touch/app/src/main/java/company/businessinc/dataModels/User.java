package company.businessinc.dataModels;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 18/11/14.
 */
public class User  implements Parcelable {
    private Integer userID;
    private Boolean status;

    public User(Integer userID, boolean status) {
        this.userID = userID;
        this.status = status;
    }

    public User(JSONObject jsonObject) throws JSONException{
        try {
            this.userID = jsonObject.getInt("userID");
        } catch(JSONException e){
            this.userID = null;
        }
        try {
            this.status = jsonObject.getBoolean("status");
        } catch (JSONException e){
            this.status = null;
        }
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status =  status;
    }

    protected User(Parcel in) {
        userID = in.readByte() == 0x00 ? null : in.readInt();
        byte statusVal = in.readByte();
        status = statusVal == 0x02 ? null : statusVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userID == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(userID);
        }
        if (status == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (status ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
