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
          const containerNumber = segment.substring(4, 19).replace(/^CN\+/, '').replace(/\+$/, '');;
          currentContainer = {
            number: containerNumber,
            portOfLoading: null,
            portOfDestination: null,
            portOfDischarge: null,
            containerCarrier: null,
            grossWeight: null,      
            vgmWeight: null      
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
                if (locationCode === '83' && !currentContainer.portOfDestination) {
                  currentContainer.portOfDestination = { code: locationCode, name: locationName };
                } else if (locationCode === '9' && !currentContainer.portOfLoading) {
                  currentContainer.portOfLoading = { code: locationCode, name: locationName };
                } else if (locationCode === '11' && !currentContainer.portOfDischarge) {
                  currentContainer.portOfDischarge = { code: locationCode, name: locationName };
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
            const measurementValue = segment.substring(8).trim();
            if (measurementCode === 'WT' && currentContainer) {
              currentContainer.grossWeight = measurementValue;
            } else if (measurementCode === 'VGM' && currentContainer) {
              currentContainer.vgmWeight = measurementValue.replace('+KGM:','');
            }
            break;
          }

    }
  })

  return baplieData
}
