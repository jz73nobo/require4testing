import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// 创建 axios 实例，配置基础URL和凭证设置
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true // 允许发送和接收 cookie
});

export default {
  // 检查认证状态 - 添加调试和确保数据格式
  async checkAuthStatus() {
    try {
      console.log('正在检查认证状态...');
      const response = await apiClient.get('/api/auth/status');
      console.log('认证状态响应:', response.data);
      
      // 确保返回的数据有正确的格式
      const responseData = response.data || {};
      return {
        authenticated: !!responseData.authenticated,
        username: responseData.username || '',
        authorities: responseData.authorities || []
      };
    } catch (error) {
      console.error('检查认证状态失败:', error);
      // 即使出错也返回明确的结构
      return { 
        authenticated: false, 
        username: '', 
        authorities: [] 
      };
    }
  },
  
  // 登录方法 - 添加调试
  async login(username, password) {
    try {
      console.log('正在登录，用户名:', username);
      const formData = new URLSearchParams();
      formData.append('username', username);
      formData.append('password', password);
      
      const response = await apiClient.post('/api/auth/login', formData, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
      console.log('登录成功响应:', response.data);
      return response.data;
    } catch (error) {
      console.error('登录失败:', error);
      if (error.response) {
        console.error('登录失败响应数据:', error.response.data);
        console.error('登录失败状态码:', error.response.status);
      }
      throw error;
    }
  },
  
  // 登出方法 - 添加调试
  async logout() {
    try {
      console.log('正在登出...');
      const response = await apiClient.post('/api/auth/logout');
      console.log('登出成功响应:', response.data);
      return response.data;
    } catch (error) {
      console.error('登出失败:', error);
      throw error;
    }
  }
};