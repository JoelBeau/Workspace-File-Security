package find;

import attributes.SrcFile;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Object in which it uses a file directory path to find all source files. Has
 * only three public method available by use for other classes as all the others
 * are used internally which are the {@link #lockAll()}, {@link #unlockAll()},
 * and the {@link #toString()} methods.
 * 
 */

public class FileSearcher {

    // Directory of all files
    private File dir;

    /**
     * {@code HashSet} of src files which are inputed as an object of
     * {@code SrcFile}
     */
    HashSet<SrcFile> srcFiles = new HashSet<>();

    /**
     * Constructs a FileSearcher object in which it sets the directory to the
     * {@code File} Object that is passed through
     * 
     * @param dir The File directory to be set to dir variable and to be searched
     * @see java.util.AbstractCollection#addAll(java.util.Collection)
     * @see #findFiles()
     */
    public FileSearcher(File dir) {
        this.dir = dir;
        srcFiles.addAll(findFiles());
    }

    /**
     * Finds all files under a directory. It provides provisions if there are
     * mutiple projects in the workspace, one project by using the
     * {@link #findSingular()} method, if the project does not
     * have any modules or packages by using {@link #checkPackages(File[])}, if there are
     * packages, in which case it gets every source code file under each package, or
     * if there are no files with in the project by using {@link #checkPackages(File[])}.
     * Gets all of the projects or files, depending on the structure of the
     * workspace by using the {@link java.io.File#list()}. Also provides provisions
     * for if the directory is a workspace used by the IDE Eclipse as they have
     * .metadata folders, it removes it from the list to search and it also ignores
     * any non source file or package folder. Once all src files have been found
     * it creates a new {@code SrcFile} Object and is added by the
     * {@link #addFilesWithPackages(File[])} method if they have packages, if not
     * they are added by the {@link #addFiles(File[])} method to the HashSet that is
     * to be returned. Private as it is invoked when creating a new instance of this
     * object and does not need to be accessed by other classes or the client.
     * 
     * @return {@code HashSet} of {@code SrcFile} Objects which is the source
     *         code of the project(s)
     */

    private HashSet<SrcFile> findFiles() {

        HashSet<SrcFile> files = new HashSet<>();

        LinkedList<String> projects = new LinkedList<>();
        projects.addAll(Arrays.asList(dir.list()));

        if (projects.getFirst().contains("metadata"))
            projects.removeFirst();

        if (Collections.frequency(projects, "src") == 1) {
            files.addAll(findSingular());
            return files;
        }

        int rm = Collections.binarySearch(projects, "School (Past)");

        if (rm >= 0)
            projects.remove(rm);

        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).contains("."))
                continue;

            String path = String.format("%s/%s/src", dir, projects.get(i));

            File[] packages = new File(path).listFiles();

            if (checkPackages(packages))
                return addFiles(packages);

            files.addAll(addFilesWithPackages(packages));

        }

        return files;

    }

    /**
     * Adds all files under every single subsequent packge under each specific
     * project to a {@code HashSet} of {@code SrcFile} Objects. Iterates through
     * each package, with in each iteration it takes the package, lists all of the
     * files present within that package by using {@link java.io.File#listFiles()}
     * and creates a new {@code File} array, which is then used by the
     * {@link #addFiles(File[])} method to add all the files as new {@code SrcFile}
     * Objects to the HashSet which is to be returned. Private because it does not
     * need to be accessed by the user or other classes, as {@link #findFiles()}
     * method already invokes it when an object of {@code FileSearcher} is created.
     * 
     * @param packages Array of {@code File} objects which contain the packages to
     *                 be searched
     * @return A {@code HashSet} of {@code SrcFile} Objects which are the source
     *         code files within said workspace/projects
     */

    private HashSet<SrcFile> addFilesWithPackages(File[] packages) {
        HashSet<SrcFile> files = new HashSet<>();
        for (int j = 0; j < packages.length; j++) {
            File[] tmp = new File(packages[j].toString()).listFiles();
            files.addAll(addFiles(tmp));
        }

        return files;
    }

    /**
     * Adds files in the {@code File[]} called tmp to {@code HashSet} of
     * {@code SrcFile} objects which is the {@code HashSet} to be returned. Does
     * this by using the {@link java.util.Arrays#stream(Object[])} method and then
     * using the
     * {@link java.util.stream.Stream#forEach(java.util.function.Consumer)} method.
     * Through the foreach method, it creates a new {@code SrcFile} Object then adds
     * it to the {@code HashSet}. Private as this function is called by and only
     * needed by {@link #addFilesWithPackages(File[])} and {@link #findSingular()}
     * and is not to be accessed outside of this class.
     * 
     * 
     * @param tmp A {@code File[]} of source code files to be added to the
     *            {@code HashSet}
     * @return A {@code HashSet} as a {@code SrcFile} Objects of all source code
     *         files as denoted by the tmp {@code File[]}
     * @see attributes.SrcFile
     */

    private HashSet<SrcFile> addFiles(File[] tmp) {
        HashSet<SrcFile> files = new HashSet<>();
        Arrays.stream(tmp).forEach(x -> {
            files.add(new SrcFile(x));
        });
        return files;
    }

    /**
     * Method used as a provision for when there is only a single project in the
     * workspace or directory as denoted by the user. If there are packages under
     * said project, then the {@code File[]} is filled with the package names. If
     * there are no packages under said project, the {@code File[]} is filled with
     * the source code files, in both ways the array is populated by using the
     * {@link java.io.File#listFiles()} method. If said project has no packages, it
     * subseqeuntly then satisfies the {@link #checkPackages(File[])} method, which then
     * returns a {@code HashSet}, populated by the {@link #addFiles(File[])} method
     * which is a {@code HashSet} of {@code SrcFile} objects. When the project does
     * have packages and subseqeuntly does not satisfy the {@link #checkPackages(File[])}
     * method, it then uses the
     * {@link java.util.AbstractCollection#addAll(java.util.Collection)} method to
     * add all of the contents of the {@code HashSet} that is returned by
     * {@link #addFilesWithPackages(File[])} method. Private as it is called and
     * only needed by {@link #findFiles()} and doesn't need to be accessed by other
     * classes or instances of this object
     * 
     * 
     * @return A {@code HashSet} of {@code SrcFile} Objects which contains every
     *         source code found under said singular project and subsequent packages
     */

    private HashSet<SrcFile> findSingular() {

        HashSet<SrcFile> files = new HashSet<>();

        String path = String.format("%s/src", dir);

        File[] packages = new File(path).listFiles();

        if (checkPackages(packages))
            return addFiles(packages);

        files.addAll(addFilesWithPackages(packages));
        return files;

    }

    /**
     * 
     * Boolean method used to check if said project in directory denoted by the user
     * has packages or if the project has any source code in it at all. First checks
     * to make sure it has either packages or source code with in the project
     * entirely which is decided by the contents of the {@code File[]} that is
     * passed through by using the {@link java.util.Arrays#asList(Object...)}.If
     * true, then it checks if there is any packages, by checking if the first file
     * contains the .java extension by using the
     * {@link java.lang.String#contains(CharSequence)}. Private as this method is
     * invoked by {@link #findSingular()} and {@link #findFiles()} and does not need
     * to be accessed by created instances of this object or by the user.
     * 
     * @param arr A {@code File[]} of the pathnames of the files, if any under the
     *            directory denoted by either the project that is being searched or
     *            the singular project as denoted by the user.
     * @return {@code true} if and only if the arr is not empty and contains source
     *         files, otherwise {@code false}
     */

    private boolean checkPackages(File[] arr) {
        return !Arrays.asList(arr).isEmpty() && arr[0].toString().contains(".java");
    }

    /**
     * Locks all files using the {@link attributes.SrcFile#lockFile()} method using
     * a for each loop. If there is an error to locking a file (setting to read
     * only), it prints out an error message, at which file and sets the return
     * boolean to false
     * 
     * @return {@code true} if and only if there is no problem "locking the files",
     *         {@code false} if otherwise
     * @see java.io.File#setReadOnly()
     */

    public boolean lockAll() {

        boolean locked = true;

        for (SrcFile x : this.srcFiles) {
            locked = x.lockFile();
            if (!locked) {
                System.out.printf("There was an error at file %s it could not lock\n", x.toString());
                locked = false;
            }
        }

        return locked;

    }

    /**
     * Unlocks all files using the {@link attributes.SrcFile#unlockFile()} method by
     * using a for each loop to access all of them that are present in the
     * {@code HashSet}. If there is an error in unlocking the file (making it
     * writable), it prints out an error message at that file and sets the return
     * boolean to false.
     * 
     * @return {@code true} if and only if there is no problem "unlocking the files",
     *         {@code false} if otherwise
     * @see java.io.File#setWritable(boolean)
     */

    public boolean unlockAll() {

        boolean unlocked = false;

        for (SrcFile x : this.srcFiles) {
            unlocked = x.unlockFile();
            if (!unlocked) {
                System.out.printf("There was an error at file %s it could not unlock", x.toString());
                unlocked = false;
            }
        }
        return unlocked;
    }

    @Override
    public String toString() {
        return this.dir.toString();
    }

}