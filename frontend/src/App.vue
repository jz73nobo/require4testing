<template>
  <div id="app">
    <!-- 未登录状态：显示登录表单 -->
    <div v-if="!isAuthenticated" class="login-container">
      <div class="login-form">
        <h2>测试管理系统</h2>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="username">用户名:</label>
            <input type="text" id="username" v-model="loginData.username" required>
          </div>
          <div class="form-group">
            <label for="password">密码:</label>
            <input type="password" id="password" v-model="loginData.password" required>
          </div>
          <button type="submit" :disabled="loading">登录</button>
        </form>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </div>
    </div>

    <!-- 已登录状态：显示功能界面 -->
    <div v-else class="app-container">
      <!-- 顶部导航栏 -->
      <nav class="app-nav">
        <div class="nav-brand">Require4Testing</div>
        <div class="nav-items">
          <span class="user-info">欢迎, {{ user.username }} ({{ user.role }})</span>
          <button @click="handleLogout" class="logout-btn">退出</button>
        </div>
      </nav>

      <!-- 功能选项卡 -->
      <div class="tabs">
        <button 
          @click="activeTab = 'requirements'" 
          :class="{ active: activeTab === 'requirements' }"
        >
          需求管理
        </button>
        <button 
          @click="activeTab = 'testCases'" 
          :class="{ active: activeTab === 'testCases' }"
          v-if="user.role === 'TEST_DESIGNER'"
        >
          测试用例
        </button>
        <button 
          @click="activeTab = 'testRuns'" 
          :class="{ active: activeTab === 'testRuns' }"
          v-if="user.role === 'TEST_MANAGER'"
        >
          测试运行
        </button>
      </div>

      <!-- 功能内容区域 -->
      <main class="main-content">
        <!-- 需求管理 -->
        <div v-if="activeTab === 'requirements'" class="tab-content">
          <h3>需求管理</h3>
          <div class="form-section">
            <h4>添加新需求</h4>
            <input v-model="newRequirement.title" placeholder="需求标题" class="input-field">
            <textarea v-model="newRequirement.description" placeholder="需求描述" class="textarea-field"></textarea>
            <button @click="addRequirement" class="submit-btn">创建需求</button>
          </div>
          
          <div class="list-section">
            <h4>需求列表</h4>
            <div v-if="requirements.length === 0" class="empty-state">
              暂无需求
            </div>
            <ul v-else class="requirement-list">
              <li v-for="req in requirements" :key="req.id" class="requirement-item">
                <strong>{{ req.title }}</strong>
                <p>{{ req.description }}</p>
                <small>创建时间: {{ formatDate(req.createdAt) }}</small>
              </li>
            </ul>
          </div>
        </div>

        <!-- 其他功能区域（可以根据需要添加） -->
        <div v-if="activeTab === 'testCases'" class="tab-content">
          <h3>测试用例管理</h3>
          <p>测试用例功能开发中...</p>
        </div>

        <div v-if="activeTab === 'testRuns'" class="tab-content">
          <h3>测试运行管理</h3>
          <p>测试运行功能开发中...</p>
        </div>
      </main>
    </div>

    <!-- 全局加载指示器 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner"></div>
    </div>
  </div>
</template>

<script>
import authService from './services/auth';
import api from './api';

