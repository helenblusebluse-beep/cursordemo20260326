<template>
  <div class="chat-layout">
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>会话</h2>
        <button class="btn" @click="newSession">新建</button>
      </div>
      <ul class="session-list">
        <li
          v-for="s in sessions"
          :key="s.id"
          :class="['session-item', { active: s.id === currentSessionId }]"
          @click="selectSession(s.id)"
        >
          {{ s.title || '新会话' }}
        </li>
      </ul>
    </aside>

    <main class="chat-main">
      <div class="messages">
        <div
          v-for="m in messages"
          :key="m.id"
          :class="['msg', m.sender === 'USER' ? 'msg-user' : 'msg-ai']"
        >
          <div class="msg-content">{{ m.content }}</div>
        </div>
      </div>

      <form class="input-bar" @submit.prevent="send">
        <textarea
          v-model="input"
          class="input"
          placeholder="请输入问题，回车发送"
        ></textarea>
        <button class="btn primary" type="submit">发送</button>
      </form>
    </main>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';

interface Session {
  id: number;
  title: string;
}

interface Message {
  id: number;
  sender: 'USER' | 'AI';
  content: string;
}

const sessions = ref<Session[]>([
  { id: 1, title: '示例会话' }
]);
const currentSessionId = ref<number>(1);
const messages = ref<Message[]>([
  { id: 1, sender: 'AI', content: '你好，我是知识库 AI，可以帮你解答问题。' }
]);
const input = ref('');

function newSession() {
  const id = Date.now();
  sessions.value.push({ id, title: '新会话' });
  currentSessionId.value = id;
  messages.value = [];
}

function selectSession(id: number) {
  currentSessionId.value = id;
  // Phase 1 仅做前端演示，不加载真实消息
}

function send() {
  if (!input.value.trim()) return;
  const userMsg: Message = {
    id: Date.now(),
    sender: 'USER',
    content: input.value
  };
  messages.value.push(userMsg);
  // Phase 1: 简单 echo，之后再接后端聊天接口
  const aiMsg: Message = {
    id: Date.now() + 1,
    sender: 'AI',
    content: '（示例回复）' + input.value
  };
  messages.value.push(aiMsg);
  input.value = '';
}

onMounted(() => {
  // 后续在这里加载会话与消息
});
</script>

<style scoped>
.chat-layout {
  display: grid;
  grid-template-columns: 220px minmax(0, 1fr);
  height: 100vh;
}
.sidebar {
  border-right: 1px solid #e5e7eb;
  padding: 12px;
  background: #f9fafb;
}
.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}
.session-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.session-item {
  padding: 6px 8px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 13px;
}
.session-item.active {
  background: #2563eb;
  color: #ffffff;
}
.chat-main {
  display: flex;
  flex-direction: column;
}
.messages {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}
.msg {
  margin-bottom: 8px;
  display: flex;
}
.msg-user {
  justify-content: flex-end;
}
.msg-ai {
  justify-content: flex-start;
}
.msg-content {
  max-width: 70%;
  padding: 8px 10px;
  border-radius: 12px;
  font-size: 14px;
}
.msg-user .msg-content {
  background: #2563eb;
  color: #ffffff;
}
.msg-ai .msg-content {
  background: #f3f4f6;
  color: #111827;
}
.input-bar {
  display: flex;
  gap: 8px;
  padding: 8px 12px;
  border-top: 1px solid #e5e7eb;
}
.input {
  flex: 1;
  resize: none;
  border-radius: 8px;
  border: 1px solid #d1d5db;
  padding: 8px 10px;
  font-size: 14px;
}
.btn {
  padding: 8px 12px;
  border-radius: 999px;
  border: none;
  background: #e5e7eb;
  cursor: pointer;
  font-size: 13px;
}
.btn.primary {
  background: #2563eb;
  color: #ffffff;
}
</style>

