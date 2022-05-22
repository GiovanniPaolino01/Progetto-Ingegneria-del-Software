package it.polimi.ingsw.NETWORK.SERVER;

import it.polimi.ingsw.CONTROLLER.ControllerAction;
import it.polimi.ingsw.CONTROLLER.ControllerActionDifficult;
import it.polimi.ingsw.CONTROLLER.ControllerTurn;
import it.polimi.ingsw.MODEL.Game;
import it.polimi.ingsw.NETWORK.VIEW.RemoteView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<String, SocketClientConnection> waitingRoom2E = new HashMap<>();
    private Map<String, SocketClientConnection> waitingRoom2D = new HashMap<>();
    private Map<String, SocketClientConnection> waitingRoom3E = new HashMap<>();
    private Map<String, SocketClientConnection> waitingRoom3D = new HashMap<>();
    private Map<String, SocketClientConnection> waitingRoom4E = new HashMap<>();
    private Map<String, SocketClientConnection> waitingRoom4D = new HashMap<>();
    private Map<SocketClientConnection, SocketClientConnection> playingConnection = new HashMap<>();

    //Deregister connection
    public synchronized void deregisterConnection(ClientConnection c) {
        ClientConnection opponent = playingConnection.get(c);
        if(opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(c);
        playingConnection.remove(opponent);

        Iterator<String> iterator = waitingRoom2E.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingRoom2E.get(iterator.next())==c){
                iterator.remove();
            }
        }

        iterator = waitingRoom2D.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingRoom2D.get(iterator.next())==c){
                iterator.remove();
            }
        }

        iterator = waitingRoom3E.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingRoom3E.get(iterator.next())==c){
                iterator.remove();
            }
        }

        iterator = waitingRoom3D.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingRoom3D.get(iterator.next())==c){
                iterator.remove();
            }
        }

        iterator = waitingRoom4E.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingRoom4E.get(iterator.next())==c){
                iterator.remove();
            }
        }

        iterator = waitingRoom4D.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingRoom4D.get(iterator.next())==c){
                iterator.remove();
            }
        }
    }

    //Wait for another player
    public synchronized void lobby2E(SocketClientConnection c, String name) throws Exception{
        //public synchronized void lobby(Connection c, String name){
        boolean easy = true;
        if(!waitingRoom2E.containsKey(name)){
            waitingRoom2E.put(name, c);
        }
        else{
            throw new Exception();
        }
        if(waitingRoom2E.size() == 2) {
            List<String> keys = new ArrayList<>(waitingRoom2E.keySet());
            SocketClientConnection c1 = waitingRoom2E.get(keys.get(0));
            SocketClientConnection c2 = waitingRoom2E.get(keys.get(1));
            RemoteView player1 = new RemoteView(c1);
            RemoteView player2 = new RemoteView(c2);
            Game model = new Game(keys.get(0), keys.get(1));

            ControllerAction controllerAction = new ControllerAction(model, keys);
            ControllerTurn controllerTurn = new ControllerTurn(controllerAction, model, keys);

            //la remoteView osserva il game e la connection
            //il controller osserva la remote view
            model.addObserver(player1);
            model.addObserver(player2);
            c1.addObserver(player1);
            c2.addObserver(player2);
            player1.addObserver(controllerTurn);
            player2.addObserver(controllerTurn);

            playingConnection.put(c1, c2);
            playingConnection.put(c2, c1);
            waitingRoom2E.clear();

            model.startGame(easy);
            System.out.println("Game created");
        }
    }

    public synchronized void lobby2D(SocketClientConnection c, String name) throws Exception{
        boolean easy = false;
        if(!waitingRoom2D.containsKey(name)){
            waitingRoom2D.put(name, c);
        }else{
            throw new Exception();
        }

        if(waitingRoom2D.size() == 2) {
            List<String> keys = new ArrayList<>(waitingRoom2D.keySet());
            SocketClientConnection c1 = waitingRoom2D.get(keys.get(0));
            SocketClientConnection c2 = waitingRoom2D.get(keys.get(1));
            RemoteView player1 = new RemoteView(c1);
            RemoteView player2 = new RemoteView(c2);
            Game model = new Game(keys.get(0), keys.get(1));

            ControllerAction controllerAction = new ControllerActionDifficult(model, keys);
            ControllerTurn controllerTurn = new ControllerTurn(controllerAction, model, keys);

            //la remoteView osserva il game e la connection
            //il controller osserva la remote view
            model.addObserver(player1);
            model.addObserver(player2);
            c1.addObserver(player1);
            c2.addObserver(player2);
            player1.addObserver(controllerTurn);
            player2.addObserver(controllerTurn);

            playingConnection.put(c1, c2);
            playingConnection.put(c2, c1);
            waitingRoom2D.clear();

            model.startGame(easy);
            System.out.println("Game created");
        }
    }

    public synchronized void lobby3E(SocketClientConnection c, String name) throws Exception{
        boolean easy = true;
        if(!waitingRoom3E.containsKey(name)){
            waitingRoom3E.put(name, c);
        }else{
            throw new Exception();
        }
        if(waitingRoom3E.size() == 3) {
            List<String> keys = new ArrayList<>(waitingRoom3E.keySet());
            SocketClientConnection c1 = waitingRoom3E.get(keys.get(0));
            SocketClientConnection c2 = waitingRoom3E.get(keys.get(1));
            SocketClientConnection c3 = waitingRoom3E.get(keys.get(2));
            RemoteView player1 = new RemoteView(c1);
            RemoteView player2 = new RemoteView(c2);
            RemoteView player3 = new RemoteView(c3);
            Game model = new Game(keys.get(0), keys.get(1), keys.get(2));

            ControllerAction controllerAction = new ControllerAction(model, keys);
            ControllerTurn controllerTurn = new ControllerTurn(controllerAction, model, keys);

            //la remoteView osserva il game e la connection
            //il controller osserva la remote view
            model.addObserver(player1);
            model.addObserver(player2);
            model.addObserver(player3);
            c1.addObserver(player1);
            c2.addObserver(player2);
            c3.addObserver(player3);
            player1.addObserver(controllerTurn);
            player2.addObserver(controllerTurn);
            player3.addObserver(controllerTurn);


            playingConnection.put(c1, c2);
            playingConnection.put(c1, c3);
            playingConnection.put(c2, c1);
            playingConnection.put(c2, c3);
            playingConnection.put(c3, c1);
            playingConnection.put(c3, c2);
            waitingRoom3E.clear();

            model.startGame(easy);
            System.out.println("Game created");
        }
    }

    public synchronized void lobby3D(SocketClientConnection c, String name) throws Exception{
        boolean easy = false;
        if(!waitingRoom3D.containsKey(name)){
            waitingRoom3D.put(name, c);
        }else{
            throw new Exception();
        }
        if(waitingRoom3D.size() == 3) {
            List<String> keys = new ArrayList<>(waitingRoom3D.keySet());
            SocketClientConnection c1 = waitingRoom3D.get(keys.get(0));
            SocketClientConnection c2 = waitingRoom3D.get(keys.get(1));
            SocketClientConnection c3 = waitingRoom3D.get(keys.get(2));
            RemoteView player1 = new RemoteView(c1);
            RemoteView player2 = new RemoteView(c2);
            RemoteView player3 = new RemoteView(c3);
            Game model = new Game(keys.get(0), keys.get(1), keys.get(2));

            ControllerAction controllerAction = new ControllerActionDifficult(model, keys);
            ControllerTurn controllerTurn = new ControllerTurn(controllerAction, model, keys);


            model.addObserver(player1);
            model.addObserver(player2);
            model.addObserver(player3);
            c1.addObserver(player1);
            c2.addObserver(player2);
            c3.addObserver(player3);
            player1.addObserver(controllerTurn);
            player2.addObserver(controllerTurn);
            player3.addObserver(controllerTurn);

            playingConnection.put(c1, c2);
            playingConnection.put(c1, c3);
            playingConnection.put(c2, c1);
            playingConnection.put(c2, c3);
            playingConnection.put(c3, c1);
            playingConnection.put(c3, c2);
            waitingRoom3D.clear();

            model.startGame(easy);
            System.out.println("Game created");
        }
    }

    public synchronized void lobby4E(SocketClientConnection c, String name) throws Exception{
        boolean easy = true;
        if(!waitingRoom4E.containsKey(name)){
            waitingRoom4E.put(name, c);
        }else{
            throw new Exception();
        }
        if(waitingRoom4E.size() == 4) {
            List<String> keys = new ArrayList<>(waitingRoom4E.keySet());
            SocketClientConnection c1 = waitingRoom4E.get(keys.get(0));
            SocketClientConnection c2 = waitingRoom4E.get(keys.get(1));
            SocketClientConnection c3 = waitingRoom4E.get(keys.get(2));
            SocketClientConnection c4 = waitingRoom4E.get(keys.get(3));
            RemoteView player1 = new RemoteView(c1);
            RemoteView player2 = new RemoteView(c2);
            RemoteView player3 = new RemoteView(c3);
            RemoteView player4 = new RemoteView(c4);
            Game model = new Game(keys.get(0), keys.get(1), keys.get(2), keys.get(3));


            ControllerAction controllerAction = new ControllerAction(model, keys);
            ControllerTurn controllerTurn = new ControllerTurn(controllerAction, model, keys);

            model.addObserver(player1);
            model.addObserver(player2);
            model.addObserver(player3);
            model.addObserver(player4);
            c1.addObserver(player1);
            c2.addObserver(player2);
            c3.addObserver(player3);
            c4.addObserver(player4);
            player1.addObserver(controllerTurn);
            player2.addObserver(controllerTurn);
            player3.addObserver(controllerTurn);
            player4.addObserver(controllerTurn);

            playingConnection.put(c1, c2);
            playingConnection.put(c1, c3);
            playingConnection.put(c1, c4);
            playingConnection.put(c2, c1);
            playingConnection.put(c2, c3);
            playingConnection.put(c2, c4);
            playingConnection.put(c3, c1);
            playingConnection.put(c3, c2);
            playingConnection.put(c3, c4);
            playingConnection.put(c4, c1);
            playingConnection.put(c4, c2);
            playingConnection.put(c4, c3);
            waitingRoom4E.clear();

            model.startGame(easy);
            System.out.println("Game created");
        }
    }

    public synchronized void lobby4D(SocketClientConnection c, String name) throws Exception{
        boolean easy = false;
        if(!waitingRoom4D.containsKey(name)){
            waitingRoom4D.put(name, c);
        }else{
            throw new Exception();
        }
        if(waitingRoom4D.size() == 4) {
            List<String> keys = new ArrayList<>(waitingRoom4D.keySet());
            SocketClientConnection c1 = waitingRoom4D.get(keys.get(0));
            SocketClientConnection c2 = waitingRoom4D.get(keys.get(1));
            SocketClientConnection c3 = waitingRoom4D.get(keys.get(2));
            SocketClientConnection c4 = waitingRoom4D.get(keys.get(3));
            RemoteView player1 = new RemoteView(c1);
            RemoteView player2 = new RemoteView(c2);
            RemoteView player3 = new RemoteView(c3);
            RemoteView player4 = new RemoteView(c4);
            Game model = new Game(keys.get(0), keys.get(1), keys.get(2), keys.get(3));

            ControllerAction controllerAction = new ControllerActionDifficult(model, keys);
            ControllerTurn controllerTurn = new ControllerTurn(controllerAction, model, keys);

            model.addObserver(player1);
            model.addObserver(player2);
            model.addObserver(player3);
            model.addObserver(player4);
            c1.addObserver(player1);
            c2.addObserver(player2);
            c3.addObserver(player3);
            c4.addObserver(player4);
            player1.addObserver(controllerTurn);
            player2.addObserver(controllerTurn);
            player3.addObserver(controllerTurn);
            player4.addObserver(controllerTurn);

            playingConnection.put(c1, c2);
            playingConnection.put(c1, c3);
            playingConnection.put(c1, c4);
            playingConnection.put(c2, c1);
            playingConnection.put(c2, c3);
            playingConnection.put(c2, c4);
            playingConnection.put(c3, c1);
            playingConnection.put(c3, c2);
            playingConnection.put(c3, c4);
            playingConnection.put(c4, c1);
            playingConnection.put(c4, c2);
            playingConnection.put(c4, c3);
            waitingRoom4D.clear();

            model.startGame(easy);
            System.out.println("Game created");
        }
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        int connections = 0;
        System.out.println("Server is running");
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                connections++;
                System.out.println("Ready for the new connection - " + connections);
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }
}
