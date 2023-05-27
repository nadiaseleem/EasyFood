# EasyFood

an android application that simplifies the cooking process by presenting users with a diverse
selection of food options from various categories. The app provides comprehensive instructions for
preparing the selected dish, accompanied by a helpful video tutorial. Moreover, the app offers search functionality, allowing users to easily search for specific meals. Additionally, the app enables users to save their favorite meals, allowing them to quickly access and refer back to them in the future.

# 💡 Preview

📱 download app apk : https://www.mediafire.com/file/zca5kywpsn9nsad/easyfood.apk/file

<p align="center">
  <img src="https://user-images.githubusercontent.com/37695970/231596964-bf7bbc1c-00f7-4c2e-b80e-b67efa89e5cb.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597100-84ccf5e4-9673-494a-a350-34466a76232f.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231596982-3c17e5bf-903e-4c46-98d9-e25d4a3ba79e.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597109-19e8eb98-3a04-4f89-b54c-d72e635847f7.png" width="279" height="600">
   <img src="https://github.com/nadiaseleem/EasyFood/assets/37695970/edb3d7cf-17e9-4673-a107-d419637a9e77" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231596994-4a4f19f3-63e8-4326-b69b-220c93c0eba8.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597011-bb0c5693-4453-4a9c-9c3a-bb73bdad4546.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597051-36f6e0fb-11ce-4e60-babe-9b5e7e05d1d9.png" width="279" height="600">
  <img src="https://user-images.githubusercontent.com/37695970/231597083-e430dd86-1a83-4807-bcf8-e61552950c46.png" width="279" height="600">

</p>


# 🌟 Libraries and technologies used

- Navigation component: to manage navigation within the app and creating a single activity that contains multiple fragments, rather than creating multiple activities. 

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
