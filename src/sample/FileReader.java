package sample;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileReader {

    private String audioFolderName;
    public ArrayList<File> audioFileList = new ArrayList<>();
    public ArrayList<String> fileNames = new ArrayList<>();
    public ArrayList<String> translationAudioFiles = new ArrayList<>();
    public ArrayList<String> translations = new ArrayList<>();


    private Main main;


    public FileReader(String audioFolderName, Main main){

        this.main = main;
        this.audioFolderName = audioFolderName;

    }

    public void readTranslations(String audioListFilename, String translationsFileName){

        File audioListFile = new File ("translations/"+audioListFilename);
        File translationsListFile = new File ("translations/"+translationsFileName);

        if(!audioListFile.exists()){

            System.out.println("File audioList not found");
            return;

        }
        if(!translationsListFile.exists()){

            System.out.println("File translations not found");
            return;

        }

        try {
            translationAudioFiles = (ArrayList<String>) Files.readAllLines(Paths.get("translations/" + audioListFilename));
            translations = (ArrayList<String>) Files.readAllLines(Paths.get("translations/" + translationsFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getTranslation(String audioFile){

        if(translationAudioFiles.isEmpty() || translations.isEmpty()){
            return "no translations found";
        }

        int pointer = translationAudioFiles.indexOf(audioFile);
        if(pointer <= 0) return "no translations found";
        String translation = translations.get(pointer);
        if(translation == null) return "no translations found";
        return translation;

    }

    public void readAudioFiles(){

        File audioDirectory = new File(audioFolderName);

        if(audioDirectory.isDirectory()){

            File[] audioFiles = audioDirectory.listFiles();

            if(audioFiles == null) {
                
                main.showStatus("No audio files");
                return;

            };

            for (File audioFile : audioFiles) {

                if(audioFile.getName().contains(".mp3") ||audioFile.getName().contains(".wav"))
                audioFileList.add(audioFile);
                fileNames.add(audioFile.getName());

            }

        }else{
            main.showStatus("No audio files");
            return;
        }

    }

}
