import java.util.Scanner;

/**
 * @author Rohan More
 * @project Cryptography-RSA
 */
public class entry {

    public static void main(String[] args) {
        RSA rsa = new RSA();
        if (!rsa.generateKeys()) {
            System.exit(0);
        }

        menu();
        String option;
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("Enter option: ");
            option = scan.next();
            switch (option) {
                case "1":
                    rsa.encrypt();
                    break;
                case "2":
                    rsa.decrypt();
                    break;
                case "3":
                    rsa.generateKeys();
                    break;
                case "4":
                    getHistory(rsa);
                    break;
                case "5":
                    rsa.saveConfig();
                    break;
                case "6":
                    rsa.loadConfig();
                    break;
                case "7":
                    rsa.viewConfig();
                    break;
                case "8":
                    menu();
                    break;
                case "9":
                    System.exit(0);
                default:
                    System.out.println("Command not found...\n");
            }
        }
    }

    /**
     * Display the help menu
     */
    static void menu() {
        System.out.println("---------------RSA MENU---------------\n" +
                "1) Encrypt Message\n" +
                "2) Decrypt Message\n" +
                "3) Generate New Keys\n" +
                "4) View History\n" +
                "5) Save Config\n" +
                "6) Load Config\n" +
                "7) View Config\n" +
                "8) Help Menu\n" +
                "9) Exit\n" +
                Color.RED + "Note: Please only enter Integers when encrypting and decrypting (BigIntegers)" + Color.RESET + "\n" +
                "--------------------------------------\n");
    }

    /**
     * Get the last encrypted value and decrypted value the user entered
     */
    static void getHistory(RSA rsa) {
        System.out.println("\nLast Message Encrypted: " + rsa.lastEncryptedMessage);
        System.out.println("Last Message Decrypted: " + rsa.lastDecryptedMessage + "\n");
    }

}
