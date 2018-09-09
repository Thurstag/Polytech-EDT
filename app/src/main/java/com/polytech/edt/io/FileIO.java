package com.polytech.edt.io;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileIO {

    public static void write(Context context, String filename, String content) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("ERROR", "", e);
        }
    }

    public static String read(Context context, String filename) {
        try {
            FileInputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String buffer;

                while ((buffer = bufferedReader.readLine()) != null) {
                    builder.append(buffer);
                }

                inputStream.close();
                return builder.toString();
            }
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            Log.e("ERROR", "", e);
        }
        return null;
    }
}
