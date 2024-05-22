package edi.editor.ediApi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodecoParser {

    public static class Container {
        public String containerNumber;
        public String dateTime;
        public String location;
        public String equipmentType;
        public String fullEmptyIndicator;
        public String sealNumber;
        public String weight;
    }

    public static class CodecoData {
        public List<Container> containers = new ArrayList<>();
    }

    public static CodecoData parse(BufferedReader reader) throws IOException {
        CodecoData codecoData = new CodecoData();
        String line;
        Container currentContainer = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            System.out.println("Processing segment: " + line);

            String[] elements = line.split("\\+");
            if (elements.length == 0) continue;

            String segmentId = elements[0];
            System.out.println("Segment ID: " + segmentId);

            switch (segmentId) {
                case "EQD":
                    if (elements.length > 2) {
                        String containerNumber = elements[2].trim();
                        currentContainer = new Container();
                        currentContainer.containerNumber = containerNumber;
                        currentContainer.equipmentType = elements.length > 3 ? elements[3].trim() : null;
                        codecoData.containers.add(currentContainer);
                        System.out.println("New container added: " + containerNumber);
                    }
                    break;

                case "DTM":
                    if (elements.length > 1 && currentContainer != null) {
                        currentContainer.dateTime = elements[1].split(":")[1].trim();
                        System.out.println("Date/Time updated: " + currentContainer.dateTime);
                    }
                    break;

                case "LOC":
                    if (elements.length > 2 && currentContainer != null) {
                        currentContainer.location = elements[2].split(":")[0].trim();
                        System.out.println("Location updated: " + currentContainer.location);
                    }
                    break;

                case "FTX":
                    if (elements.length > 4 && currentContainer != null) {
                        currentContainer.fullEmptyIndicator = elements[4].trim();
                        System.out.println("Full/Empty Indicator updated: " + currentContainer.fullEmptyIndicator);
                    }
                    break;

                case "SEL":
                    if (elements.length > 1 && currentContainer != null) {
                        currentContainer.sealNumber = elements[1].trim();
                        System.out.println("Seal Number updated: " + currentContainer.sealNumber);
                    }
                    break;

                case "MEA":
                    System.out.println("ELEMENT LENGTH: " + elements.length);
                    if (elements.length > 3 && currentContainer != null) {
                        String[] measurementParts = elements[3].split(":");
                        if (measurementParts.length > 1 && measurementParts[0].equals("KGM")) {
                            currentContainer.weight = measurementParts[1].trim();
                            System.out.println("Weight updated: " + currentContainer.weight);
                        }
                    } else if (elements.length == 3 && currentContainer != null) {
                        String[] measurementParts = elements[2].split(":");
                        if (measurementParts.length > 1 && measurementParts[0].equals("KGM")) {
                            currentContainer.weight = measurementParts[1].trim();
                            System.out.println("Weight updated: " + currentContainer.weight);
                        }
                    }
                    break;

                default:
                    System.out.println("Unhandled segment ID: " + segmentId);
                    break;
            }
        }

        return codecoData;
    }
}
