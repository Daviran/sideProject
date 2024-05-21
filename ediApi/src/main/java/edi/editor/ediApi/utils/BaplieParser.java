package edi.editor.ediApi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaplieParser {

    public static class Container {
        public String number;
        public String portOfLoading;
        public String portOfDischarge;
        public String portOfDestination;
        public String containerCarrier;
        public String grossWeight;
        public String vgmWeight;
        public String position;
        public String shipper;
    }

    public static class BaplieData {
        public List<Container> containers = new ArrayList<>();
    }

    public static BaplieData parse(BufferedReader reader) throws IOException {
        BaplieData baplieData = new BaplieData();
        List<String> lines = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            lines.add(line.trim());
        }

        Collections.reverse(lines);  // Reverse the lines to read from bottom to top

        Container currentContainer = null;

        for (String segment : lines) {
            if (segment.isEmpty()) continue;

            System.out.println("Processing segment: " + segment); // Debug statement

            // Split the segment using the '+' delimiter
            String[] elements = segment.split("\\+");
            if (elements.length == 0) continue;

            String segmentId = elements[0];
            System.out.println("Segment ID: " + segmentId); // Debug statement
            System.out.println("Elements: " + java.util.Arrays.toString(elements)); // Debug statement

            switch (segmentId) {
                case "EQD":
                    if (elements.length > 2) {
                        String containerNumber = elements[2].trim();
                        currentContainer = new Container();
                        currentContainer.number = containerNumber;
                        baplieData.containers.add(currentContainer);
                        System.out.println("New container added: " + containerNumber); // Debug statement
                    }
                    break;

                case "LOC":
                    if (elements.length > 2) {
                        String locationCode = elements[1];
                        String locationName = elements[2].split(":")[0].trim();
                        if (currentContainer != null) {
                            switch (locationCode) {
                                case "9":
                                    currentContainer.portOfLoading = locationName;
                                    System.out.println("POL updated: " + locationName); // Debug statement
                                    break;
                                case "11":
                                    currentContainer.portOfDischarge = locationName;
                                    System.out.println("POD updated: " + locationName); // Debug statement
                                    break;
                                case "83":
                                    currentContainer.portOfDestination = locationName;
                                    System.out.println("POD updated: " + locationName); // Debug statement
                                    break;
                                case "147":
                                    currentContainer.position = locationName;
                                    System.out.println("Position updated: " + locationName); // Debug statement
                                    break;
                            }
                        }
                    }
                    break;

                case "MEA":
                    if (elements.length > 3) {
                        String measurementCode = elements[1].trim();
                        String measurementValue = elements[3].split(":")[1].trim();
                        if (currentContainer != null) {
                            if (measurementCode.equals("AAE")) {
                                currentContainer.grossWeight = measurementValue;
                                System.out.println("Gross weight updated: " + measurementValue); // Debug statement
                            } else if (measurementCode.equals("VGM")) {
                                currentContainer.vgmWeight = measurementValue;
                                System.out.println("VGM weight updated: " + measurementValue); // Debug statement
                            }
                        }
                    }
                    break;

                case "NAD":
                    if (elements.length > 2) {
                        String carrierCode = elements[2].trim().split(":")[0];
                        if (currentContainer != null) { // Ensure current container is not null
                            currentContainer.containerCarrier = carrierCode;
                            System.out.println("Carrier updated: " + carrierCode); // Debug statement
                        }
                    }
                    break;
            }
        }

        // Reverse the containers list to maintain the original order
        Collections.reverse(baplieData.containers);

        return baplieData;
    }
}
