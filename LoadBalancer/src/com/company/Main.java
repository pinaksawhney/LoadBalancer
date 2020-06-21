package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class to generate random outputs servers based on capacity weights across multiple runs
 *
 * */

public class Main {

    /** HashMap to persist metadata across multiple runs */
    static ConcurrentHashMap<Integer, RandomCollection<String>> metaData =  new ConcurrentHashMap<>();

    /**
     * Main driver
     * * */
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name = reader.readLine();
        HashMap<String, Integer> server = parseInput(name);
        int hash = calculateHash(server);

        int total=0;
        for (Map.Entry<String,Integer> entry : server.entrySet()) {
            total+=entry.getValue();
        }

        File tmpDir = new File("data.ser");
        RandomCollection<String> rc = null;
        if(tmpDir.exists()) {
            readHashMap();
            if(metaData.containsKey(hash))  rc = metaData.get(hash);
            else {
                rc = new RandomCollection<>();
                for (Map.Entry<String, Integer> entry : server.entrySet()) {
                    int percentage = (entry.getValue() * 100) / total;
                    rc.add(percentage, entry.getKey());
                }
            }
        } else {
            // Run Random class here
            rc = new RandomCollection<>();

            for (Map.Entry<String, Integer> entry : server.entrySet()) {
                int percentage = (entry.getValue() * 100) / total;
                rc.add(percentage, entry.getKey());
            }
        }

        System.out.println(rc.next());
        metaData.put(hash, rc);
        writeHashMap();
    }

    /**
     * calculates hash value for the given input string to avoid collisions
     *
     * @param server: parsed input map having server weights and names
     * @return calculated hash value
     * */
    public static Integer calculateHash(HashMap<String, Integer> server) {
        Integer hash = 0;
        for (Map.Entry<String,Integer> entry : server.entrySet()) {
            hash+=(entry.getValue()*100);
            hash+=((entry.getKey().charAt(0)-'A')*26);
        }
        return hash;
    }

    /**
     * save metaData and write the Map in local store to persist across multiple runs
     *
     *  */
    public static void writeHashMap() throws IOException {
        // Convert Map to byte array and save to file
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.ser"));
        out.writeObject(metaData);
        out.close();
    }

    /**
     * Load bytes from the local file and convert it to hashMap
     *
     * @return : loaded metadata hashmap from the saved file
     *  */
    public static ConcurrentHashMap<Integer, RandomCollection<String>> readHashMap() {
        try {
            FileInputStream fis = new FileInputStream("data.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            metaData = (ConcurrentHashMap<Integer, RandomCollection<String>>) ois.readObject();
            ois.close();
            fis.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return metaData;
    }

    /**
     * parse input and get server names and weights
     *
     * @param input: unparsed input as a string
     * @return : hashmap with server name as key and server weights as value
     * */
    private static HashMap<String, Integer> parseInput(String input) {
        HashMap<String, Integer> commandMap  = new HashMap<>();
        String[] serversWeights = input.split("\\s+");

        for(int i=0; i<serversWeights.length; i++) {
            String[] perServer = serversWeights[i].split(":");
            commandMap.put(perServer[0], Integer.parseInt(perServer[1]));
        }
        return commandMap;
    }
}
