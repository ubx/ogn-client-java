/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client.demo;

import static java.lang.System.out;

import java.util.Arrays;
import java.util.List;

import org.ogn.client.AircraftBeaconListener;
import org.ogn.client.OgnClient;
import org.ogn.client.OgnClientFactory;
import org.ogn.client.OgnClientProperties;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;
import org.ogn.commons.beacon.impl.AircraftDescriptorImpl;
import org.ogn.commons.flarm.FlarmNetDescriptorProvider;
import org.ogn.commons.utils.JsonUtils;

/**
 * A small demo program demonstrating the usage of the ogn-client with aircraft descriptor providers
 * 
 * @author wbuczak
 */
public class OgnDemoAircraftBeaconsClient2 {

    static {
        // ignore parsing receiver beacons, we are not interested in them in this demo and there is
        // no point in wasting CPU on that
        System.setProperty(OgnClientProperties.PROP_OGN_CLIENT_IGNORE_RECEIVER_BEACONS, "true");
    }

    static class AcListener implements AircraftBeaconListener {
        @Override
        public void onUpdate(AircraftBeacon beacon, AircraftDescriptor descriptor) {
            out.println("*********************************************");

            // print the beacon
            out.println(JsonUtils.toJson(beacon));

            // if the aircraft has been recognized print its descriptor too
            if (descriptor.isKnown()) {
                out.println(JsonUtils.toJson(descriptor));
            }

            out.println("*********************************************");
        }
    }

    /**
     * A custom descriptor provider. For the use of this demo this custom descriptor provider always returns the
     * same descriptor, no matter what address is passed to it
     * 
     * @author wbuczak
     */
    static class MyCustomAircraftDescriptorProvider implements AircraftDescriptorProvider {

        @Override
        public AircraftDescriptor getDescritor(String address) {
            // return always the same descriptor (just for this demo)
            return new AircraftDescriptorImpl("SP-NZA", "ZA", "Private", "EPML", "Cessna 172S", "122.500");
        }

    }

    public static void main(String[] args) throws Exception {

        // create an instance of FlarmNet descriptor provider
        AircraftDescriptorProvider adp = new FlarmNetDescriptorProvider();

        // create an instance of "custom" descriptor provider
        AircraftDescriptorProvider adp2 = new MyCustomAircraftDescriptorProvider();

        // put the two descriptors into a list
        // NOTE: the order matters. The OGN client will try to query for the aircraft information the first provider
        // in the list. Only if no match is found it will continue with the second provider etc..
        List<AircraftDescriptorProvider> aircraftDescProviders = Arrays.asList(new AircraftDescriptorProvider[] { adp, adp2
                });

        // create ogn client and give it the previously created descriptor providers
        OgnClient client = OgnClientFactory.createClient(aircraftDescProviders);

        System.out.println("connecting...");

        client.connect();

        client.subscribeToAircraftBeacons(new AcListener());

        Thread.sleep(Long.MAX_VALUE);
    }
}