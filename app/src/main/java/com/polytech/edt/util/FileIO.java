package com.polytech.edt.util;

import android.content.Context;

import com.polytech.edt.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

public class FileIO {

    public static String path;

    static {
        path = "data/data/" + App.class.getPackage().getName() + "/";
    }

    public static void write(Context context, String filename, String content) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public static void write(String filename, String content) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(filename));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public static boolean exists(String path) {
        return new File(path).exists();
    }

    public static boolean dirExists(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public static boolean mkdir(String path) {
        return new File(path).mkdir();
    }

    public static Collection<File> files(String path) {
        return new TreeSet<>(Arrays.asList(new File(path).listFiles()));
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
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

    public static String fileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
