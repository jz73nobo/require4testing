<template>
  <div class="login-container">
    <form @submit.prevent="login">
      <div>
        <label for="username">username:</label>
        <input type="text" id="username" v-model="username" required>
      </div>
      <div>
        <label for="password">password:</label>
        <input type="password" id="password" v-model="password" required>
      </div>
      <button type="submit">Log in</button>
    </form>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    };
  },
  methods: {
    async login() {
      try {
        // 创建JSON格式的请求数据
        const requestData = {
          username: this.username,
          password: this.password
        };
        
        const response = await axios.post('http://localhost:8080/api/auth/login', requestData, {
          headers: {
            'Content-Type': 'application/json'  // 修改为JSON格式
          },
          withCredentials: true  // 允许发送和接收cookie
        });
        
        console.log('Login successful:', response.data);
        this.errorMessage = '';
        
        // 登录成功后跳转到存在的路由
        this.$router.push('/requirements');
      } catch (error) {
        console.error('Login Failed:', error);
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data.message || 'Login Failed';
        } else {
          this.errorMessage = 'Network error, please try again later';
        }
      }
    }
  }
};
</script>

<style scoped>
.error {
  color: red;
}
</style>