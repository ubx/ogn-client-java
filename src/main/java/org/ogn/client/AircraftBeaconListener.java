/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client;

import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;

/**
 * Interface that needs to be implemented by all entities subscribing to AircraftBeacons
 * 
 * @author wbuczak
 */
public interface AircraftBeaconListener {
    /**
     * @param beacon aircraft beacon
     * @param descriptor static aircraft descriptor. If an aircraft is not recognized by the system the
     *            <code>isKnown()</code> descriptor's method will return false. It is up to the implementing class to
     *            verify if descriptor is "known", as in case of unknown descriptor its attributes are lilely to be
     *            null.
     */
    void onUpdate(final AircraftBeacon beacon, final AircraftDescriptor descriptor);
}
