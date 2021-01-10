# CSCI2020U_Final_proj

GROUP MEMBERS: Tirth Patel, Deepan Patel, Setu Patel, Tharuni Iranjan, Nisarg Bhatt

CONTRIBUTIONS:
Tirth: Server Socket backend (including Multi-threading), System GUI (Server GUI), System Error Handling
Deepan: Client GUI, System Error Handling, messageDatabase File IO
Setu: Login GUI, Login backend, Login File IO
Tharuni: Client GUI
Nisarg: Gradle

YOUTUBE URL: https://youtu.be/cuFupqlidkE
GITHUB URL: https://github.com/TirthPOnTechU/CSCI2020U_Final_proj
REPO URL: https://github.com/TirthPOnTechU/CSCI2020U_Final_proj.git

HOW TO RUN:
1. clone the repo into your computer
2. open intellij
3. open the build.gradle file (as a project)
4. run the ServerDriver.java file

MAIN USE CASE:
1. Login with any login provided in the userDatabase.txt (Login any number of users)
2. Start chatting
3. close Server window

What to do if an error occurs?
If an error occurs the reason for that might be the paths used in our code. So you will have to update the paths.
Step 1) get the absolute path of messageDatabase.txt and the files you have to add this path to are: clientHandler.java (line 16)
Step 2) get the absolute path of userDatabase.txt and the files you have to add this path to are: Login.java (line 37)
Step 3) get the absolute path of whisper.png and the files you have to add this path to are: Login.java (line 146)
Now rerun your code and make sure you have added the absolute path for each file.

NOTE: Sorry we did not know before that you were going to mark contributions based on git logs so we communicated for the project by sending updated files over email, however the contributions were as stated before.

