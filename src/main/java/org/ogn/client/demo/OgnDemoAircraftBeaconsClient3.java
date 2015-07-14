/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client.demo;

import static java.lang.System.out;

import org.ogn.client.AircraftBeaconListener;
import org.ogn.client.OgnClient;
import org.ogn.client.OgnClientConstants;
import org.ogn.client.OgnClientFactory;
import org.ogn.client.OgnClientProperties;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;
import org.ogn.commons.db.FileDbDescriptorProvider;
import org.ogn.commons.db.ogn.OgnDb;
import org.ogn.commons.igc.IgcLogger;
import org.ogn.commons.utils.JsonUtils;

/**
 * A small demo program demonstrating the usage of the ogn-client with aircraft
 * descriptor providers.
 * 
 * @author wbuczak
 */
public class OgnDemoAircraftBeaconsClient3 {

	static {
		// ignore parsing receiver beacons, we are not interested in them in
		// this demo and there is
		// no point in wasting CPU on that
		System.setProperty(OgnClientProperties.PROP_OGN_CLIENT_IGNORE_RECEIVER_BEACONS, "true");
	}

	static IgcLogger igcLogger = new IgcLogger();

	// enable if you want to log to IGC files
	static boolean logIGC = false;

	static class AcListener implements AircraftBeaconListener {

		@Override
		public void onUpdate(AircraftBeacon beacon, AircraftDescriptor descriptor, String rawBeacon) {
			out.println("*********************************************");

			// print the beacon
			out.println(JsonUtils.toJson(beacon));

			// if the aircraft has been recognized print its descriptor too
			if (descriptor.isKnown()) {
				out.println(JsonUtils.toJson(descriptor));
			}

			if (logIGC)
				igcLogger.log(beacon, descriptor);

			out.println("*********************************************");
		}
	}

	public static void main(String[] args) throws Exception {

		AircraftDescriptorProvider adp = new FileDbDescriptorProvider<OgnDb>(OgnDb.class);

		// create ogn client and pass it a reference to the previously created
		// descriptor provider
		OgnClient client1 = OgnClientFactory.createClient();

		// create second instance of OGN client, this one will connect to a
		// different port
		OgnClient client2 = OgnClientFactory.getBuilder().port(OgnClientConstants.OGN_DEFAULT_SRV_PORT + 1000)
				.descriptorProviders(adp).build();

		System.out.println("connecting...");
		client1.connect();

		// set some filter to the second instance of OGN client
		client2.connect("r/+49.782/+19.450/100");

		client1.subscribeToAircraftBeacons(new AcListener());
		client2.subscribeToAircraftBeacons(new AcListener());

		Thread.sleep(Long.MAX_VALUE);
	}
}