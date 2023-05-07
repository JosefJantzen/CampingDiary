# CampingDiary
CampingDiary is an android diary app, especially for your camping trips around the world with a van or motorhome. <br>
I developed the app quite some time ago, with the purpose to extend my knowledge in Java and bigger projects. The focused was more on the development acspects in Java and the functionality of the app rather then creating a very pretty user interface. <br>
The app isn't 100% complete but can already be used and I reached anyway all my learning goals.<br>
You can find all the todos [here](#todos).

The app is completely written in Java and uses Room (SQLite) as a local database. <br>
The App uses the [Places API](https://developers.google.com/maps/documentation/places/web-service) and [Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk/overview)
to get informations about places, the user selected, view them on a map, etc. To compile and [use the app](#use-the-app), you need your own api key. 

## Brief overview
The main page of the app gives you an overview during your trip. The app tracks where you spent your night and all the fun events you did, like sightseeing, hiking, etc. but also things like fueling, supply and disposal of your camping car and your mileage. In addition there is an overview of the money you spent in all the different types of activities during your trip.<br><br>
![Screenshot 1](/assets/screenshot1.png)
<br><br>
The creation of a trip looks like the following and is pretty similar to creating all the other things in the app e.g. an event<br><br>
![Screenshot 2](/assets/screenshot2.png)
<br><br>
All the overview pages give you the most important informations about the event. The overview of the nights looks like the following screenshot<br><br>
![Screenshot 3](/assets/screenshot3.png)
<br><br>

## Todos

* Look at old trips. At the moment you can only take a look at your current trip until it ends.
* Severeal icons doesn't match the activity
* Pages to get a complete view of nights and events with all informations
* Overall statistics of all trips
* Settings and about page

## Use the app
Because of the necessary api key for the Places API and Maps SDK I can't provide an APK. You need to do the following steps to get your own api key:
1. Create a project in the google console and create an api key for the places api as described [here](https://developers.google.com/maps/documentation/places/web-service/get-api-key). Select there not only the places api but also the Maps SDK for Android as described [here](https://developers.google.com/maps/documentation/android-sdk/get-api-key). In the end you should have only one api key for both APIs.
2. Clone the project and open it in Android Studio.
3. Create a local.properties file in the project root folder, if it doesn't exist.
4. Create in that file the variable `MAPS_API_KEY` and initialize it with your api key<br>
```properties
MAPS_API_KEY=<API_KEY>
```
5. Compile the app and run it on your device.