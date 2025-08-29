<template>
  <div class="p-4">
    <h2>Requirements</h2>
    <ul>
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

const requirements = ref([]);
const title = ref("");
const description = ref("");

async function load() {
  const res = await api.get("/requirements");
  requirements.value = res.data;
}

async function addRequirement() {
  await api.post("/requirements", {
    title: title.value,
    description: description.value,
    createdBy: 1,
  });
  title.value = "";
  description.value = "";
  load();
}

onMounted(load);
</script>
