package com.Logic.Interface;

import com.Logic.Object.CLocation;


// Any activity that wants to receive system location has to implement this interface.
// When the System Location received, SDLocationListener will send it to a class that implements this interface
public interface LocationReceiver {
//  Method is invoked if the location changes. Method does nothing if the context does not implement this interface. Error message will be shown.
    void onLocationUpdated(CLocation location);
}
