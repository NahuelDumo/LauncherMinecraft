package launcher;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import launcher.utilities.FileDownlander;

import java.net.URL;

public class LauncherView extends Application {
    private TextField nameField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Launcher de Minecraft");

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 1280, 720);

        URL videoUrl = getClass().getResource("/minecraftinicio.mp4");
        Media media = new Media(videoUrl.toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Línea divisoria en la parte inferior
        Line separatorLine = new Line(0, scene.getHeight() - 60, scene.getWidth(), scene.getHeight() - 60);
        separatorLine.setStroke(Color.rgb(200, 200, 200));

        // Crear el rectángulo en la parte inferior
        Pane bottomPane = new Pane();
        bottomPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        bottomPane.setPrefHeight(60);
        bottomPane.getChildren().add(separatorLine);

        // Campo de nombre
        nameField = new TextField();
        nameField.setPromptText("Ingresa tu nombre:");
        nameField.setPrefWidth(150);
        nameField.setFocusTraversable(false); // Evita que el campo de nombre esté seleccionado por defecto

        // Establecer posición en la esquina inferior izquierda
        nameField.setLayoutX(10); // Ajusta esta coordenada X según tus necesidades
        nameField.setLayoutY(scene.getHeight() - 50); // Ajusta esta coordenada Y según tus necesidades

        // Botón "Noticias"
        Button newsButton = new Button("-- Noticias --");
        newsButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-text-fill: white;");
        newsButton.setPrefSize(120, 20);
        newsButton.setLayoutX(scene.getWidth() - newsButton.getPrefWidth() - 10);
        newsButton.setLayoutY(scene.getHeight() - 50);
        newsButton.setOnAction(event -> showNews());

        // Botón "Jugar"
        Button playButton = new Button("Jugar");
        playButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-text-fill: white;");
        playButton.setPrefSize(120, 40);
        playButton.setLayoutX((scene.getWidth() - playButton.getPrefWidth()) / 2);
        playButton.setLayoutY(scene.getHeight() - 50);
        playButton.setOnAction(event -> {
            handlePlayButtonClick();
            nameField.getParent().requestFocus(); // Hacer que el campo de nombre obtenga el foco
        });

        // Añadir elementos al rectángulo inferior
        bottomPane.getChildren().addAll(nameField, newsButton, playButton);

        StackPane.setAlignment(bottomPane, Pos.BOTTOM_CENTER);
        root.getChildren().addAll(mediaView, bottomPane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handlePlayButtonClick() {
        String playerName = nameField.getText().trim();
        System.out.println(playerName);
        if (!playerName.isEmpty()) {
            FileDownlander.main(null, playerName);
            
            
        } else {
            showWarningAlert("Por favor, ingrese un nombre.");
        }
    }

    private void showWarningAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showNews() {
        Alert newsAlert = new Alert(AlertType.INFORMATION);
        newsAlert.setTitle("Noticias");
        newsAlert.setHeaderText("Servidor actual: Servidor XYZ");
        newsAlert.setContentText("¡Bienvenidos a nuestro servidor de Minecraft! Estamos emocionados de tenerte aquí. ¡Diviértete jugando!");
        newsAlert.showAndWait();
    }
}

/*
 * cambios pendientes: 
 * -Agregar barra visual que disponga informacion sobre como prosigue la descarga y/o instalacion de minecraft
 */