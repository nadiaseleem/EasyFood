# EasyFood

an android application that simplifies the cooking process by presenting users with a diverse
selection of food options from various categories. The app provides comprehensive instructions for
preparing the selected dish, accompanied by a helpful video tutorial. Moreover, the app offers search functionality, allowing users to easily search for specific meals. Additionally, the app enables users to save their favorite meals, allowing them to quickly access and refer back to them in the future.

# 💡 Preview

download app apk : https://www.mediafire.com/file/r27yhrexk7203iw/EasyFood.apk/file

<p align="center">
  <img src="https://user-images.githubusercontent.com/37695970/231596964-bf7bbc1c-00f7-4c2e-b80e-b67efa89e5cb.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597100-84ccf5e4-9673-494a-a350-34466a76232f.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231596982-3c17e5bf-903e-4c46-98d9-e25d4a3ba79e.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597109-19e8eb98-3a04-4f89-b54c-d72e635847f7.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231596994-4a4f19f3-63e8-4326-b69b-220c93c0eba8.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597011-bb0c5693-4453-4a9c-9c3a-bb73bdad4546.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597051-36f6e0fb-11ce-4e60-babe-9b5e7e05d1d9.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597083-e430dd86-1a83-4807-bcf8-e61552950c46.png" width="279" height="600">

</p>

# 🌟 Libraries and technologies used

- Navigation component : to manage navigation within the app and creating a single activity that contains multiple fragments, rather than creating multiple activities. 

- Retrofit : to make HTTP connection with the rest API and convert meal json file to Kotlin/Java
  object.
  
- Room : to save data (favourite meals) in a local database on the user's device. This can be useful for caching data or for offline functionality.

- MVVM & LiveData : these libraries help in saperating the logic code from the views. This makes the app easier to maintain and update. 
  LiveData helps in saving the state of the app's views in case the screen configuration changes (e.g., if the device is rotated).
  
- RxKotlin : to handle asynchronous background threading, such as network requests or database operations, without blocking the main thread or causing performance issues.

- Dagger Hilt: for dependency injection to simplify the app's architecture and make it easier to manage dependencies between different components.

- view binding : to automates the process of inflating views in the app's UI instead of manually inflating views in code.

- Glide : for loading and caching images in the app's UI.
