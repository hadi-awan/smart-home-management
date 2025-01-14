<template>
  <v-form @submit.prevent="handleSignup">
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
export default {
  data() {
    return {
      name: '',
      email: '',
      password: '',
      isParent: false,
      isChild: false,
      isGuest: false,
    };
  },
  methods: {
    handleSignup() {
      const signupData = {
        name: this.name,
        email: this.email,
        password: this.password,
        isParent: this.isParent,
        isChild: this.isChild,
        isGuest: this.isGuest,
      };

      console.log("Emitting signup data:", signupData);

      // Emit the event to the parent to handle the request
      this.$emit('signup', signupData);

      // Perform the POST request
      axios.post('http://localhost:8080/user/store', signupData)
          .then(response => {
            console.log("User created successfully:", response.data);
          })
          .catch(error => {
            console.error("There was an error creating the user:", error);
          });
    }
  }
};
</script>
