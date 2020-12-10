README for Team Project Four (CS400 @ UW Madison)
==================================================

Every member of a team must have an individual README.txt file filled in in their folder on
the team's GitHub repo.

Name of submitting team member: Jerry Xu
Wisc email of submitting team member: mxu269@wisc.edu
Team name: IA
Role of submitting team member: Front End 2
TA: Mui Cai
Lecturer: Florian Hermiel

Contributions Week 1:
---------------------
This week's main goal was to set up a demo server (for http requests) and client (with
reactjs). This is done to ensure my approach for the front end is realistic in terms of
workload and technical complexity. There are two main folders under mxu269_Front_End_2: java
and reactjs. The prior holds all java source files and dependencies used for the (http) server,
and the later encapsulates the client web application built with reactjs which I will refer as
the client from now on.

The client can be opened by calling "npm start" within "reactjs/budget-tracker", however a prebuilt
version will be avaliable as part of the final submission.

Server progress:
    - Created Server.java Main.java HttpConnection.java
    - Successfully demoed login API that generates JWT

Client progress:
    - Created empty reactjs project
    - Successfully demoed HTML elements editable.
    - Successfully demoed login API works to obtain JWT in console.

Contributions Week 2:
---------------------

This week's main goal is to get jwt working along side with the "/login" route in reactjs.

Now user can go to "localhost:8000/login" to login. If successful, the page will reroute
to "/dashboard" and the jwt added to local storage, otherwise it will show a red badge
saying the credentials are wrong.


Contributions Week 3:
---------------------

Finished all Server API end points.
Finished all React components and routes.
Finished the application. Now server integrates perfectly with backend.

Files written by me:
--------------------
/java/src
├── HttpConnection.java
├── Main.java
└── Server.java

/react/budget-tracker/
── index.js
components
├── basic.component.js
├── daily-chart.component.js
├── dashboard.component.js
├── filter.component.js
├── footer.component.js
├── landing-page.component.js
├── login.component.js
├── navbar.component.js
├── new-transaction.component.js
├── page-not-found.component.js
├── redirectModal.component.js
├── search.component.js
├── signup.component.js
├── transaction-cell.component.js
├── transaction-table.component.js
└── transaction.component.js
styles
├── navbar.css
├── newtransaction.css
├── signin.css
└── transactioncard.css
js
 └── PrivateRoute.js
services
├── auth-header.js
├── auth.service.js
└── user.service.js


Files submitted with this project that were developed in an earlier project:
----------------------------------------------------------------------------
none.

However, there is a lot of automatically generated react project structures files in /reactjs/budget. This is necessary
to setup 'npm install, npm start' to work. A lot of the boiler plates are useless, but I didn't delete them because I'm
not sure which ones are useful for npm to build the projects. The files listed above are the "core logic" of the
react.js client, the rests are just meta data to keep the structure.

Web address at which the program is available:
----------------------------------------------
not currently hosted.

Additional notes about the submission:
--------------------------------------
!! IMPORTANT !! :
The program (both the java-Server:8000 and Reactjs-Client:3000) needs to be run locally.

Before you start, make sure you have nodejs installed from 'nodejs.org'. This is necessary be cause the react project
uses npm.

The program has two parts that need to run concurrently. On a linux/unix machine, you can do 'make run-all' to run it in a
single line. Or alternatively, do 'make run' and 'make run-client' in 2 terminals. The 'make-run' will start a java
server, and the 'make run-client' will use npm to start hosting the client webpage.

If there are any issues, please reach out to me at mxu269@wisc.edu to talk about technical details of the app.
