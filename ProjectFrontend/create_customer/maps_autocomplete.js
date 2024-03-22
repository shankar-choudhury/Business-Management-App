// Initialize Google Maps Places Autocomplete
function initAutocomplete() {
    var addressInputs = document.getElementsByClassName('autocomplete');
    Array.prototype.forEach.call(addressInputs, function(input) {
        var autocomplete = new google.maps.places.Autocomplete(input);
        autocomplete.setFields(['address_components', 'formatted_address']);
        autocomplete.addListener('place_changed', function() {
            var place = autocomplete.getPlace();
            if (!place.geometry) {
                window.alert("No details available for input: '" + place.name + "'");
                return;
            }
            var addressComponents = place.address_components;
            var formattedAddress = place.formatted_address;
            fillAddressFields(addressComponents, input);
        });
    });
}

// Fill address fields with the selected place's address components
function fillAddressFields(addressComponents, input) {
    var address = {
        streetNumber: '',
        street: '',
        city: '',
        state: '',
        zipcode: ''
    };
    addressComponents.forEach(function(component) {
        switch (component.types[0]) {
            case 'street_number':
                address.streetNumber = component.long_name;
                break;
            case 'route':
                address.street += component.long_name;
                break;
            case 'locality':
                address.city = component.long_name;
                break;
            case 'administrative_area_level_1':
                address.state = component.short_name;
                break;
            case 'postal_code':
                address.zipcode = component.long_name;
                break;
        }
    });
    // Set the values of the corresponding input fields
    if (input.id === 'addressInput') {
        document.getElementById('buildingNumberInput').value = address.streetNumber;
        document.getElementById('streetInput').value = address.street;
        document.getElementById('cityInput').value = address.city;
        document.getElementById('stateInput').value = address.state;
        document.getElementById('zipcodeInput').value = address.zipcode;
    } else if (input.id === 'billingAddressInput') {
        document.getElementById('billingBuildingNumberInput').value = address.streetNumber;
        document.getElementById('billingStreetInput').value = address.street;
        document.getElementById('billingCityInput').value = address.city;
        document.getElementById('billingStateInput').value = address.state;
        document.getElementById('billingZipcodeInput').value = address.zipcode;
    }
}

// Load the Google Maps Places API
function loadGoogleMapsScript() {
    var script = document.createElement('script');
    script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBCKtOTd-T2TUv_aJRgGcqiApRtTm5A1_U&libraries=places&callback=initAutocomplete';
    script.defer = true;
    script.async = true;
    document.body.appendChild(script);
}

// Load the Google Maps script when the window is loaded
window.onload = loadGoogleMapsScript;