<template>
  <div>
    <v-data-table
        :headers="headers"
        :items="users"
        class="elevation-1"
    >
      <template v-slot:item.actions="{ item }">
        <v-btn
            color="error"
            small
            @click="deleteUser(item.id)"
        >
          Delete
        </v-btn>
      </template>
    </v-data-table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UserList',
  data() {
    return {
      users: [],
      headers: [
        { text: 'Name', value: 'name' },
        { text: 'Email', value: 'email' },
        { text: 'Role', value: 'role' },
        { text: 'Actions', value: 'actions', sortable: false }
      ]
    };
  },
  methods: {
    fetchUsers() {
      axios.get('http://localhost:8080/users')
          .then(response => {
            this.users = response.data;
          })
          .catch(error => {
            console.error('Error fetching users:', error);
          });
    },
    deleteUser(userId) {
      if (confirm('Are you sure you want to delete this user?')) {
        axios.post(`http://localhost:8080/users/destroy?id=${userId}`)
            .then(response => {
              console.log('User deleted successfully');
              this.fetchUsers(); // Refresh the list after deletion
            })
            .catch(error => {
              console.error('Error deleting user:', error);
            });
      }
    }
  },
  mounted() {
    this.fetchUsers();
  }
};
</script>