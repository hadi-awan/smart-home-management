<template>
  <v-form @submit.prevent="submitForm">
    <v-text-field
        v-model="name"
        label="Name"
        required
    />
    <v-text-field
        v-model="email"
        label="Email"
        required
        type="email"
    />
    <v-text-field
        v-model="password"
        label="Password"
        required
        type="password"
    />
    <v-checkbox
        v-model="isParent"
        label="Parent"
    />
    <v-checkbox
        v-model="isChild"
        label="Child"
    />
    <v-checkbox
        v-model="isGuest"
        label="Guest"
    />
    <v-btn type="submit" color="primary">Sign Up</v-btn>
  </v-form>
</template>

<script>
import axios from 'axios';

export default {
  name: 'SignupForm',
  data() {
    return {
      name: '',
      email: '',
      password: '',
      isParent: false,
      isChild: false,
      isGuest: false
    };
  },
  methods: {
    async submitForm() {
      try {
        const params = {
          name: this.name,
          email: this.email,
          password: this.password,
          isParent: this.isParent,
          isChild: this.isChild,
          isGuest: this.isGuest
        };

        console.log('Sending signup data:', params);

        const response = await axios.post('http://localhost:8080/user/store', null, {
          params: params
        });

        console.log('Signup response:', response.data);
        if (response.data.success === "true") {
          alert('User created successfully!');
          // Optionally redirect to login
          // this.$router.push('/login');
        } else {
          alert('Signup failed: ' + (response.data.message || 'Unknown error'));
        }
      } catch (error) {
        console.error('Error during signup:', error);
        alert('Error during signup: ' + (error.response?.data?.message || error.message));
      }
    }
  }
};
</script>