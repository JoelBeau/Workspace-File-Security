package attributes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * An object class used to store information about the user's account, which
 * includes the account's username, password and the last known action of the
 * security status of files which is denoted by the user. If any information is
 * changed about the account it is updated both in the file and in this object,
 * regularly.
 */

public class Account {

    private String user;
    private String pass;
    public String lastAction;

    private static Scanner in = new Scanner(System.in);

    /**
     * Creates an new {@code Account} that sets the current username, password, and
     * last action of the user's account through the private currentInfo() method
     * 
     * @throws FileNotFoundException if it can not find the file found that is
     *                               denoted by the {@link #currentInfo()} method
     */
    public Account() throws FileNotFoundException {
        currentInfo();
    }

    /**
     * Gets current info about the current user account's username and password and
     * sets them into private variables. Private as it is invoked by
     * {@link #resetPassword(FileWriter, File, File)},
     * {@link #changeUser(FileWriter, File, File)}, and
     * {@link #updateAction(String)} and does not need to be accessed outside of
     * this class by other classes or the user
     * 
     * @throws FileNotFoundException if account info file is not found
     */
    private void currentInfo() throws FileNotFoundException {
        Scanner cur = new Scanner(new File("info.dat"));
        this.user = cur.nextLine();
        this.pass = cur.nextLine();
        this.lastAction = cur.nextLine();
        cur.close();

    }

    /**
     * Login interface in order to access the application's features, if username or
     * password is wrong, user is prompted to either reset it, barrring the their
     * username matches the account info, or request to view their username.
     * 
     * @returns {@code true} once user has logged in successfully
     * @throws IOException if account info file is not found in
     *                     {@link #changeInfo(String)}
     */

    public boolean login() throws IOException {

        System.out.println("User Name:");
        String user = in.nextLine();
        System.out.println("Password:");
        String password = in.nextLine();

        if (user.equals(this.user) && password.equals(this.pass))
            return true;
        else {
            System.out.println("Incorrect username or password!");
            System.out.println("Would you like to reset your password or find username? (y/n)");
            String chg = in.nextLine();
            if (chg.equals("y")) {
                System.out.println("What would you like to do, username or password? ");
                chg = in.nextLine();
                if (chg.equals("password")) {
                    System.out.println("Please confirm the username first");
                    if (in.nextLine().equals(this.user))
                        this.changeInfo("password");
                    else
                        System.out.println("Wrong user!");
                } else if (chg.equals("username"))
                    System.out.printf("Your username is: %s\n", this.user);
            } else
                System.out.println("Please try agian!");

        }

        return login();

    }

    /**
     * Changes the field with in the user's account information with either the
     * username or password. Creates a {@code File} object of the current account
     * file (info.dat) and creates a tmp {@code File} object, which is where the new
     * information will be written to using {@code FileWriter}. It then refers to
     * one of two methods, depending one which field the user wants to change.
     * 
     * Using either:
     * 
     * {@link #resetPassword(FileWriter, File, File)} or
     * {@link #changeUser(FileWriter, File, File)}
     * 
     * @param field the field to be changed with in account
     * @throws IOException if cur file or tmp file is not found
     * @see java.io.File#createNewFile()
     * @see java.io.FileWriter
     */

    public void changeInfo(String field) throws IOException {
        File cur = new File("info.dat");

        File tmp = new File("tmp.dat");
        tmp.createNewFile();

        FileWriter fw = new FileWriter(tmp);

        switch (field) {
            case "username":
                changeUser(fw, cur, tmp);
                break;
            case "password":
                resetPassword(fw, cur, tmp);
                break;

        }
    }

    /**
     * Method used to reset the password, writes uses {@code FileWriter} to wrtie
     * all of the information that is not being changed to the new account
     * information file. Then uses {@code Scanner} to obtain the new password from
     * the user, uses {@code FileWriter} again to write the new pasword to the file.
     * Deletes the old account info file and renames the new file to info.dat.
     * then updates the account info variables (username, password, lastaction)
     * using {@link #currentInfo()} method. Private because it is invoked by the
     * {@link #changeInfo(String)} method and does not need to be accessed outside
     * of class or by user.
     * 
     * @param fw  {@code FileWriter} object to write information to new file
     * @param cur Old info {@code File} object
     * @param tmp New info {@code File} object
     * @throws IOException if {@code FileWriter} object finds the file and it exists
     *                     but it is in a different directory, cannot be created, or
     *                     cannot be opened for any other reason
     * @see java.io.FileWriter
     */

    private void resetPassword(FileWriter fw, File cur, File tmp) throws IOException {

        fw.write(user + "\n");

        System.out.println("What would you like your new password to be?");
        fw.write(in.nextLine() + "\n");

        fw.write(this.lastAction);

        fw.flush();
        fw.close();

        cur.delete();
        tmp.renameTo(cur);

        currentInfo();

    }

    /**
     * Method used to change the username, writes uses {@code FileWriter} to wrtie
     * all of the information that is not being changed to the new account
     * information file. Then uses {@code Scanner} to obtain the new username from
     * the user, uses {@code FileWriter} again to write the new username to the
     * file.
     * Deletes the old account info file and renames the new file to info.dat.
     * then updates the account info variables (username, password, lastaction)
     * using {@link #currentInfo()}
     * 
     * @param fw  The {@code FileWriter} object used to write the new information to
     *            the new file
     * @param cur Old info {@code File} object
     * @param tmp New info {@Code File} object
     * @throws IOException if {@code FileWriter} finds the file and it exists but it
     *                     is in adifferent directory, cannot be created,
     *                     or cannot be opened for any other reason
     */

    public void changeUser(FileWriter fw, File cur, File tmp) throws IOException {

        System.out.println("What would you like your new user name to be?");
        fw.write(in.nextLine() + "\n");
        fw.write(this.pass + "\n");
        fw.write(this.lastAction);

        fw.flush();
        fw.close();

        cur.delete();
        tmp.renameTo(cur);

        currentInfo();

    }

    /**
     * Updates the last action of the security of the files in the denoted directory
     * by the user, uses the same concept as the
     * {@link #changeUser(FileWriter, File, File)} and
     * {@link #resetPassword(FileWriter, File, File)} methods. It creates a
     * {@code File} Object of the current account info file, and a {@code File}
     * Object of the new account info file. Creates a {@code FileWriter} Object used
     * to write to the new info file. Writes the information that is not being
     * changed i.e. the user's account and password. Uses the action variable to
     * determine what to write, which is either "locked" or "unlocked". Then updates
     * the account info variables through {@link #currentInfo()} method
     * 
     * @param action The new current status of the user's files
     * @throws IOException if {@code FileWriter} finds the file and it exists but it
     *                     is in adifferent directory, cannot be created,
     *                     or cannot be opened for any other reason
     * @see java.io.File#createNewFile()
     */
    public void updateAction(String action) throws IOException {

        File cur = new File("info.dat");

        File tmp = new File("tmp.dat");
        tmp.createNewFile();

        FileWriter fw = new FileWriter(tmp);

        fw.write(this.user + "\n");
        fw.write(this.pass + "\n");
        fw.write(action);

        fw.flush();
        fw.close();

        cur.delete();
        tmp.renameTo(cur);

        currentInfo();

    }

    /**
     * Gets the current value of the {@code String} variable user, which contains
     * the username of the user's account as denoted by the account info file called
     * info.dat
     * 
     * @return current value of the user variable
     */
    public String getUser() {
        return user;
    }

}
