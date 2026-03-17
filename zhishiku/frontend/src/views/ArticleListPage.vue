<template>
  <div class="article-list">
    <div class="toolbar">
      <input
        v-model="keyword"
        class="search-input"
        placeholder="搜索文档标题"
        type="text"
      />
      <button class="btn" @click="loadArticles">搜索</button>
    </div>
    <div class="list">
      <article
        v-for="article in filteredArticles"
        :key="article.id"
        class="item"
        @click="goDetail(article.id)"
      >
        <h2 class="item-title">{{ article.title }}</h2>
        <p class="item-summary">
          {{ article.summary || article.content.slice(0, 80) + '...' }}
        </p>
      </article>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { fetchArticles, type Article } from '../api/articles';

const router = useRouter();
const articles = ref<Article[]>([]);
const keyword = ref('');

async function loadArticles() {
  // 示例：默认空间 id 为 1
  try {
    articles.value = await fetchArticles(1);
  } catch (e) {
    // 简化处理：真实项目中应有错误提示
    console.error(e);
  }
}

const filteredArticles = computed(() => {
  if (!keyword.value.trim()) {
    return articles.value;
  }
  return articles.value.filter(a =>
    a.title.toLowerCase().includes(keyword.value.toLowerCase())
  );
});

function goDetail(id: number) {
  router.push({ name: 'articleDetail', params: { id } });
}

onMounted(loadArticles);
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}
.search-input {
  flex: 1;
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #d1d5db;
  font-size: 14px;
}
.btn {
  padding: 6px 12px;
  border-radius: 6px;
  border: none;
  background-color: #2563eb;
  color: #ffffff;
  font-size: 14px;
  cursor: pointer;
}
.list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.item {
  padding: 12px 14px;
  background-color: #ffffff;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  cursor: pointer;
}
.item-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 4px;
}
.item-summary {
  font-size: 13px;
  color: #6b7280;
}
</style>