export default {
  name: 'App',
  data() {
    return {
      isAuthenticated: false,
      user: null,
      loading: false,
      errorMessage: '',
      activeTab: 'requirements',
      
      // 登录数据
      loginData: {
        username: '',
        password: ''
      },
      
      // 需求管理数据
      newRequirement: {
        title: '',
        description: ''
      },
      requirements: []
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
            role: authStatus.authorities?.[0]?.replace('ROLE_', '') || 'USER'
          };
          // 加载需求列表
          await this.loadRequirements();
        }
      } catch (error) {
        console.error('检查认证状态失败:', error);
        this.errorMessage = '认证检查失败';
      } finally {
        this.loading = false;
      }
    },

    async handleLogin() {
      this.loading = true;
      this.errorMessage = '';
      try {
        const response = await authService.login(this.loginData.username, this.loginData.password);
        console.log('登录成功:', response);
    
        // 等待一下让session设置完成
        await new Promise(resolve => setTimeout(resolve, 100));
    
        // 重新检查认证状态
        await this.checkAuthStatus();
    
        // 如果还是没跳转，强制刷新页面
        if (this.isAuthenticated) {
        console.log('认证成功，但界面未更新，强制刷新页面...');
        window.location.reload();
        }
    
      } catch (error) {
      console.error('登录失败:', error);
      this.errorMessage = error.response?.data?.message || '登录失败，请检查用户名和密码';
      } finally {
      this.loading = false;
      } 
    },

    async handleLogout() {
      this.loading = true;
      try {
        await authService.logout();
        this.isAuthenticated = false;
        this.user = null;
        this.loginData = { username: '', password: '' };
        this.requirements = [];
      } catch (error) {
        console.error('登出失败:', error);
        this.errorMessage = '登出失败';
      } finally {
        this.loading = false;
      }
    },

    async loadRequirements() {
      try {
        const response = await api.get('/requirements');
        this.requirements = response.data;
      } catch (error) {
        console.error('加载需求失败:', error);
        this.errorMessage = '加载需求失败';
      }
    },

    async addRequirement() {
      if (!this.newRequirement.title.trim()) {
        this.errorMessage = '请输入需求标题';
        return;
      }

      try {
        await api.post('/requirements', {
          title: this.newRequirement.title,
          description: this.newRequirement.description,
          createdBy: 1 // 这里需要从用户信息中获取实际ID
        });
        
        this.newRequirement = { title: '', description: '' };
        await this.loadRequirements();
        this.errorMessage = '';
      } catch (error) {
        console.error('添加需求失败:', error);
        this.errorMessage = '添加需求失败';
      }
    },

    formatDate(dateString) {
      return new Date(dateString).toLocaleString();
    }
  }
}
</script>

<style>
/* 全局样式 */
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #f5f5f5;
}

#app {
  min-height: 100vh;
}

/* 登录页面样式 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #185b9fff, #2c75d2);
}

.login-form {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.login-form h2 {
  text-align: center;
  color: #185b9fff;
  margin-bottom: 2rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.login-form button {
  width: 100%;
  padding: 0.75rem;
  background-color: #185b9fff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
}

.login-form button:hover {
  background-color: #13478a;
}

.error {
  color: #f44336;
  text-align: center;
  margin-top: 1rem;
}

/* 主应用样式 */
.app-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.app-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
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
  gap: 1rem;
}

.user-info {
  color: #333;
}

.logout-btn {
  padding: 0.5rem 1rem;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #d32f2f;
}

/* 选项卡样式 */
.tabs {
  display: flex;
  background: white;
  padding: 0 2rem;
  border-bottom: 1px solid #ddd;
}

.tabs button {
  padding: 1rem 2rem;
  background: none;
  border: none;
  border-bottom: 3px solid transparent;
  cursor: pointer;
  color: #666;
}

.tabs button.active {
  color: #185b9fff;
  border-bottom-color: #185b9fff;
}

.tabs button:hover {
  color: #185b9fff;
}

/* 主内容区域 */
.main-content {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.tab-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 表单样式 */
.form-section {
  margin-bottom: 2rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid #eee;
}

.input-field, .textarea-field {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 1rem;
  font-size: 1rem;
}

.textarea-field {
  min-height: 100px;
  resize: vertical;
}

.submit-btn {
  padding: 0.75rem 1.5rem;
  background-color: #185b9fff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #13478a;
}

/* 列表样式 */
.requirement-list {
  list-style: none;
  padding: 0;
}

.requirement-item {
  background: #f9f9f9;
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 4px;
  border-left: 4px solid #185b9fff;
}

.requirement-item strong {
  color: #185b9fff;
}

.requirement-item p {
  margin: 0.5rem 0;
  color: #666;
}

.requirement-item small {
  color: #999;
}

.empty-state {
  text-align: center;
  color: #999;
  padding: 2rem;
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