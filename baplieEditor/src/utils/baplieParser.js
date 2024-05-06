export function parseBaplieData(ediContent) {
  const baplieData = {
    ports: [],
    containers: []
  }
  const segments = ediContent.split('\n')
  let currentContainer = null

  segments.forEach((segment) => {
    const segmentId = segment.substring(0, 3)
    switch (segmentId) {
        case 'EQD': {
          // Parse container details
          const containerNumber = segment.substring(4, 19).trim();
          currentContainer = {
            number: containerNumber,
            portOfLoading: null,
            portOfDestination: null,
            portOfCall: null,
            containerCarrier: null, // New field for container carrier
            grossWeight: null,      // New field for gross weight
            vgmWeight: null         // New field for VGM weight
          };
          if (!baplieData.containers) {
            baplieData.containers = [];
          }
          baplieData.containers.push(currentContainer);
          break;
        }
        case 'LOC': {
          // Parse location details
          const locationParts = segment.split('+');
          if (locationParts.length >= 3) {
            const locationCode = locationParts[1];
            const locationName = locationParts[2].split(':')[0];
            if (currentContainer) {
              if (!currentContainer.portOfLoading) {
                currentContainer.portOfLoading = { code: locationCode, name: locationName };
              } else if (!currentContainer.portOfDestination) {
                currentContainer.portOfDestination = { code: locationCode, name: locationName };
              } else if (!currentContainer.portOfCall) {
                currentContainer.portOfCall = { code: locationCode, name: locationName };
              }
            }
          }
          break;
        }
        case 'NAD': {
            // Parse container carrier
            const carrierCode = segment.substring(7, 10).trim();
            if (currentContainer) {
              currentContainer.containerCarrier = carrierCode;
            }
            break;
          }
          case 'MEA': {
            // Parse measurements
            const measurementCode = segment.substring(4, 7);
            const measurementValue = segment.substring(8, 13).trim();
            if (measurementCode === 'WT' && currentContainer) {
              currentContainer.grossWeight = measurementValue;
            } else if (measurementCode === 'VGM' && currentContainer) {
              currentContainer.vgmWeight = measurementValue;
            }
            break;
          }
      // case 'DTM': {
      //   // Parse date/time
      //   const dateTimeQualifier = segment.substring(4, 7).trim();
      //   const dateTime = segment.substring(8).trim();
      //   // Handle different qualifiers and store accordingly
      //   switch (dateTimeQualifier) {
      //     case '132':
      //       baplieData.voyage.departureDate = dateTime;
      //       break;
      //     case '133':
      //       baplieData.voyage.arrivalDate = dateTime;
      //       break;
      //     // Add more cases for other qualifiers as needed
      //   }
      //   break;
      // }
      // case 'TDT': {
      //   // Parse transport details
      //   const transportMode = segment.substring(4, 7).trim();
      //   const carrierCode = segment.substring(14, 17).trim();
      //   const transportDetails = { mode: transportMode, carrierCode: carrierCode };
      //   baplieData.voyage.transportDetails = transportDetails;
      //   break;
      // }
      // case 'RFF': {
      //   // Parse reference number
      //   const referenceQualifier = segment.substring(4, 7).trim();
      //   const referenceNumber = segment.substring(8, 30).trim();
      //   // Handle different qualifiers and store accordingly
      //   switch (referenceQualifier) {
      //     case 'BM':
      //       baplieData.voyage.bookingReference = referenceNumber;
      //       break;
      //     // Add more cases for other qualifiers as needed
      //   }
      //   break;
      // }
      // // Add cases for other common segment types
      // // For example: VES, VEH, SEL, etc.
    }
  })

  return baplieData
}
