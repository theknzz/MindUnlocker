package pt.isec.mindunlocker;

import android.content.Context;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ReadWriteFiles {

    File directory;
    File file;

    private Context context;


    public ReadWriteFiles(Context context) {
        this.context = context;
        directory = context.getCacheDir();
    }

    public void writeFile(GameplayActivity gameplayActivity, String filename) {
        try {
            FileOutputStream fOs = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oOs = new ObjectOutputStream(fOs);
            oOs.writeObject(gameplayActivity);
            oOs.flush();
            oOs.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void readFile(String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);

            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                // Error occurred when opening raw file for reading.
            } finally {
                String contents = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void deleteFile(String filename) {
            File file = new File(filename);

            file.delete();
    }
}
