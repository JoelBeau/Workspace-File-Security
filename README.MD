Author: Joel Beauregard

Hello!

This repository contains my source code files for my Workspace File Security Program, which can lock a user's project files or unlock them depending on what they would like to do.

Affter getting the directory of the workspace, the program has several options along with a functioning user account with a username and login. The user can lock the workspace files (make them read only), unlock them (make them writable, check the status of their project files, change the file directory to a different workspace, change account information (such as username and password), and exit the program.

User can not access any of those options until the username and password they input matches what is in the info.dat file which contains the current username and password. 

Account.java is an object of the user's account with the information always updated if changed, all account information is in the info.dat file

SrcFile.java is an object of the current source code file that can be locked or unlocked using java.util.io.File's methods

FileSearcher.java is an object that uses a directory that is prompted for when craeating an instnace of this object to find all of the source files in a specific diretory along with methods to lock and unlock all of said files under said directory

App.java is used to run the program create user interface menus and to create instances of the object Account.java and FileSearcher.java 

More indepth documentation can be found in each file.

Thanks,
Joel
