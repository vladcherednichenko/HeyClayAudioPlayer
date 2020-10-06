package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PhraseReader {

    private ArrayList<ArrayList<String>> phraseParts = new ArrayList<>();
    private ArrayList<String> allParts = new ArrayList<>();
    private ArrayList<MediaPlayer> players = new ArrayList<>();
    private ArrayList<String> playersFileNames = new ArrayList();
    private FileReader fileReader;
    private Main main;

    private ArrayList<String> fileSequence = new ArrayList<String>();

    public PhraseReader(FileReader fileReader, Main main){

        this.fileReader = fileReader;
        this.main = main;

    }

    public void parsePhrase(String phrase){

        phraseParts.clear();
        allParts.clear();
        players.clear();
        fileSequence.clear();
        playersFileNames.clear();

        String[] tempParts;
        tempParts = phrase.split("\\+");

        for (String tempPart : tempParts) {

            System.out.println(tempPart);
            phraseParts.add(new ArrayList<>(Arrays.asList(tempPart
                    .replace("[", "")
                    .replace("]", "")
                    .replace("{", "")
                    .replace("}", "")
                    .split(","))));

        }

        for(ArrayList<String> list : phraseParts){

            allParts.addAll(list);

        }

        for (String s: allParts){
            System.out.println(s);
        }

    }

    public void playPhrase(){

        Random rand = new Random();
        ArrayList<String> phrase = new ArrayList<>();

        for (int i = 0; i < phraseParts.size(); i++){

            if(phraseParts.get(i).size()> 1){

                phrase.add(phraseParts.get(i).get(rand.nextInt(phraseParts.get(i).size()-1)));

            }else{

                phrase.add(phraseParts.get(i).get(0));

            }

        }

        playNext(0, phrase);

    }

    private void playNext(int fileNumber, ArrayList<String> sequence){

        if(fileNumber > sequence.size() - 1) { return; }

        String fileExt = "";

        if(sequence.get(fileNumber).contains("-")) {

            playSubSequence(
                    0,
                    new ArrayList<>(Arrays.asList(sequence.get(fileNumber).split("-"))),
                    fileNumber,
                    sequence);


        } else{

            if(new File("audio/" + sequence.get(fileNumber) + ".mp3").exists()){
                fileExt = ".mp3";
            }else if(new File("audio/" + sequence.get(fileNumber) + ".wav").exists()){
                fileExt = ".wav";
            }else{
                main.showStatus( "file " + sequence.get(fileNumber) + fileExt + " not found");
                return;
            }

            String filename = "audio/" + sequence.get(fileNumber) + fileExt;
            Media hit = new Media(new File(filename).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            players.add(mediaPlayer);
            playersFileNames.add(filename);

            if(fileNumber == sequence.size() -1){
                playAll(0);
            }else{
                playNext(fileNumber + 1, sequence);
            }

        }

    }

    private void playSubSequence(int filePointer, ArrayList<String> sequence, int mainFilePointer, ArrayList<String> mainSequence){

        if(filePointer > sequence.size() - 1) {

            return;

        }

        String fileExt = "";

        if(new File("audio/" + sequence.get(filePointer) + ".mp3").exists()){
            fileExt = ".mp3";
        }else if(new File("audio/" + sequence.get(filePointer) + ".wav").exists()){
            fileExt = ".wav";
        }else{
            main.showStatus( "file " + sequence.get(filePointer) + fileExt + " not found");
            return;
        }

        String filename = "audio/" + sequence.get(filePointer) + fileExt;
        Media hit = new Media(new File(filename).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        players.add(mediaPlayer);
        playersFileNames.add(filename);
        if(filePointer == sequence.size() -1){
            playNext(mainFilePointer + 1, mainSequence);
            if(filePointer == sequence.size() -1){
                playAll(0);
            }
        }else{
            playSubSequence(filePointer + 1, sequence, mainFilePointer, mainSequence);
        }

    }


    private void playAll(int fileNumber){

        if(fileNumber > players.size()-1){

            System.out.println("finished");
            main.showStatus("Finished");
            main.showNowPlaying("");
            main.showNowPlayingPhrase("");
            return;

        }

        players.get(fileNumber).setOnEndOfMedia(()->{

            playAll(fileNumber + 1);

        });

        players.get(fileNumber).play();
        main.showNowPlayingPhrase(
                fileReader.getTranslation(
                        playersFileNames.get(fileNumber)
                                .replace(".mp3", "")
                                .replace(".wav", "")
                                .replace("audio/", "")));
        main.showNowPlaying(playersFileNames.get(fileNumber));


    }


}
