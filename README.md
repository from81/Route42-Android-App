[![codecov](https://codecov.io/gh/from81/Route42-Android-App/branch/master/graph/badge.svg?token=1ywMys6vfM)](https://codecov.io/gh/from81/Route42-Android-App)

test 

# About

Group project for [COMP6442 Software Construction](https://programsandcourses.anu.edu.au/course/COMP6442), a course taken as part of Master of Computing at Australian National University.

The project required groups of 4 students to create an Android application to demonstrate concepts learned in class.
This project was undertaken over the course of 6 weeks (second half of the semester).

We developed a fitness tracking social networking app for Android, and received one of the highest marks.

For more info, see [Design Document](docs/report.md).

# Changelog

1. Set up initial repository and gradle project.
2. Add login screen.
3. Add Firebase User Authentication.
4. Create User data class and repository class.
5. Add Firebase Firestore for data storage.
6. Design change: Shifted to Single Activity architecture.
7. Added Post data model, adapter, and material card view, and created map fragment using mapbox.
8. Add Firebase Cloud Storage for storage of user generated content.
9. Add Glide for image rendering and caching on Feed.
10. Add Profile screen (tap on username in the feed to navigate to the profile).
11. Add follow & block functionality.
12. Newsfeed complete - dynamically updates by inserting new posts into the beginning of the recyclerview. 
13. Add demo mode using ScheduledExecutorService.
14. Switch to Material Dark UI
15. Removed Activity and Athletic related attributes, including Route, from Post.
16. Optimized fragment transaction from FeedFragment to ProfileFragment by using `add` instead of `replace` and `.addToBackStack`.
17. Add basic search bar and function in FeedFragment. Will add two aspects(1.update prefect search(like in sql),2. add hashtag and context search)
18. Add the ability to show list of following / followed users.
19. Added lon, lat, locationName to each posts. Added location name to Post Card. 
20. Implemented hide-on-scroll
21. Got rid of mapbox stuff and switched to google maps in MapFragment. Now, MapFragment takes lon and lat as an input draws a line between two coordinates.
22. Now you can click on the name of the location to see it on the map with respect to your current location.
23. Updated color scheme and corrected full-screen mode.
24. Added `PhotoMapFragment` and enabled geoQuery search for nearby posts.
25. Added `blocked` field to `User` model and display `Show blocked users` button when a user is looking at his/her own profile. Now users can see list of users that they blocked.
26. Parsing 
    - add REST API client using retrofit
    - update FeedFragment to use regular PostAdapter instead of FirestorePostAdapter
    - make FeedFragment Searchview issue query only on submit
    - use single thread executor service to make REST API request 
      - incorporated KNN endpoint via KDTree  
27. Enabled user create workout activity and save workout post feature.
    - create 'ActiveMapFragment' which displays map and user activity route data and metrics
    - Allow user to Pause / Resume workout activity when navigating away, or by clicking Activity button on page
    - create 'CreatePostFragment' which enables user to save new post   
    - Hashtags in post description are auto-parsed. Separate hashtags with punctuation. 
    - enable mock location using MockLocation class, to feed location data provider. (non-emulator only) 
28. Hash passwords before storing in Firebase
29. Added refresh on swipe down to FeedFragment
30. Add toast when search result has no query
31. Schedulable actions
    - user can schedule a post or a like 
    - scheduled worker parses .xml and .txt files and performs database upload
    

# Requirements
- Android Gradle Plugin Version 7.0.1
- Gradle version 7.0.2
- Android Emulator: Pixel 4 API 30

# Getting Started

> If you are reading this for the first time after the merge, you will need to run `firebase init` again at the project root directory to add new services.

1. Set up the development environment
   - Android Studio is required. If you haven't already done so, download and install it.
   - Add the Google Play services SDK to Android Studio. The Maps SDK for Android is distributed as part of the Google Play services SDK, which you can add through the SDK Manager (in Preferences).
2. Set up an Android device in Android Studio
   - To run an app that uses the Maps SDK for Android, you must deploy it to an Android device or Android emulator that is based on Android 4.0 or higher and includes the Google APIs.
   - To use the Android Emulator, you can create a virtual device and install the emulator by using the Android Virtual Device (AVD) Manager that comes with Android Studio.
3. Go to [this page](https://docs.mapbox.com/android/maps/guides/install/#configure-credentials) and get two things: Mapbox public access token and secret access token by following the steps under "Configure credentials".
   - public access token: Make a copy of `app/src/main/res/values/api_template.xml` in the same directory and name it `api.xml`. Uncomment `<!--    <string name="mapbox_api_key" translatable="false" templateMergeStrategy="preserve">YOUR_KEY_HERE</string>-->` and paste your key.  
   - secret access token (Downloads:Read scope): Make a copy of `gradle_template.properties` in the same location and name it `gradle.properties`. Uncomment `MAPBOX_SECRET_TOKEN` and enter your secret access token.
4. Navigate to the **project root directory**, and set up Firebase Emulator. When developing and testing, we will use the emulator in order to not incur extra costs. When testing the app locally, you need to use the Emulator.
    1. Go to [this site](https://console.firebase.google.com/) and sign in with your own Google account.
    2. Follow [this guide](https://firebase.google.com/docs/cli#install_the_firebase_cli) to install Firebase CLI. If you are on mac, run :
        1. `curl -sL https://firebase.tools | bash`
        2. `firebase login`
    3. Follow [this guide](https://firebase.google.com/docs/emulator-suite/install_and_configure) to install the emulator. For now, we will need User Authentication emulator.
        - Run `firebase init`  at **project root directory** to download the emulators.
5. Run `firebase emulators:start` on the **project root directory** to start the emulators.
   - Emulators needed:
      - Firebase User Authentication
      - Firebase Cloud Firestore
      - Firebase Cloud Storage
   - When prompted for file names, (i.e. firestore.rules) always select the default value (just press enter). The rule files are already included in this repo.
6. Open the project root directory in Android Studio. It should recognize `build.gradle` files and set up the project for you.
7. Compile and run.

# Testing
```
./gradlew test
./gradlew createDebugCoverageReport
```

# Fake data

In the app-level `build.gradle` file, you will see the following line:

> `buildConfigField("boolean", "loadData", "false")`

When you are starting the app for the first time after starting your local Firebase emulators, the databases will be empty.
Change the above line to:

> `buildConfigField("boolean", "loadData", "true")`

When the app starts with `loadData = True`, the app will fetch data files `users.json` and `posts.json`, which are already included in this repo, and load them into Cloud Firestore.
It will also create two test users for debugging / testing purposes. The program will use the credentials in `build.gradle` file.
As long as the emulators don't turn off, once the data is inserted you can change the `loaddata` back to `buildConfigField("boolean", "loadData", "false")` so that the app doesn't attempt to insert the same data every time it starts.
In summary, you only need to do the above steps once, each time you start the emulators.
If you want to see the images rendered in the app, you need to open `locahlhost:4000` and go to `Cloud storage` and manually create the image files.

# Links

- [REST API Repository](https://github.com/from81/Route42-Database-API)

# Note

Due to migrating from gitlab, API keys have also been migrated. However, they have all been deactivated and removed.
