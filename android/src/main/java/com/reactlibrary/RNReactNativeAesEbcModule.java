
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.common.logging.FLog;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64.*;

public class RNReactNativeAesEbcModule extends ReactContextBaseJavaModule {
  private static final String TAG = "RNReactNativeAesEbc";
  private final ReactApplicationContext reactContext;

  public RNReactNativeAesEbcModule(ReactApplicationContext reactContext){
    super(reactContext);
    this.reactContext = reactContext;
  }

 @ReactMethod
  public String encrypt(String message, ReadableArray bytes) {
    byte[] key = new byte[32];
    byte[] crypted = null;
		try {
      for (int i = 0; i < bytes.size(); i++) {
        key[i] = (byte) bytes.getInt(i);
      }

			SecretKeySpec skey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(message.getBytes());
		} catch (Exception e) {
			FLog.e(TAG, e.toString());
		}
		java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
		
		return new String(encoder.encodeToString(crypted));
  }

  @Override
  public String getName() {
    return "RNReactNativeAesEbc";
  }
}