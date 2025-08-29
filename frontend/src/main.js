import { createApp } from "vue";
import { createPinia } from "pinia";
import PrimeVue from "primevue/config";

// ✅ 正确引入 PrimeVue v4 主题 preset
import Aura from "@primevue/themes/aura"; 
import "primeicons/primeicons.css"; // 图标样式

// 导入根组件和路由
import App from "./App.vue";
import { router } from "./router";

// 创建Vue应用并配置所有插件
const app = createApp(App);

app.use(createPinia());
app.use(PrimeVue, {
  theme: {
    preset: Aura,  // 使用 Aura 主题
    options: {
      prefix: "p", // 默认类名前缀
      darkModeSelector: "system" // 可选: 跟随系统深色模式
    }
  }
});
app.use(router);

app.mount("#app");
