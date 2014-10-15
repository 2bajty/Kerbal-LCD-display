package dwabajty.kerbaldisplay;

import jssc.SerialPortException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestStarter {

    static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws SerialPortException {
        final LcdController lcdController = LcdController.openPort("COM8");
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    long time = System.currentTimeMillis();
                    String timeString = Long.toString(time);
                    String trimedTimeString = timeString.substring(timeString.length()-8, timeString.length());
                    lcdController.setText(trimedTimeString);
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }, 100, 7, TimeUnit.MILLISECONDS);

        lcdController.setListener(new LcdController.OnButtonPressListener() {
            @Override
            public void onButtonPress(int id, int value) {
                System.out.println("value = " + value);
            }
        });
    }
}
