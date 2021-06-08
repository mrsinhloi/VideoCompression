# VideoCompression

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.mrsinhloi:VideoCompression:1.0.1'
	}
  
Step 3. Use

  ```
  VideoCompression(this, videoPath, object : CompressionListener {
            override fun onSuccess(path: String) {
                TODO("Not yet implemented")
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onFailed(exception: Throwable) {
                TODO("Not yet implemented")
            }

   }).compress()
```
