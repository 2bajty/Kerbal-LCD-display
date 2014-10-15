package dwabajty.kerbaldisplay;

import com.google.gson.annotations.SerializedName;

public class TeleResponse {
    @SerializedName("v.altitude")
    double altitude;

    @SerializedName("o.inclination")
    double inclination;
}
