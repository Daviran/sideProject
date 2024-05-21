package edi.editor.ediApi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodecoParser {

    public static class Container {
        public String number;
        public String portOfLoading;
        public String portOfDischarge;
        public String containerCarrier;
        public String grossWeight;
        public String vgmWeight;
    }

    public static class CodecoData {
        public List<Container> containers = new ArrayList<>();
    }

    public static CodecoData parse(BufferedReader reader) throws IOException {
        CodecoData codecoData = new CodecoData();
        Container currentContainer = null;

        String line;
        while ((line = reader.readLine()) != null) {
            String segment = line.trim();
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
                        codecoData.containers.add(currentContainer);
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

        return codecoData;
    }
}
