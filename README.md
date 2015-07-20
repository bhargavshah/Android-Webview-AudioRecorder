# Android-Webview-Audio-Recorder

A sample Android application that uses Android SDK's MediaRecorder to record audio.

The native API is exposed to an Android WebView to be leveraged in HTML/JavaScript.

#### Native API:
* initialize(String filename) -
To init MediaRecorder instances, creating a file amongst other things.
* record() -
To record audio in that file
* stop() -
To stop recording audio
* play(String filename) -
To play the recorded audio. (Only for testing purposes. I mean to play the recorded audio using HTML5 audio player)
* getBase64(String filename) -
Returns a base64 encoded string of audio data; to be used in HTML5 audio player.
