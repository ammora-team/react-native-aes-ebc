package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.common.logging.FLog;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RNReactNativeAesEbcModule extends ReactContextBaseJavaModule {
  private static final String TAG = "RNReactNativeAesEbc";
  private final ReactApplicationContext reactContext;

  public RNReactNativeAesEbcModule(ReactApplicationContext reactContext){
    super(reactContext);
    this.reactContext = reactContext;
  }

  protected void intToBytes(int x, List<Byte> bytesAppend) {
    for (int i = 0; x != 0; i++, x >>>= 8) {
      bytesAppend.add((byte) (x & 0xFF));
    }
	}

	protected void toByteArray(float value, List<Byte> bytesAppend) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		byteBuffer.putFloat(value);
    byte[] inByteBufferArray = byteBuffer.array();

		for (int i = 0; i < 4; i++) {
			bytesAppend.add(inByteBufferArray[i]);
		}
  }

  @ReactMethod
  public void encryptSensors(ReadableArray message, ReadableArray keys, Promise promise) {
    List<Byte> bytes = new ArrayList<>(message.size());
    for (int i = 0; i < message.size(); i++) {
      ReadableType type = message.getType(i);
      if (type == ReadableType.Number) {
        bytes.add((byte) message.getInt(i));
      } else {
        for (byte b :message.getString(i).getBytes()) {
          bytes.add(b);
        }
      }
    }

    byte[] bytesArr = new byte[64];
    for (int i = 0; i < bytes.size(); i++) {
      bytesArr[i] = bytes.get(i);
    }

    encrypt(bytesArr, keys, promise);
  }

  @ReactMethod
  public void encryptGps(ReadableArray message, ReadableArray keys, int blocks, Promise promise) {
    /*List<Byte> bytes = new ArrayList<>(message.size());
    for (int i = 0; i < message.size(); i++) {
      ReadableType type = message.getType(i);
      if (type == ReadableType.Number) {

        bytes.add((byte) message.getInt(i));
      } else {
        for (byte b :message.getString(i).getBytes()) {
          bytes.add(b);
        }
      }
    }*/

    List<Byte> bytesList = new ArrayList<Byte>(16 * blocks);
    byte[] bytesArr = new byte[16 * blocks];

    int SIZE = 6;
    for (int i = 0; i < blocks; i++) {
      int length = ((SIZE * i) + i);
      intToBytes(message.getInt(0 + length), bytesList); // time
      toByteArray((float) message.getDouble(1 + length), bytesList);
      toByteArray((float) message.getDouble(2 + length), bytesList);

      bytesList.add((byte) message.getInt(3 + length));
      bytesList.add((byte) message.getInt(4 + length));
      bytesList.add((byte) message.getInt(5 + length));
      bytesList.add((byte) message.getInt(6 + length));
    }

    for (int i = 0; i < bytesList.size(); i++) {
      bytesArr[i] = bytesList.get(i);
    }

    encrypt(bytesArr, keys, promise);
  }

  protected void encrypt(byte[] bytesArr, ReadableArray keys, Promise promise) {
		try {
      byte[] crypted = null;
      byte[] key = new byte[32];
      for (int i = 0; i < keys.size(); i++) {
        key[i] = (byte) keys.getInt(i);
      }

      SecretKeySpec skey = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, skey);
      crypted = cipher.doFinal(bytesArr);

      Base64.Encoder encoder = Base64.getEncoder();
      promise.resolve(new String(encoder.encodeToString(crypted)));
    } catch (Exception e) {
      FLog.e(TAG, e.toString());
      promise.reject(e);
    }
  }

  @Override
  public String getName() {
    return "RNReactNativeAesEbc";
  }
}