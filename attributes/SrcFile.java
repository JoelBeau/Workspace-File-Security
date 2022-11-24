package attributes;

import java.io.File;

/**
 * Object which mimmicks the the {@code File} object. Used to store any source
 * file in a directory denoted by the user. It can be locked and unlocked
 */

public class SrcFile {

    private File f;

    /**
     * Constrcuts a new {@code SrcFile} and sets the {@code File} object to whatever
     * is passed through in the constructor
     * 
     * @param f File to be set to the class vairable, f
     */
    public SrcFile(File f) {
        this.f = f;
    }

    /**
     * Locks the current file by setting it to read only
     *
     * @return {@code true} if the file did successfully get set to read only,
     *         {@code false} if otherwise
     * @see java.io.File#setReadOnly()
     */
    public boolean lockFile() {
        return this.f.setReadOnly();
    }

    /**
     * Checks if the file can write
     *
     * 
     * @return {@code true} if it can write, {@code false} if otherwise
     * @see java.io.File#canWrite()
     */
    public boolean isLocked() {
        return !this.f.canWrite();
    }

    /**
     * Unlocks the file to become writable again
     * 
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     * @see java.io.File#setWritable(boolean)
     */

    public boolean unlockFile() {
        return this.f.setWritable(true);
    }

    @Override
    public String toString() {
        return this.f.toString();
    }

}