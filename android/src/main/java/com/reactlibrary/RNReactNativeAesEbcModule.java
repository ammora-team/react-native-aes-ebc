
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

  protected void shortBytes(int value, List<Byte> bytesAppend) {
    ByteBuffer buf = ByteBuffer.allocate(2);
    buf.order(ByteOrder.LITTLE_ENDIAN);
    buf.putShort((short) value);
    byte[] bytes = buf.array();
    
    for (int i = 0; i < 2; i++) {
			bytesAppend.add(bytes[i]);
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
    List<Byte> bytesList = new ArrayList<Byte>(64);
    byte[] bytesArr = new byte[64];

    // 0 - 14
    for (byte b: message.getString(0).getBytes()) {
      bytesList.add(b);
    }

    // 15
    for (byte b: "0".getBytes()) {
      bytesList.add(b);
    }

    // 16
    for (byte b: "0".getBytes()) {
      bytesList.add(b);
    }

    // 17
    for (byte b: "0".getBytes()) {
      bytesList.add(b);
    }

    // baterry 18-19
    shortBytes(message.getInt(1), bytesList);

    // if charger 20
    for (byte b: message.getString(2).getBytes()) {
      bytesList.add(b);
    }

    // temperature 21
    bytesList.add((byte) 0);

    // mem write 22-23
    shortBytes(message.getInt(3), bytesList);

    // mem read 24-25
    shortBytes(message.getInt(4), bytesList);

    // accel 26
    bytesList.add((byte) message.getInt(5));

    // rede 27
    bytesList.add((byte) message.getInt(6));

    // fibra 28-29
    shortBytes(0, bytesList);

    // case 30-31
    shortBytes(0, bytesList);

    // panic 32
    for (byte b: message.getString(7).getBytes()) {
      bytesList.add(b);
    }

    // 33 case
    for (byte b: "0".getBytes()) {
      bytesList.add(b);
    }

    // gps 34
    bytesList.add((byte) 0);

    // satellites 35
    bytesList.add((byte) 0);

    // id fail 36
    for (byte b: "0".getBytes()) {
      bytesList.add(b);
    }

    // carrier 37
    for (byte b: message.getString(8).getBytes()) {
      bytesList.add(b);
    }

    // iccid 38
    for (byte b: message.getString(9).getBytes()) {
      bytesList.add(b);
    }

    for (int i = 0; i < bytesList.size(); i++) {
      bytesArr[i] = bytesList.get(i);
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
      int indexSize = (SIZE * i) + i;

      intToBytes(message.getInt(0 + indexSize), bytesList); // time
      toByteArray((float) message.getDouble(1 + indexSize), bytesList);
      toByteArray((float) message.getDouble(2 + indexSize), bytesList);

      bytesList.add((byte) message.getInt(3 + indexSize));
      bytesList.add((byte) message.getInt(4 + indexSize));
      bytesList.add((byte) message.getInt(5 + indexSize));
      bytesList.add((byte) message.getInt(6 + indexSize));
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
      Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
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