
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64.*;

public class RNReactNativeAesEbcModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNReactNativeAesEbcModule(ReactApplicationContext reactContext){
    super(reactContext);
    this.reactContext = reactContext;
  }

  @ReactMethod
  public String encrypt(String message, String key) {
    byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(message.getBytes());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
		
		return new String(encoder.encodeToString(crypted));
  }

  @Override
  public String getName() {
    return "RNReactNativeAesEbc";
  }
}