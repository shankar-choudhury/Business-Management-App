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
            createdAddresses: [], 
            createdCards: [], 
        };
    },
    computed: {
        customerFieldsFilled() {
            return this.firstName && this.lastName && this.email && this.phoneNumber;
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
        async createThisAddress() {
            try {
                const response = await axios.post('http://localhost:8080/addresses', {
                    buildingNumber: this.buildingNumber,
                    street: this.street,
                    city: this.city,
                    state: this.state, 
                    zipcode: this.zipcode
                });
                console.log(response.data);
                alert('Customer address created successfully');
                // Create an address object with response data
                const customerAddress = response.data;
                // Push onto createdAddress list
                this.createdAddresses.push(customerAddress); // Fixed here
                // Reset form fields
                this.buildingNumber = '';
                this.street = '';
                this.city = '';
                this.state = '';
                this.zipcode = '';
                // Hide the modal after successful submission
                $('#addressModal').modal('hide');
            } catch (error) {
                console.error('Error creating address:', error);
                alert('Error creating customer address. Try debugging error.');
            }
        },
        async createCreditCard() {
            try {
                const response = await axios.post('http://localhost:8080/credit-cards', {
                    number: this.cardNumber,
                    expMonth: this.expMonth,
                    expYear: this.expYear
                });
                console.log(response.data);
                alert('Credit card created successfully');
                // Push onto createdCards
                this.createdCards.push(response.data); // Fixed here
                // Reset form fields
                this.cardNumber = '';
                this.expMonth = '';
                this.expYear = '';
                // Hide the modal after successful submission
                $('#cardModal').modal('hide');
            } catch (error) {
                console.error('Error creating credit card:', error);
                alert('Error creating credit card. Try debugging error.');
            }
        }
    },
});