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
            isEditing: false // Add a flag to indicate whether the selected customer is being edited
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
            this.isEditing = true; // Set editing flag to true when a customer is selected
            this.showDropdown = false;    
        },
        async updateCustomer() {
            axios.put(`http://localhost:8080/customers/${this.selectedCustomer.id}`, this.selectedCustomer)
                .then(response => {
                    console.log('Customer updated:', response.data);
                    // Optionally, you can display a success message or perform any other action upon successful update
                    this.isEditing = false; // Reset editing flag after updating
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
        cancelEdit() {
            // Reset fields and flags when canceling edit
            this.firstName = this.selectedCustomer.firstName;
            this.lastName = this.selectedCustomer.lastName;
            this.email = this.selectedCustomer.email;
            this.phoneNumber = this.selectedCustomer.phoneNumber;
            this.isEditing = false;
        }
    }
});