import axios from "axios";

// 创建 axios 实例，统一配置基础设置
const api = axios.create({
  baseURL: "http://localhost:8080/api", // 后端API的基础地址
  withCredentials: true // 允许发送和接收 cookie
});

// 移除 JWT 相关的请求拦截器
// 因为后端使用 Session 认证，不需要手动设置 Authorization 头

export default api;