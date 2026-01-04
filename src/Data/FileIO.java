package Data;

import java.io.*;
import Bank.*;

public class FileIO {

    private static final String DATA_FILE = "data";

    // Keep as static for now to avoid changing system design.
    // We improve reliability without changing external behavior.
    public static Bank bank = null;

    public static void Read() {
        // If file doesn't exist or read fails, fall back safely to a new Bank
        try (FileInputStream fis = new FileInputStream(DATA_FILE);
             ObjectInputStream oin = new ObjectInputStream(fis)) {

            Object obj = oin.readObject();
            if (obj instanceof Bank) {
                bank = (Bank) obj;
            } else {
                bank = new Bank();
            }

        } catch (FileNotFoundException e) {
            // First run: no persisted data yet
            bank = new Bank();

        } catch (IOException | ClassNotFoundException e) {
            // Corrupt file / incompatible serialization / IO failure
            bank = new Bank();
            // Optional: log for debugging (kept minimal for coursework)
            // e.printStackTrace();
        }
    }

    public static void Write() {
        // If bank is null, ensure we don't serialize a null reference unexpectedly
        if (bank == null) {
            bank = new Bank();
        }

        try (FileOutputStream fout = new FileOutputStream(DATA_FILE);
             ObjectOutputStream out = new ObjectOutputStream(fout)) {

            out.writeObject(bank);
            out.flush();

        } catch (IOException e) {
            // Optional: log for debugging
            // e.printStackTrace();
        }
    }
}
