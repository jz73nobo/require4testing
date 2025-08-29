<template>
  <div class="p-4">
    <h2>Login</h2>
    <input v-model="username" placeholder="Username" class="p-inputtext p-mb-2" />
    <input v-model="password" type="password" placeholder="Password" class="p-inputtext p-mb-2" />
    <button @click="doLogin" class="p-button">Login</button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "../stores/auth";
import { useRouter } from "vue-router";

const username = ref("");
const password = ref("");
const auth = useAuthStore();
const router = useRouter();

async function doLogin() {
  try {
    await auth.login(username.value, password.value);
    router.push("/requirements");
  } catch (e) {
    alert("Login failed");
  }
}
</script>
