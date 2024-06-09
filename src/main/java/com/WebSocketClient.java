import com.data_storage.*;
import java.net.URI;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    
    private DataStorage storage;

    public WebSocketClient(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.storage = dataStorage;
        System.out.println("Client created successfully");
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Client open");
        System.out.println("Connection established");
    }

    @Override
    public void onMessage(String message) {
        try {
            String[] information = message.split(",");
            if (information.length == 4) {
                if (information[3].endsWith("%")) {
                    information[3] = information[3].substring(0, information[3].length() - 1);
                }
                storage.addPatientData(
                    Integer.parseInt(information[0]),
                    Double.parseDouble(information[3]),
                    information[2],
                    Long.parseLong(information[1])
                );
                System.out.println("Data stored successfully");
            } else {
                System.out.println("Invalid message format");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid data format: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed with exit code " + code + ". Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("An error occurred: " + ex.getMessage());
    }

    public static void main(String[] args) {
        try {
            URI uri = new URI("ws://localhost:8080/websocket");
            DataStorage dataStorage = new DataStorage();
            WebSocketClient client = new WebSocketClient(uri, dataStorage);
            client.connectBlocking(); // Connect to the server
            client.send("Hello, WebSocket!"); // Send a message to the server
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
