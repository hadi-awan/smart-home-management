<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-card class="elevation-12">
          <v-toolbar color="primary" dark flat>
            <v-toolbar-title>Login</v-toolbar-title>
            <v-spacer></v-spacer>
          </v-toolbar>
          <v-card-text>
            <v-form @submit.prevent="submitForm">
              <v-text-field
                  v-model="email"
                  label="Email"
                  name="email"
                  prepend-icon="mdi-email"
                  type="email"
                  required
              ></v-text-field>

              <v-text-field
                  v-model="password"
                  label="Password"
                  name="password"
                  prepend-icon="mdi-lock"
                  type="password"
                  required
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                color="primary"
                @click="submitForm"
                :loading="loading"
            >
              Login
            </v-btn>
          </v-card-actions>
          <v-snackbar
              v-model="snackbar"
              :color="snackbarColor"
              timeout="3000"
          >
            {{ snackbarText }}
          </v-snackbar>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Login',
  data() {
    return {
      email: '',
      password: '',
      loading: false,
      snackbar: false,
      snackbarText: '',
      snackbarColor: 'success'
    };
  },

  methods: {
    async submitForm() {
      if (!this.email || !this.password) {
        this.showError('Please fill in all fields');
        return;
      }

      this.loading = true;

      try {
        // Create form data to match backend's @RequestParam expectation
        const formData = new URLSearchParams();
        formData.append('email', this.email);
        formData.append('password', this.password);

        const response = await axios.get(`http://localhost:8080/user/login?${formData.toString()}`, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        });

        console.log('Login response:', response.data); // Add this for debugging

        if (response.data) {
          // Check if we got a user object back (successful login)
          if (response.data.id) {
            this.showSuccess('Login successful!');
            localStorage.setItem('user', JSON.stringify(response.data));
            this.$router.push('/');
          } else if (response.data.success === "false") {
            this.showError(response.data.message || 'Login failed');
          } else {
            this.showError('Invalid credentials');
          }
        } else {
          this.showError('Invalid credentials');
        }
      } catch (error) {
        console.error('Login error:', error);
        if (error.response) {
          console.log('Error response:', error.response.data); // Add this for debugging
        }
        const errorMessage = error.response?.data?.message || 'Login failed. Please try again.';
        this.showError(errorMessage);
      } finally {
        this.loading = false;
      }
    },

    showSuccess(text) {
      this.snackbarText = text;
      this.snackbarColor = 'success';
      this.snackbar = true;
    },

    showError(text) {
      this.snackbarText = text;
      this.snackbarColor = 'error';
      this.snackbar = true;
    }
  }
};
</script>

<style scoped>
.fill-height {
  min-height: calc(100vh - 64px);
}
</style>