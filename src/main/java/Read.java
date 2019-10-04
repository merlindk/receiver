import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Arrays;

public class Read {

    private static byte[] toWrite = new byte[0];

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

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
                //System.out.println("----------------------------------------------------------------");
                byte[] newData = new byte[userPort.bytesAvailable()];
                int numRead = userPort.readBytes(newData, newData.length);
                //System.out.println("el Array antes trim: " + Arrays.toString(newData));
                newData = trimArray(newData);
                //System.out.println("el Array desp trim: " + Arrays.toString(newData));

                byte[] aux = new byte[toWrite.length + newData.length];
                System.arraycopy(toWrite, 0, aux, 0, toWrite.length);
                System.arraycopy(newData, 0, aux, toWrite.length, newData.length);
                toWrite = aux;

                //System.out.println("Se leyeron " + numRead + " bytes.");
                //System.out.println("Utiles " + newData.length + " bytes.");

                if(numRead > newData.length && newData.length > 0) {
                    try {
                        System.out.println("#############################################################");
                        //System.out.println("En total Se leyeron " + toWrite.length + " bytes.");
                        //System.out.println("el Array recuperado: " + Arrays.toString(toWrite));
                        String yourObject = SerializationUtils.deserialize(toWrite);
                        toWrite = new byte[0];
                        System.out.println(ANSI_RED + yourObject + ANSI_RESET);
                        System.out.println("#############################################################");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                }

            }
        });
    }

    private static byte[] trimArray(byte[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] != -120) {
                byte[] aux = new byte[i + 1];
                System.arraycopy(array, 0, aux, 0, i + 1);
                return aux;
            } else if(i == 0){
                return new byte[0];
            }
        }
        return array;
    }
}