/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client;

import org.ogn.commons.beacon.ReceiverBeacon;

/**
 * Interface that needs to be implemented by all entities subscribing to
 * ReceiverBeacons
 * 
 * @author wbuczak
 */
public interface ReceiverBeaconListener {
	/**
	 * @param beacon
	 *            receiver beacon
	 * @param rawBeacon
	 *            raw beacon as received from the remote system
	 */
	void onUpdate(final ReceiverBeacon beacon, final String rawBeacon);
}
