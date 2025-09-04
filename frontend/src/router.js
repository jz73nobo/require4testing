import { createRouter, createWebHistory } from "vue-router";
import Login from "./pages/Login.vue";
import Requirements from "./pages/Requirements.vue";
import authService from "./services/auth";

// Define route rules
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
    meta: { requiresAuth: true } // Add authentication required flag
  },
  {
    path: "/",
    redirect: "/login"
  }
];

// Create router instance
const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Add route guard
router.beforeEach(async (to, from, next) => {
  // Check if target route requires authentication
  if (to.matched.some(record => record.meta.requiresAuth)) {
    try {
      // Check authentication status
      const authStatus = await authService.checkAuthStatus();
      
      if (authStatus.authenticated) {
        // Authenticated, allow access
        next();
      } else {
        // Not authenticated, redirect to login page
        next({ name: "Login" });
      }
    } catch (error) {
      console.error("Authentication check failed:", error);
      next({ name: "Login" });
    }
  } else {
    // Routes that don't require authentication, allow direct access
    next();
  }
});

export { router };