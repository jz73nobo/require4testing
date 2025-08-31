import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";

// 创建Vue应用
const app = createApp(App);
app.use(createPinia());
app.mount("#app");