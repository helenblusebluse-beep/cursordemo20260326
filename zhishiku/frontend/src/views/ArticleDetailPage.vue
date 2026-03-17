<template>
  <div class="article-detail" v-if="article">
    <h1 class="title">{{ article.title }}</h1>
    <p class="summary" v-if="article.summary">{{ article.summary }}</p>
    <div class="content" v-html="article.content"></div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { fetchArticle, type Article } from '../api/articles';

const route = useRoute();
const article = ref<Article | null>(null);

async function load() {
  const id = Number(route.params.id);
  if (!id) return;
  try {
    article.value = await fetchArticle(id);
  } catch (e) {
    console.error(e);
  }
}

onMounted(load);
</script>

<style scoped>
.title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 8px;
}
.summary {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 16px;
}
.content {
  padding: 14px 0;
  line-height: 1.7;
  font-size: 14px;
  color: #111827;
}
</style>

