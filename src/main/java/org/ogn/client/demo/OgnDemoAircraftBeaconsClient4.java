/**
 * Copyright (c) 2014 OGN, All Rights Reserved.
 */

package org.ogn.client.demo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.ogn.client.*;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;
import org.ogn.commons.db.FileDbDescriptorProvider;
import org.ogn.commons.db.ogn.OgnDb;
import org.ogn.commons.igc.IgcLogger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A small demo program demonstrating the usage of the ogn-client with aircraft descriptor providers.
 *
 * @author wbuczak
 */
public class OgnDemoAircraftBeaconsClient4 {

    static IgcLogger igcLogger = new IgcLogger();
    // enable if you want to log to IGC files
    static boolean logIGC = true;

    static {
        // ignore parsing receiver beacons, we are not interested in them in
        // this demo and there is
        // no point in wasting CPU on that
        System.setProperty(OgnClientProperties.PROP_OGN_CLIENT_IGNORE_RECEIVER_BEACONS, "true");
    }

    static Map<String, String> readFilter(String fileName) {
        Map<String, String> id2reg = new HashMap<String, String>();
        try {
            Reader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
            for (CSVRecord record : records) {
                id2reg.put(record.get("ID"), record.get("CALL"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id2reg;
    }

    public static void main(String[] args) throws Exception {

        Map<String, String> id2reg = null;
        if (args.length > 0 && args[0] != null && args[0].length() > 0) {
            String filters[] = args[0].split(",");
            for (String filter : filters) {
                if (id2reg == null) {
                    id2reg = readFilter(filter);
                } else {
                    id2reg.putAll(readFilter(filter));
                }
            }
            AcListener.id2reg = id2reg;
            igcLogger.setId2reg(id2reg);
        }

        AircraftDescriptorProvider adp = new FileDbDescriptorProvider<OgnDb>(OgnDb.class);
        OgnClient client1 = OgnClientFactory.createClient();
        OgnClient client2 = OgnClientFactory.getBuilder().port(OgnClientConstants.OGN_DEFAULT_SRV_PORT + 1000)
                .descriptorProviders(adp).build();

        System.out.println("connecting...");
        client1.connect();

        // set some filter to the second instance of OGN client
        client2.connect("r/+49.782/+19.450/100");

        client1.subscribeToAircraftBeacons(new AcListener());
        client2.subscribeToAircraftBeacons(new AcListener());
        System.out.println("...connected");

        Thread.sleep(Long.MAX_VALUE);
    }

    static class AcListener implements AircraftBeaconListener {

        static Map<String, String> id2reg = null;

        @Override
        public void onUpdate(AircraftBeacon beacon, Optional<AircraftDescriptor> descriptor) {

            if (logIGC && id2reg != null && id2reg.containsKey(beacon.getAddress())) {
                igcLogger.log(beacon, descriptor);
            } else {
                //out.println("*********************************************");
                //out.println(JsonUtils.toJson(beacon));
            }
        }
    }


}