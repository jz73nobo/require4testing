import { createRouter, createWebHistory } from "vue-router";
import Login from "./pages/Login.vue";
import Requirements from "./pages/Requirements.vue";
import authService from "./services/auth";

// 定义路由规则
const routes = [
  { 
    path: "/login", 
    component: Login,
    name: "Login" 
  },
  { 
    path: "/requirements", 
    component: Requirements,
    name: "Requirements",
    meta: { requiresAuth: true } // 添加需要认证的标记
  },
  {
    path: "/",
    redirect: "/login"
  }
];

// 创建路由器实例
const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 添加路由守卫
router.beforeEach(async (to, from, next) => {
  // 检查目标路由是否需要认证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    try {
      // 检查认证状态
      const authStatus = await authService.checkAuthStatus();
      
      if (authStatus.authenticated) {
        // 已认证，允许访问
        next();
      } else {
        // 未认证，重定向到登录页
        next({ name: "Login" });
      }
    } catch (error) {
      console.error("认证检查失败:", error);
      next({ name: "Login" });
    }
  } else {
    // 不需要认证的路由，直接允许访问
    next();
  }
});

export { router };