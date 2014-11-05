/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client;

import org.ogn.commons.beacon.OgnBeacon;

public interface OgnBeaconListener<BeaconType extends OgnBeacon> {
    void onUpdate(final BeaconType beacon);
}
