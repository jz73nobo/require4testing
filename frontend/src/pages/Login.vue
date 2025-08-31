<template>
  <div class="login-container">
    <form @submit.prevent="login">
      <div>
        <label for="username">用户名:</label>
        <input type="text" id="username" v-model="username" required>
      </div>
      <div>
        <label for="password">密码:</label>
        <input type="password" id="password" v-model="password" required>
      </div>
      <button type="submit">登录</button>
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
        
        console.log('登录成功:', response.data);
        this.errorMessage = '';
        
        // 登录成功后跳转到存在的路由
        this.$router.push('/requirements');
      } catch (error) {
        console.error('登录失败:', error);
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data.message || '登录失败';
        } else {
          this.errorMessage = '网络错误，请稍后重试';
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