package dwabajty.kerbaldisplay;

import jssc.SerialPortException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Starter {
    public static void main(String[] args) throws InterruptedException, URISyntaxException, SerialPortException {
        System.out.println("Connecting");

        final LcdController lcdController = LcdController.openPort("COM8");

        final KerbalSocketClient client = new KerbalSocketClient(new URI("ws://127.0.0.1:8085/datalink"));
        client.setListener(new KerbalSocketClient.OnTeleResponseListener() {
            @Override
            public void onTeleResponse(TeleResponse response) {
                String timeString = String.format("%08d", (int)response.altitude);
                String trimedTimeString = timeString.substring(timeString.length()-8, timeString.length());
                try {
                    lcdController.setText(trimedTimeString);
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        });
        lcdController.setListener(new LcdController.OnButtonPressListener() {
            @Override
            public void onButtonPress(int id, int value) {
                if(id == 1 && value == 1) {
                    client.send("{\"run\":[\"f.stage\"]}");
                }
            }
        });

        client.connectBlocking();
    }
}
