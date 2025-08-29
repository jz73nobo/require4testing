import axios from "axios";

// 创建 axios 实例，统一配置基础设置
const api = axios.create({
  baseURL: "http://localhost:8080/api", // 后端API的基础地址
});

// 请求拦截器：在每个请求发送前自动添加JWT token
api.interceptors.request.use((config) => {
  // 从浏览器本地存储获取token
  const token = localStorage.getItem("token");
  
  // 如果token存在，就添加到请求头
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  
  return config; // 返回修改后的请求配置
});

// 导出配置好的api实例，供其他文件使用
export default api;