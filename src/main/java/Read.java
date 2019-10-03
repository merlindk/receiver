import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Arrays;

public class Read {

    public static void main(String args[]) {
        final SerialPort userPort = SerialPort.getCommPort("COM6");
        //Initializing port
        userPort.setBaudRate(115200);
        userPort.openPort();
        if (userPort.isOpen()) {
            System.out.println("Port initialized!");
        } else {
            System.out.println("Port not available");
            return;
        }

        userPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }

                byte[] newData = new byte[userPort.bytesAvailable()];
                int numRead = userPort.readBytes(newData, newData.length);
                System.out.println("el Array antes trim: " + Arrays.toString(newData));
                newData = trimArray(newData);
                System.out.println("el Array desp trim: " + Arrays.toString(newData));

                System.out.println("Se leyeron " + numRead + " bytes.");
                System.out.println("Utiles " + newData.length + " bytes.");
                try {
                    String yourObject = SerializationUtils.deserialize(newData);
                    System.out.println(yourObject);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }

            }
        });
    }

    private static byte[] trimArray(byte[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == 0 && array[i + 1] == 0) {
                byte[] aux = new byte[i];
                System.arraycopy(array, 0, aux, 0, i);
                return aux;
            }
        }
        return array;
    }
}