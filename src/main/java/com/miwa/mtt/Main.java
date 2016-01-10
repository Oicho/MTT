package com.miwa.mtt;

import com.miwa.mtt.util.ApplicationProperties;


import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: mtt stubname1 [stubname2, ...]");
            return;
        }

        ApplicationProperties.getInstance().load("application.properties");
        for (String stubPath : args) {
            try {
                new DirectoryBrowser().run(stubPath);
            } catch (FileNotFoundException e) {
                System.err.println("Couldn't find stub's files matching: " + stubPath);
            }
        }
    }
}
