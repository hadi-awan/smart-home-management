
<template>
  <div class="login-form">
    <form @submit.prevent="submitForm()">
      <h3>Sign In</h3>

      <div class="login-box">
        <label>Email address</label>
        <input id="email" v-model="email" type="email" />
      </div>

      <div class="login-box">
        <label>Password</label>
        <input id="password" v-model="password" type="password" />
      </div>

      <button type="submit">Sign In</button>
    </form>
    <br />
  </div>
</template>

<script>
export default {
  name: 'login',
  data() {
    return {
      email: '',
      password: ''
    };
  },

  methods: {
    submitForm() {
      if (this.isParent === '') this.isParent = false;
      if (this.isChild === '') this.isChild = false;
      if (this.isGuest === '') this.isGuest = false;

      const path = `/user/store?name=${encodeURIComponent(this.name)}&email=${encodeURIComponent(this.email)}&password=${encodeURIComponent(this.password)}&isParent=${this.isParent}&isChild=${this.isChild}&isGuest=${this.isGuest}&active=true`;

      axios.post(path)
          .then(response => {
            console.log(response);
            window.location.href = '/';
          })
          .catch(error => {
            console.error(error);
            alert('Signup failed. Please try again.');
          });
    }
  }
};
</script>
