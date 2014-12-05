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
     * @param descriptor static aircraft descriptor or null if unavailable
     */
    void onUpdate(final AircraftBeacon beacon, final AircraftDescriptor descriptor);
}
