import { createRouter, createWebHistory } from "vue-router";
// 导入页面组件
import Login from "./pages/Login.vue";
import Requirements from "./pages/Requirements.vue";
// import TestRuns from "./pages/TestRuns.vue";
// import Assignments from "./pages/Assignments.vue";

// 定义路由规则：URL路径 对应 哪个组件
const routes = [
  { 
    path: "/login", 
    component: Login,
    name: "Login" 
  },
  { 
    path: "/requirements", 
    component: Requirements,
    name: "Requirements" 
  },
//   { 
//     path: "/testruns", 
//     component: TestRuns,
//     name: "TestRuns" 
//   },
//   { 
//     path: "/assignments", 
//     component: Assignments,
//     name: "Assignments" 
//   },
  {
    // 默认路由：访问根路径时重定向到登录页
    path: "/",
    redirect: "/login"
  }
];

// 创建路由器实例
export const router = createRouter({
  history: createWebHistory(), // 使用HTML5的History模式（URL没有#号）
  routes, // 路由规则
});