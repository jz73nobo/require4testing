<template>
  <div id="app">
    <!-- Unauthenticated state: show login form -->
    <div v-if="!isAuthenticated" class="login-container">
      <div class="login-form">
        <h2>Test Management System</h2>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username" v-model="loginData.username" required>
          </div>
          <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" v-model="loginData.password" required>
          </div>
          <button type="submit" :disabled="loading">Login</button>
        </form>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </div>
    </div>

    <!-- Authenticated state: show functional interface -->
    <div v-else class="app-container">
      <!-- Top navigation bar -->
      <nav class="app-nav">
        <div class="nav-brand">Require4Testing</div>
        <div class="nav-items">
          <span class="user-info">Welcome, {{ user.username }} ({{ user.role }})</span>
          <button @click="handleLogout" class="logout-btn">Logout</button>
        </div>
      </nav>

      <!-- Function tabs -->
      <div class="tabs">
        <button 
          @click="activeTab = 'requirements'" 
          :class="{ active: activeTab === 'requirements' }"
        >
          Requirements Management
        </button>
        <button 
          @click="activeTab = 'testCases'" 
          :class="{ active: activeTab === 'testCases' }"
          v-if="user.role === 'TEST_DESIGNER'"
        >
          Test Cases
        </button>
        <button 
          @click="activeTab = 'testRuns'" 
          :class="{ active: activeTab === 'testRuns' }"
          v-if="user.role === 'TEST_MANAGER'"
        >
          Test Runs
        </button>
      </div>

      <!-- Functional content area -->
      <main class="main-content">

        <!-- Test Runs Page -->
        <div v-if="activeTab === 'testRuns'" class="tab-content">
          <h3>Test Run Management</h3>
          <div class="form-section">
            <h4>Create New Test Run</h4>
            <input v-model="newTestRun.name" placeholder="Test Run Name" class="input-field">
            <button @click="addTestRun" class="submit-btn">Create Test Run</button>
          </div>

          <div class="list-section">
            <h4>Test Runs List</h4>
            <div v-if="testRuns.length === 0" class="empty-state">No test runs yet</div>
            <ul v-else class="requirement-list">
              <li v-for="tr in testRuns" :key="tr.id" class="requirement-item">
                <strong>{{ tr.name }}</strong>
                <p>Created: {{ formatDate(tr.createdAt) }}</p>
              </li>
            </ul>
          </div>
        </div>


        <!-- Test Cases Page -->
        <div v-if="activeTab === 'testCases'" class="tab-content">
          <h3>Test Case Management</h3>
          <div class="form-section">
            <h4>Add New Test Case</h4>
            <input v-model="newTestCase.title" placeholder="Test Case Title" class="input-field">
            <textarea v-model="newTestCase.description" placeholder="Test Case Description" class="textarea-field"></textarea>
            <select v-model="newTestCase.requirementId" class="input-field">
              <option disabled value="">Select Requirement</option>
              <option v-for="req in requirements" :value="req.id" :key="req.id">
                {{ req.title }}
              </option>
            </select>
            <button @click="addTestCase" class="submit-btn">Create Test Case</button>
          </div>

          <div class="list-section">
            <h4>Test Cases List</h4>
            <div v-if="testCases.length === 0" class="empty-state">No test cases yet</div>
            <ul v-else class="requirement-list">
              <li v-for="tc in testCases" :key="tc.id" class="requirement-item">
                <strong>{{ tc.title }}</strong>
                <p>{{ tc.description }}</p>
                <small>Requirement: {{ tc.requirement?.title }}, Created: {{ formatDate(tc.createdAt) }}</small>
              </li>
            </ul>
          </div>
        </div>


        <!-- Assignments Page -->
        <div v-if="activeTab === 'assignments'" class="tab-content">
          <h3>My Assignments</h3>
          <div v-if="assignments.length === 0" class="empty-state">No assignments</div>
          <ul v-else class="requirement-list">
            <li v-for="as in assignments" :key="as.id" class="requirement-item">
              <strong>{{ as.testCase.title }}</strong>
              <p>Requirement: {{ as.testCase.requirement.title }}</p>
              <p>Result: 
                <select v-model="as.result" @change="updateAssignment(as)">
                  <option>NOT_RUN</option>
                  <option>PASS</option>
                  <option>FAIL</option>
                </select>
              </p>
              <input v-model="as.comment" @blur="updateAssignment(as)" placeholder="Comment" class="input-field">
            </li>
          </ul>
        </div>


        <!-- Requirements Management -->
        <div v-if="activeTab === 'requirements'" class="tab-content">
          <h3>Requirements Management</h3>
          <div class="form-section">
            <h4>Add New Requirement</h4>
            <input v-model="newRequirement.title" placeholder="Requirement Title" class="input-field">
            <textarea v-model="newRequirement.description" placeholder="Requirement Description" class="textarea-field"></textarea>
            <button @click="addRequirement" class="submit-btn">Create Requirement</button>
          </div>
          
          <div class="list-section">
            <h4>Requirements List</h4>
            <div v-if="requirements.length === 0" class="empty-state">
              No requirements yet
            </div>
            <ul v-else class="requirement-list">
              <li v-for="req in requirements" :key="req.id" class="requirement-item">
                <strong>{{ req.title }}</strong>
                <p>{{ req.description }}</p>
                <small>Created at: {{ formatDate(req.createdAt) }}</small>
              </li>
            </ul>
          </div>
        </div>
      </main>
    </div>

    <!-- Global loading indicator -->
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
      
      // Login data
      loginData: {
        username: '',
        password: ''
      },
      
      // Requirements management data
      newRequirement: {
        title: '',
        description: ''
      },
      requirements: [],

      // Test Cases for Test Designer
      newTestCase: {
        title: '',
        description: '',
        requirementId: ''
      },
      testCases: [],

      // Test Runs for Test Manager
      newTestRun: { title: ''},
      testRuns: [],

      // Tester
      assignments: []
    };
  },
  async created() {
    // Check authentication status when app starts
    await this.checkAuthStatus();
  },
  methods: {
    async loadAssignments(){
      const res = await api.get('/assignments/my');
      this.assignments = res.data;
    },
    async updateAssignment(as){
      await api.patch(`/assignments/${as.id}/result`, {
        result: as.result,
        comment: as.comment
      });
    },

    async loadTestRuns(){
      const res = await api.get('/testruns');
      this.testRuns = res.data;
    },
    async addTestRun(){
      await api.post('/testruns',{
        title: this.newTestRun.title
      }),
      this.newTestRun = { name: ''};
      await this.loadTestRuns();
    },
    async loadTestCases() {
      const res = await api.get('/testcases');
      this.testCases = res.data;
    },
    async addTestCase() {
      await api.post('/testcases', {
        title: this.newTestCase.title,
        description: this.newTestCase.description,
        requirementId: this.newTestCase.requirementId // 直接发送requirementId而不是嵌套对象
      });
      this.newTestCase = { title: '', description: '', requirementId: '' };
      await this.loadTestCases();
    },

    async checkAuthStatus() {
      try {
        const authStatus = await authService.checkAuthStatus();
        this.isAuthenticated = authStatus.authenticated;
        if (this.isAuthenticated) {
          this.user = {
            username: authStatus.username,
            role: authStatus.authorities?.[0]?.replace('ROLE_', '') || 'USER'
          };
          
          // 根据角色设置默认标签页
          switch (this.user.role) {
            case 'REQ_ENGINEER':
              this.activeTab = 'requirements';
              await this.loadRequirements();  // 只有需求工程师才加载需求
              break;
            case 'TEST_MANAGER':
              this.activeTab = 'testRuns';
              await this.loadTestRuns();      // 只有测试经理才加载测试运行
              break;
            case 'TEST_DESIGNER':
              this.activeTab = 'testCases';
              await this.loadTestCases();     // 只有测试设计师才加载测试用例
              await this.loadRequirements();  // 测试设计师也需要看到需求
              break;
            case 'TESTER':
              this.activeTab = 'assignments';
              await this.loadAssignments();   // 只有测试人员才加载任务
              break;
            default:
              this.activeTab = 'requirements';
          }
        }
      } catch (error) {
        console.error('Failed to check authentication status:', error);
      }
    },

    async handleLogin() {
      this.loading = true;
      this.errorMessage = '';
      try {
        const response = await authService.login(this.loginData.username, this.loginData.password);
        console.log('Login successful:', response);
    
        // Wait a bit for session setup to complete
        await new Promise(resolve => setTimeout(resolve, 100));
    
        // Recheck authentication status
        await this.checkAuthStatus();
    
        // If still not redirected, force page refresh
        if (this.isAuthenticated) {
        console.log('Authentication successful, but interface not updated, forcing page refresh...');
        window.location.reload();
        }
    
      } catch (error) {
      console.error('Login failed:', error);
      this.errorMessage = error.response?.data?.message || 'Login failed, please check username and password';
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
        console.error('Logout failed:', error);
        this.errorMessage = 'Logout failed';
      } finally {
        this.loading = false;
      }
    },

    async loadRequirements() {
      try {
        const response = await api.get('/requirements');
        this.requirements = response.data;
      } catch (error) {
        console.error('Failed to load requirements:', error);
        this.errorMessage = 'Failed to load requirements';
      }
    },

    async addRequirement() {
      if (!this.newRequirement.title.trim()) {
        this.errorMessage = 'Please enter requirement title';
        return;
      }

      try {
        await api.post('/requirements', {
          title: this.newRequirement.title,
          description: this.newRequirement.description,
          createdBy: 1 // This should get the actual ID from user info
        });
        
        this.newRequirement = { title: '', description: '' };
        await this.loadRequirements();
        this.errorMessage = '';
      } catch (error) {
        console.error('Failed to add requirement:', error);
        this.errorMessage = 'Failed to add requirement';
      }
    },

    formatDate(dateString) {
      return new Date(dateString).toLocaleString();
    }
  }
}
</script>

<style>
/* Global styles */
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #f5f5f5;
}

#app {
  min-height: 100vh;
}

/* Login page styles */
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

/* Main application styles */
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

/* Tab styles */
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

/* Main content area */
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

/* Form styles */
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

/* List styles */
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

/* Loading indicator */
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