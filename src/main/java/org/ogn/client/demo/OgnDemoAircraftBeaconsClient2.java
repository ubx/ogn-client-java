/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client.demo;

import static java.lang.System.out;

import org.ogn.client.AircraftBeaconListener;
import org.ogn.client.OgnClient;
import org.ogn.client.OgnClientFactory;
import org.ogn.client.OgnClientProperties;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;
import org.ogn.commons.beacon.impl.AircraftDescriptorImpl;
import org.ogn.commons.db.FileDbDescriptorProvider;
import org.ogn.commons.db.flarmnet.FlarmNetDb;
import org.ogn.commons.db.ogn.OgnDb;
import org.ogn.commons.igc.IgcLogger;
import org.ogn.commons.utils.JsonUtils;

/**
 * A small demo program demonstrating the usage of the ogn-client with aircraft
 * descriptor providers.
 * 
 * @author wbuczak
 */
public class OgnDemoAircraftBeaconsClient2 {

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
		public void onUpdate(AircraftBeacon beacon, AircraftDescriptor descriptor) {
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

	/**
	 * A custom descriptor provider. For the use of this demo this custom
	 * descriptor provider always returns the same descriptor, no matter what
	 * address is passed to it
	 * 
	 * @author wbuczak
	 */
	static class MyCustomAircraftDescriptorProvider implements AircraftDescriptorProvider {

		@Override
		public AircraftDescriptor findDescriptor(String address) {
			// return always the same descriptor (just for this demo)
			return new AircraftDescriptorImpl("SP-NZA", "ZA", "Cessna 172S", true, true);
		}

	}

	public static void main(String[] args) throws Exception {

		// create an instance of FlarmNet descriptor provider
		AircraftDescriptorProvider adp1 = new FileDbDescriptorProvider<FlarmNetDb>(FlarmNetDb.class);

		// create an instance of OGN descriptor provider
		AircraftDescriptorProvider adp2 = new FileDbDescriptorProvider<OgnDb>(OgnDb.class, 10);

		// create an instance of "custom" descriptor provider
		AircraftDescriptorProvider adp3 = new MyCustomAircraftDescriptorProvider();

		// NOTE: the order matters. The OGN client will try to query for the
		// aircraft information the first provider
		// in the list. Only if no match is found it will continue with the
		// second provider etc..
		// create ogn client and give it the previously created descriptor
		// providers
		OgnClient client = OgnClientFactory.createClient(new AircraftDescriptorProvider[] { /*
																							 * adp1
																							 * ,
																							 */adp2 /*
																									 * ,
																									 * adp3
																									 */});

		System.out.println("connecting...");

		// connect with no filter
		client.connect();

		// client.connect("r/+49.98284/+20.09165/100");

		client.subscribeToAircraftBeacons(new AcListener());

		Thread.sleep(Long.MAX_VALUE);
	}
}