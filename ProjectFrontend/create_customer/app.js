new Vue({
    el: '#app',
    data() {
        return {
            firstName: '',
            lastName: '',
            email: '',
            phoneNumber: '',
            buildingNumber: '',
            street: '',
            city: '',
            state: '',
            zipcode: '',
            cardNumber: '',
            expMonth: '',
            expYear: '',
            billingBuildingNumber: '', 
            billingStreet: '',         
            billingCity: '',           
            billingState: '',          
            billingZipcode: '', 
            createdAddresses: [], 
            createdCards: [], 
        };
    },
    computed: {
        customerFieldsFilled() {
            return this.firstName && 
            this.lastName && 
            this.email && 
            this.phoneNumber;
        }, 
        addressFieldsFilled() {
            return this.buildingNumber && 
            this.street && 
            this.city && 
            this.state && 
            this.zipcode;
        },
        creditCardFieldsFilled() {
            return this.cardNumber &&
            this.expMonth &&
            this.expYear &&
            this.billingBuildingNumber &&
            this.billingStreet &&
            this.billingCity &&
            this.billingState &&
            this.billingZipcode;
        }
    },
    methods: {
        async validateAndCreateCustomer() {
            if (this.customerFieldsFilled) {
                // If all fields are filled, create the customer
                await this.createCustomer();
            } else {
                // If any field is empty, show an alert
                alert('Please fill in all customer fields before submitting.');
            }
        },
        async createCustomer() {
            try {
                const customerData = {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email,
                    phoneNumber: this.phoneNumber,
                    addressList: this.createdAddresses, // Send the list of addresses directly
                    creditCardList: this.createdCards   // Send the list of credit cards directly
                };
        
                const response = await axios.post('http://localhost:8080/customers', customerData);
                console.log(response.data);
                alert('Customer created successfully');
                // Reset form fields
                this.firstName = '';
                this.lastName = '';
                this.email = '';
                this.phoneNumber = '';
                this.createdAddresses = []; // Clear the address list
                this.createdCards = [];     // Clear the credit card list
            } catch (error) {
                console.error('Error creating customer:', error);
                alert('Error creating customer. Try debugging error.');
            }
        },
        createAddress() {
            if (!this.addressFieldsFilled) {
                alert('Please fill in all address fields before adding the address.');
                return;
            }
            const address = {
                buildingNumber: this.buildingNumber,
                street: this.street,
                city: this.city,
                state: this.state,
                zipcode: this.zipcode
            };
            // Add newly created address to this customer's address list
            this.createdAddresses.push(address);
            alert('Address added successfully');
            // Reset form fields
            this.buildingNumber = '';
            this.street = '';
            this.city = '';
            this.state = '';
            this.zipcode = '';
            // Close modal
            $('#addressModal').modal('hide');
        },
        createCreditCard() {
            if (!this.creditCardFieldsFilled) {
                alert('Please fill in all credit card fields, including billing address details, before adding the credit card');
                return;
            }
            const creditCard = {
                number: this.cardNumber,
                expMonth: this.expMonth,
                expYear: this.expYear,
                billingAddress: {
                    buildingNumber: this.billingBuildingNumber,
                    street: this.billingStreet,
                    city: this.billingCity,
                    state: this.billingState,
                    zipcode: this.billingZipcode
                }
            };
            // Add newly created credit card to this customer's credit card list
            this.createdCards.push(creditCard);
            alert('Credit Card added successfully');
            // Reset form fields
            this.cardNumber = '';
            this.expMonth = '';
            this.expYear = '';
            this.billingBuildingNumber = '';
            this.billingStreet = '';
            this.billingCity = '';
            this.billingState = '';
            this.billingZipcode = '';
            // Close modal
            $('#cardModal').modal('hide');
        }
    },
});