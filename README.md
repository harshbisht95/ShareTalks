# ShareTalks
## Generate Captions for Images with Tensorflow Lite on Android.
This repository contains an individual Android Studio project that creates a platform for communication among traders of all skill levels discuss strategy and the hottest stocks moving the market. 
</br>
</br>
<img src="example.gif" width="200" height="400" />
## Development Environment
- Android Studio 4.1
- Android SDK
- Java 1.8
- Google Firebase
### Step 1- Train the caption generation model.
  - [Caption Generation](https://github.com/harshbisht95/image-captioning.git)
### Step 2- Convert the trained model to Tensorflow Lite(tflite) model.
  - Steps to convert model:
    ```
    import tensorflow as tf
    new_model= tf.keras.models.load_model(filepath="drive/My Drive/model.h5")
    tflite_converter = tf.lite.TFLiteConverter.from_keras_model(new_model)
    # Set quantize to true 
    tflite_converter.post_training_quantize=True
    tflite_model = tflite_converter.convert()
    open("drive/My Drive/tflite_model.tflite", "wb").write(tflite_model)
    ```
### Step 3- Create Android Project.
  - Place the converted ```tflite_model.tflite``` file to folder ``` /android/app/src/main/assets ```
  - There are 2 more assets ``` wordtoix and ixtoword ``` which are mapped JSON files(Tokens to Vocab and Vice-versa).
    - To use tflite we need to add dependency to gradle file: 
    ``` implementation 'org.tensorflow:tensorflow-lite:0.0.0-nightly' ```
    Tensorflow also provides support library for easy use of loading the model and other stuff.
    ``` implementation 'org.tensorflow:tensorflow-lite-support:0.0.0-nightly' ```
  - TFLite model get compressed by Android. In order to avoid auto compression add the following to the gradle file.
    ```
    aaptOptions {
      noCompress "tflite"
      noCompress "lite"
    }
    ```
  - The app need a Camera to take photos for generating caption.
    - The inbuilt Camera app can be used using Intent or one can make custom camera using API's like:
      - [Camera API](https://developer.android.com/guide/topics/media/camera).
      - [CameraX API](https://developer.android.com/training/camerax)
      - There are others too.
#### NOTE: 
1 Issue I faced here was that tranfer learning was not available in tflite. In my model I have use ``` Inception V3 model features ``` .
So I converted the Inception model to tflite and placed it in the ``` /android/app/src/main/assets ``` folder. In android backend I fed the image as intput to
Inception model and the output from it was the input for my model. 
##### The output might not be very promising due to Hardware limitation in training the model. Only 1652 vocabularies are used.
## Creds: </br> 
###### All Google page links are purple :joy: . Listing those which I remember.
  - [Android Devloper](https://developer.android.com/)
  - [Google Firebase](https://firebase.google.com/)
  - [GeekforGeeks](https://www.geeksforgeeks.org/how-to-use-firebase-firestore-as-a-realtime-database-in-android/)
