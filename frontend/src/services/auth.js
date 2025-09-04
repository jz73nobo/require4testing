import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// Create axios instance with base URL and credential settings
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true // Allow sending and receiving cookies
});

export default {
  // Check authentication status - add debugging and ensure data format
  async checkAuthStatus() {
    try {
      console.log('Checking authentication status...');
      const response = await apiClient.get('/api/auth/status');
      console.log('Authentication status response:', response.data);
      
      // Ensure returned data has correct format
      const responseData = response.data || {};
      return {
        authenticated: !!responseData.authenticated,
        username: responseData.username || '',
        authorities: responseData.authorities || []
      };
    } catch (error) {
      console.error('Failed to check authentication status:', error);
      // Return clear structure even if error occurs
      return { 
        authenticated: false, 
        username: '', 
        authorities: [] 
      };
    }
  },
  
  // Login method - add debugging
  async login(username, password) {
    try {
      console.log('Logging in, username:', username);
      const formData = new URLSearchParams();
      formData.append('username', username);
      formData.append('password', password);
      
      const response = await apiClient.post('/api/auth/login', formData, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
      console.log('Login successful response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Login failed:', error);
      if (error.response) {
        console.error('Login failed response data:', error.response.data);
        console.error('Login failed status code:', error.response.status);
      }
      throw error;
    }
  },
  
  // Logout method - add debugging
  async logout() {
    try {
      console.log('Logging out...');
      const response = await apiClient.post('/api/auth/logout');
      console.log('Logout successful response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Logout failed:', error);
      throw error;
    }
  }
};