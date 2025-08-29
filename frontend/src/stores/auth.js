import { defineStore } from "pinia";
import api from "../api";

// 定义认证状态管理器
export const useAuthStore = defineStore("auth", {
  // state: 存储数据的地方（类似于data）
  state: () => ({
    // 从localStorage读取已保存的token，如果没有则为null
    token: localStorage.getItem("token") || null,
    // 从localStorage读取已保存的用户角色，如果没有则为null
    role: localStorage.getItem("role") || null,
  }),
  
  // getters: 计算属性（可选，这里我们先不用）
  getters: {
    // 检查用户是否已登录
    isAuthenticated: (state) => !!state.token,
    // 检查是否是管理员
    isAdmin: (state) => state.role === "ADMIN",
  },
  
  // actions: 方法，用于修改state的数据
  actions: {
    // 登录方法
    async login(username, password) {
      try {
        // 调用后端登录API
        const res = await api.post("/auth/login", { username, password });
        
        // 保存返回的token和role到store
        this.token = res.data.token;
        this.role = res.data.role;
        
        // 同时保存到localStorage，刷新页面后不会丢失
        localStorage.setItem("token", this.token);
        localStorage.setItem("role", this.role);
        
        return res.data; // 返回登录结果
      } catch (error) {
        // 登录失败时的处理
        console.error("Login Failed:", error);
        throw error; // 重新抛出错误，让组件能够捕获
      }
    },
    
    // 登出方法
    logout() {
      // 清空store中的数据
      this.token = null;
      this.role = null;
      
      // 清空localStorage中的所有数据
      localStorage.clear();
    },
  },
});