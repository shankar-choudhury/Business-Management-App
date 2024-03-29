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
            billingZipcode: ''
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
            // Check if the credit card list has been modified
            if (JSON.stringify(this.selectedCustomer.creditCardList) === JSON.stringify(this.selectedCustomerCopy.creditCardList)) {
                alert('No changes were made to the credit card list.');
                return;
            }
            axios.put(`http://localhost:8080/customers/${this.selectedCustomer.id}`, {
                firstName: this.selectedCustomer.firstName,
                lastName: this.selectedCustomer.lastName,
                email: this.selectedCustomer.email,
                phoneNumber: this.selectedCustomer.phoneNumber,
                creditCardList: this.selectedCustomer.creditCardList,
            })
            .then(response => {
                alert('Customer updated successfully.');
                this.resetForm();
            })
           .catch(error => {
                console.error('Error updating credit cards:', error);
            });
        },
        async validateAndUpdateCustomer() {
            if (!this.isPhoneNumberValid) {
                alert('Please enter a valid phone number.');
                return;
            }
            if (this.customerFieldsFilled) {
                await this.updateCustomer();
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
        async updateAddress() {
        },
        selectCreditCard(creditCard) {
            this.selectedCreditCard = creditCard;
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

            axios.post('http://localhost:8080/credit-cards', newCreditCard)
            .then(response => {
                const createdCard = response.data;
                this.creditcards = this.creditcards || [];
                this.creditcards.push(createdCard);
                this.selectedCustomer.creditCardList = this.selectedCustomer.creditCardList || [];
                this.selectedCustomer.creditCardList.push(newCreditCard);
                console.log(this.selectedCustomer.creditCardList);
                alert('Credit card added successfully.');
                this.resetCreditCardForm();
                $('#addCreditCardModal').modal('hide');
            })
            .catch(error => {
                console.error('Error adding credit card', error);
                alert('An error occurred while addong the credit card.');
            })
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