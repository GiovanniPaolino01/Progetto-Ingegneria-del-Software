package it.polimi.ingsw.NETWORK.CLIENT;

import it.polimi.ingsw.MODEL.*;
import it.polimi.ingsw.MODEL.CharacterCards.*;
import it.polimi.ingsw.MODEL.Exception.MissingTowerException;
import it.polimi.ingsw.NETWORK.ClientAppGUI;
import it.polimi.ingsw.NETWORK.MESSAGES.ClientAction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;


public  class LoginController {

    @FXML
    private TextField GAMEMODE_text_field;
    @FXML
    private TextField NICKNAME_field_text;
    @FXML
    private TextField NUMPLAYERS_text_field;

    @FXML
    private Label IP;
    @FXML
    private Label PORT;
    @FXML
    private Label WAIT;
    @FXML
    private TextField IP_text_field;
    @FXML
    private TextField PORT_text_field;

    private Stage stage = new Stage();
    private ActionEvent event = new ActionEvent();
    private Scene scene;
    private Parent root;

    private Boolean buttonIsClicked = false;
    private ClientAction action = ClientAction.STAI_FERMO;
    public LoginController() throws IOException {
    }

    /**
     * initialize the controller
     */
    @FXML
    public void initialize() {
        ClientModelGUI.setController(this);
    }

    /**
     * create a new client gui
     */
    private void callClient(){
        ClientCLI client = new ClientCLI("GUI");

        try {
            client.run(sendIP(), sendPort());
        }
        catch(Exception e){}
    }

    /**
     * allows me to go to lobby
     * @param e
     */
    @FXML
    protected void goToLobby(ActionEvent e) {
        this.event = e;
        setButtonIsClicked(true);
    }

    /**
     * when i click the button this function take IP and PORT and create a new client gui
     * @param event
     * @throws IOException
     */
    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {
        this.event = event;
        System.out.println(IP_text_field.getText());
        System.out.println(PORT_text_field.getText());

        callClient();
    }

    /**
     * allows to send the IP
     * @return
     */
    public String sendIP()  {
        String ip = IP_text_field.getText();
        return ip;
    }

    /**
     * allows to send the PORT
     * @return
     */
    public int sendPort(){
        int port = Integer.parseInt(PORT_text_field.getText());
        return port;
    }

    /**
     * allows to send the type of the game
     * @return
     */
    public String sendTypeGame()  {
        String typeGame = GAMEMODE_text_field.getText();
        return typeGame;
    }

    /**
     * allows to send the number of the players
     * @return
     */
    public String sendNumPlayers(){
        String numPlayers = NUMPLAYERS_text_field.getText();
        return numPlayers;
    }

    /**
     * allows to send the nickname
     * @return
     */
    public String sendNickname()  {
        String nick = NICKNAME_field_text.getText();
        setButtonIsClicked(false);

        return nick;
    }

    /**
     * change the page to login page
     */
    public void changeToLoginPage(){
        Platform.runLater(()->{
            try {
                if(ClientModelGUI.boardCreated){
                    stage = ClientModelGUI.stage;
                }else{
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    stage.setOnCloseRequest(e -> { Platform.exit(); System.exit(0); });
                }
                root = FXMLLoader.load(ClientAppGUI.class.getResource("inserimento_dati_giocatore.fxml"));
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }catch(Exception e){}

        });
    }

    /**
     * change the page to waiting page
     */
    public void changeToWaitingPage(){
        Platform.runLater(()-> {
            Parent root = null;
            try {
                root = FXMLLoader.load(ClientAppGUI.class.getResource("waitingroom.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
            }
        });
    }

    /**
     * change to disconnecition page
     */
    public void changeToDisconnectedPage(){
        Platform.runLater(()-> {
            try {
                //SETTO LO STAGE
                if(ClientModelGUI.boardCreated == true) {
                    stage = ClientModelGUI.stage;
                } else if(WAIT != null){
                    stage = (Stage) WAIT.getScene().getWindow();
                } else if(GAMEMODE_text_field != null){
                    stage = (Stage) GAMEMODE_text_field.getScene().getWindow();
                }else if(IP_text_field != null){
                    stage = (Stage) IP_text_field.getScene().getWindow();
                }

                BorderPane layout = new BorderPane();
                Text text = new Text("Sorry you have been disconnected");

                layout.setCenter(text);

                scene = new Scene(layout);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
            }
        });
    }

    /**
     * change to keep playing page
     */
    public void changeToKeepPlayingPage(){
        Platform.runLater(()-> {
            try {
                stage = ClientModelGUI.stage;
                BorderPane layout = new BorderPane();
                GridPane gp = new GridPane();

                EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event){
                        if(event.getTarget() instanceof ImageView){
                            ClientModelGUI.setActionPlayed(((ImageView)(event.getTarget())).getAccessibleText());
                            ClientModelGUI.setButtonIsClicked(true);
                            System.out.println("ho schiacciato un bottone");
                        }
                    }
                };


                Text text = new Text("Do you wanna keep playing?");
                gp.add(text, 1,1);

                Image img = new Image("it/polimi/ingsw/NETWORK/Images/si.jpg", 40, 40, false, true, true);
                ImageView view= new ImageView(img);
                view.setAccessibleText("yes");

                view.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

                gp.add(view, 0,2);

                img = new Image("it/polimi/ingsw/NETWORK/Images/no.jpg", 40, 40, false, true, true);;
                view = new ImageView(img);
                view.setAccessibleText("no");

                view.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

                gp.add(view, 2,2);

                //mostro chi ha vinto in cima
                for(Player p : ClientModelGUI.listPlayer){
                    if(p.getNicknameClient().equals(ClientModelGUI.nickname)){
                        if(p.getTeam().getColourTower().equals(ClientModelGUI.winner)){
                            text = new Text("You win the game");
                        } else{
                            text = new Text("You lost the game");
                        }
                    }
                }
                layout.setTop(text);


                gp.getColumnConstraints().add(new ColumnConstraints(50));
                gp.getColumnConstraints().add(new ColumnConstraints(150));
                gp.getColumnConstraints().add(new ColumnConstraints(50));

                gp.getRowConstraints().add(new RowConstraints(20));

                gp.setAlignment(Pos.CENTER);
                layout.setCenter(gp);

                scene = new Scene(layout);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
            }
        });
    }

