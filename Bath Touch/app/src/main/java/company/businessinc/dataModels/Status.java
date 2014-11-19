package company.businessinc.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 19/11/14.
 */
public class Status {
    private Boolean status;

    public Status(Boolean status) {
        this.status = status;
    }

    public Status() {
        this.status = null;
    }

    public Status(JSONObject jsonObject) throws JSONException{
        try {
            this.status = jsonObject.getBoolean("status");
        } catch (JSONException e){
            this.status = null;
        }
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
