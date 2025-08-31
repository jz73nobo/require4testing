<template>
  <div id="app">
    <!-- 顶部导航栏（如果用户已登录） -->
    <nav v-if="isAuthenticated" class="app-nav">
      <div class="nav-brand">测试管理系统</div>
      <div class="nav-items">
        <span class="user-info">欢迎, {{ user.username }}</span>
        <button @click="logout" class="logout-btn">退出</button>
      </div>
    </nav>
    
    <!-- 主要内容区域 -->
    <main :class="{'with-nav': isAuthenticated}">
      <router-view></router-view>
    </main>
    
    <!-- 全局加载指示器 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner"></div>
    </div>
  </div>
</template>

<script>
import authService from './services/auth';

export default {
  name: 'App',
  data() {
    return {
      isAuthenticated: false,
      user: null,
      loading: false
    };
  },
  async created() {
    // 应用启动时检查认证状态
    await this.checkAuthStatus();
  },
  methods: {
    async checkAuthStatus() {
      this.loading = true;
      try {
        const authStatus = await authService.checkAuthStatus();
        this.isAuthenticated = authStatus.authenticated;
        if (this.isAuthenticated) {
          this.user = {
            username: authStatus.username,
            authorities: authStatus.authorities
          };
          
          // 如果当前在登录页面且已认证，重定向到主页
          if (this.$route.path === '/login') {
            this.$router.push('/dashboard');
          }
        } else {
          // 如果未认证且当前不在登录页面，重定向到登录页
          if (this.$route.path !== '/login') {
            this.$router.push('/login');
          }
        }
      } catch (error) {
        console.error('检查认证状态失败:', error);
        // 发生错误时也重定向到登录页
        if (this.$route.path !== '/login') {
          this.$router.push('/login');
        }
      } finally {
        this.loading = false;
      }
    },
    
    async logout() {
      this.loading = true;
      try {
        await authService.logout();
        this.isAuthenticated = false;
        this.user = null;
        this.$router.push('/login');
      } catch (error) {
        console.error('登出失败:', error);
        alert('登出失败，请重试');
      } finally {
        this.loading = false;
      }
    }
  },
  watch: {
    // 监听路由变化，确保认证状态正确
    '$route'(to) {
      if (!this.isAuthenticated && to.path !== '/login') {
        this.$router.push('/login');
      }
    }
  }
}
</script>

<style>
/* 全局样式 */
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #185b9fff;
}

#app {
  min-height: 100vh;
}

/* 导航栏样式 */
.app-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.nav-brand {
  font-size: 1.5rem;
  font-weight: bold;
  color: #185b9fff;
}

.nav-items {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  color: #333;
}

.logout-btn {
  padding: 8px 16px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #d32f2f;
}

/* 主要内容区域 */
main.with-nav {
  padding-top: 60px; /* 为导航栏留出空间 */
  min-height: calc(100vh - 60px);
}

/* 加载指示器 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #185b9fff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>