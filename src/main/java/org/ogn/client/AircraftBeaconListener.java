/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client;

import java.util.Optional;

import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;

/**
 * Interface that needs to be implemented by all entities subscribing to AircraftBeacons
 * 
 * @author wbuczak
 */
public interface AircraftBeaconListener {
	/**
	 * @param beacon
	 *            aircraft beacon
	 * @param descriptor
	 *            static aircraft descriptor, present if an aircraft is recognized by the system.
	 */
	void onUpdate(final AircraftBeacon beacon, final Optional<AircraftDescriptor> descriptor);
}
