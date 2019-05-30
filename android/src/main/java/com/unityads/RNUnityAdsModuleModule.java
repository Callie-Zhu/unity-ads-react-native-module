
package com.unityads;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactContext;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import android.support.annotation.Nullable;

public class RNUnityAdsModuleModule extends ReactContextBaseJavaModule implements IUnityAdsListener {

  private final ReactApplicationContext reactContext;

  public RNUnityAdsModuleModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNUnityAdsModule";
  }

  private void sendEvent(ReactContext reactContext,
                         String eventName,
                         @Nullable WritableMap params) {
    reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
  }

  @ReactMethod
  public void initialize(String gameID) {
    UnityAds.initialize(getCurrentActivity(), gameID, this, false);
  }

  @ReactMethod
  public void isInitialized(final Callback callback) {
    callback.invoke(UnityAds.isInitialized());
  }

  @ReactMethod
  public void getListener(final Callback callback) {
    callback.invoke(UnityAds.getListener());
  }

  @ReactMethod
  public void isSupported(final Callback callback) {
    callback.invoke(UnityAds.isSupported());
  }

  @ReactMethod
  public void getVersion(final Callback callback) {
    callback.invoke(UnityAds.getVersion());
  }

  @ReactMethod
  public void isReady(String placementID, final Callback callback) {
    if (placementID.equals(null) || placementID.isEmpty()) {
      callback.invoke(UnityAds.isReady());
    } else {
      callback.invoke(UnityAds.isReady(placementID));
    }
  }

  @ReactMethod
  public void getPlacementState(String placementID, final Callback callback) {
    if (placementID.equals(null) || placementID.isEmpty()) {
      callback.invoke(UnityAds.getPlacementState().toString());
    } else {
      callback.invoke(UnityAds.getPlacementState(placementID).toString());
    }
  }

  @ReactMethod
  public void show(String placementID) {
    if (placementID.equals(null) || placementID.isEmpty())   {
      if (UnityAds.isReady()) {
        UnityAds.show(getCurrentActivity());
      }
    } else {
      if (UnityAds.isReady(placementID)) {
        UnityAds.show(getCurrentActivity(), placementID);
      }
    }
  }

  // listener methods
  @Override
  public void onUnityAdsReady(final String placementID) {
    WritableMap params = Arguments.createMap();

    params.putString("placementID", placementID);

    sendEvent(reactContext,"Ready", params);
  }

  @Override
  public void onUnityAdsStart(String placementID) {
    WritableMap params = Arguments.createMap();

    params.putString("placementID", placementID);

    sendEvent(reactContext,"Start", params);
  }

  @Override
  public void onUnityAdsFinish(String placementID, UnityAds.FinishState result) {
    WritableMap params = Arguments.createMap();

    params.putString("placementID", placementID);
    params.putString("result", result.toString());

    sendEvent(reactContext, "Finish", params);
  }

  @Override
  public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
    WritableMap params = Arguments.createMap();

    params.putString("error", error.toString());
    params.putString("message", message);

    sendEvent(reactContext,"Error", params);
  }
}
