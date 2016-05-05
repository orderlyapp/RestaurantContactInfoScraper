package com.orderly.restaurantcontactinfoscraper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;

/**
 * Created by joshuaking on 5/2/16.
 */
public class FileUtils {
    public static void createAndWriteToFile(File file, String data, boolean openWhenDone, OpenOption... options) throws IOException {
        file.createNewFile();
        if (file.exists() && file.canWrite()) {
            Files.write(file.toPath(), data.getBytes(), options);
            if (openWhenDone) {
                Desktop.getDesktop().open(file);
            }
        } else {
            System.out.println(data);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("An error occurred while creating the file so the data was output into the console.");
        }
    }
}
