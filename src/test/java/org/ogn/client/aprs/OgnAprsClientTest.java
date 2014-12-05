/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client.aprs;

import static org.junit.Assert.assertEquals;
import static org.ogn.client.OgnClientProperties.PROP_OGN_SRV_NAME;
import static org.ogn.client.OgnClientProperties.PROP_OGN_SRV_PORT_UNFILTERED;
import static org.ogn.client.OgnClientProperties.PROP_OGN_SRV_RECONNECTION_TIMEOUT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ogn.client.AircraftBeaconListener;
import org.ogn.client.OgnClient;
import org.ogn.client.OgnClientFactory;
import org.ogn.client.ReceiverBeaconListener;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.ReceiverBeacon;

public class OgnAprsClientTest {

    static final Integer APRS_SRV_PORT = 9999;
    static {
        System.setProperty(PROP_OGN_SRV_NAME, "localhost");
        System.setProperty(PROP_OGN_SRV_PORT_UNFILTERED, APRS_SRV_PORT.toString());
        System.setProperty(PROP_OGN_SRV_RECONNECTION_TIMEOUT, "1000");
    }

    static List<String> serverSentences = new ArrayList<String>();

    TcpMockAprsServer aprsServer;

    static class AcListener implements AircraftBeaconListener {

        List<AircraftBeacon> beacons = new ArrayList<>();

        @Override
        public void onUpdate(AircraftBeacon beacon, AircraftDescriptor descriptor) {
            beacons.add(beacon);
        }
    }

    static class RbListener implements ReceiverBeaconListener {
        List<ReceiverBeacon> beacons = new ArrayList<>();

        @Override
        public void onUpdate(ReceiverBeacon beacon) {
            beacons.add(beacon);
        }
    }

    @BeforeClass
    public static void classSetUp() throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(
                OgnAprsClientTest.class.getResourceAsStream("server-sentences.txt"), "UTF-8"));

        String line;
        while ((line = br.readLine()) != null) {
            serverSentences.add(line);
        }

        br.close();

    }

    @Before
    public void setUp() {
        //
    }

    @Test
    public void test1() throws Exception {
        aprsServer = new TcpMockAprsServer(APRS_SRV_PORT, serverSentences, 300);
        aprsServer.runOneCycle();
        Thread.sleep(2000);

        OgnClient client = OgnClientFactory.createClient();
        client.connect();

        AcListener acListener = new AcListener();
        RbListener rbListener = new RbListener();

        client.subscribeToAircraftBeacons(acListener);

        // this call should be ignored - a reference to the same listener is passed
        client.subscribeToAircraftBeacons(acListener);
        // this call should be ignored - a reference to the same listener is passed
        client.subscribeToAircraftBeacons(acListener);

        Thread.sleep(500);
        client.subscribeToReceiverBeacons(rbListener);

        Thread.sleep(6000);

        // make sure all aircraft beacons have been received
        assertEquals(12, acListener.beacons.size());
        // make sure all receiver beacons have been received
        assertEquals(2, rbListener.beacons.size());
        client.disconnect();
    }

    @After
    public void tearDown() {
        aprsServer.stop();
    }
}
