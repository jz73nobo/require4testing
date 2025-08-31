import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// 创建 axios 实例，配置基础URL和凭证设置
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true // 允许发送和接收 cookie
});

export default {
  // 检查认证状态
  async checkAuthStatus() {
    try {
      const response = await apiClient.get('/api/auth/status');
      return response.data;
    } catch (error) {
      console.error('检查认证状态失败:', error);
      return { authenticated: false };
    }
  },
  
  // 登录方法
  async login(username, password) {
    try {
      const formData = new URLSearchParams();
      formData.append('username', username);
      formData.append('password', password);
      
      const response = await apiClient.post('/api/auth/login', formData, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
      return response.data;
    } catch (error) {
      console.error('登录失败:', error);
      throw error;
    }
  },
  
  // 登出方法
  async logout() {
    try {
      const response = await apiClient.post('/api/auth/logout');
      return response.data;
    } catch (error) {
      console.error('登出失败:', error);
      throw error;
    }
  }
};