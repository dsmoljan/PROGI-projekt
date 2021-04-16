# Instructions for local startup

Application is divided into backend and frontend. Currently, both parts are prepared for CI/CD to Heroku, 
so if frontend is started locally, requests are sent to deployed backend, unless explicitly redirected to local backend.

## Frontend startup
To start frontend from the command line, go to directory IzvorniKod\frontend\app\src and execute the following commands:

### `npm install`

The command installs all neccessary node_modules required for the frontend part of the app to run.

### `npm start`

Starts the application on [http://localhost:3000](http://localhost:3000).

The page is reloaded with every saved change in the code.\
Stranica se ponovno učitava svakom spremljenom promjenom u kodu.\
You will also see any lint errors in the console.

### Sending requests to local backend

In case you want to send request to backend part deployed locally, you need to change all parts of the code which send requests to deployed backend (something like backend.herokuapp.com) to [http://localhost:8080](http://localhost:8080).

## Backend startup

To start backend from the command line, go to directory IzvorniKod\backend\src\main\java\hr\fer\progi\dogGO and execute the following command:

### `mvn spring-boot:run`

The command starts up the backend part of the app on [http://localhost:8080](http://localhost:8080).

