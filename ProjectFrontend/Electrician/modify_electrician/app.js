new Vue({
    el: '#app',
    data() {
        return {
            fullName: '',
            electricians: [],
            selectedElectrician: null,
            address: {
                id: null,
                buildingNumber: '',
                street: '',
                city: '',
                state: '',
                zipcode: ''
            },
            homeBuildingNumber: '',
            homeStreet: '',
            homeCity: '',
            homeState: '',
            homeZipcode: '',
        };
    },
    methods: {
        fetchElectricians() {
            if (this.fullName.trim() !== '') {
                const firstAndLastName = this.fullName.trim().split(' ');
                const searchFirstName = firstAndLastName[0];
                const searchLastName = firstAndLastName[1];
                axios.get(`http://localhost:8080/electricians/search?firstName=${searchFirstName}&lastName=${searchLastName}`)
                    .then(response => {
                        this.electricians = response.data;
                    })
                    .catch(error => {
                        console.error('Error fetching electricians:', error);
                    });
            } else {
                this.electricians = [];
            }
        },
        selectElectrician(electrician) {
            this.fullName = electrician.firstName + ' ' + electrician.lastName;
            this.selectedElectrician = electrician;
            // Assign address fields directly to the address object
            this.address.id = electrician.homeAddress.id;
            this.address.buildingNumber = electrician.homeAddress.buildingNumber;
            this.address.street = electrician.homeAddress.street;
            this.address.city = electrician.homeAddress.city;
            this.address.state = electrician.homeAddress.state;
            this.address.zipcode = electrician.homeAddress.zipcode;
            console.log(this.address);
        },
        deleteElectrician() {
            if (confirm("Are you sure you want to delete this electrician?")) {
                axios.delete(`http://localhost:8080/electricians/${this.selectedElectrician.id}`)
                    .then(response => {
                        alert('Electrician deleted successfully.');
                        // Clear selected electrician and electrician list
                        this.selectedElectrician = null;
                        this.fullName = '';
                        this.electricians = [];
                    })
                    .catch(error => {
                        console.error('Error deleting electrician:', error);
                        alert('An error occurred while deleting the electrician.');
                    });
            }
        },
        saveChanges() {
            axios.put(`http://localhost:8080/electricians/personalDetails/${this.selectedElectrician.id}`, {
                salary: this.selectedElectrician.salary,
                firstName: this.selectedElectrician.firstName,
                lastName: this.selectedElectrician.lastName,
                email: this.selectedElectrician.email,
                phoneNumber: this.selectedElectrician.phoneNumber
            })
            .then(response => {
                alert('Electrician updated successfully.');
                this.selectedElectrician = null;
                this.fullName = '';
                this.electricians = []; 
            })
            .catch(error => {
                console.error('Error updating electrician:', error);
                alert('An error occurred while updating the electrician.');
            });
        },
        openModifyAddressModal() {
            // Populate address fields in the modal with selected electrician's address
            this.address.id = this.selectedElectrician.homeAddress.id;
            this.address.buildingNumber = this.selectedElectrician.homeAddress.buildingNumber;
            this.address.street = this.selectedElectrician.homeAddress.street;
            this.address.city = this.selectedElectrician.homeAddress.city;
            this.address.state = this.selectedElectrician.homeAddress.state;
            this.address.zipcode = this.selectedElectrician.homeAddress.zipcode;
            // Show the modal
            $('#modifyAddressModal').modal('show');
        },
        updateAddress() {
            // Send updated address details to the backend
            axios.put(`http://localhost:8080/electricians/homeAddress/${this.selectedElectrician.id}`, this.address)
                .then(response => {
                    // Handle success
                    alert('Address updated successfully.');
                    // Close the modal
                    $('#modifyAddressModal').modal('hide');
                })
                .catch(error => {
                    // Handle error
                    console.error('Error updating address:', error);
                    alert('An error occurred while updating the address.');
                });
        }
    }
});