    /**
     * change to board page
     */
    public void changeToBoard(){
        Platform.runLater(()->{

            if(ClientModelGUI.boardCreated == true) {
                stage = ClientModelGUI.stage;
            } else{
                stage = (Stage) WAIT.getScene().getWindow();
                ClientModelGUI.boardCreated = true;
                ClientModelGUI.stage = stage;
            }

            BorderPane layout = new BorderPane();

            GridPane topBoard;
            GridPane centerBoard;
            GridPane bottomBoard;
            GridPane rightBoard;
            HBox UpperBoard = new HBox();

            //FUNZIONE DI DRAG HANDLE(gestisce move mother nature, move student in dining room e move student in island)
            EventHandler<DragEvent> dragHandler = new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    if(dragEvent.getEventType().equals(dragEvent.DRAG_DONE)){
                        if (dragEvent.getTransferMode() == TransferMode.MOVE) {

                            if (ClientModelGUI.getCardThrown().equals("")) {
                                if (ClientModelGUI.getActionPlayed().substring(0, 6).equals("island")) {//MOVEMOTHER NATURE
                                    if (((ImageView) (dragEvent.getSource())).getAccessibleText().equals("mothernature")) {
                                        ClientModelGUI.setAction(ClientAction.PLAY_MOVE_MOTHERNATURE);
                                        ClientModelGUI.setButtonIsClicked(true);
                                    } else {                                                                 //MOVE_STUDENT_IN_ISLAND
                                        ClientModelGUI.setAction(ClientAction.PLAY_MOVE_STUDENT_IN_ISLAND);
                                        String num;
                                        if (ClientModelGUI.getActionPlayed().length() == 7) {
                                            num = ClientModelGUI.getActionPlayed().substring(6, 7);
                                        } else {
                                            num = ClientModelGUI.getActionPlayed().substring(6, 8);
                                        }
                                        ClientModelGUI.setActionPlayed2(num);
                                        ClientModelGUI.setActionPlayed(((ImageView) (dragEvent.getSource())).getAccessibleText());
                                        ClientModelGUI.setButtonIsClicked(true);
                                    }

                                } else if (ClientModelGUI.getActionPlayed().substring(0, 9).equals("dashboard")) {//MOVE_STUDENT_IN_DININGROOM
                                    if (!((ImageView) (dragEvent.getSource())).getAccessibleText().equals("mothernature")) {
                                        ClientModelGUI.setActionPlayed(((ImageView) (dragEvent.getSource())).getAccessibleText());
                                        ClientModelGUI.setAction(ClientAction.PLAY_MOVE_STUDENT_IN_DININGROOM);
                                        ClientModelGUI.setButtonIsClicked(true);

                                    }
                                }
                                //System.out.println(((ImageView) (dragEvent.getSource())).getAccessibleText());
                                //System.out.println(dragEvent.getTarget());

                            }if(ClientModelGUI.getCardThrown().equals("Priest")){
                                String student = ((ImageView)(dragEvent.getTarget())).getAccessibleText();
                                if(student.substring(0,1).equals("c")) {
                                    String parameter = student.substring(1, student.length());

                                    ClientModelGUI.setAction(ClientAction.PLAY_CHARACTERCARD);

                                    if(ClientModelGUI.getActionPlayed().length() == 7){
                                        ClientModelGUI.setActionPlayed3(ClientModelGUI.getActionPlayed().substring(6,7));
                                    }else{
                                        ClientModelGUI.setActionPlayed3(ClientModelGUI.getActionPlayed().substring(6,8));
                                    }

                                    ClientModelGUI.setActionPlayed2(parameter);
                                    ClientModelGUI.setActionPlayed("Priest");
                                    ClientModelGUI.setButtonIsClicked(true);

                                    ClientModelGUI.setCardThrown("");
                                }
                            }
                        }
                    }
                }
            };

            //FUNZIONE DI EVEN HANDLE   (take cloud, charactercard activation o tutto l'effetto di satyr,PostMan e Knight)
            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    //STO GIOCANDO UN'AZIONE NORMALE
                    if(ClientModelGUI.getCardThrown().equals("")) {
                        if (e.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                            if (e.getTarget() instanceof GridPane) {
                                if (((GridPane) (e.getTarget())).getAccessibleText().substring(0, 5).equals("cloud")) {
                                    ClientModelGUI.setAction(ClientAction.PLAY_TAKE_CLOUD);
                                    ClientModelGUI.setButtonIsClicked(true);

                                    ClientModelGUI.setActionPlayed(((GridPane) (e.getTarget())).getAccessibleText());
                                }
                                else if (((GridPane) (e.getTarget())).getAccessibleText().equals("Satyr")) {
                                    ClientModelGUI.setAction(ClientAction.PLAY_CHARACTERCARD);
                                    ClientModelGUI.setButtonIsClicked(true);
                                    ClientModelGUI.setActionPlayed("Satyr");
                                }
                                else if (((GridPane) (e.getTarget())).getAccessibleText().equals("PostMan")) {
                                    ClientModelGUI.setAction(ClientAction.PLAY_CHARACTERCARD);
                                    ClientModelGUI.setButtonIsClicked(true);
                                    ClientModelGUI.setActionPlayed("PostMan");
                                }
                                else if (((GridPane) (e.getTarget())).getAccessibleText().equals("Knight")) {
                                    ClientModelGUI.setAction(ClientAction.PLAY_CHARACTERCARD);
                                    ClientModelGUI.setButtonIsClicked(true);
                                    ClientModelGUI.setActionPlayed("Knight");
                                }
                                else if (((GridPane) (e.getTarget())).getAccessibleText().equals("Priest")) {
                                    ClientModelGUI.setCardThrown("Priest");
                                }
                                else if (((GridPane) (e.getTarget())).getAccessibleText().equals("Woman")) {
                                    ClientModelGUI.setCardThrown("Woman");
                                }
                                else if (((GridPane) (e.getTarget())).getAccessibleText().equals("Pirate")) {
                                    ClientModelGUI.setCardThrown("Pirate");
                                }
                                else if(((GridPane) (e.getTarget())).getAccessibleText().equals("Jester")){
                                    ClientModelGUI.setCardThrown("Jester");
                                    ClientModelGUI.setColors1(0);
                                    ClientModelGUI.setColors2(0);
                                }
                                else if(((GridPane) (e.getTarget())).getAccessibleText().equals("Minstrell")) {
                                    ClientModelGUI.setCardThrown("Minstrell");
                                    ClientModelGUI.setColors1(0);
                                    ClientModelGUI.setColors2(0);
                                }
                                else if (((GridPane) (e.getTarget())).getAccessibleText().substring(0, 6).equals("island")) {
                                }
                            } else if (e.getTarget() instanceof ImageView) {
                                //System.out.println(((ImageView) (e.getTarget())).getAccessibleText());
                                if(((ImageView) (e.getTarget())).getAccessibleText().length() >= 4) {
                                    if (((ImageView) (e.getTarget())).getAccessibleText().substring(0, 4).equals("card")) {
                                        ClientModelGUI.setAction(ClientAction.PLAY_CARD);
                                        ClientModelGUI.setButtonIsClicked(true);
                                        ClientModelGUI.setActionPlayed(((ImageView) (e.getTarget())).getAccessibleText());
                                    }
                                }
                            }
                        }
                    }

                    //HO ATTIVATO UNA CARTA QUINDI O ESEGUO L'AZIONE O SI ANNULLA
                    else{
                        if(e.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                            if (ClientModelGUI.getCardThrown().equals("Woman")) {

                                if(e.getTarget() instanceof ImageView){
                                    String student = ((ImageView)(e.getTarget())).getAccessibleText();
                                    if(student.substring(0,1).equals("c")){
                                        String parameter = student.substring(1,student.length());

                                        ClientModelGUI.setAction(ClientAction.PLAY_CHARACTERCARD);
                                        ClientModelGUI.setButtonIsClicked(true);
                                        ClientModelGUI.setActionPlayed("Woman");
                                        ClientModelGUI.setActionPlayed2(parameter);
                                    }
                                }

                                System.out.println("resettato");
                                ClientModelGUI.setCardThrown("");

                            }
                            else if (ClientModelGUI.getCardThrown().equals("Pirate")) {
                                if (e.getTarget() instanceof GridPane) {
                                    if (((GridPane) (e.getTarget())).getAccessibleText().length() > 6) {
                                        if (((GridPane) (e.getTarget())).getAccessibleText().substring(0, 6).equals("island")) {
                                            ClientModelGUI.setAction(ClientAction.PLAY_CHARACTERCARD);
                                            ClientModelGUI.setButtonIsClicked(true);
                                            ClientModelGUI.setActionPlayed("Pirate");

                                            String num;
                                            if (((GridPane) (e.getTarget())).getAccessibleText().length() == 7) {
                                                num = ((GridPane) (e.getTarget())).getAccessibleText().substring(6, 7);
                                            } else {
                                                num = ((GridPane) (e.getTarget())).getAccessibleText().substring(6, 8);
                                            }
                                            ClientModelGUI.setActionPlayed2(num);
                                        }
                                    }
                                }
                                ClientModelGUI.setCardThrown("");
                            }
                            else if (ClientModelGUI.getCardThrown().equals("Jester")) {
                                if(e.getTarget() instanceof ImageView){
                                    String student = ((ImageView)(e.getTarget())).getAccessibleText();
                                    System.out.println(student);
                                    if(student.substring(0,1).equals("j")){
                                        String parameter = student.substring(1,student.length());

                                        if(ClientModelGUI.getColors1() == 0) {
                                            ClientModelGUI.setActionPlayed2(parameter);
                                            ClientModelGUI.setColors1(1);
                                        }else if(ClientModelGUI.getColors1() == 1){
                                            ClientModelGUI.setActionPlayed4(parameter);
                                            ClientModelGUI.setColors1(2);
                                        }else if(ClientModelGUI.getColors1() == 2) {
                                            ClientModelGUI.setActionPlayed6(parameter);
                                            ClientModelGUI.setColors1(3);
                                        }
                                        System.out.println("aggiunto colore pool, "+ ClientModelGUI.getColors1());
                                    }else if(!student.substring(0,1).equals("c") && !student.substring(0, 1).equals("d")){
                                        if(ClientModelGUI.getColors2() == 0) {
                                            ClientModelGUI.setActionPlayed3(student);
                                            ClientModelGUI.setColors2(1);
                                        }else if(ClientModelGUI.getColors2() == 1){
                                            ClientModelGUI.setActionPlayed5(student);
                                            ClientModelGUI.setColors2(2);
                                        }else if(ClientModelGUI.getColors2() == 2) {
                                            ClientModelGUI.setActionPlayed7(student);
                                            ClientModelGUI.setColors2(3);
                                        }
                                        System.out.println("aggiunto colore entrance, " + ClientModelGUI.getColors2());
                                    }
                                } else if(e.getTarget() instanceof GridPane){
                                    System.out.println("sto per inviare: " + ((GridPane) (e.getTarget())).getAccessibleText());
                                    if(((GridPane) (e.getTarget())).getAccessibleText().equals("Jester")) {
                                        if (ClientModelGUI.getColors2() != 0 && ClientModelGUI.getColors1() != 0) {

                                            ClientModelGUI.setActionPlayed("Jester");
                                            ClientModelGUI.setButtonIsClicked(true);
                                            ClientModelGUI.setAction(ClientAction.PLAY_CHARACTERCARD);

                                            if (ClientModelGUI.getColors1() > ClientModelGUI.getColors2()) {
                                                ClientModelGUI.setActionPlayed8(String.valueOf(ClientModelGUI.getColors2()));
                                            } else {
                                                ClientModelGUI.setActionPlayed8(String.valueOf(ClientModelGUI.getColors1()));
                                            }

                                            System.out.println("inviato");
                                            ClientModelGUI.setCardThrown("");
                                        }
                                    }

                                }
                            } else if(ClientModelGUI.getCardThrown().equals("Minstrell")) {
                                if (e.getTarget() instanceof ImageView) {
                                    String student = ((ImageView) (e.getTarget())).getAccessibleText();
                                    if (student.substring(0, 1).equals("d")) {
                                        String parameter = student.substring(1, student.length());

                                        if (ClientModelGUI.getColors1() == 0) {
                                            ClientModelGUI.setActionPlayed2(parameter);
                                            ClientModelGUI.setColors1(1);
                                        } else if (ClientModelGUI.getColors1() == 1) {
                                            ClientModelGUI.setActionPlayed3(parameter);
                                            ClientModelGUI.setColors1(2);
                                        }
                                        System.out.println("aggiunto colore diningroom, " + ClientModelGUI.getColors1());
                                    } else if (!student.substring(0, 1).equals("c") && !student.substring(0,1).equals("j")) {
                                        if (ClientModelGUI.getColors2() == 0) {
                                            ClientModelGUI.setActionPlayed4(student);
                                            ClientModelGUI.setColors2(1);
                                        } else if (ClientModelGUI.getColors2() == 1) {
                                            ClientModelGUI.setActionPlayed5(student);
                                            ClientModelGUI.setColors2(2);
                                        }
                                        System.out.println("aggiunto colore entrance, " + ClientModelGUI.getColors2());
                                    }
                                }else if(e.getTarget() instanceof GridPane){
                                    System.out.println("sto per inviare: " + ((GridPane) (e.getTarget())).getAccessibleText());
                                    if(((GridPane) (e.getTarget())).getAccessibleText().equals("Minstrell")) {
                                        if (ClientModelGUI.getColors2() != 0 && ClientModelGUI.getColors1() != 0) {

                                            ClientModelGUI.setActionPlayed("Minstrell");
                                            ClientModelGUI.setButtonIsClicked(true);
                                            ClientModelGUI.setAction(ClientAction.PLAY_CHARACTERCARD);

                                            if (ClientModelGUI.getColors1() > ClientModelGUI.getColors2()) {
                                                ClientModelGUI.setActionPlayed8(String.valueOf(ClientModelGUI.getColors2()));
                                            } else {
                                                ClientModelGUI.setActionPlayed8(String.valueOf(ClientModelGUI.getColors1()));
                                            }

                                            System.out.println("inviato");
                                            ClientModelGUI.setCardThrown("");
                                        }
                                    }

                                }
                            }
                        }
                    }
                }};


            //FUNZIONE DI EVENT HANDLE  (invio carte assistente)
            EventHandler<ActionEvent> actionHandler =
                    new EventHandler<ActionEvent>(){

                        public void handle(ActionEvent t) {
                            if(((Button)(t.getTarget())).getText().substring(0,4).equals("card")){
                                //System.out.println(((Button)(t.getTarget())).getText());
                                ClientModelGUI.setAction(ClientAction.PLAY_CARD);
                                ClientModelGUI.setButtonIsClicked(true);

                                ClientModelGUI.setActionPlayed(((Button)(t.getTarget())).getText());
                            }
                            else{
                                //System.out.println(((Button)(t.getTarget())).getText());
                            }
                            //System.out.println(t.getEventType().getName());
                        }};


            //LAYOUT
            layout.setStyle("-fx-background-color:#267bf1");

            //TOP BOARD
            topBoard = showTopBoard();
            topBoard.setAlignment(Pos.CENTER);

            //CENTER BOARD
            centerBoard = showCenterBoard(eventHandler, dragHandler);
            //centerBoard.setStyle("-fx-background-color: #C0C0C0;");
            centerBoard.setAlignment(Pos.CENTER);

            //BOTTOM BOARD
            bottomBoard = showBottomBoard(eventHandler, dragHandler);
            bottomBoard.setAlignment(Pos.CENTER);

            //RIGHT BOARD
            rightBoard = showRightBoard(eventHandler, dragHandler);
            rightBoard.setAlignment(Pos.CENTER);

            //ADDING EVERYTHING TO THE BOARD
            layout.setTop(topBoard);
            layout.setRight(rightBoard);
            layout.setBottom(bottomBoard);
            layout.setCenter(centerBoard);

            scene = new Scene(layout);
            stage.setScene(scene);
            stage.show();
        });
    }


    /**
     * shows the top of the board
     * @return
     */
    public GridPane showTopBoard(){
        GridPane topBoard = new GridPane();
        topBoard.setStyle("-fx-background-color:#FFFFFF;");
        Text t;
        if(ClientModelGUI.nickname.equals(ClientModelGUI.currentPlayer)) {

            t = new Text("it's your turn");
        }else{

            t = new Text("wait for your turn");
        }
        t.setStyle("-fx-font: 15 Arial;");
        topBoard.add(t,0,1);

        return topBoard;
    }


    /**
     * shows the bottom of the board
     * @return
     */
    public GridPane showBottomBoard(EventHandler<? super MouseEvent> eventHandler, EventHandler<? super DragEvent> dragHandler){
        GridPane bottomBoard = new GridPane();
        int width;
        int heigth;
        Image img;
        Player p;
        try{
            p = ClientModelGUI.getPlayer(ClientModelGUI.nickname);

            //PLANCIA
            GridPane gpane = createDashBoard(2, ClientModelGUI.nickname, dragHandler, eventHandler, true);
            bottomBoard.add(gpane, 0,0);


            //CARTE (solo del giocatore corrente)
            GridPane cardsRow = new GridPane();
            cardsRow.getRowConstraints().add(new RowConstraints(105));
            cardsRow.getRowConstraints().add(new RowConstraints(40));

            width = 65;
            heigth = 75;

            for (int i = 1; i <= p.getDeck().size(); i++) {
                String address = getAddressCard(p.getDeck().getCardOfIndex(i - 1).getValue());
                img = new Image(address, width, heigth, false, true, true);
                ImageView view = new ImageView(img);
                view.setAccessibleText("card" + p.getDeck().getCardOfIndex(i - 1).getValue());
                view.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

                cardsRow.add(view, i, 0);
            }


            //COINS
            if(ClientModelGUI.showCoins){
                String address = "it/polimi/ingsw/NETWORK/Images/Moneta_base.png" ;

                img = new Image(address, 40, 40, false, true, true);
                ImageView view = new ImageView(img);

                Text text = new Text(String.valueOf(p.getNumCoins()));
                text.setTabSize(20);
                cardsRow.add(view, 1,1);
                cardsRow.add(text, 2,1);
            }


            bottomBoard.add(cardsRow, 2,0);

        }catch(Exception e){}

        //LAYOUT CONSTRAINTS
        bottomBoard.getColumnConstraints().add(new ColumnConstraints(440));
        bottomBoard.getColumnConstraints().add(new ColumnConstraints(20));

        return bottomBoard;
    }

    /**
     * shows the center of the board
     * @return
     */
    public GridPane showCenterBoard(EventHandler<? super MouseEvent> eventHandler, EventHandler<? super DragEvent> dragHandler){
        GridPane centerBoard = new GridPane();
        Button button;

        //ISOLE
        int i, sub;
        GridPane pane;

        i = ClientModelGUI.listIsland.get(0).getNumIsland();
        sub = ClientModelGUI.listIsland.get(0).getNumSubIsland();


        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 1, 0);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata

            String address = "it/polimi/ingsw/NETWORK/Images/ponte_orizzontale.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            centerBoard.add(view, 2, 0);
        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 3, 0);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_orizzontale.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            centerBoard.add(view, 4, 0);

        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 5, 0);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_orizzontale.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            centerBoard.add(view, 6, 0);
        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 7, 0);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata (angolo alto dx)
            GridPane gpane = new GridPane();
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_obliquo_bello.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            GridPane.setValignment(view, VPos.BOTTOM);

            centerBoard.add(view, 8, 0);

        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 8, 1);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_verticale.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            centerBoard.add(view, 8, 2);

        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 8, 3);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata (angolo basso dx)
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_obliquo_bello.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            view.setRotate(90);
            GridPane.setValignment(view, VPos.TOP);

            centerBoard.add(view, 8, 4);
        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 7, 4);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_orizzontale.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            centerBoard.add(view, 6, 4);
        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 5, 4);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_orizzontale.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            centerBoard.add(view, 4, 4);
        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 3, 4);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_orizzontale.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            centerBoard.add(view, 2, 4);
        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 1, 4);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata (angolo basso sx)
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_obliquo_bello2.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            view.setRotate(270);
            GridPane.setValignment(view, VPos.TOP);

            centerBoard.add(view, 0, 4);
        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 0, 3);
        sub = sub -1;

        if(sub == 0){
            i = ClientModelGUI.listIsland.get(i+1).getNumIsland();
            sub = ClientModelGUI.listIsland.get(i).getNumSubIsland();
        }else{
            //stampo il ponte nella posizione indicata
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_verticale.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            centerBoard.add(view, 0, 2);
        }

        pane = createIsland(i, sub, eventHandler, dragHandler);
        centerBoard.add(pane, 0, 1);
        sub = sub -1;

        if(sub == 0){
        }else{
            //stampo il ponte nella posizione indicata (angolo alto sx)
            String address = "it/polimi/ingsw/NETWORK/Images/ponte_obliquo_bello2.png";
            Image img = new Image(address, 70, 70, false, true, true);
            ImageView view = new ImageView(img);
            GridPane.setValignment(view, VPos.BOTTOM);

            centerBoard.add(view, 0, 0);
        }

        //NUVOLE
        GridPane gpane = createCloud(0, eventHandler);

        centerBoard.add(gpane, 1, 1);

        gpane = createCloud(1, eventHandler);
        centerBoard.add(gpane, 3, 1);

        if(ClientModelGUI.listCloud.size() >= 3){
            gpane = createCloud(2, eventHandler);
            centerBoard.add(gpane, 5, 1);

            if(ClientModelGUI.listCloud.size() == 4){
                gpane = createCloud(3, eventHandler);
                centerBoard.add(gpane, 7, 1);
            }
        }

        //CHARACTER CARD
        int x = 1;
        if(ClientModelGUI.characterCards.size() != 0) {
            for (int j = 0; j < ClientModelGUI.characterCards.size(); j++) {
                GridPane gp = createCharacterCard(ClientModelGUI.characterCards.get(j).getNameCard(), j, eventHandler, dragHandler);
                centerBoard.add(gp, x+j * 2, 3);
            }
        }

        //LAYOUT CONSTRAINTS
        centerBoard.getColumnConstraints().add(new ColumnConstraints(100));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(80));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(50));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(80));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(50));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(80));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(50));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(100));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(100));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(30));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(100));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(30));
        centerBoard.getColumnConstraints().add(new ColumnConstraints(100));

        centerBoard.getRowConstraints().add(new RowConstraints(100));
        centerBoard.getRowConstraints().add(new RowConstraints(100));
        centerBoard.getRowConstraints().add(new RowConstraints(30));
        centerBoard.getRowConstraints().add(new RowConstraints(100));
        centerBoard.getRowConstraints().add(new RowConstraints(100));

        return centerBoard;
    }

    /**
     * shows the right of the board
     * @return
     */
    private GridPane showRightBoard(EventHandler<? super MouseEvent> eventHandler, EventHandler<? super DragEvent> dragHandler){
        GridPane rightBoard = new GridPane();
        int j = 0;
        for(Player p : ClientModelGUI.listPlayer){
            if(!p.getNicknameClient().equals(ClientModelGUI.nickname)){
                j++;

                Text t = new Text(p.getNicknameClient());
                GridPane dashboard = createDashBoard(1, p.getNicknameClient(), dragHandler, eventHandler, false);

                rightBoard.add(t, 1, j*2);
                rightBoard.add(dashboard,1, 1+j*2);
            }
        }
        rightBoard.getColumnConstraints().add(new ColumnConstraints(10));
        rightBoard.getColumnConstraints().add(new ColumnConstraints(220));
        rightBoard.getColumnConstraints().add(new ColumnConstraints(10));

        rightBoard.getRowConstraints().add(new RowConstraints(0));
        rightBoard.getRowConstraints().add(new RowConstraints(110));
        rightBoard.getRowConstraints().add(new RowConstraints(0));
        rightBoard.getRowConstraints().add(new RowConstraints(110));
        rightBoard.getRowConstraints().add(new RowConstraints(0));
        rightBoard.getRowConstraints().add(new RowConstraints(110));

        return rightBoard;
    }

    /**
     * create island on the board
     * @param i
     * @param sub
     * @param eventHandler
     * @param dragHandler
     * @return
     */
    private GridPane createIsland(int i, int sub, EventHandler<? super MouseEvent> eventHandler, EventHandler<? super DragEvent> dragHandler) {
        GridPane pane = new GridPane();

        //funzioni per drag and drop con mother nature
        pane.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    ClientModelGUI.setActionPlayed(((GridPane)event.getTarget()).getAccessibleText());
                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            }
        });

        pane.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getGestureSource() != pane && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        pane.setMinSize(100,100);
        pane.setMaxSize(100,100);

        pane.setAccessibleText("island"+i);

        Image img = new Image("it/polimi/ingsw/NETWORK/Images/island1.png", pane.getWidth(), pane.getHeight(), false, true, true);

        BackgroundImage bimage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(pane.getWidth(), pane.getHeight(), true, true, true, false));
        Background backGround = new Background(bimage);
        pane.setBackground(backGround);

        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

        pane.getColumnConstraints().add(new ColumnConstraints(15));
        pane.getRowConstraints().add(new RowConstraints(20));

        //SETTO GLI STUDENTI
        if(sub > 1){}
        else {
            int width = 10;
            int heigth = 10;

            img = new Image("it/polimi/ingsw/NETWORK/Images/student_green.png", width, heigth, false, true, true);
            ImageView view = new ImageView(img);
            pane.add(view, 1, 1);

            img = new Image("it/polimi/ingsw/NETWORK/Images/student_red.png", width, heigth, false, true, true);
            view = new ImageView(img);
            pane.add(view, 1, 2);

            img = new Image("it/polimi/ingsw/NETWORK/Images/student_yellow.png", width, heigth, false, true, true);
            view = new ImageView(img);
            pane.add(view, 1, 3);

            img = new Image("it/polimi/ingsw/NETWORK/Images/student_pink.png", width, heigth, false, true, true);
            view = new ImageView(img);
            pane.add(view, 3, 1);

            img = new Image("it/polimi/ingsw/NETWORK/Images/student_blue.png", width, heigth, false, true, true);
            view = new ImageView(img);
            pane.add(view, 3, 2);

            if (ClientModelGUI.listIsland.get(i).getHasMotherNature()) {
                img = new Image("it/polimi/ingsw/NETWORK/Images/mother_nature.png", width * 2, heigth * 2, false, true, true);
                view = new ImageView(img);
                view.setAccessibleText("mothernature");

                view.addEventFilter(DragEvent.DRAG_DONE, dragHandler);

                view.setOnDragDetected(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent e) {
                        Dragboard db = ((ImageView) (e.getTarget())).startDragAndDrop(TransferMode.ANY);

                        ClipboardContent content = new ClipboardContent();
                        content.putString(((ImageView) (e.getTarget())).getAccessibleText());
                        db.setContent(content);

                        event.consume();
                    }
                });

                pane.add(view, 5, 1);
            }


            int j = 1;
            for (Colour c : Colour.values()) {
                Text text = new Text();
                text.minHeight(10);
                text.minWidth(10);
                text.setText(String.valueOf(ClientModelGUI.listIsland.get(i).countStudentsOfColour(c)));
                if (j < 4) pane.add(text, 2, j);
                else pane.add(text, 4, j - 3);
                j++;
            }

        }
        //SETTING TOWERS
        String address = "";
        try {
            address = getAddressTower(ClientModelGUI.listIsland.get(i).getColourTower());
        } catch (MissingTowerException e) {
            e.printStackTrace();
        }
        if(address!="") {
            img = new Image(address, 20, 20, false, true, true);
            ImageView view = new ImageView(img);
            pane.add(view, 6, 2);
        }

        return pane;
    }

    /**
     * create island on the board
     * @param i
     * @param eventHandler
     * @return
     */
    private GridPane createCloud(int i, EventHandler<? super MouseEvent> eventHandler){
        GridPane gpane = new GridPane();

        gpane.setMinHeight(100);
        gpane.setMinWidth(100);

        gpane.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

        gpane.setAccessibleText("cloud" + i);

        //SELEZIONO L'IMMAGINE DELLA NUVOLA CORRETTA
        String address = "it/polimi/ingsw/NETWORK/Images/cloud_card_1.png";
        if(ClientModelGUI.listCloud.size() == 3){
            address = "it/polimi/ingsw/NETWORK/Images/cloud_card_5_2_and_4_players_game.png";
        }else {
            if (i == 1) {
                address = "it/polimi/ingsw/NETWORK/Images/cloud_card_2.png";
            } else if (i == 2) {
                address = "it/polimi/ingsw/NETWORK/Images/cloud_card_3.png";
            } else if (i == 3) {
                address = "it/polimi/ingsw/NETWORK/Images/cloud_card_4.png";
            }
        }

        Image img = new Image(address, gpane.getWidth(), gpane.getHeight(), false, true, true);

        BackgroundImage bimage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(gpane.getWidth(), gpane.getHeight(), true, true, true, false));
        Background backGround = new Background(bimage);
        gpane.setBackground(backGround);

        //AGGIUNGO GLI STUDENTI ALLA NUVOLA
        int width = 30;
        int height = 30;

        //CASO 3 GIOCATORI
        if(ClientModelGUI.listCloud.size() == 3){

            gpane.getRowConstraints().add(new RowConstraints(height));
            gpane.getRowConstraints().add(new RowConstraints(height));
            gpane.getRowConstraints().add(new RowConstraints(height));
            gpane.getColumnConstraints().add(new ColumnConstraints(height));
            gpane.getColumnConstraints().add(new ColumnConstraints(width));
            gpane.getColumnConstraints().add(new ColumnConstraints(width));

            if (!ClientModelGUI.listCloud.get(i).empty()) {
                for (int j = 0; j < 4; j++) {
                    if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.BLUE)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_blue.png";
                    } else if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.RED)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_red.png";
                    } else if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.YELLOW)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_yellow.png";
                    } else if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.GREEN)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_green.png";
                    } else if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.PINK)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_pink.png";
                    }

                    img = new Image(address, width, height, false, true, true);
                    ImageView view = new ImageView(img);
                    int y = 1;
                    int x = 0;
                    if(j == 1){
                        x = 1;
                        y = 0;
                    }else if(j == 2){
                        x = 1;
                        y = 2;
                    }else if(j == 3){
                        x = 2;
                    }
                    gpane.add(view, x, y);
                }
            }
        }

        //CASO 2 O 4 GIOCATORI
        else {

            gpane.getRowConstraints().add(new RowConstraints(10));
            gpane.getRowConstraints().add(new RowConstraints(height - 10));
            gpane.getRowConstraints().add(new RowConstraints(height - 5));
            gpane.getColumnConstraints().add(new ColumnConstraints(5));
            gpane.getColumnConstraints().add(new ColumnConstraints(width + 5));
            gpane.getColumnConstraints().add(new ColumnConstraints(width - 25));

            if (!ClientModelGUI.listCloud.get(i).empty()) {
                for (int j = 0; j < 3; j++) {
                    if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.BLUE)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_blue.png";
                    } else if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.RED)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_red.png";
                    } else if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.YELLOW)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_yellow.png";
                    } else if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.GREEN)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_green.png";
                    } else if (ClientModelGUI.listCloud.get(i).getStudent(j).getColour().equals(Colour.PINK)) {
                        address = "it/polimi/ingsw/NETWORK/Images/student_pink.png";
                    }

                    img = new Image(address, width, height, false, true, true);
                    ImageView view = new ImageView(img);

                    int y = 2;
                    if (j == 1) {
                        y = 3;
                    } else if (j == 2) {
                        y = 1;
                    }
                    gpane.add(view, j + 1, y);
                }
            }
        }

        return gpane;
    }

    /**
     * create island on the board
     * @param factor
     * @param client
     * @param dragHandler
     * @param eventHandler
     * @param drag
     * @return
     */
    private GridPane createDashBoard(int factor, String client, EventHandler<? super DragEvent> dragHandler, EventHandler<? super MouseEvent> eventHandler , boolean drag){

        //PLANCIA
        GridPane gpane = new GridPane();

        //setto il drag event solo se è la mia board
        if(drag) {
            gpane.setAccessibleText("dashboard");
            gpane.setOnDragDropped(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {

                    Dragboard db = event.getDragboard();
                    boolean success = false;

                    if (db.hasString()) {
                        ClientModelGUI.setActionPlayed("dashboard");
                        success = true;
                    }

                    event.setDropCompleted(success);
                    event.consume();
                }
            });
            gpane.setOnDragOver(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {
                    if (event.getGestureSource() != gpane && event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                }
            });
        }

        gpane.setMinSize(220*factor, 110*factor);
        gpane.setMaxSize(220*factor, 110*factor);

        Image img = new Image("it/polimi/ingsw/NETWORK/Images/PLANCIA GIOCO_2.png", gpane.getWidth(), gpane.getHeight(), false, true, true);
        BackgroundImage bimage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(gpane.getWidth(), gpane.getHeight(), true, true, true, false));
        Background backGround = new Background(bimage);
        gpane.setBackground(backGround);

        gpane.getColumnConstraints().add(new ColumnConstraints(5*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(5*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));

        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(4*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(15*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(1*factor));
        gpane.getColumnConstraints().add(new ColumnConstraints(10*factor));

        gpane.getRowConstraints().add(new RowConstraints(20*factor));
        gpane.getRowConstraints().add(new RowConstraints(10*factor));
        gpane.getRowConstraints().add(new RowConstraints(5*factor));
        gpane.getRowConstraints().add(new RowConstraints(10*factor));
        gpane.getRowConstraints().add(new RowConstraints(5*factor));
        gpane.getRowConstraints().add(new RowConstraints(10*factor));
        gpane.getRowConstraints().add(new RowConstraints(5*factor));
        gpane.getRowConstraints().add(new RowConstraints(10*factor));
        gpane.getRowConstraints().add(new RowConstraints(5*factor));
        gpane.getRowConstraints().add(new RowConstraints(10*factor));


        int width = 10*factor;
        int heigth = 10*factor;

        for(int i = 0; i < ClientModelGUI.listPlayer.size(); i++){

            if(ClientModelGUI.listPlayer.get(i).getNicknameClient().equals(client)){
                //ENTRANCE
                int y = 1;
                int x = 3;
                for(int j = 0; j < ClientModelGUI.listPlayer.get(i).getEntrance().getStudentGroup().size(); j++) {

                    if(j % 2 != 0){
                        x = 1;
                        y = y + 2;
                    }else{
                        x = 3;
                    }

                    String address = getAddress(ClientModelGUI.listPlayer.get(i).getEntrance().getStudentGroup().get(j).getColour());
                    img = new Image(address, width, heigth, false, true, true);
                    ImageView view = new ImageView(img);

                    if(drag) {
                        view.setAccessibleText(ClientModelGUI.colourToString(ClientModelGUI.listPlayer.get(i).getEntrance().getStudentGroup().get(j).getColour()));
                        view.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
                        view.addEventFilter(DragEvent.DRAG_DONE, dragHandler);
                        view.setOnDragDetected(new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent e) {
                                Dragboard db = ((ImageView) (e.getTarget())).startDragAndDrop(TransferMode.ANY);

                                //System.out.println("drag detected");

                                ClipboardContent content = new ClipboardContent();
                                content.putString(((ImageView) (e.getTarget())).getAccessibleText());
                                db.setContent(content);

                                event.consume();
                            }
                        });
                    }
                    gpane.add(view, x, y);
                }

                //DINING ROOM
                y = 1;
                for(Colour c : Colour.values()) {
                    x = 5;
                    for (int j = 0; j < ClientModelGUI.listPlayer.get(i).numStudentsDiningRoom(c); j++) {
                        String address = getAddress(c);
                        img = new Image(address, width, heigth, false, true, true);
                        ImageView view = new ImageView(img);

                        view.setAccessibleText("d" + ClientModelGUI.colourToString(c));
                        view.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

                        gpane.add(view, x, y);
                        x = x + 2;
                    }
                    y = y + 2;
                }

                //PROFESSORS
                y = 1;
                for(Colour c : Colour.values()) {
                    x = 25;
                    if(ClientModelGUI.listPlayer.get(i).professorPresent(c)) {
                        String address = getAddressProfessor(c);
                        img = new Image(address, width, heigth, false, true, true);
                        ImageView view = new ImageView(img);
                        view.setAccessibleText(ClientModelGUI.colourToString(c));
                        gpane.add(view, x, y);
                    }
                    y = y + 2;
                }

                //TOWERS
                y = 1;
                x = 27;
                ColourTower colourTower = ClientModelGUI.listPlayer.get(i).getTeam().getColourTower();
                for(int j = 0; j < ClientModelGUI.listPlayer.get(i).getTeam().getNumberOfTower(); j++) {

                    String address = getAddressTower(colourTower);
                    img = new Image(address, width, heigth, false, true, true);
                    ImageView view = new ImageView(img);
                    //view.setAccessibleText(ClientModelGUI.colourTowerToString(colourTower));
                    gpane.add(view, x, y);

                    if(j%2 == 1) {
                        x = 27;
                        y = y + 2;
                    }else{
                        x = x + 2;
                    }

                }
            }
        }

        return gpane;
    }

    /**
     * create island on the board
     * @param name
     * @param k
     * @param eventHandler
     * @param dragHandler
     * @return
     */
    private GridPane createCharacterCard(String name, int k,EventHandler<? super MouseEvent> eventHandler, EventHandler<? super DragEvent> dragHandler){
        GridPane gp = new GridPane();

        Image img = new Image(getAddressCharacter(name), 65, 120, false, true, true);

        BackgroundImage bimage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(gp.getWidth(), gp.getHeight(), true, true, true, false));
        Background backGround = new Background(bimage);
        gp.setBackground(backGround);

        gp.setAccessibleText(name);

        //setto gli studenti presenti sulla carta se ce ne sono
        ImageView view;

        if(name.equals("Priest")){
            Priest priest = (Priest)(ClientModelGUI.characterCards.get(k));

            for(int j = 0; j < priest.getPool().size(); j++){
                img = new Image(getAddress(priest.getPool().get(j).getColour()), 20, 20, false, true, true);
                view = new ImageView(img);

                view.setAccessibleText("c"+ClientModelGUI.colourToString(priest.getPool().get(j).getColour()));
                view.addEventFilter(DragEvent.DRAG_DONE, dragHandler);
                view.setOnDragDetected(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent e) {
                        Dragboard db = ((ImageView)(e.getTarget())).startDragAndDrop(TransferMode.ANY);

                        ClipboardContent content = new ClipboardContent();
                        content.putString(((ImageView)(e.getTarget())).getAccessibleText());
                        db.setContent(content);

                        event.consume();
                    }
                });

                gp.add(view, 1,j);
            }
        }
        else if(name.equals("Woman")){
            Woman woman = (Woman)(ClientModelGUI.characterCards.get(k));

            for(int j = 0; j < woman.getPool().size(); j++){
                img = new Image(getAddress(woman.getPool().get(j).getColour()), 20, 20, false, true, true);
                view = new ImageView(img);

                view.setAccessibleText("c"+ClientModelGUI.colourToString(woman.getPool().get(j).getColour()));
                view.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

                gp.add(view, 1,j);
            }
        }
        else if(name.equals("Jester")){
            ConcreteCharacterCard cha = ClientModelGUI.characterCards.get(k);
            Jester jester = (Jester)cha;

            for(int j = 0; j < jester.getPool().size(); j++){
                img = new Image(getAddress(jester.getPool().get(j).getColour()), 20, 20, false, true, true);
                view = new ImageView(img);

                view.setAccessibleText("j"+ClientModelGUI.colourToString(jester.getPool().get(j).getColour()));

                gp.add(view, 1,j);
            }
        }

        gp.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

        return gp;
    }

    /**
     * returns the address of the parameter
     * @param c
     * @return
     */
    private String getAddress(Colour c){
        String address = "";

        if (c.equals(Colour.BLUE)){
            address = "it/polimi/ingsw/NETWORK/Images/student_blue.png";
        }else if(c.equals(Colour.RED)){
            address = "it/polimi/ingsw/NETWORK/Images/student_red.png";
        }else if(c.equals(Colour.YELLOW)){
            address = "it/polimi/ingsw/NETWORK/Images/student_yellow.png";
        }else if(c.equals(Colour.GREEN)){
            address = "it/polimi/ingsw/NETWORK/Images/student_green.png";
        }else if(c.equals(Colour.PINK)){
            address = "it/polimi/ingsw/NETWORK/Images/student_pink.png";
        }

        return address;
    }

    /**
     * returns the address of the card with the number i
     * @param i
     * @return
     */
    private String getAddressCard(int i){
        String address = "";

        if(i == 1){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (1).png";
        }else if(i == 2){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (2).png";
        }else if(i == 3){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (3).png";
        }else if(i == 4){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (4).png";
        }else if(i == 5){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (5).png";
        }else if(i == 6){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (6).png";
        }else if(i == 7){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (7).png";
        }else if(i == 8){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (8).png";
        }else if(i == 9){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (9).png";
        }else if(i == 10){
            address = "it/polimi/ingsw/NETWORK/Images/Assistente (10).png";
        }

        return address;
    }

    /**
     * returns the address of the character with name s
     * @param s
     * @return
     */
    private String getAddressCharacter(String s){
        String address = "";

        if(s.equals("Jester")){
            address = "it/polimi/ingsw/NETWORK/Images/Jester.jpg";
        }else if(s.equals("Knight")){
            address = "it/polimi/ingsw/NETWORK/Images/Knight.jpg";
        }else if(s.equals("Minstrell")){
            address = "it/polimi/ingsw/NETWORK/Images/Minstrell.jpg";
        }else if(s.equals("Pirate")){
            address = "it/polimi/ingsw/NETWORK/Images/Pirate.jpg";
        }else if(s.equals("PostMan")){
            address = "it/polimi/ingsw/NETWORK/Images/PostMan.jpg";
        }else if(s.equals("Priest")){
            address = "it/polimi/ingsw/NETWORK/Images/Priest.jpg";
        }else if(s.equals("Satyr")){
            address = "it/polimi/ingsw/NETWORK/Images/Satyr.jpg";
        }else if(s.equals("Woman")){
            address = "it/polimi/ingsw/NETWORK/Images/Woman.jpg";
        }

        return address;

    }

    /**
     * returns the address of the professor with colour c
     * @param c
     * @return
     */
    private String getAddressProfessor(Colour c){
        String address = "";

        if (c.equals(Colour.BLUE)){
            address = "it/polimi/ingsw/NETWORK/Images/teacher_blue.png";
        }else if(c.equals(Colour.RED)){
            address = "it/polimi/ingsw/NETWORK/Images/teacher_red.png";
        }else if(c.equals(Colour.YELLOW)){
            address = "it/polimi/ingsw/NETWORK/Images/teacher_yellow.png";
        }else if(c.equals(Colour.GREEN)){
            address = "it/polimi/ingsw/NETWORK/Images/teacher_green.png";
        }else if(c.equals(Colour.PINK)){
            address = "it/polimi/ingsw/NETWORK/Images/teacher_pink.png";
        }

        return address;
    }

    /**
     * returns the address of the tower with colour c
     * @param c
     * @return
     */
    private String getAddressTower(ColourTower c){
        String address = "";
        if (c.equals(ColourTower.BLACK)){
            address = "it/polimi/ingsw/NETWORK/Images/black_tower.png";
        }else if(c.equals(ColourTower.GREY)){
            address = "it/polimi/ingsw/NETWORK/Images/grey_tower.png";
        }else if(c.equals(ColourTower.WHITE)) {
            address = "it/polimi/ingsw/NETWORK/Images/white_tower.png";
        }

        return address;
    }

    /**
     * returns if the button has been clicked
     * @return
     */
    public boolean getButtonIsClicked() {
        return buttonIsClicked;
    }

    /**
     * set if the button has been clicked
     * @param b
     */
    private void setButtonIsClicked(boolean b) {
        //System.out.println("settato a" + b + "il buttone");
        buttonIsClicked = b;
    }
}