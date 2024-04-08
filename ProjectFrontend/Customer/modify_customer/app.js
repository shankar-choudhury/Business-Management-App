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
            cardNumber: '',
            expMonth: '',
            expYear: '',
            billingBuildingNumber: '',
            billingStreet: '',
            billingCity: '',
            billingState: '',
            billingZipcode: '',
            isAddressSelected: false, 
            newAddress: { 
                buildingNumber: '',
                street: '',
                city: '',
                state: '',
                zipcode: ''
            },
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
        },
        isAddressFilled() {
            return (
                this.newAddress.buildingNumber &&
                this.newAddress.street &&
                this.newAddress.city &&
                this.newAddress.state &&
                this.newAddress.zipcode
            );
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
            this.selectedCustomer = JSON.parse(JSON.stringify(customer));
            this.selectedCustomerCopy = JSON.parse(JSON.stringify(customer));
            this.addresses = customer.addressList;
            this.creditcards = customer.creditCardList;
            this.isEditing = true;
            this.showDropdown = false;    
        },
        async updateCustomer() {
            axios.put(`http://localhost:8080/customers/${this.selectedCustomer.id}`, {
                firstName: this.selectedCustomer.firstName,
                lastName: this.selectedCustomer.lastName,
                email: this.selectedCustomer.email,
                phoneNumber: this.selectedCustomer.phoneNumber,
                addressList: this.selectedCustomer.addressList, 
                creditCardList: this.selectedCustomer.creditCardList
            })
            .then(response => {
                alert('Customer updated successfully.');
                this.resetForm();
            })
           .catch(error => {
                console.error('Error updating credit cards:', error);
                this.creditcards.pop();
            });
        },
        async validateAndUpdateCustomer() {
            if (!this.isPhoneNumberValid) {
                alert('Please enter a valid phone number.');
                return;
            }
            if (this.customerFieldsFilled) {
                try {
                    await this.updateCustomer();
                    // If updateCustomer succeeds, reset addCreditCard modal fields
                    this.resetCreditCardForm();
                } catch (error) {
                    console.error('Error updating customer:', error);
                    alert('An error occurred while updating the customer.');
                    // If updateCustomer fails, remove newly added credit card from creditcards list
                }
            } else {
                alert('Please fill in all customer fields before submitting.');
            }
        },
        selectAddress(address) {
            this.newAddress = {
                id: address.id,
                buildingNumber: address.buildingNumber,
                street: address.street,
                city: address.city,
                state: address.state,
                zipcode: address.zipcode
            };
            this.selectedAddress = address;
        },
        addAddress() {
            // Check if all address fields are filled
            if (!this.isAddressFilled) {
                alert('Please fill in all address fields before adding.');
                return;
            }

            // Push the new address to selectedCustomer's addressList
            this.selectedCustomer.addressList.push({
                buildingNumber: this.newAddress.buildingNumber,
                street: this.newAddress.street,
                city: this.newAddress.city,
                state: this.newAddress.state,
                zipcode: this.newAddress.zipcode
            });

            // Reset the newAddress object and address selection
            this.resetNewAddress();
            alert('Address added successfully.');
        },
        resetNewAddress() {
            this.newAddress = {
                buildingNumber: '',
                street: '',
                city: '',
                state: '',
                zipcode: ''
            };
        },
        updateAddress() {
            // Check if a valid address is selected
            if (!this.selectedAddress) {
                alert('Please select an address to update.');
                return;
            }
        
            // Find the index of the selected address in the addressList of selectedCustomer
            const index = this.selectedCustomer.addressList.findIndex(address => address.id === this.selectedAddress.id);

            // Check if the address is found
            if (index === -1) {
                alert('Selected address not found in selected customer\'s address list.');
                return;
            }

            // Update the address in the addressList of selectedCustomer directly from the input fields
            this.selectedCustomer.addressList[index].buildingNumber = this.newAddress.buildingNumber;
            this.selectedCustomer.addressList[index].street = this.newAddress.street;
            this.selectedCustomer.addressList[index].city = this.newAddress.city;
            this.selectedCustomer.addressList[index].state = this.newAddress.state;
            this.selectedCustomer.addressList[index].zipcode = this.newAddress.zipcode;

            const addressListIndex = this.addresses.findIndex(address => address.id === this.selectedAddress.id);
            this.addresses[addressListIndex].buildingNumber = this.newAddress.buildingNumber;
            this.addresses[addressListIndex].street = this.newAddress.street;
            this.addresses[addressListIndex].city = this.newAddress.city;
            this.addresses[addressListIndex].state = this.newAddress.state;
            this.addresses[addressListIndex].zipcode = this.newAddress.zipcode;
        
            this.resetAddressSelection();
            
            alert('Address updated successfully.');
            console.log(this.selectedCustomer.addressList[index]);
        },
        deleteAddress() {
            if (this.selectedAddress) {
                console.log(this.selectedAddress.id);
                axios.delete(`http://localhost:8080/addresses/${this.selectedAddress.id}`)
                .then(response => {
                    alert('Address deleted successfully.');
                    this.selectedCustomer.addressList = this.addresses.filter(address => address.id != this.selectedAddress.id);
                    this.addresses = this.addresses.filter(address => address.id !== this.selectedAddress.id);

                    // Remove associated credit cards from the creditcards array
                    this.creditcards = this.creditcards.filter(card => {
                    // Check if the credit card has a billing address and if it matches the deleted address
                        return card.billingAddress && 
                        card.billingAddress.id !== this.selectedAddress.id;
                    });
                    this.selectedCustomer.creditCardList = this.selectedCustomer.creditCardList.filter(card => {
                        return card.billingAddress &&
                        card.billingAddress.id !== this.selectedAddress.id;
                    })

                    this.selectedAddress = null;
                    console.log(this.addresses);
                })
                .catch(error => {
                    console.error('Error deleting address:', error);
                    alert('An error occurred while deleting the credit card.');
                });
            }
        }, 
        selectCreditCard(creditCard) {
            this.selectedCreditCard = creditCard;
            console.log(creditCard);
        },
        addCreditCard() {
            const newCreditCard = {
                number: this.cardNumber,
                expMonth: this.expMonth,
                expYear: this.expYear,
                billingAddress: {
                    buildingNumber: this.billingBuildingNumber,
                    street:  this.billingStreet,
                    city: this.billingCity,
                    state: this.billingState, 
                    zipcode: this.billingZipcode
                }
            }

            // Check if the new credit card number already exists
            const existingCreditCard = this.creditcards.find(card => card.number === newCreditCard.number);
            if (existingCreditCard) {
                alert('Credit card with the same number already exists.');
                return; // Exit the function without adding the credit card
            }

            this.creditcards = this.creditcards || [];
            this.creditcards.push(newCreditCard);
            this.selectedCustomer.creditCardList = this.selectedCustomer.creditCardList || [];
            this.selectedCustomer.creditCardList.push(newCreditCard);
            console.log(this.selectedCustomer.creditCardList);
            console.log(this.creditcards);
            alert('Credit card added successfully.');
            this.resetCreditCardForm();
            $('#addCreditCardModal').modal('hide');
        },
        showAddCreditCardModal() {
            $('#addCreditCardModal').modal('show');
        },
        deleteCreditCard() {
            if (this.selectedCreditCard) {
                axios.delete(`http://localhost:8080/credit-cards/${this.selectedCreditCard.id}`)
                    .then(response => {
                        alert('Credit card deleted successfully.');
                        this.creditcards = this.creditcards.filter(card => card.id !== this.selectedCreditCard.id);
                        this.selectedCreditCard = null;
                        console.log(response);
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
        resetCreditCardForm() {
            this.cardNumber = '';
            this.expMonth = '';
            this.expYear = '';
            this.billingBuildingNumber = '';
            this.billingStreet = '';
            this.billingCity = '';
            this.billingState = '';
            this.billingZipcode = '';
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