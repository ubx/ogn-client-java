/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client;

import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.ReceiverBeacon;

public interface OgnClient {

    /**
     * subscribes a listener to the aircraft beacons
     * 
     * @param listener
     */
    void subscribeToAircraftBeacons(OgnBeaconListener<AircraftBeacon> listener);

    /**
     * unsubscribes a listener from receiving aircraft beacons
     * 
     * @param listener
     */
    void unsubscribeFromAircraftBeacons(OgnBeaconListener<AircraftBeacon> listener);

    /**
     * subscribes a listener to the base stations beacons
     * 
     * @param listener
     */
    void subscribeToReceiverBeacons(OgnBeaconListener<ReceiverBeacon> listener);

    /**
     * unsubscribes a listener from receiving base stations beacons
     * 
     * @param listener
     */
    void unsubscribeFromReceiverBeacons(OgnBeaconListener<ReceiverBeacon> listener);

    /**
     * connects the client to the OGN APRS service (no filtering)
     */
    void connect();

    /**
     * connects to the OGN APRS service
     * 
     * @param aprsFilter optional APRS filter, if null no filter will be used, as it is in case of {@link #connect()
     *            connect()}.
     * @see <a href="http://www.aprs-is.net/javAPRSFilter.aspx">Server-side Filter Commands</a>
     */
    void connect(String aprsFilter);

    /**
     * disconnects a client from the OGN APRS service
     */
    void disconnect();
}