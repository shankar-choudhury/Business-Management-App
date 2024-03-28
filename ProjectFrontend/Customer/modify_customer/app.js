new Vue({
    el: '#app',
    data() {
        return {
            fullName: '',
            firstName: '',
            lastName: '',
            email: '',
            phoneNumber: '',     
            customers: [],
            showDropdown: false,
            selectedCustomer: null,
            selectedCustomerCopy: null,
            isEditing: false, 
            addresses: [],
            selectedAddress: null,
            newAddress: { 
                id: '',
                buildingNumber: '',
                street: '',
                city: '',
                state: '',
                zipcode: ''
            },
            creditcards: [],
            selectedCreditCard: null,
        };
    },
    computed: {
        customerFieldsFilled() {
            return this.firstName && 
            this.lastName && 
            this.email && 
            this.phoneNumber;
        },
        isPhoneNumberValid() {
            return /^(\(?\d{3}\)?[-.\s]?)?\d{3}[-.\s]?\d{4}$/.test(this.phoneNumber);
        } 
    },
    methods: {
        fetchCustomers() {
            if (this.fullName.trim() !== '') {
                const firstAndLastName = this.fullName.trim().split(' ');
                var searchFirstName = firstAndLastName[0];
                var searchLastName = firstAndLastName[1];
                axios.get(`http://localhost:8080/customers/search?firstName=${searchFirstName}&lastName=${searchLastName}`)
                    .then(response => {
                        console.log('Response data:', response.data);
                        this.customers = response.data;
                        this.showDropdown = true;
                    })
                    .catch(error => {
                        console.error('Error fetching customers:', error);
                    });
            } else {
                this.customers = [];
                this.showDropdown = false;
            }
        },
        updateFirstName(value) {
            this.selectedCustomer.firstName = value;
        },
        updateLastName(value) {
            this.selectedCustomer.lastName = value;
        },
        updateEmail(value) {
            this.selectedCustomer.email = value;
        },
        updatePhoneNumber(value) {
            this.selectedCustomer.phoneNumber = value;
        },
        selectCustomer(customer) {
            this.fullName = customer.firstName + ' ' + customer.lastName;
            this.firstName = customer.firstName;
            this.lastName = customer.lastName;
            this.email = customer.email;
            this.phoneNumber = customer.phoneNumber;
            // Deep copy of the customer object
            this.selectedCustomer = JSON.parse(JSON.stringify(customer));
            this.selectedCustomerCopy = JSON.parse(JSON.stringify(customer));
            this.addresses = customer.addressList;
            this.creditcards = customer.creditCardList;
            this.isEditing = true; // Set editing flag to true when a customer is selected
            this.showDropdown = false;    
        },
        async updateCustomer() {
            axios.put(`http://localhost:8080/customers/${this.selectedCustomer.id}`, this.selectedCustomer)
                .then(response => {
                    console.log('Customer updated:', response.data);
                    alert('Customer updated successfully.');
                    // Optionally, you can display a success message or perform any other action upon successful update
                    this.resetForm(); // Reset editing flag after updating
                })
                .catch(error => {
                    console.error('Error updating customer:', error);
                    // Optionally, you can display an error message or perform any other action upon unsuccessful update
                });
        },
        async validateAndUpdateCustomer() {
            if (!this.isPhoneNumberValid) {
                alert('Please enter a valid phone number.');
                return;
            }
            if (this.customerFieldsFilled) {
                // If all fields are filled, create the customer
                await this.updateCustomer();
            } else {
                // If any field is empty, show an alert
                alert('Please fill in all customer fields before submitting.');
            }
        },
        selectAddress(address) {
            // Assign the selected address to the newAddress object
            this.newAddress = {
                id: address.id,
                buildingNumber: address.buildingNumber,
                street: address.street,
                city: address.city,
                state: address.state,
                zipcode: address.zipcode
            };
            console.log(this.newAddress);
            // Set the selectedAddress property for reference
            this.selectedAddress = address;
        },
        async updateAddress() {
            // Make sure new address is not empty
            if (this.newAddress.buildingNumber && this.newAddress.street && this.newAddress.city && this.newAddress.state && this.newAddress.zipcode) {
                // Assuming you have an API endpoint to update the address, modify the URL accordingly
                axios.put(`http://localhost:8080/addresses/${this.newAddress.id}`, this.newAddress)
                    .then(response => {
                        console.log('Address updated:', response.data);
                        alert('Address updated successfully.');
                        // Reset form and reload addresses
                        this.newAddress = {
                            buildingNumber: '',
                            street: '',
                            city: '',
                            state: '',
                            zipcode: ''
                        };
                        this.resetAddressSelection(); 
                        $('#addressModifyModal').modal('hide'); 
                        this.resetForm(); 
                    })
                    .catch(error => {
                        console.error('Error updating address:', error);
                    });
            } else {
                alert('Please fill in all address fields before submitting.');
            }
        },
        selectCreditCard(creditCard) {
            this.selectedCreditCard = creditCard;
        },
        addCreditCard() {
            // Implement functionality to add a credit card
            // This could involve opening a form inside the modal to add a new credit card
        },
        deleteCreditCard() {
            if (this.selectedCreditCard) {
                axios.delete(`http://localhost:8080/credit-cards/${this.selectedCreditCard.id}`)
                    .then(response => {
                        console.log('Credit card deleted:', response.data);
                        alert('Credit card deleted successfully.');
                        // Remove the deleted credit card from the creditcards array
                        this.creditcards = this.creditcards.filter(card => card.id !== this.selectedCreditCard.id);
                        this.selectedCreditCard = null;
                    })
                    .catch(error => {
                        console.error('Error deleting credit card:', error);
                        alert('An error occurred while deleting the credit card.');
                    });
            }
        },
        resetAddressSelection() {
            this.selectedAddress = null;
            this.newAddress = {
                buildingNumber: '',
                street: '',
                city: '',
                state: '',
                zipcode: ''
            };
        },
        resetForm() {
            this.fullName = '';
            this.firstName = '';
            this.lastName = '';
            this.email = '';
            this.phoneNumber = '';
            this.selectedCustomer = null;
            this.selectedCustomerCopy = null;
            this.isEditing = false;
            this.customers = []; 
            this.showDropdown = false;
        }
    }
});