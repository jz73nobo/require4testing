<template>
  <div class="p-4">
    <h2>Requirements</h2>
    <div v-if="error" class="error">{{ error }}</div>
    <ul v-else>
      <li v-for="r in requirements" :key="r.id">{{ r.title }} - {{ r.description }}</li>
    </ul>

    <h3>Add Requirement</h3>
    <input v-model="title" placeholder="Title" />
    <input v-model="description" placeholder="Description" />
    <button @click="addRequirement">Add</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "../api";
import { useRouter } from "vue-router";

const router = useRouter();
const requirements = ref([]);
const title = ref("");
const description = ref("");
const error = ref("");

async function load() {
  try {
    const res = await api.get("/requirements");
    requirements.value = res.data;
    error.value = "";
  } catch (err) {
    console.error("Failed to load requirements:", err);
    if (err.response && err.response.status === 401) {
      error.value = "Unauthorized, please login first";
      router.push("/login");
    } else {
      error.value = "Failed to load, please try again";
    }
  }
}

async function addRequirement() {
  try {
    await api.post("/requirements", {
      title: title.value,
      description: description.value,
      createdBy: 1,
    });
    title.value = "";
    description.value = "";
    load();
  } catch (err) {
    console.error("Failed to add requirement:", err);
    if (err.response && err.response.status === 401) {
      error.value = "Unauthorized, please login first";
      router.push("/login");
    } else {
      error.value = "Failed to add, please try again";
    }
  }
}

onMounted(load);
</script>

<style scoped>
.error {
  color: red;
  margin-bottom: 1rem;
}
</style>