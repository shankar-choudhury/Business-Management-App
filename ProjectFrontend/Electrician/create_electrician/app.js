new Vue({
    el: '#app',
    data() {
        return {
            firstName: '',
            lastName: '',
            email: '',
            phoneNumber: '',
            salary: '',
            buildingNumber: '',
            street: '',
            city: '',
            state: '',
            zipcode: ''
        };
    },
    methods: {
        async createElectrician() {
            try {
                const electricianData = {
                    salary: this.salary,
                    homeAddress: { 
                        buildingNumber: this.buildingNumber,
                        street: this.street,
                        city: this.city,
                        state: this.state,
                        zipcode: this.zipcode
                    },
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email,
                    phoneNumber: this.phoneNumber,
                    jobs: [],
                };
        
                const response = await axios.post('http://localhost:8080/electricians', electricianData);
                console.log(response.data);
                alert('Electrician created successfully');
                // Reset form fields
                this.firstName = '';
                this.lastName = '';
                this.email = '';
                this.phoneNumber = '';
                this.salary = '';
                this.buildingNumber = '';
                this.street = '';
                this.city = '';
                this.state = '';
                this.zipcode = '';
            } catch (error) {
                console.error('Error creating electrician:', error);
            }
        },
    }
})