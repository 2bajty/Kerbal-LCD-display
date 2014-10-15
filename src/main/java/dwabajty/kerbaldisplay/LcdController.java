package dwabajty.kerbaldisplay;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class LcdController {

    SerialPort serialPort;
    String text = "  x     ";
    String led = "        ";

    StringBuffer buffer = new StringBuffer();
    private OnButtonPressListener listener;

    private LcdController(String portName) throws SerialPortException {
        serialPort = new SerialPort(portName);
        serialPort.openPort();//Open serial port
        serialPort.setParams(115200, 8, 1, 0);
        serialPort.addEventListener(new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent event) {
                if(event.isRXCHAR()){//If data is available
                    if(event.getEventValue() > 0){//Check bytes count in the input buffer
                        //Read data, if 10 bytes available

                        try {
                            byte input[] = serialPort.readBytes(event.getEventValue());
                            for(byte b : input) {
                                if(b == '\n') {
                                    onCommand(buffer.toString());
                                    buffer.delete(0, buffer.length());
                                } else {
                                    buffer.append(new String(new byte[]{b}));
                                }
                            }
                        } catch (SerialPortException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    System.out.println("event = " + event);
                }
            }
        });
    }

    public void onCommand(String command) {
        if(command.startsWith("B1")) {
            int state = 0;
            if(command.charAt(2) == '+') {
                state = 1;
            }
            if(listener != null) {
                listener.onButtonPress(1, state);
            }
        }
    }

    public void setListener(OnButtonPressListener listener) {
        this.listener = listener;
    }

    public static LcdController openPort(String portName) throws SerialPortException {
        return new LcdController(portName);
    }

    public void setText(String text) throws SerialPortException {
        this.text = text;
        updateDisplay();
    }

    public void setLeds(Led[] leds) {
        String ledString = "";
        for(int i = 0; i < leds.length && i < 8; i++) {
            ledString += leds[i].c;
        }
    }

    public void updateDisplay() throws SerialPortException {
        String toBeWritten = text + led + "\n";

        serialPort.writeBytes(toBeWritten.getBytes());
    }

    public enum Led {
        Red('R'), Green('G'), Both('B'), Off(' ');

        public final char c;
        Led(char c) {
            this.c = c;
        }
    }

    public interface OnButtonPressListener {
        void onButtonPress(int id, int value);
    }
}
