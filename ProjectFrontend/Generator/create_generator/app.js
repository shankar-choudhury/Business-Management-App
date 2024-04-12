new Vue({
    el: '#app',
    data() {
        return {
            manufacturer: '',
            kWSize: ''
        };
    },
    methods: {
        async createGenerator() {
            try {
                const generatorData = {
                    manufacturer: this.manufacturer,
                    kwsize: this.kWSize
                };
                console.log(generatorData);
                const response = await axios.post('http://localhost:8080/generators', generatorData);
                console.log(response.data);
                alert('Generator created successfully');
                // Reset form fields
                this.manufacturer = '';
                this.kWSize = '';
            } catch (error) {
                console.error('Error creating generator:', error);
            }
        },
    }
})