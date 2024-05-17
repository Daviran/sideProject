package edi.editor.ediApi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaplieParser {

    public static class Port {
        public String code;
        public String name;

        public Port(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    public static class Container {
        public String number;
        public Port portOfLoading;
        public Port portOfDestination;
        public Port portOfDischarge;
        public String containerCarrier;
        public String grossWeight;
        public String vgmWeight;
    }

    public static class BaplieData {
        public List<Container> containers = new ArrayList<>();
    }

    public static BaplieData parse(BufferedReader reader) throws IOException {
        BaplieData baplieData = new BaplieData();
        Container currentContainer = null;

        List<String> segments = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            segments.add(line);
        }

        // Process segments in reverse order
        for (int i = segments.size() - 1; i >= 0; i--) {
            String segment = segments.get(i).trim();
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
                                case "83":
                                    if (currentContainer.portOfDestination == null) {
                                        currentContainer.portOfDestination = new Port(locationCode, locationName);
                                        System.out.println("POD updated: " + locationName); // Debug statement
                                    }
                                    break;
                                case "9":
                                    if (currentContainer.portOfLoading == null) {
                                        currentContainer.portOfLoading = new Port(locationCode, locationName);
                                        System.out.println("POL updated: " + locationName); // Debug statement
                                    }
                                    break;
                                case "11":
                                    if (currentContainer.portOfDischarge == null) {
                                        currentContainer.portOfDischarge = new Port(locationCode, locationName);
                                        System.out.println("POD updated: " + locationName); // Debug statement
                                    }
                                    break;
                            }
                        }
                    }
                    break;

                case "NAD":
                    if (elements.length > 2) {
                        String carrierCode = elements[2].trim().split(":")[0];
                        if (currentContainer != null) {
                            currentContainer.containerCarrier = carrierCode;
                            System.out.println("Carrier updated: " + carrierCode); // Debug statement
                        }
                    }
                    break;

                case "MEA":
                    if (elements.length > 3) {
                        String measurementCode = elements[1].trim();
                        String[] measurementParts = elements[3].split(":");
                        if (measurementParts.length > 1) {
                            String measurementValue = measurementParts[1].trim();
                            if (currentContainer != null) {
                                if (measurementCode.equals("AAE")) {
                                    currentContainer.grossWeight = measurementValue;
                                    System.out.println("Gross weight updated: " + measurementValue); // Debug statement
                                } else if (measurementCode.equals("KGM") || measurementCode.equals("VGM")) {
                                    currentContainer.vgmWeight = measurementValue;
                                    System.out.println("VGM weight updated: " + measurementValue); // Debug statement
                                }
                            }
                        }
                    }
                    break;
            }
        }

        // Reverse the order of containers to reflect the correct order
        // might need some rework later
        List<Container> reversedContainers = new ArrayList<>();
        for (int i = baplieData.containers.size() - 1; i >= 0; i--) {
            reversedContainers.add(baplieData.containers.get(i));
        }
        baplieData.containers = reversedContainers;

        return baplieData;
    }
}