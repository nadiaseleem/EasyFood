# EasyFood

an android application that simplifies the cooking process by presenting users with a diverse
selection of food options from various categories. The app provides comprehensive instructions for
preparing the selected dish, accompanied by a helpful video tutorial. Moreover, the app offers search functionality, allowing users to easily search for specific meals. Additionally, the app enables users to save their favorite meals, allowing them to quickly access and refer back to them in the future.

# 💡 Preview

📱 download app apk : https://www.mediafire.com/file/smu6x3apundnmja/easyFood.apk/file

<p align="center">
  <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/f8dd4864-4055-4bdc-af81-eef654d46e2e" width="279" height="600">
  <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/3614a0d3-ceb8-423b-a673-2ece9f6122c1" width="279" height="600">
  <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/4f881fc9-1837-4381-9154-280be20d5b2a" width="279" height="600">
  <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/e75a6314-f487-41e8-91e0-334c78c96725" width="279" height="600">
   <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/d0e39117-5c43-4b67-9c3b-dae160b73506" width="279" height="600">
  <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/ccfe5d13-43b0-4d6e-bb99-d7bd0d8a46d2" width="279" height="600">
  <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/94d3e99f-b0a3-4c84-b746-336845ee337b" width="279" height="600">
  <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/2b0273dd-0a0c-4316-b9bd-17ed3f5154a4" width="279" height="600">
  <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/2c466c73-4616-49a6-9e81-9f01ea8e160e" width="279" height="600">

</p>




# 🌟 Libraries and technologies used

- Navigation component: to manage navigation within the app and creating a single activity that contains multiple fragments, rather than creating multiple activities. (Single Activity Pattern)

- Retrofit: to make HTTP connection with the rest API and convert meal json file to Kotlin/Java
  object.
  
- Room: to save data (favourite meals) in a local database on the user's device. This can be useful for caching data or for offline functionality.

- MVVM:  this pattern helps in saperating the app's UI logic from its business logic. This makes the app easier to maintain and update. 

- LiveData:  a library for reactive programming in Android that helps to create observable data that can be updated and observed in real-time.
  it is useful for managing the state of the app's UI, since it allows changes to the data to be propagated to any observers automatically.
 LiveData is designed to be lifecycle-aware, which means it automatically cleans up any observers when the component they're attached to (such as an activity or fragment) is destroyed. 
  
- RxKotlin: to handle asynchronous background threading, such as network requests or database operations, without blocking the main thread or causing performance issues.

- Dagger Hilt: for dependency injection to simplify the app's architecture and make it easier to manage dependencies between different components.

- view binding : to automates the process of inflating views in the app's UI instead of manually inflating views in code.

- Glide : for loading and caching images in the app's UI.
