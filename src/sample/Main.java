package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<ActionEvent> {

    private Button button;
    private TextArea inputTextBox;
    private TextField inputAudioListFileName;
    private TextField inputTranslationsFile;
    private Label errorLabel;
    private Label nowPlayingLabel;
    private Label nowPlayingPhraseLabel;

    private Label audioListFileLabel;
    private Label translationsFileLabel;
    private Label statusTitleLabel;
    private Label nowPlayingTitleLabel;
    private Label nowPlayingPhraseTitleLabel;

    private FileReader fileReader = new FileReader("audio", this);
    private PhraseReader reader = new PhraseReader(fileReader, this);


    @Override
    public void start(Stage primaryStage) throws Exception{

        fileReader.readAudioFiles();

        button = new Button();
        button.setText("Play");
        button.setOnAction(this);
        button.setMinWidth(100);

        inputTextBox = new TextArea();
        inputTextBox.setMaxHeight(100);
        inputTextBox.setMinWidth(800);

        errorLabel = new Label();
        nowPlayingLabel = new Label();
        audioListFileLabel = new Label();
        translationsFileLabel = new Label();
        statusTitleLabel = new Label();
        nowPlayingTitleLabel = new Label();
        nowPlayingPhraseLabel = new Label();
        nowPlayingPhraseTitleLabel = new Label();
        inputAudioListFileName = new TextField();
        inputTranslationsFile = new TextField();


        GridPane grid = new GridPane();
        grid.setPadding( new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(8);


        grid.add(audioListFileLabel, 0, 0, 1, 1);
        grid.add(inputAudioListFileName, 1, 0, 1, 1);
        grid.add(translationsFileLabel, 0, 1, 1, 1);
        grid.add(inputTranslationsFile, 1, 1, 1, 1);
        grid.add(inputTextBox, 1, 2, 1, 1);
        grid.add(button, 0, 2, 1, 1);
        grid.add(nowPlayingTitleLabel, 0, 3, 1,1);
        grid.add(nowPlayingPhraseTitleLabel, 0, 4, 1,1);
        grid.add(statusTitleLabel, 0, 5, 1, 1);
        grid.add(nowPlayingLabel, 1, 3, 1,1);
        grid.add(nowPlayingPhraseLabel, 1, 4, 1, 1);
        grid.add(errorLabel, 1, 5, 1, 1);

        nowPlayingTitleLabel.setText("Now playing: ");
        nowPlayingPhraseTitleLabel.setText("Phrase playing: ");
        statusTitleLabel.setText("Status: ");


        audioListFileLabel.setText("Audio List file: ");
        translationsFileLabel.setText("Translations file: ");

        inputAudioListFileName.setText("IT1.txt");
        inputTranslationsFile.setText("IT2.txt");

        primaryStage.setTitle("HeyClayAudio");
        primaryStage.setScene(new Scene(grid, 820, 270));
        primaryStage.show();

    }


    public static void main(String[] args) {

        launch(args);

    }


    @Override
    public void handle(ActionEvent actionEvent) {

        showStatus("Playing");
        fileReader.readTranslations(inputAudioListFileName.getText(), inputTranslationsFile.getText());

        // clean phrase
        String phrase = inputTextBox.getText();


        int start = Math.min(
                phrase.indexOf('[') >= 0? phrase.indexOf('[') :40,
                phrase.indexOf('{') >=0? phrase.indexOf('{'): 40);
        int end = Math.max(
                phrase.lastIndexOf(']'),
                phrase.lastIndexOf('}'));

        reader.parsePhrase(phrase.substring(start, end));

        reader.playPhrase();

    }

    public void showStatus(String message){

        errorLabel.setText(message);

    }

    public void showNowPlaying(String message){

        nowPlayingLabel.setText(message);

    }

    public void showNowPlayingPhrase(String message){

        nowPlayingPhraseLabel.setText(message);

    }

}
