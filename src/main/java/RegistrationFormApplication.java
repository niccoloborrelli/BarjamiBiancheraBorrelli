
import com.example.demo.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class RegistrationFormApplication extends Application {
    String userType;
    String email;
    String registrationType;
    GridPane rootPane;
    Stage primaryStage;
    String selectedStoreChain;
    String selectedStoreAddress;
    String reservationType;
    WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
            .build();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        this.primaryStage = primaryStage;
        createInitianlPage();
        //createAfterLoginAsCustomerPage();
    }


    private void createRegistrationTypePage(){
        rootPane = createGridPane();
        addRegistrationTypeControls(rootPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createStoreSelectionPage(){
        rootPane = createGridPane();
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/store");
        List<StoreInfoVisualization> response = bodySpec.retrieve().bodyToFlux(StoreInfoVisualization.class).collectList().block();

        if(response != null){
            ArrayList <StoreInfoVisualization> siv  = new ArrayList<>(response);

            addStoreSelectionControls(rootPane,siv);

            // Create a scene with registration form grid pane as the root node
            Scene scene = new Scene(rootPane, 365, 650);
            // Set the scene in primary stage
            primaryStage.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.show();
        }
        else
            System.out.println("Error in /store call");

    }

    private void createDepartmentSelectionPage(StoreInfoVisualization siv){
        rootPane = createGridPane();
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/departments");
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(siv);
        List<String> response = headersSpec.retrieve().bodyToFlux(String.class).collectList().block();

        if(response != null){
            String[] dep = response.get(0).substring(1, response.get(0).length() - 1).split(",");
            List<String> depList = Arrays.asList(dep);
            ArrayList <String> departments = new ArrayList<String>(depList);

            addDepartmentSelectionControlsForBooking(rootPane,departments);
            // Create a scene with registration form grid pane as the root node
            Scene scene = new Scene(rootPane, 365, 650);
            // Set the scene in primary stage
            primaryStage.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.show();
        }
        else
            System.out.println("Error in /departments call");

    }

    private void createDepartmentSelectionPageForBooking(StoreInfoVisualization siv){
        rootPane = createGridPane();
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/departments");
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(siv);
        List<String> response = headersSpec.retrieve().bodyToFlux(String.class).collectList().block();


        if(response != null){
            String[] dep = response.get(0).substring(1, response.get(0).length() - 1).split(",");
            List<String> depList = Arrays.asList(dep);
            ArrayList <String> departments = new ArrayList<String>(depList);

            addDepartmentSelectionControlsForBooking(rootPane,departments);
            // Create a scene with registration form grid pane as the root node
            Scene scene = new Scene(rootPane, 365, 650);
            // Set the scene in primary stage
            primaryStage.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.show();
        }
        else
            System.out.println("Error in /departments call");

    }

    private void createAfterLoginAsCustomerPage(){
        rootPane = createGridPane();
        addAfterLoginControls(rootPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createAfterLoginAsScanClerkPage(){
        rootPane = createGridPane();
        addAfterLoginClerkControls(rootPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createAfterLoginAsManagerPage(){
        rootPane = createGridPane();
        addAfterLoginManagerControls(rootPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createMonitorReservationPage(ArrayList<ReservationStatusMessage> reservations){
        rootPane = createGridPane();
        addMonitorReservationControls(rootPane,reservations);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }
    private void createPrefStorePage(){
        rootPane = createGridPane();
        addPrefStoreControls(rootPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createSelectScanStorePage(){
        rootPane = createGridPane();
        addScanStoreControls(rootPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createSelectManagerStoreToMonitorPage(){
        rootPane = createGridPane();
        addManagerStoreSelectionControls(rootPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createLoginPage(){
        rootPane = createGridPane();
        addLoginControls(rootPane,"login");
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createRegistrationPage(){
        rootPane = createGridPane();

        addLoginControls(rootPane,"registration");
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createAddNewStorePage(){
        rootPane = createGridPane();
        addCreateNewStoreControls(rootPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private void createInitianlPage(){
        // Create the registration form grid pane
        rootPane = createGridPane();
        // Add UI controls to the registration form grid pane
        addInitialControls(rootPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(rootPane, 365, 650);
        // Set the scene in primary stage
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();
        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);
        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        // Set the horizontal gap between columns
        gridPane.setHgap(0);
        // Set the vertical gap between rows
        gridPane.setVgap(25);
        // Add Column Constraints
        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        gridPane.setStyle("-fx-background-color: #a7a7a7; -fx-background-radius: 30,25,20;");
        return gridPane;
    }

    private GridPane createSmallerGrid() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();
        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);
        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        // Set the horizontal gap between columns
        gridPane.setHgap(0);
        // Set the vertical gap between rows
        gridPane.setVgap(0);
        // Add Column Constraints
        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(90, 90, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(20,20, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        gridPane.setStyle("-fx-background-color: #d7d5d5; -fx-background-radius: 30,25,20,15,10;");
        gridPane.setPrefSize(800,120);
        return gridPane;
    }

    private void addPrefStoreControls(GridPane gridPane){
        Label prefStoreLabel;
        Button button;
        prefStoreLabel = new Label("Select Favourite Store");

        prefStoreLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        gridPane.add(prefStoreLabel, 0, 0, 3 , 1);
        GridPane.setMargin(prefStoreLabel, new Insets(-25, 0,40,0));
        GridPane.setHalignment(prefStoreLabel, HPos.CENTER);

        // Add Email Label
        Label chainLabel = new Label("Chain : ");
        gridPane.add(chainLabel, 0, 1);
        chainLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        // Add Email Text Field
        TextField chainField = new TextField();

        chainField.setPrefHeight(40);
        gridPane.add(chainField, 1, 1);

        GridPane.setMargin(chainLabel, new Insets(0, 0,30,0));
        GridPane.setMargin(chainField, new Insets(0, 0,30,0));
        GridPane.setHalignment(chainLabel, HPos.CENTER);
        GridPane.setHalignment(chainField, HPos.CENTER);


        Label addressLabel = new Label("Address : ");
        gridPane.add(addressLabel, 0, 2);
        addressLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        TextField addressField = new TextField();
        addressField.setPrefHeight(40);
        gridPane.add(addressField, 1, 2);

        GridPane.setMargin(addressLabel, new Insets(0, 0,40,0));
        GridPane.setMargin(addressField, new Insets(0, 0,40,0));
        GridPane.setHalignment(addressLabel, HPos.CENTER);
        GridPane.setHalignment(addressField, HPos.CENTER);

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        chainField.setStyle("-fx-background-radius: 10,7,5,3;");
        addressField.setStyle("-fx-background-radius: 10,7,5,3;");
        button = formatSubmitPrefStoreButton(gridPane,styleString);
        formatGoBackButtonAfterLogin(gridPane,styleString);

        button.setOnAction(actionEvent -> {
            StoreMessage storeMessage = new StoreMessage(chainField.getText(), addressField.getText(), email);
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("/preferredStore");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(storeMessage);
            Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);
            response.subscribe(System.out::println);
        });
    }

    private void addScanStoreControls(GridPane gridPane){
        Label storeToScan;
        storeToScan = new Label("Select Store to Scan");

        storeToScan.setFont(Font.font("Helvetica", FontWeight.BOLD, 22));
        gridPane.add(storeToScan, 0, 0, 3 , 1);
        GridPane.setMargin(storeToScan, new Insets(-25, 0,120,0));
        GridPane.setHalignment(storeToScan, HPos.CENTER);

        // Add Email Label
        Label chainLabel = new Label("Chain : ");
        gridPane.add(chainLabel, 0, 1);
        chainLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        // Add Email Text Field
        TextField chainField = new TextField();

        chainField.setPrefHeight(40);
        gridPane.add(chainField, 1, 1);

        GridPane.setMargin(chainLabel, new Insets(0, 0,15,0));
        GridPane.setMargin(chainField, new Insets(0, 0,15,0));
        GridPane.setHalignment(chainLabel, HPos.CENTER);
        GridPane.setHalignment(chainField, HPos.CENTER);


        Label addressLabel = new Label("Address : ");
        gridPane.add(addressLabel, 0, 2);
        addressLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        TextField addressField = new TextField();
        addressField.setPrefHeight(40);
        gridPane.add(addressField, 1, 2);

        GridPane.setMargin(addressLabel, new Insets(0, 0,40,0));
        GridPane.setMargin(addressField, new Insets(0, 0,40,0));
        GridPane.setHalignment(addressLabel, HPos.CENTER);
        GridPane.setHalignment(addressField, HPos.CENTER);

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        chainField.setStyle("-fx-background-radius: 10,7,5,3;");
        addressField.setStyle("-fx-background-radius: 10,7,5,3;");
        formatSubmitStoreToScan(gridPane,styleString);
    }


    private void addManagerStoreSelectionControls(GridPane gridPane){
        Label storeToScan;
        storeToScan = new Label("Select Store to Monitor");

        storeToScan.setFont(Font.font("Helvetica", FontWeight.BOLD, 22));
        gridPane.add(storeToScan, 0, 0, 3 , 1);
        GridPane.setMargin(storeToScan, new Insets(-25, 0,120,0));
        GridPane.setHalignment(storeToScan, HPos.CENTER);

        // Add Email Label
        Label chainLabel = new Label("Chain : ");
        gridPane.add(chainLabel, 0, 1);
        chainLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        // Add Email Text Field
        TextField chainField = new TextField();

        chainField.setPrefHeight(40);
        gridPane.add(chainField, 1, 1);

        GridPane.setMargin(chainLabel, new Insets(0, 0,15,0));
        GridPane.setMargin(chainField, new Insets(0, 0,15,0));
        GridPane.setHalignment(chainLabel, HPos.CENTER);
        GridPane.setHalignment(chainField, HPos.CENTER);


        Label addressLabel = new Label("Address : ");
        gridPane.add(addressLabel, 0, 2);
        addressLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        TextField addressField = new TextField();
        addressField.setPrefHeight(40);
        gridPane.add(addressField, 1, 2);

        GridPane.setMargin(addressLabel, new Insets(0, 0,40,0));
        GridPane.setMargin(addressField, new Insets(0, 0,40,0));
        GridPane.setHalignment(addressLabel, HPos.CENTER);
        GridPane.setHalignment(addressField, HPos.CENTER);

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        chainField.setStyle("-fx-background-radius: 10,7,5,3;");
        addressField.setStyle("-fx-background-radius: 10,7,5,3;");

        Button storeToMonitorButton = new Button("Select Store");
        storeToMonitorButton.setDefaultButton(true);
        storeToMonitorButton.setPrefWidth(150);

        gridPane.add(storeToMonitorButton, 0, 3, 2, 1);
        GridPane.setHalignment(storeToMonitorButton, HPos.CENTER);
        storeToMonitorButton.setStyle(styleString);
        storeToMonitorButton.setOnAction(event -> {
            //invia la richiesta di monitorare nuovo store al Server? ce ne veramente bisogno? si devi vedere solo se
            //lo store richiesto esiste
            StoreMessage storeMessage = new StoreMessage(chainField.getText(), addressField.getText(), email);
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("/preferredStore");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(storeMessage);
            Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);
            response.subscribe(System.out::println);
            if(response.block().equals("added correctly")){
                selectedStoreChain = chainField.getText();
                selectedStoreAddress = addressField.getText();
            }
            createAfterLoginAsManagerPage();
        });
    }

    private void addCreateNewStoreControls(GridPane gridPane){
        Label storeToScan;
        Button submit;
        storeToScan = new Label("Select Store to Scan");

        storeToScan.setFont(Font.font("Helvetica", FontWeight.BOLD, 22));
        gridPane.add(storeToScan, 0, 0, 3 , 1);
        GridPane.setMargin(storeToScan, new Insets(-25, 0,80,0));
        GridPane.setHalignment(storeToScan, HPos.CENTER);

        // Add Email Label
        Label chainLabel = new Label("Chain : ");
        gridPane.add(chainLabel, 0, 1);
        chainLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        // Add Email Text Field
        TextField chainField = new TextField();

        chainField.setPrefHeight(40);
        gridPane.add(chainField, 1, 1);

        GridPane.setMargin(chainLabel, new Insets(0, 0,10,0));
        GridPane.setMargin(chainField, new Insets(0, 0,10,0));
        GridPane.setHalignment(chainLabel, HPos.CENTER);
        GridPane.setHalignment(chainField, HPos.CENTER);


        Label addressLabel = new Label("Address : ");
        gridPane.add(addressLabel, 0, 2);
        addressLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        TextField addressField = new TextField();
        addressField.setPrefHeight(40);
        gridPane.add(addressField, 1, 2);

        GridPane.setMargin(addressLabel, new Insets(0, 0,10,0));
        GridPane.setMargin(addressField, new Insets(0, 0,10,0));
        GridPane.setHalignment(addressLabel, HPos.CENTER);
        GridPane.setHalignment(addressField, HPos.CENTER);

        Label maxCapability = new Label("Max Capability : ");
        gridPane.add(maxCapability, 0, 3);
        maxCapability.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        TextField maxCapabilityField = new TextField();
        maxCapabilityField.setPrefHeight(40);
        gridPane.add(maxCapabilityField, 1, 3);

        GridPane.setMargin(maxCapability, new Insets(0, 0,10,0));
        GridPane.setMargin(maxCapabilityField, new Insets(0, 0,10,0));
        GridPane.setHalignment(maxCapability, HPos.CENTER);
        GridPane.setHalignment(maxCapabilityField, HPos.CENTER);


        Label openTimeLabel = new Label("Opening Time : ");
        gridPane.add(openTimeLabel, 0, 4);
        openTimeLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        TextField openTimeField = new TextField();
        openTimeField.setPrefHeight(40);
        gridPane.add(openTimeField, 1, 4);

        GridPane.setMargin(openTimeLabel, new Insets(0, 0,10,0));
        GridPane.setMargin(openTimeField, new Insets(0, 0,10,0));
        GridPane.setHalignment(openTimeLabel, HPos.CENTER);
        GridPane.setHalignment(openTimeField, HPos.CENTER);

        Label closureTimeLabel = new Label("Closure Time : ");
        gridPane.add(closureTimeLabel, 0, 5);
        closureTimeLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        TextField closureTimeField = new TextField();
        closureTimeField.setPrefHeight(40);
        gridPane.add(closureTimeField, 1, 5);

        GridPane.setMargin(closureTimeLabel, new Insets(0, 0,40,0));
        GridPane.setMargin(closureTimeField, new Insets(0, 0,40,0));
        GridPane.setHalignment(closureTimeLabel, HPos.CENTER);
        GridPane.setHalignment(closureTimeField, HPos.CENTER);


        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        chainField.setStyle("-fx-background-radius: 10,7,5,3;");
        addressField.setStyle("-fx-background-radius: 10,7,5,3;");
        maxCapabilityField.setStyle("-fx-background-radius: 10,7,5,3;");
        openTimeField.setStyle("-fx-background-radius: 10,7,5,3;");
        closureTimeField.setStyle("-fx-background-radius: 10,7,5,3;");
        submit = formatSubmitNewStoreButton(gridPane,styleString);

        submit.setOnAction(actionEvent -> {
            StoreCreationMessage storeCreationMessage = new StoreCreationMessage(chainField.getText(), addressField.getText(),
                    openTimeField.getText(), closureTimeField.getText());
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("/create");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(storeCreationMessage);
            Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);
            response.subscribe(System.out::println);
            createAfterLoginAsManagerPage();
        });
    }

    private void addLoginControls(GridPane gridPane,String type){
        Label loginLabel;
        Button submit;
        // Add Email Label
        if(type.equals("login"))
            loginLabel = new Label("Login");
        else
            loginLabel = new Label("Registration");

        loginLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 36));
        gridPane.add(loginLabel, 0, 0, 3 , 1);
        GridPane.setMargin(loginLabel, new Insets(-25, 0,40,0));
        GridPane.setHalignment(loginLabel, HPos.CENTER);

        // Add Email Label
        Label emailLabel = new Label("Email ID : ");
        gridPane.add(emailLabel, 0, 1);
        emailLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        // Add Email Text Field
        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1, 1);

        GridPane.setMargin(emailLabel, new Insets(0, 0,30,0));
        GridPane.setMargin(emailField, new Insets(0, 0,30,0));
        GridPane.setHalignment(emailLabel, HPos.CENTER);
        GridPane.setHalignment(emailField, HPos.CENTER);


        Label passwordLabel = new Label("Password : ");
        gridPane.add(passwordLabel, 0, 2);
        passwordLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 2);

        GridPane.setMargin(passwordLabel, new Insets(-30, 0,41,0));
        GridPane.setMargin(passwordField, new Insets(-30, 0,41,0));
        GridPane.setHalignment(passwordLabel, HPos.CENTER);
        GridPane.setHalignment(passwordField, HPos.CENTER);

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        // Add Submit Button
        if (type.equals("login"))
            submit = formatSubmitLoginButton(gridPane,styleString);
        else
             submit = formatSubmitRegistrationButton(gridPane,styleString);
        formatGoBackButton(gridPane,styleString);

        passwordField.setStyle("-fx-background-radius: 10,7,5,3;");
        emailField.setStyle("-fx-background-radius: 10,7,5,3;");

        submit.setOnAction(actionEvent -> {
            email = emailField.getText();
            System.out.println(type);
            LoginMessage loginMessage = new LoginMessage(emailField.getText(), passwordField.getText(), registrationType);
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
            WebClient.RequestBodySpec bodySpec;
            if(type.equals("login"))
                bodySpec = uriSpec.uri("/" + type +"Request");
            else
                bodySpec = uriSpec.uri("/" + type + "Request");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(loginMessage);
            Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);
            response.subscribe(System.out::println);

            if(type.equals("login") && Objects.equals(response.block(), "customer")){
                createAfterLoginAsCustomerPage();
            }
            else if(type.equals("login") && Objects.equals(response.block(), "manager")){
                createAfterLoginAsManagerPage();
            }
            else if(type.equals("login") && Objects.equals(response.block(), "scanClerk")){
                createAfterLoginAsScanClerkPage();
            }
            else
                createLoginPage();

        });

    }

    private void addRegistrationTypeControls(GridPane gridPane){
        Label headerLabel = new Label("Select Type of Registration");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 22));
        gridPane.add(headerLabel, 0,0,3,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(-70, 0,150,0));

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        formatUserRegistrationButton(gridPane,styleString);
        formatScanRegistrationButton(gridPane,styleString);
        formatManagerRegistrationButton(gridPane,styleString);
    }

    private void addAfterLoginControls(GridPane gridPane){
        Image cart = new Image(this.getClass().getResourceAsStream("ShoppingCart.png"));
        // Add Header
        ImageView cartView = new ImageView(cart);
        cartView.setFitHeight(120);
        cartView.setFitWidth(120);

        Label headerLabel = new Label("CLup");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 36));
        gridPane.add(cartView, 0,0);
        gridPane.add(headerLabel, 1,0);
        GridPane.setHalignment(cartView, HPos.CENTER);
        GridPane.setMargin(cartView, new Insets(0, -100,120,0));
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0,120,0));

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        // Add Login Button
        formatLineUpButton(gridPane,styleString);
        // Add Submit Button
        formatBookAVisitButton(gridPane,styleString);
        //
        formatMonitorButton(gridPane,styleString);
        //
        formatPreferedStoreButton(gridPane,styleString);

    }

    private void addStoreSelectionControls(GridPane gridPane,ArrayList <StoreInfoVisualization> siv){

        Label headerLabel = new Label("Select The Store");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 32));
        gridPane.add(headerLabel, 0,0,3,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0,10,0));

        int i=1;
        for (StoreInfoVisualization sivTemp: siv) {
            GridPane temp = createSmallerGrid();
            gridPane.add(temp,0,i,2,1);
            formatSubGrid(temp,sivTemp);
            i++;
        }
    }

    private void addMonitorReservationControls(GridPane gridPane,ArrayList <ReservationStatusMessage> reservations){
        Label headerLabel = new Label("Click to delete: ");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 26));
        gridPane.add(headerLabel, 0,0,3,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0,10,0));

        int i=1;
        for (ReservationStatusMessage resTemp: reservations) {
            GridPane temp = createSmallerGrid();
            gridPane.add(temp,0,i,2,1);
            formatSubGridMonitorReservation(temp,resTemp);
            i++;
        }

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        Button backButton = new Button("Back");
        backButton.setDefaultButton(true);
        backButton.setPrefWidth(130);
        gridPane.add(backButton, 0, i, 2, 1);
        GridPane.setHalignment(backButton, HPos.CENTER);
        backButton.setStyle(styleString);
        backButton.setOnAction(event -> createAfterLoginAsCustomerPage());

    }


    private void formatSubGrid(GridPane gridPane,StoreInfoVisualization siv){
        System.out.println(siv.getChain());
        Image logoIm = new Image(this.getClass().getResourceAsStream(siv.getChain()+".png"));
        ImageView logo = new ImageView(logoIm);
        logo.setFitWidth(80);
        logo.setPreserveRatio(true);
        gridPane.add(logo,0,0,1,3);

        //gridPane.setGridLinesVisible(true);
        Label addressLabel = new Label("Address : " + siv.getAddress());
        gridPane.add(addressLabel, 0, 1,2,1);
        addressLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        GridPane.setMargin(addressLabel, new Insets(0, 20,-10,0));
        if(siv.getChain().equals("Lidl")||siv.getChain().equals("Carrefour"))
            GridPane.setMargin(addressLabel, new Insets(0, 20,-30,0));


        Date date = new Date(Long.parseLong(siv.getOpenTime()));
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        Label openingLabel = new Label("Open Time : " + dateFormatted);
        
        gridPane.add(openingLabel, 0, 2,2,1);
        openingLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        GridPane.setMargin(openingLabel, new Insets(0, 20,-10,0));
        if(siv.getChain().equals("Lidl")||siv.getChain().equals("Carrefour"))
            GridPane.setMargin(openingLabel, new Insets(-30, 20,-30,0));

        date = new Date(Long.parseLong(siv.getClosureTime()));
        formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormatted = formatter.format(date);
        Label closureTime = new Label("Closure Time : " + dateFormatted);
        gridPane.add(closureTime, 0, 3,2,1);
        closureTime.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        GridPane.setMargin(closureTime, new Insets(0, 20,-10,0));
        if(siv.getChain().equals("Lidl")||siv.getChain().equals("Carrefour"))
            GridPane.setMargin(closureTime, new Insets(-30, 20,0,0));

        logo.setOnMouseClicked(mouseEvent -> {
            selectedStoreAddress = siv.getAddress();
            selectedStoreChain = siv.getChain();
            if(reservationType.equals("lineUp_Request"))
                createDepartmentSelectionPage(siv);
            else if(reservationType.equals("booking_Request"))
                createDepartmentSelectionPageForBooking(siv);
        });

    }

    private void formatSubGridMonitorReservation(GridPane gridPane,ReservationStatusMessage res){
        Image logoIm = new Image(this.getClass().getResourceAsStream(res.getChain()+".png"));
        ImageView logo = new ImageView(logoIm);
        logo.setFitWidth(80);
        logo.setPreserveRatio(true);
        gridPane.add(logo,0,0,1,3);

        //gridPane.setGridLinesVisible(true);
        Label addressLabel = new Label("Address : " + res.getAddress());
        gridPane.add(addressLabel, 0, 1,2,1);
        addressLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        GridPane.setMargin(addressLabel, new Insets(0, 20,-10,0));
        if(res.getChain().equals("Lidl")||res.getChain().equals("Carrefour"))
            GridPane.setMargin(addressLabel, new Insets(0, 20,-30,0));

        Date date = new Date(Long.parseLong(res.getEntryTime()));
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        Label entryTime = new Label("Entry Time : " + dateFormatted);
        gridPane.add(entryTime, 0, 2,2,1);
        entryTime.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        GridPane.setMargin(entryTime, new Insets(0, 20,-10,0));
        if(res.getChain().equals("Lidl")||res.getChain().equals("Carrefour"))
            GridPane.setMargin(entryTime, new Insets(-30, 20,-30,0));

        Label code = new Label("Code : " + res.getCode());
        gridPane.add(code, 0, 3,2,1);
        code.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        GridPane.setMargin(code, new Insets(0, 20,-10,0));
        if(res.getChain().equals("Lidl")||res.getChain().equals("Carrefour"))
            GridPane.setMargin(code, new Insets(-30, 20,0,0));

        logo.setOnMouseClicked(mouseEvent -> {
            DeleteMessage deleteMessage = new DeleteMessage(res.getChain(), res.getAddress(), res.getCode(), email);
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("/delete");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(deleteMessage);
            Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);
            response.subscribe(System.out::print);
        });
    }

    private void addDepartmentSelectionControls(GridPane gridPane,ArrayList<String> departments){
        Label headerLabel = new Label("Select The Departments");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 26));
        gridPane.add(headerLabel, 0,0,3,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0,50,0));

        // Instantiate a new Grid Pane
        GridPane tempPane = new GridPane();
        // Position the pane at the center of the screen, both vertically and horizontally
        tempPane.setAlignment(Pos.CENTER);
        // Set a padding of 20px on each side
        tempPane.setPadding(new Insets(5, 5, 5, 5));
        // Set the horizontal gap between columns
        tempPane.setHgap(110);
        // Set the vertical gap between rows
        tempPane.setVgap(0);
        // Add Column Constraints
        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(90, 90, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(40,40, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        tempPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        Label timeLabel = new Label("time : ");
        gridPane.add(timeLabel, 0, 1);
        timeLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        // Add Email Text Field
        TextField timeField = new TextField();
        timeField.setPrefHeight(40);
        gridPane.add(timeField, 1, 1);
        GridPane.setMargin(timeLabel, new Insets(0, 0,30,0));
        GridPane.setMargin(timeField, new Insets(0, 0,30,0));
        GridPane.setHalignment(timeLabel, HPos.CENTER);
        GridPane.setHalignment(timeField, HPos.CENTER);

        Label durationLabel = new Label("Duration : ");
        gridPane.add(durationLabel, 0, 2);
        durationLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        TextField durationField = new TextField();
        durationField.setPrefHeight(40);
        gridPane.add(durationField, 1, 2);
        GridPane.setMargin(durationLabel, new Insets(-30, 0,41,0));
        GridPane.setMargin(durationField, new Insets(-30, 0,41,0));
        GridPane.setHalignment(durationLabel, HPos.CENTER);
        GridPane.setHalignment(durationField, HPos.CENTER);
        tempPane.setStyle("-fx-background-color: #d7d5d5; -fx-background-radius: 10,7,5,3;");
        tempPane.setPrefSize(600,230);
        gridPane.add(tempPane,0,1,2,2);

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        // i create and add checkbox
        int i = 0;
        int j = 0;
        ArrayList <CheckBox> checkBoxesList = new ArrayList<CheckBox>();
        for (String s:departments) {
            CheckBox temp = new CheckBox(s);
            checkBoxesList.add(temp);
            tempPane.add(temp,i,j);
            i++;
            if(i==2){
                i= 0;
                j++;
            }
            tempPane.setHalignment(temp, HPos.LEFT);
            tempPane.setMargin(temp, new Insets(5, 0,20,0));

        }

        Button submitButton = new Button("Send");
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(130);
        gridPane.add(submitButton, 0, 3, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,5,0));

        submitButton.setStyle(styleString);
        submitButton.setOnAction(actionEvent -> {
            List<String> dep = new ArrayList<>();
            for (CheckBox c:checkBoxesList) {
                if(c.isSelected()){
                    dep.add(c.getText());
                }
            }
            RequestMessage requestMessage = new RequestMessage(email, durationField.getText(), String.valueOf(LocalDate.now()), selectedStoreChain,
                    selectedStoreAddress, reservationType, dep, timeField.getText());
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("/Request");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(requestMessage);
            Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);
            response.subscribe(System.out::print);
            createAfterLoginAsCustomerPage();
        });
    }

    private void addDepartmentSelectionControlsForBooking(GridPane gridPane,ArrayList<String> departments){
        Label headerLabel = new Label("Select The Departments");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 26));
        gridPane.add(headerLabel, 0,0,3,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0,50,0));
        // Instantiate a new Grid Pane
        GridPane tempPane = new GridPane();
        // Position the pane at the center of the screen, both vertically and horizontally
        tempPane.setAlignment(Pos.CENTER);
        // Set a padding of 20px on each side
        tempPane.setPadding(new Insets(5, 5, 5, 5));
        // Set the horizontal gap between columns
        tempPane.setHgap(110);
        // Set the vertical gap between rows
        tempPane.setVgap(0);
        // Add Column Constraints
        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(90, 90, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(40,40, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        // Add Email Label
        Label timeLabel = new Label("time : ");
        gridPane.add(timeLabel, 0, 1);
        timeLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        // Add Email Text Field
        TextField timeField = new TextField();
        timeField.setPrefHeight(40);
        gridPane.add(timeField, 1, 1);
        GridPane.setMargin(timeLabel, new Insets(0, 0,30,0));
        GridPane.setMargin(timeField, new Insets(0, 0,30,0));
        GridPane.setHalignment(timeLabel, HPos.CENTER);
        GridPane.setHalignment(timeField, HPos.CENTER);

        Label durationLabel = new Label("Duration : ");
        gridPane.add(durationLabel, 0, 2);
        durationLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        TextField durationField = new TextField();
        durationField.setPrefHeight(40);
        gridPane.add(durationField, 1, 2);
        GridPane.setMargin(durationLabel, new Insets(-30, 0,41,0));
        GridPane.setMargin(durationField, new Insets(-30, 0,41,0));
        GridPane.setHalignment(durationLabel, HPos.CENTER);
        GridPane.setHalignment(durationField, HPos.CENTER);

        tempPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        tempPane.setStyle("-fx-background-color: #d7d5d5; -fx-background-radius: 10,7,5,3;");
        tempPane.setPrefSize(600,230);
        gridPane.add(tempPane,0,3,2,2);
        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";
        // i create and add checkbox
        int i = 0;
        int j = 0;
        ArrayList <CheckBox> checkBoxesList = new ArrayList<CheckBox>();
        for (String s:departments) {
            CheckBox temp = new CheckBox(s);
            checkBoxesList.add(temp);
            tempPane.add(temp,i,j);
            i++;
            if(i==2){
                i= 0;
                j++;
            }
            tempPane.setHalignment(temp, HPos.LEFT);
            tempPane.setMargin(temp, new Insets(5, 0,20,0));
        }
        Button submitButton = new Button("Send");
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(130);
        gridPane.add(submitButton, 0, 5, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,5,0));
        submitButton.setStyle(styleString);

        submitButton.setOnAction(actionEvent -> {
            List<String> dep = new ArrayList<>();
            for (CheckBox c:checkBoxesList) {
                if(c.isSelected()){
                    dep.add(c.getText());
                }
            }
            RequestMessage requestMessage = new RequestMessage(email, durationField.getText(), String.valueOf(LocalDate.now()), selectedStoreChain,
                                        selectedStoreAddress, reservationType, dep, timeField.getText());
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("/Request");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(requestMessage);
            Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);
            response.subscribe(System.out::print);
            createAfterLoginAsCustomerPage();
        });

    }


    private void addInitialControls(GridPane gridPane) {
        Image cart = new Image(this.getClass().getResourceAsStream("ShoppingCart.png"));
        // Add Header
        ImageView cartView = new ImageView(cart);
        cartView.setFitHeight(120);
        cartView.setFitWidth(120);

        Label headerLabel = new Label("CLup");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 36));
        gridPane.add(cartView, 0,0);
        gridPane.add(headerLabel, 1,0);
        GridPane.setHalignment(cartView, HPos.CENTER);
        GridPane.setMargin(cartView, new Insets(0, -100,120,0));
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0,120,0));

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        // Add Login Button
        formatLoginButton(gridPane,styleString);
        // Add Submit Button
        formatRegistrationButton(gridPane,styleString);

    }

    private void addAfterLoginClerkControls(GridPane gridPane){
        Image cart = new Image(this.getClass().getResourceAsStream("ShoppingCart.png"));
        // Add Header
        ImageView cartView = new ImageView(cart);
        cartView.setFitHeight(120);
        cartView.setFitWidth(120);

        Label headerLabel = new Label("CLup \nScan Clerk");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 22));
        gridPane.add(cartView, 0,0);
        gridPane.add(headerLabel, 1,0);
        GridPane.setHalignment(cartView, HPos.CENTER);
        GridPane.setMargin(cartView, new Insets(0, -100,120,0));
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0,120,0));

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";

        // Add Email Label
        Label codeLabel = new Label("Code to scan: ");
        gridPane.add(codeLabel, 0, 1);
        codeLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        // Add Email Text Field
        TextField codeField = new TextField();
        codeField.setPrefHeight(40);
        gridPane.add(codeField, 1, 1);

        GridPane.setMargin(codeLabel, new Insets(0, 0,30,0));
        GridPane.setMargin(codeField, new Insets(0, 0,30,0));
        GridPane.setHalignment(codeLabel, HPos.CENTER);
        GridPane.setHalignment(codeField, HPos.CENTER);

        Button submitCodeButton = new Button("Submit Code");
        submitCodeButton.setDefaultButton(true);
        submitCodeButton.setPrefWidth(132);
        gridPane.add(submitCodeButton, 0, 3, 2, 1);
        GridPane.setHalignment(submitCodeButton, HPos.CENTER);
        GridPane.setMargin(submitCodeButton, new Insets(20, 0,5,0));
        submitCodeButton.setStyle(styleString);
        codeField.setStyle("-fx-background-radius: 10,7,5,3;");

        submitCodeButton.setOnAction(event -> {
            ScanMessage scanMessage = new ScanMessage(Integer.parseInt(codeField.getText()), email);
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("/scanRequest");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(scanMessage);
            Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);
            response.subscribe(System.out::println);
            codeField.clear();
        });

        Button selectStore = new Button("Select Store");
        selectStore.setDefaultButton(true);
        selectStore.setPrefWidth(132);
        gridPane.add(selectStore, 0, 4, 2, 1);
        GridPane.setHalignment(selectStore, HPos.CENTER);
        GridPane.setMargin(selectStore, new Insets(0, 0,5,0));
        selectStore.setStyle(styleString);
        codeField.setStyle("-fx-background-radius: 10,7,5,3;");

        selectStore.setOnAction(event -> {
            createSelectScanStorePage();// submit to server the code ( magari stampa errore se qualcosa non va nel verso giusto)
        });

    }

    private void addAfterLoginManagerControls(GridPane gridPane){
        Image cart = new Image(this.getClass().getResourceAsStream("ShoppingCart.png"));
        // Add Header
        ImageView cartView = new ImageView(cart);
        cartView.setFitHeight(120);
        cartView.setFitWidth(120);

        Label headerLabel = new Label("CLup \nStore Manager");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        gridPane.add(cartView, 0,0);
        gridPane.add(headerLabel, 1,0);
        GridPane.setHalignment(cartView, HPos.CENTER);
        GridPane.setMargin(cartView, new Insets(0, -80,90,0));
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0,90,0));

        String styleString= "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;\n";
        ;


        Label n_lineUp = new Label("n_lineUp: ");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        gridPane.add(n_lineUp, 0,1);
        Label n_lineUpValue = new Label("");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        gridPane.add( n_lineUpValue, 1,1);
        GridPane.setHalignment(n_lineUpValue, HPos.CENTER);
        GridPane.setMargin(n_lineUpValue, new Insets(0, 0,10,0));
        GridPane.setHalignment( n_lineUp, HPos.LEFT);
        GridPane.setMargin( n_lineUp, new Insets(0, 0,10,0));

        Label n_booking = new Label("n_Booking: ");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        gridPane.add(n_booking, 0,2);
        Label n_bookingValue = new Label("");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        gridPane.add( n_bookingValue, 1,2);
        GridPane.setHalignment(n_bookingValue, HPos.CENTER);
        GridPane.setMargin(n_bookingValue, new Insets(0, 0,10,0));
        GridPane.setHalignment( n_booking, HPos.LEFT);
        GridPane.setMargin( n_booking, new Insets(0, 0,10,0));

        Label n_customers = new Label("n_Customers: ");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        gridPane.add(n_customers, 0,3);
        Label n_customersValue = new Label("");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        gridPane.add( n_customersValue, 1,3);
        GridPane.setHalignment(n_customersValue, HPos.CENTER);
        GridPane.setMargin(n_customersValue, new Insets(0, 0,10,0));
        GridPane.setHalignment( n_customers, HPos.LEFT);
        GridPane.setMargin( n_customers, new Insets(0, 0,10,0));

        Button updateStatusButton = new Button("Update Status");
        updateStatusButton.setDefaultButton(true);
        updateStatusButton.setPrefWidth(150);
        gridPane.add(updateStatusButton, 0, 5, 2, 1);
        GridPane.setHalignment(updateStatusButton, HPos.CENTER);
        GridPane.setMargin(updateStatusButton, new Insets(0, 0,5,0));
        updateStatusButton.setStyle(styleString);

        updateStatusButton.setOnAction(event -> {
            // qui devo chiedere al server di aggiornare i valori di n_customersValue n_bookingValue e n_lineUpValue
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("/status");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(email);
            List<String> response = headersSpec.retrieve().bodyToFlux(String.class).collectList().block();

            if(response != null){
                String[] num = response.get(0).substring(1, response.get(0).length() - 1).split(",");
                List<String> depList = Arrays.asList(num);
                ArrayList <String> numeri = new ArrayList<String>(depList);
                System.out.println("#Customers inside: " + numeri.get(0));
                System.out.println("#LineUp Request: " + numeri.get(1));
                System.out.println("#Booking Request: " + numeri.get(2));
                n_customersValue.setText(numeri.get(0));
                n_lineUpValue.setText(numeri.get(1));
                n_bookingValue.setText(numeri.get(2));

            }





        });

        Button create_new_store = new Button("Create New Store");
        create_new_store.setDefaultButton(true);
        create_new_store.setPrefWidth(150);
        gridPane.add(create_new_store, 0, 6, 2, 1);
        GridPane.setHalignment(create_new_store, HPos.CENTER);
        GridPane.setMargin(create_new_store, new Insets(0, 0,5,0));
        create_new_store.setStyle(styleString);
        create_new_store.setOnAction(event -> createAddNewStorePage());

        Button selectNewStore = new Button("Select Store");
        selectNewStore.setDefaultButton(true);
        selectNewStore.setPrefWidth(150);
        gridPane.add(selectNewStore, 0, 7, 2, 1);
        GridPane.setHalignment(selectNewStore, HPos.CENTER);
        GridPane.setMargin(selectNewStore, new Insets(0, 0,5,0));
        selectNewStore.setStyle(styleString);
        selectNewStore.setOnAction(event -> createSelectManagerStoreToMonitorPage());
    }

    private void formatUserRegistrationButton(GridPane gridPane,String styleString){
        Button customerRegistration = new Button("Register as Customer");
        customerRegistration.setDefaultButton(true);
        customerRegistration.setPrefWidth(180);
        gridPane.add(customerRegistration, 0, 1, 3, 1);
        GridPane.setHalignment(customerRegistration, HPos.CENTER);
        GridPane.setMargin(customerRegistration, new Insets(20, 0,5,0));
        customerRegistration.setStyle(styleString);
        customerRegistration.setOnAction(event -> {
            registrationType="customer";
            createRegistrationPage();
        });
    }

    private void formatScanRegistrationButton(GridPane gridPane,String styleString){
        Button scanRegistration = new Button("Register as ScanClerk");
        scanRegistration.setDefaultButton(true);
        scanRegistration.setPrefWidth(180);
        gridPane.add(scanRegistration, 0, 2, 3, 1);
        GridPane.setHalignment(scanRegistration, HPos.CENTER);
        GridPane.setMargin(scanRegistration, new Insets(20, 0,5,0));
        scanRegistration.setStyle(styleString);
        scanRegistration.setOnAction(event -> {
            registrationType="scanClerk";
            createRegistrationPage();
        });
    }

    private void formatManagerRegistrationButton(GridPane gridPane,String styleString){
        Button managerRegistration = new Button("Register as Manager");
        managerRegistration.setDefaultButton(true);
        managerRegistration.setPrefWidth(180);
        gridPane.add(managerRegistration, 0, 3, 3, 1);
        GridPane.setHalignment(managerRegistration, HPos.CENTER);
        GridPane.setMargin(managerRegistration, new Insets(20, 0,5,0));
        managerRegistration.setStyle(styleString);
        managerRegistration.setOnAction(event -> {
            registrationType="manager";
            createRegistrationPage();
        });
    }

    private void formatLoginButton(GridPane gridPane,String styleString){
        Button loginButton = new Button("Login");
        loginButton.setDefaultButton(true);
        loginButton.setPrefWidth(132);
        gridPane.add(loginButton, 0, 1, 2, 1);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setMargin(loginButton, new Insets(20, 0,5,0));
        loginButton.setStyle(styleString);
        loginButton.setOnAction(event -> createLoginPage());
    }

    private Button formatSubmitPrefStoreButton(GridPane gridPane,String styleString){
        Button prefButton = new Button("Submit");
        prefButton.setDefaultButton(true);
        prefButton.setPrefWidth(132);
        gridPane.add(prefButton, 0, 3, 2, 1);
        GridPane.setHalignment(prefButton, HPos.CENTER);
        GridPane.setMargin(prefButton, new Insets(20, 0,5,0));
        prefButton.setStyle(styleString);

        return prefButton;
    }

    private void formatRegistrationButton(GridPane gridPane,String styleString){
        Button registrationBotton = new Button("Sign Up");
        registrationBotton.setDefaultButton(true);
        registrationBotton.setPrefWidth(132);

        gridPane.add(registrationBotton, 0, 2, 2, 1);
        GridPane.setHalignment(registrationBotton, HPos.CENTER);
        registrationBotton.setStyle(styleString);
        registrationBotton.setOnAction(event -> createRegistrationTypePage());
    }

    private void formatPreferedStoreButton(GridPane gridPane,String styleString){
        Button prefStoreButton = new Button("Pref Store");
        prefStoreButton.setDefaultButton(true);
        prefStoreButton.setPrefWidth(150);

        gridPane.add(prefStoreButton, 0, 4, 2, 1);
        GridPane.setHalignment(prefStoreButton, HPos.CENTER);
        prefStoreButton.setStyle(styleString);
        prefStoreButton.setOnAction(event -> createPrefStorePage());
    }

    private Button formatSubmitNewStoreButton(GridPane gridPane,String styleString){
        Button newStore = new Button("CreateStore");
        newStore.setDefaultButton(true);
        newStore.setPrefWidth(150);

        gridPane.add(newStore, 0, 6, 2, 1);
        GridPane.setHalignment(newStore, HPos.CENTER);
        newStore.setStyle(styleString);
        newStore.setOnAction(event -> createAfterLoginAsManagerPage());
        return newStore;
    }

    private void formatSubmitStoreToScan(GridPane gridPane,String styleString){
        Button storeToScanButton = new Button("Select Store");
        storeToScanButton.setDefaultButton(true);
        storeToScanButton.setPrefWidth(150);

        gridPane.add(storeToScanButton, 0, 3, 2, 1);
        GridPane.setHalignment(storeToScanButton, HPos.CENTER);
        storeToScanButton.setStyle(styleString);
        storeToScanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //invia la richiesta e crea nuovo
                createAfterLoginAsScanClerkPage();
            }
        });
    }



    private void formatMonitorButton(GridPane gridPane,String styleString){
        Button monitorButton = new Button("Monitor Res.");
        monitorButton.setDefaultButton(true);
        monitorButton.setPrefWidth(150);

        gridPane.add(monitorButton, 0, 3, 2, 1);
        GridPane.setHalignment(monitorButton, HPos.CENTER);
        monitorButton.setStyle(styleString);
        monitorButton.setOnAction(event -> {
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("/reservations");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(email);
            List<ReservationStatusMessage> response = headersSpec.retrieve().bodyToFlux(ReservationStatusMessage.class).collectList().block();

            if(response != null){
                ArrayList <ReservationStatusMessage> lista = new ArrayList<>(response);
                createMonitorReservationPage(lista);
            }
            else
                System.out.println("Error in /reservations call");
        });

    }
    private void formatLineUpButton(GridPane gridPane,String styleString){
        Button lineUpBotton = new Button("Line Up");
        lineUpBotton.setDefaultButton(true);
        lineUpBotton.setPrefWidth(150);

        gridPane.add(lineUpBotton, 0, 1, 2, 1);
        GridPane.setHalignment(lineUpBotton, HPos.CENTER);
        lineUpBotton.setStyle(styleString);
        lineUpBotton.setOnAction(event -> {
            reservationType = "lineUp_Request";
            createStoreSelectionPage();
            // put line up request from controller to server
        });
    }

    private void formatBookAVisitButton(GridPane gridPane,String styleString){
        Button bookAVisitButton = new Button("Book a Visit");
        bookAVisitButton.setDefaultButton(true);
        bookAVisitButton.setPrefWidth(150);

        gridPane.add(bookAVisitButton, 0, 2, 2, 1);
        GridPane.setHalignment(bookAVisitButton, HPos.CENTER);
        bookAVisitButton.setStyle(styleString);
        bookAVisitButton.setOnAction(event -> {
            // put book a visit request from controller to server
            reservationType = "booking_Request";
            createStoreSelectionPage();
        });
    }

    private Button formatSubmitLoginButton(GridPane gridPane,String styleString){
        Button submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(130);
        gridPane.add(submitButton, 0, 3, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,5,0));

        submitButton.setStyle(styleString);
        return submitButton;

    }

    private Button formatSubmitRegistrationButton(GridPane gridPane,String styleString){
        Button submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(130);
        gridPane.add(submitButton, 0, 3, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,5,0));
        submitButton.setStyle(styleString);
        return submitButton;

    }

    private void formatGoBackButtonAfterLogin(GridPane gridPane,String styleString){
        Button backButton = new Button("Back");
        backButton.setDefaultButton(true);
        backButton.setPrefWidth(130);
        gridPane.add(backButton, 0, 4, 2, 1);
        GridPane.setHalignment(backButton, HPos.CENTER);
        backButton.setStyle(styleString);
        backButton.setOnAction(event -> createAfterLoginAsCustomerPage());
    }

    private void formatGoBackButton(GridPane gridPane,String styleString){
        Button backButton = new Button("Back");
        backButton.setDefaultButton(true);
        backButton.setPrefWidth(130);
        gridPane.add(backButton, 0, 4, 2, 1);
        GridPane.setHalignment(backButton, HPos.CENTER);
        backButton.setStyle(styleString);
        backButton.setOnAction(event -> createInitianlPage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}