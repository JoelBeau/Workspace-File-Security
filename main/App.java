package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import attributes.Account;
import find.FileSearcher;

public class App {

    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to File security application center!!");
        System.out.println("Please enter your user name and pasword to begin: ");

        Account a = new Account();

        if (a.login()) {
            System.out.println("You have successfully logged into your account!");
            System.out.println("Please enter the directory of your workspace files before you can continue ->");
            FileSearcher fs = new FileSearcher(new File(in.nextLine()));
            menu(fs, a);
        }

    }

    /**
     * User Interface menu in the terminal for the user to decide what they would
     * like to do in the application. Gives them 6 different options each invoking a
     * different method from either the {@link attributes.Account} class or
     * {@link find.FileSearcher} class. If the user decides to change account
     * information the {@link #changeInfoMenu(Account, FileSearcher)} method is
     * called. Each time the user decides to change the security of the files
     * (locking or unlocking them) {@link attributes.Account#updateAction(String)}
     * method is called and updated appropiately. Calls itself after every choice
     * until the user decides to exit. If the user decides to change the directory
     * of where they would like to change the security of their files
     * 
     * @param fs An instance of the object {@link find.FileSearcher}
     * @param a  An instance of the object {@link attributes.Account}
     * @throws IOException If files in specified directory can not be found as used
     *                     by {@link find.FileSearcher#lockAll()},
     *                     {@link find.FileSearcher#unlockAll()}, and the
     *                     {@link find.FileSearcher} object instance
     * 
     * @see attributes.SrcFile#lockFile()
     * @see attributes.SrcFile#unlockFile()
     * @see find.FileSearcher#lockAll()
     * @see find.FileSearcher#unlockAll()
     */
    static void menu(FileSearcher fs, Account a) throws IOException {
        System.out.printf("\nCurrent Directory: %s\n", fs.toString());
        System.out.println("Options are as follows:\n");
        System.out.println("1. Lock all of your workspace files in the directory denoted by you");
        System.out.println("2. Unlock all of your workspace files in the directory denoted by you");
        System.out.println("3. Check the status of your workspace in the directory denoted by you");
        System.out.println("4. Change directory");
        System.out.println("5. Update account information");
        System.out.println("6. Exit the file application security center");
        String action = a.lastAction;

        switch (in.nextLine()) {

            case "1":
                System.out.printf(
                        "You are about to lock all of the files in the directory: %s do you want to continue? (y/n)\n",
                        fs.toString());
                if (in.nextLine().equals("y"))
                    if (fs.lockAll()) {
                        action = "locked";
                        System.out.println("\nAll files have been successfully locked!");
                    }
                break;

            case "2":
                System.out.printf(
                        "You are about to unlock all of the files in the directory: %s do you want to continue? (y/n)\n",
                        fs.toString());
                if (in.nextLine().equals("y"))
                    if (fs.unlockAll()) {
                        action = "unlocked";
                        System.out.println("\nAll files have been successfully unlocked!");
                    }
                break;

            case "3":
                System.out.printf("\nThe status of your files are: %s\n", a.lastAction);
                break;
            case "4":
                System.out.println("Please enter the directory you wish to naviage to:");
                fs = new FileSearcher(new File(in.nextLine()));
                System.out.println("\nDirectory successfully changed!");
                break;
            case "5":
                changeInfoMenu(a, fs);
                break;
            case "6":
                System.out.println("You have successfully logged out!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, try again!");
                menu(fs, a);

        }

        a.updateAction(action);
        System.out.println("You are back at the main menu, please enter a option!");
        menu(fs, a);

    }

    /**
     * 
     * User menu in the terminal to decide what they would like to do with their
     * account information. Displays the current account information of the user
     * with their username being visible and their password being replaced with 7
     * astriks. Given three choices: change username, change password, or exit back
     * to main menu. For the first two choices it will then use the
     * {@link attributes.Account#changeInfo(String)} method with the choice they
     * choose respectively as the parameter. If user would like to exit, it calls
     * the {@link #menu(FileSearcher, Account)} to travel back to the main menu of
     * the application. Keeps going until the user exits to the main menu.
     * 
     * @param a  An instance of the object {@link attributes.Account}
     * @param fs An instance of the object {@link find.FileSearcher}
     * @throws IOException If something goes wrong in the
     *                     {@link attributes.Account#changeInfo(String)} method when
     *                     updating the user account information
     */

    static void changeInfoMenu(Account a, FileSearcher fs) throws IOException {
        System.out.println("Your current account info is as follows:");

        System.out.printf("\nUsername: %s\n", a.getUser());
        System.out.println("Password: *******\n");

        System.out.println("1. Change username");
        System.out.println("2. Change password");
        System.out.println("3. Exit back to main menu");

        switch (in.nextLine()) {
            case "1":
                System.out.println("Please login again to continue:");
                if (a.login()) {
                    a.changeInfo("username");
                    System.out.printf("\nYou have successfully changed your user name to: %s\n", a.getUser());
                    break;
                }
            case "2":
                System.out.println("Please login again to continue:");
                if (a.login()) {
                    a.changeInfo("password");
                    System.out.println("\nYou have successfuly changed your password!");
                    break;
                }
            case "3":
                System.out.println("You are back at the main menu!");
                menu(fs, a);
                break;
            default:
                System.out.println("Invalid option, try again!");
                changeInfoMenu(a, fs);

        }

        changeInfoMenu(a, fs);

    }

}