<template>
  <v-app>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
      <v-card class="pa-6" elevation="10" max-width="500px">
        <v-card-title class="text-h5 font-weight-bold justify-center">
          Create an Account
        </v-card-title>
        <v-card-text>
          <v-form ref="form" v-model="valid">
            <!-- Email Field -->
            <v-text-field
                v-model="email"
                label="Email"
                :rules="[rules.required, rules.email]"
                outlined
                dense
            ></v-text-field>

            <!-- Password Field -->
            <v-text-field
                v-model="password"
                label="Password"
                :rules="[rules.required, rules.minLength]"
                type="password"
                outlined
                dense
            ></v-text-field>

            <!-- Confirm Password Field -->
            <v-text-field
                v-model="confirmPassword"
                label="Confirm Password"
                :rules="[rules.required, matchPassword]"
                type="password"
                outlined
                dense
            ></v-text-field>
          </v-form>
        </v-card-text>

        <v-card-actions class="d-flex justify-between">
          <v-btn text @click="goToLogin">Already have an account? Log In</v-btn>
          <v-btn color="primary" @click="submitForm">Sign Up</v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      valid: false,
      email: '',
      password: '',
      confirmPassword: '',
      rules: {
        required: value => !!value || 'This field is required.',
        email: value => /.+@.+\..+/.test(value) || 'E-mail must be valid.',
        minLength: value =>
            (value && value.length >= 6) || 'Password must be at least 6 characters.',
        matchPassword: value =>
            value === this.password || 'Passwords must match.',
      },
    };
  },
  methods: {
    submitForm() {
      if (this.$refs.form.validate()) {
        console.log('Form is valid:', {
          email: this.email,
          password: this.password,
        });
        // Proceed with form submission logic (e.g., API call)
      } else {
        console.log('Form validation failed.');
      }
    },
    goToLogin() {
      // Logic to navigate to the login page
      console.log('Navigate to login');
    },
  },
};
</script>

<style scoped>
/* Styling for a clean and modern look */
.v-card {
  border-radius: 12px;
}

.v-card-title {
  color: #424242;
}

.v-btn {
  text-transform: none;
}
</style>
