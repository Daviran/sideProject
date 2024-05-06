export function parseBaplieData(ediContent) {
    const baplieData = {
      ports: [],
      containers: [],
      vessels: [],
      voyage: {},
      equipment: [],
      seals: [],
      cargo: [],
      hazardousCargo: [],
      dangerousGoods: [],
      stowageInstructions: [],
      freeText: [],
      remarks: [],
      otherInformation: [],
      summary: {},
    };
  
    const segments = ediContent.split("'");
  
    segments.forEach(segment => {
      const segmentId = segment.substring(0, 3);
      switch (segmentId) {
        case 'LOC': {
          // Parse location details
          const portName = segment.substring(4, 24).trim();
          const portCode = segment.substring(24, 27).trim();
          const portDetails = { name: portName, code: portCode };
          baplieData.ports.push(portDetails);
          break;
        }
        case 'EQD': {
          // Parse container details
          const containerNumber = segment.substring(4, 19).trim();
          const containerDetails = { number: containerNumber };
          baplieData.containers.push(containerDetails);
          break;
        }
        case 'DTM': {
          // Parse date/time
          const dateTimeQualifier = segment.substring(4, 7).trim();
          const dateTime = segment.substring(8).trim();
          // Handle different qualifiers and store accordingly
          switch (dateTimeQualifier) {
            case '132':
              baplieData.voyage.departureDate = dateTime;
              break;
            case '133':
              baplieData.voyage.arrivalDate = dateTime;
              break;
            // Add more cases for other qualifiers as needed
          }
          break;
        }
        case 'TDT': {
          // Parse transport details
          const transportMode = segment.substring(4, 7).trim();
          const carrierCode = segment.substring(14, 17).trim();
          const transportDetails = { mode: transportMode, carrierCode: carrierCode };
          baplieData.voyage.transportDetails = transportDetails;
          break;
        }
        case 'RFF': {
          // Parse reference number
          const referenceQualifier = segment.substring(4, 7).trim();
          const referenceNumber = segment.substring(8, 30).trim();
          // Handle different qualifiers and store accordingly
          switch (referenceQualifier) {
            case 'BM':
              baplieData.voyage.bookingReference = referenceNumber;
              break;
            // Add more cases for other qualifiers as needed
          }
          break;
        }
        // Add cases for other common segment types
        // For example: VES, VEH, SEL, etc.
      }
    });
  
    return baplieData;
  }