package dwabajty.kerbaldisplay;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.*;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class KerbalSocketClient extends WebSocketClient {
    Gson gson = new Gson();

    OnTeleResponseListener listener;

    public KerbalSocketClient(URI serverUri) {
        super(serverUri, new Draft_17());
    }

    public void setListener(OnTeleResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected!");
        send("{\"+\":[\"v.altitude\"], \"+\":[\"o.inclination\"], \"rate\":10}");
    }

    @Override
    public void onMessage(String message) {
        TeleResponse response = gson.fromJson(message, TeleResponse.class);
        if(listener != null) {
            listener.onTeleResponse(response);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("code = " + code);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("ex = " + ex);
    }

    public interface OnTeleResponseListener {
        void onTeleResponse(TeleResponse response);
    }
}
