
# react-native-react-native-aes-ebc

## Getting started

`$ npm install react-native-react-native-aes-ebc --save`

### Mostly automatic installation

`$ react-native link react-native-react-native-aes-ebc`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-react-native-aes-ebc` and add `RNReactNativeAesEbc.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNReactNativeAesEbc.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNReactNativeAesEbcPackage;` to the imports at the top of the file
  - Add `new RNReactNativeAesEbcPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-react-native-aes-ebc'
  	project(':react-native-react-native-aes-ebc').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-react-native-aes-ebc/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-react-native-aes-ebc')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNReactNativeAesEbc.sln` in `node_modules/react-native-react-native-aes-ebc/windows/RNReactNativeAesEbc.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using React.Native.Aes.Ebc.RNReactNativeAesEbc;` to the usings at the top of the file
  - Add `new RNReactNativeAesEbcPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNReactNativeAesEbc from 'react-native-react-native-aes-ebc';

// TODO: What to do with the module?
RNReactNativeAesEbc;
```
  