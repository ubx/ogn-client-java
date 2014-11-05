/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client.demo;

import static java.lang.System.out;

import org.ogn.client.OgnBeaconListener;
import org.ogn.client.OgnClient;
import org.ogn.client.OgnClientFactory;
import org.ogn.client.OgnClientProperties;
import org.ogn.commons.beacon.ReceiverBeacon;
import org.ogn.commons.utils.JsonUtils;

/**
 * A small demo program demonstrating the basic usage of the ogn-client.
 * 
 * @author wbuczak
 */
public class OgnDemoReceiverBeaconsClient {

    static {
        System.setProperty(OgnClientProperties.PROP_OGN_CLIENT_IGNORE_AIRCRAFT_BEACONS, "true");
    }

    static class BaseRadioBeaconListener implements OgnBeaconListener<ReceiverBeacon> {
        @Override
        public void onUpdate(ReceiverBeacon beacon) {
            out.println(JsonUtils.toJson(beacon));
        }
    }

    public static void main(String[] args) throws Exception {
        OgnClient client = OgnClientFactory.createClient();

        System.out.println("connecting...");
        // client.connect("r/+51.537/+5.472/250");

        client.connect();

        client.subscribeToReceiverBeacons(new BaseRadioBeaconListener());

        Thread.sleep(Long.MAX_VALUE);

    }

}