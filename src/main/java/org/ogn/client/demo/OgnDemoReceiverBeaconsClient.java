/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client.demo;

import static java.lang.System.out;

import org.ogn.client.OgnClient;
import org.ogn.client.OgnClientConstants;
import org.ogn.client.OgnClientFactory;
import org.ogn.client.OgnClientProperties;
import org.ogn.client.ReceiverBeaconListener;
import org.ogn.commons.beacon.ReceiverBeacon;
import org.ogn.commons.utils.JsonUtils;

/**
 * A small demo program demonstrating the basic usage of the ogn-client.
 * 
 * @author wbuczak
 */
public class OgnDemoReceiverBeaconsClient {

    static {
        // ignore parsing aircraft beacons, we are not interested in them in this demo and there is
        // no point in wasting CPU on that
        System.setProperty(OgnClientProperties.PROP_OGN_CLIENT_IGNORE_AIRCRAFT_BEACONS, "true");
    }

    static class RbListener implements ReceiverBeaconListener {
        @Override
        public void onUpdate(ReceiverBeacon beacon, String rawBeacon) {
            out.println(JsonUtils.toJson(beacon));
        }
    }

    public static void main(String[] args) throws Exception {
        // OgnClient client = OgnClientFactory.createClient();

        OgnClient client = OgnClientFactory.getBuilder().port(OgnClientConstants.OGN_DEFAULT_SRV_PORT + 1000).build();

        System.out.println("connecting...");
        // client.connect("r/+51.537/+5.472/250");

        client.connect();

        client.subscribeToReceiverBeacons(new RbListener());

        Thread.sleep(Long.MAX_VALUE);

    }

}