import com.fazecast.jSerialComm.SerialPort;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner ob = new Scanner(System.in);
        SerialPort comPort = SerialPort.getCommPort("COM6");
        comPort.setBaudRate(9600);
        comPort.openPort();
        InputStream inputStream = comPort.getInputStream();

        byte[] bytes;

        System.out.println("Ingrese el largo:");
        int input = ob.nextInt();
        while (input != 0) {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[13];
            nRead = inputStream.read(data, 0, data.length);
            buffer.write(data, 0, nRead);


            bytes = buffer.toByteArray();
            System.out.println("Se recupero: " + bytes.length);
            int[][] matriz = SerializationUtils.deserialize(bytes);

            System.out.println(matriz[0][0]);

            System.out.println("Ingrese el largo:");
            input = ob.nextInt();
        }


    }
}
