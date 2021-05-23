import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Rohan More
 * @project Cryptography-RSA
 */
public class RSA {

    BigInteger p, q, e;
    BigInteger n, z, d;
    BigInteger lastEncryptedMessage;
    String lastDecryptedMessage;
    boolean autoCopy;

    /**
     * Save the current keys to a config text file
     */
    public void saveConfig() {
        try {
            FileWriter config = new FileWriter("./config.txt");
            config.write(p.toString(16));
            config.write("\n");
            config.write(q.toString(16));
            config.write("\n");
            config.write(e.toString(16));
            config.write("\n");

            config.write(n.toString(16));
            config.write("\n");
            config.write(z.toString(16));
            config.write("\n");
            config.write(d.toString(16));
            config.write("\n");
            config.write(Boolean.toString(autoCopy));
            config.close();

            System.out.println("Successfully saved config!\n");

        } catch (IOException ioException) {
            System.out.println("Failed to save config!\n");
        }
    }

    /**
     * Load a config text file from the users machine
     */
    public void loadConfig() {
        try {
            Scanner config = new Scanner(new File("./config.txt"));
            ArrayList<BigInteger> values = new ArrayList<>();
            while (config.hasNext()) {
                if (config.hasNextBoolean()) {
                    String temp = config.nextLine();
                    autoCopy = Boolean.parseBoolean(temp);
                    break;
                }
                values.add(new BigInteger(config.nextLine(), 16));
            }
            config.close();

            p = values.get(0);
            q = values.get(1);
            e = values.get(2);
            n = values.get(3);
            z = values.get(4);
            d = values.get(5);

            System.out.println("Successfully loaded config!\n");
            viewConfig();

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Couldn't find your config!\n");
        }
    }

    /**
     * View the current installed config file
     */
    public void viewConfig() {
        System.out.println(
                "---------------Current Config---------------\n" +
                "p = " + p + "\n" +
                "q = " + q + "\n" +
                "e = " + e + "\n" +
                "n = " + n + "\n" +
                "z = " + z + "\n" +
                "d = " + d + "\n" +
                "Automatically copy to clipboard: " + autoCopy + "\n" +
                "--------------------------------------------\n");
    }

    /**
     * Generate keys and required variables
     */
    public boolean generateKeys() {
        System.out.println("Generating Keys, please wait...");
        try {
            //Delete history of messages
            lastDecryptedMessage = null;
            lastEncryptedMessage = null;

            //Generate P, Q, and E
            p = generatePrime(512, 8);
            q = generatePrime(512, 16);
            e = generatePrime(1024, 1);

            //Calculate N, Z, D
            n = calcN(p, q);
            z = calcZ(p, q);
            d = calcD(e, z);
            System.out.println("Done generating keys!");
        } catch (Exception exception) {
            System.out.println("Failed to generate RSA keys...\n" + exception + "\n");
            return false;
        }
        return true;
    }

    /**
     * Generate a probable prime number
     * This method accepts two integers bits and multiplier used to generate a random value
     */
    public BigInteger generatePrime(int bits, int multiplier) {
        Random rand = new Random(System.nanoTime() * multiplier);
        return BigInteger.probablePrime(bits, rand);
    }

    /**
     * Calculate N
     * This method accepts two BigIntegers and returns the product of them
     */
    public BigInteger calcN(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }

    /**
     * Calculate Z
     * This method accepts two BigIntegers and returns (p - 1) * (q - 1)
     */
    public BigInteger calcZ(BigInteger p, BigInteger q) {
        return (p.subtract(BigInteger.valueOf(1))).multiply(q.subtract(BigInteger.valueOf(1)));
    }

    /**
     * Calculate D
     * This method accepts two BigIntegers and returns e^-1 mod z
     */
    public BigInteger calcD(BigInteger e, BigInteger z) {
        return e.modInverse(z);
    }

    /**
     * Encrypt Plain Text
     * This method accepts a decrypted message and uses pre generated keys
     * returns plainText^e mod n
     */
    public BigInteger encrypt(BigInteger plainText) {
        return plainText.modPow(e, n);
    }

    /**
     * Encrypt the users message
     */
    public void encrypt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter message to encrypt: ");
        String decryptedMessage = scan.nextLine();

        BigInteger byteMessage = new BigInteger(decryptedMessage.getBytes());
        BigInteger encryptedMessage = encrypt(byteMessage);

        //Save a record of what was encrypted
        lastEncryptedMessage = encryptedMessage;

        //BASE64 Encoded Encrypted Message
        //System.out.println("Encrypted Message: " + Color.GREEN + Base64.getUrlEncoder().encodeToString(encryptedMessage.toByteArray()) + Color.RESET + "\n");
        System.out.println("Encrypted Message: " + Color.GREEN + encryptedMessage + Color.RESET + "\n");

        //Copy encrypted message to clipboard
        if (autoCopy) {
            StringSelection message = new StringSelection(encryptedMessage.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(message, null);
        }
    }

    /**
     * Decrypt Cipher Text
     * This method accepts a encrypted message and uses pre generated keys
     * returns cipherText^d mod n
     */
    public BigInteger decrypt(BigInteger cipherText) {
        return cipherText.modPow(d, n);
    }

    /**
     * Decrypt the users message
     */
    public void decrypt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter message to decrypt: ");
        String encryptedMessage = scan.nextLine();

        //BASE64 DECODE
        //BigInteger byteMessage = new BigInteger(Base64.getUrlDecoder().decode(encryptedMessage));
        BigInteger byteMessage = new BigInteger(encryptedMessage);
        BigInteger decryptedMessage = decrypt(byteMessage);

        //Save a record of what was decrypted
        lastDecryptedMessage = new String(decryptedMessage.toByteArray());

        System.out.println("Decrypted Message: " + Color.GREEN + new String(decryptedMessage.toByteArray()) + Color.RESET + "\n");
    }

}
