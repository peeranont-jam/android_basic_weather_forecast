# Basic Weather Forecast - Android Application

MVVM + Unit Test + UI Test

## Project Structure

- [Language] - **Kotlin**
- [Architecture] - **MVVM**
- [Tech]:
    - [Dependencies injection] - **Koin**
    - [Asynchronous Handler] - **Kotlin Coroutines + LiveData**
    - [Navigation] - **Navigation component + NavigationUI + Safeargs**
    - [HTTP Client] - **Retrofit2 + OkHttp + GSON**
    - [View Binding Method] - **View Binding**
    - [Image Handling Tool] - **Glide**
    - [Instrumented Testing Tool] - **Espresso**
    - [Unit Testing Tools] - **Junit4 + Mockk**

## Features

- Search by city name to get current weather and next 24 hrs forecast
- Provide information as following:
    - Temperature (Unit based on user selected option)
    - Humidity

## How build and run app

> Note: If you don't have any devices configured, then you need to either connect a device via USB or create an AVD to use the Android Emulator.

- To build and run your app, follow these steps:
    1. Open the project in Android Studio.
    2. In the toolbar, select your app from the run configurations drop-down menu.
    3. From the target device drop-down menu, select the device that you want to run your app on.
    4. Click Run.
- For more detail > [Click Here](https://developer.android.com/studio/run)

## How to run test in Android Studio

> Note:
> **Local unit tests** are located at `$module-name/src/test/java/`.
> **Instrumented tests** are located at `$module-name/src/androidTest/java/`.

- Before running any tests, make sure your project is fully synchronized with Gradle by clicking
  Sync Project in the toolbar. You can run tests with different levels of granularity:
    - To run all tests in a directory or file, open the Project window and do either of the
      following:
        - Right-click on a directory or file and click Run.
        - Select the directory or file and use shortcut Control+Shift+R.

    - To run all tests in a class or a specific method, open the test file in the Code Editor and do
      either of the following:
        - Press the Run test icon in the gutter.
        - Right-click on the test class or method and click Run.
        - Select the test class or method and use shortcut Control+Shift+R.

- For more detail > [Click Here](https://developer.android.com/studio/test/test-in-android-studio)

## Where to Go From Here?

If you want to implement more API calling , follow these steps:

1. Go to `$module-name/src/main/java/$package-name/data/remote/service` directory.
2. Define new function with API URL, query params, request/response body in **Service Class** file.
    - _Your can create request/response body model class
      at `$module-name/src/main/java/$package-name/data/remote/model` directory._
3. Define new function in **Repository interface class**
   at `$module-name/src/main/java/$package-name/reposity` directory.
4. Implement interface and integrate service in **RepositoryImpl class**
5. Add new function in **UseCase Class** for communicating with Repository
6. Trigger API calling in **ViewModel Class** by calling UseCase's function and handle response data
   based on business requirement here.

> Note: **UseCase Class** and **ViewModel Class** files should create in **Fragment Class** 's directory for uncomplicated and manageable
> You should visit `$module-name/src/main/java/$package-name/view/currentweather` directory for more details.

_View's child directory name based on action or fragment name._