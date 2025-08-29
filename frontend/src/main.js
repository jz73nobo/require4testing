import { createApp } from "vue";
import { createPinia } from "pinia";
import PrimeVue from "primevue/config";

// 导入PrimeVue的样式文件
import "primevue/resources/themes/lara-light-blue/theme.css"; // 主题样式
import "primeicons/primeicons.css"; // 图标样式
import './style.css'

// 导入根组件和路由
import App from "./App.vue";
import { router } from "./router";

// 创建Vue应用并配置所有插件
createApp(App)
  .use(createPinia())    // 使用Pinia状态管理
  .use(PrimeVue)         // 使用PrimeVue UI组件库
  .use(router)           // 使用Vue Router路由
  .mount("#app");        // 挂载到DOM