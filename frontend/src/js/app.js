import Vue from 'vue';
import VueX from 'vuex';
import Vuetify from 'vuetify';
import axios from 'axios';
import '@mdi/font/css/materialdesignicons.css';
import 'vuetify/dist/vuetify.min.css';
import SignupForm from '../components/Signup.vue';

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

// Register components globally
Vue.component('signup-form', SignupForm);

// Create the Vue instance
new Vue({
    el: '#app',
    vuetify,
    render: h => h(SignupForm),
    mounted() {
        console.log('Vue app mounted');
    }
});