import Vue from 'vue';
import VueX from 'vuex';
import Vuetify from 'vuetify';
import axios from 'axios';
import '@mdi/font/css/materialdesignicons.css';
import 'vuetify/dist/vuetify.min.css';

import SignupForm from '../components/Signup.vue';
import UserList from '../components/UserList.vue';
import Login from '../components/Login.vue';
import profile from '../components/Profile';
import mainpage from '../components/MainPage';
import modules from '../components/Modules';
import core from '../components/CoreModule';
import security from '../components/SecurityModule';
import heating from '../components/HeatingModule';
import custom from '../components/CustomModule';
import simulator from '../components/SimulatorModule';
import editprofile from '../components/Editprofile';
import edithome from '../components/Edithome';
import houselayout from '../components/HouseLayout';

// Use VueX and Vuetify
Vue.use(VueX);
Vue.use(Vuetify);

// Make axios available globally
window.axios = axios;

// Create the Vuetify instance with Material Design Icons
const vuetify = new Vuetify({
    icons: {
        iconfont: 'mdi'
    },
    theme: {
        themes: {
            light: {
                primary: '#667eea',
                secondary: '#764ba2'
            }
        }
    }
});

// Create the store
const store = new VueX.Store({
    state: {
        outputMessage: '',
        isAway: false,
        user: {},
        zones: [],
        simulationState: null,
        simulationStart: null,
        houseLayoutKey: 0,
    },
    mutations: {
        appendMessage(state, message) {
            state.outputMessage += message + '\n';
        },
        changeAwayState(state) {
            state.isAway = !state.isAway;
        },
        changeSimulationState(state) {
            state.simulationState = !state.simulationState;
        }
    }
});

new Vue({
    el: '#app',
    vuetify,
    store: store,
    components: {
        SignupForm,
        UserList,
        Login,
        profile,
        mainpage,
        modules,
        core,
        security,
        heating,
        custom,
        simulator,
        editprofile,
        edithome,
        houselayout,
    },
    data: {
        name: '',
        email: '',
        password: '',
        isParent: false,
        isChild: false,
        isGuest: false,
    },
    methods: {
        // This method is triggered when the form is submitted
        handleSignup(userData) {
            console.log('Signup clicked with data:', userData);

            // If userData is not passed, use the data from this component's state
            const data = {
                name: userData.name || this.name,
                email: userData.email || this.email,
                password: userData.password || this.password,
                isParent: userData.isParent || this.isParent,
                isChild: userData.isChild || this.isChild,
                isGuest: userData.isGuest || this.isGuest
            };

            // Make the POST request to create the user
            axios.post('http://localhost:8080/user/store', data)
                .then(response => {
                    // Log and display the response for debugging
                    console.log('Response:', response.data);
                    if (response.data.success === "true") {
                        // User creation was successful, display success message
                        this.$store.commit('appendMessage', 'User created successfully!');
                    } else {
                        // User creation failed, show error message
                        this.$store.commit('appendMessage', 'User creation failed: ' + (response.data.message || 'Unknown error'));
                    }
                })
                .catch(error => {
                    // Log and display any errors
                    console.error('Error during signup:', error);
                    this.$store.commit('appendMessage', 'Error during signup: ' + error.message);
                });
        },

        deleteUser(userId) {
            if (confirm('Are you sure you want to delete this user?')) {
                axios.post(`http://localhost:8080/users/destroy?id=${userId}`)
                    .then(response => {
                        console.log('User deleted successfully');
                        // Refresh your user list or handle the UI update
                    })
                    .catch(error => {
                        console.error('Error deleting user:', error);
                        // Handle the error appropriately
                    });
            }
        }
    },
    mounted() {
        // Optionally, you can handle anything upon mounting the component
    }
});