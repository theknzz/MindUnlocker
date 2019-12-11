package pt.isec.mindunlocker;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Class to save and load games in <var>path</var>,
 * location inside app's <file>files</file> folder.
 *
 * @author Gonçalo Andrade
 */

public class SavedGames {


    private Context context;

    private static final String FOLDER = "Saved_Games/";
    private static final int MAX_SAVED_GAMES = 5;

    public SavedGames(Context context) {
        this.context = context;
    }

    /**
     * Save game
     *
     * @param gameEngine <code>GameEngine</code> object to save it
     * @param filename   <code>String</code> of the file name to give the file
     * @return <code>boolean</code> - <code>true</code> if game has been saved and
     * <ocde>false</ocde> if name already exist or was there any other problem saving
     */
    public boolean saveGame(GameEngine gameEngine, String filename) {

        if (checkIfNameExists(filename)) {
            Toast.makeText(context, "File name already exists, please enter " +
            "another name", Toast.LENGTH_SHORT).show();
            return false;
        }

        checkFiles();

        try {
            //Path files = /data/user/0/pt.isec.mindunlocker/files
            FileOutputStream fOs = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oOs = new ObjectOutputStream(fOs);

            oOs.writeObject(gameEngine);
            oOs.flush();
            oOs.close();
            fOs.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "There was a problem saving the game!",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Load Game
     *
     * @param filename <code>String</code> with name of the file to load
     * @return - a object <code>GameEngine</code> read from file
     */
    public GameEngine loadGame(String filename) {

        if (!checkIfNameExists(filename)) return null;

        try {

            FileInputStream fIs = context.openFileInput(filename);
            ObjectInputStream oIs = new ObjectInputStream(fIs);

            GameEngine gE = (GameEngine) oIs.readObject();

            oIs.close();
            fIs.close();

            return gE;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("aaaa", ""+e.getMessage());
            Toast.makeText(context, "There was a problem with the load game!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * Verify if exist any file in folder with the same name
     *
     * @param filename <code>String</code> with the file name
     * @return <code>boolean</code> - <code>true</code> if exists a file with the same name,
     * <code>false</code> if not
     */
    private boolean checkIfNameExists(String filename) {
        File[] files = getAllSavedGames();
        if (files != null && files.length > 0)
            for (File f : files) {
                if (f.getName().compareTo(filename) == 0) {
                    return true;
                }
            }
        return false;
    }

    /**
     * Method to verify and manage saved games files.
     * If the folder default has more than <var>MAX_SAVED_GAMES</var>
     * of files the oldest file will be removed
     */
    private void checkFiles() {
        File[] files = getAllSavedGames();

        if (files != null && files.length >= MAX_SAVED_GAMES) {
            deleteFile(olderFile(files));
        }
    }

    /**
     * Verify which file from <code>File[]</code> is the oldest
     *
     * @param files <code>File[]</code> Arrays of the <code>File</code> to check
     * @return <code>File</code> - oldest from Array
     */
    private File olderFile(File[] files) {
        long time = files[0].lastModified();
        File f = null;

        for (File aux : files) {
            if (aux.lastModified() <= time) {
                time = aux.lastModified();
                f = aux;
            }
        }

        return f;
    }

    /**
     * Get all Files / Saved Games are in the default folder
     *
     * @return <code>File[]</code> - All files that default folder have inside
     */
    public File[] getAllSavedGames() {
        File directory = context.getFilesDir();
        return directory.listFiles();
    }

    /**
     * Delete file passed by parameter
     *
     * @param file <code>File</code> to remove
     */
    private void deleteFile(Object file) {
        File f = null;

        if (file instanceof String)
            f = new File((String) file);
        else if (file instanceof File)
            f = (File) file;

        if (f != null) {
            Toast.makeText(context, "Saved game " + f.getName() + " was removed " +
                    "(MAX Saved Games : 5)", Toast.LENGTH_LONG).show();
            f.delete();
        }
    }
}
