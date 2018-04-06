package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;
 
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import client.view.ClientUi;

import org.glassfish.tyrus.client.ClientManager;

import utility.FaceData;
 
@ClientEndpoint
public class FaceClient {
 
    private static Gson gson = null;
    private static ClientUi observer = null;

    public static void create(String host, int port, ClientUi observer) throws Exception {
        FaceClient.gson = new GsonBuilder().create();
        FaceClient.observer = observer;
        ClientManager client = ClientManager.createClient();
        client.connectToServer(FaceClient.class, 
            new URI("ws://" + host + ":" + new Integer(8025).toString() + "/faceData"));
    }

    @OnOpen
    public void onOpen(Session session) throws Exception {
        System.out.println("Connected ... " + session.getId());
    }
 
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.println("Received ...." + message);
        FaceData faceData = gson.fromJson(message, FaceData.class);
        observer.setFaceData(faceData);
    }
 
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(String.format("Session %s close because of %s", session.getId(), closeReason));
    }
 
}