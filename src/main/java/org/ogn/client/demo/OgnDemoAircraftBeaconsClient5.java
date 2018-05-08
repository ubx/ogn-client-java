/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client.demo;

import org.ogn.client.*;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;
import org.ogn.commons.db.FileDbDescriptorProvider;
import org.ogn.commons.db.ogn.OgnDb;
import org.ogn.commons.igc.IgcLogger;

import java.util.Optional;

/**
 * A small demo program demonstrating the usage of the ogn-client with aircraft descriptor providers.
 *
 * @author wbuczak
 */
public class OgnDemoAircraftBeaconsClient5 {

    static IgcLogger igcLogger = new IgcLogger();
    // enable if you want to log to IGC files

    static {
        // ignore parsing receiver beacons, we are not interested in them in
        // this demo and there is
        // no point in wasting CPU on that
        System.setProperty(OgnClientProperties.PROP_OGN_CLIENT_IGNORE_RECEIVER_BEACONS, "true");
    }


    public static void main(String[] args) throws Exception {


        AircraftDescriptorProvider adp = new FileDbDescriptorProvider<OgnDb>(OgnDb.class);
        OgnClient client = OgnClientFactory.getBuilder().port(OgnClientConstants.OGN_DEFAULT_SRV_PORT + 1000)
                .descriptorProviders(adp).build();

        System.out.println("connecting...");

        // set some filter to the second instance of OGN client
        if (args.length > 0 && args[0] != null && args[0].length() > 0) {
            client.connect(args[0]);
        } else {
            client.connect();
        }
        client.subscribeToAircraftBeacons(new AcListener());
        System.out.println("...connected!");

        Thread.sleep(Long.MAX_VALUE);
    }

    static class AcListener implements AircraftBeaconListener {


        @Override
        public void onUpdate(AircraftBeacon beacon, Optional<AircraftDescriptor> descriptor) {

            if (descriptor.isPresent()) {
                igcLogger.log(beacon, descriptor);
            }
        }
    }


}