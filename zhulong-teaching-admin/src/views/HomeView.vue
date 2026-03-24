<script setup lang="ts">
const modules = [
  { name: '考务管理', desc: '各级在线考试管理' },
  { name: '教务管理', desc: '师资与教务任务管理' },
  { name: '教学数据', desc: '多维度教学数据展示' },
  { name: '就业管理', desc: '就业服务相关管理' },
  { name: '反馈管理', desc: '多渠道反馈管理' },
  { name: '就业数据稽查', desc: '学员就业数据稽查' },
]
</script>

<template>
  <div class="home">
    <header class="home__intro">
      <h1 class="home__title">教学后台</h1>
      <p class="home__subtitle">教学业务管理服务平台</p>
    </header>

    <div class="home__stage">
      <div class="home__illustration" aria-hidden="true">
        <div class="home__illustration-inner">
          <div class="home__cube home__cube--a" />
          <div class="home__cube home__cube--b" />
          <div class="home__screen" />
          <div class="home__figure home__figure--1" />
          <div class="home__figure home__figure--2" />
        </div>
      </div>

      <ul class="home__modules">
        <li v-for="m in modules" :key="m.name" class="home__module">
          <div class="home__module-icon" />
          <div class="home__module-text">
            <div class="home__module-name">{{ m.name }}</div>
            <div class="home__module-desc">{{ m.desc }}</div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.home {
  min-height: 100%;
  padding: 40px 48px 56px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.home__intro {
  text-align: center;
  margin-bottom: 32px;
}

.home__title {
  margin: 0;
  font-size: 36px;
  font-weight: 700;
  color: #303133;
  letter-spacing: 0.04em;
}

.home__subtitle {
  margin: 12px 0 0;
  font-size: 16px;
  color: #606266;
}

.home__stage {
  width: min(1100px, 100%);
  display: grid;
  grid-template-columns: 1fr;
  gap: 32px;
  align-items: start;
}

@media (min-width: 960px) {
  .home__stage {
    grid-template-columns: 1.1fr 0.9fr;
    align-items: center;
  }
}

.home__illustration {
  position: relative;
  min-height: 280px;
  border-radius: 16px;
  background: linear-gradient(145deg, #e8f1ff 0%, #f5f9ff 45%, #ffffff 100%);
  border: 1px solid #e4e7ed;
  overflow: hidden;
}

.home__illustration-inner {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 20% 30%, rgba(64, 158, 255, 0.18), transparent 55%),
    radial-gradient(circle at 80% 60%, rgba(103, 194, 58, 0.12), transparent 50%);
}

.home__cube {
  position: absolute;
  border-radius: 10px;
  background: linear-gradient(135deg, #409eff, #79bbff);
  opacity: 0.9;
  box-shadow: 0 12px 30px rgba(64, 158, 255, 0.25);
}

.home__cube--a {
  width: 72px;
  height: 72px;
  left: 18%;
  bottom: 22%;
  transform: rotate(12deg);
}

.home__cube--b {
  width: 48px;
  height: 48px;
  right: 22%;
  top: 28%;
  transform: rotate(-8deg);
  background: linear-gradient(135deg, #67c23a, #95d475);
  box-shadow: 0 10px 24px rgba(103, 194, 58, 0.22);
}

.home__screen {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 46%;
  height: 42%;
  transform: translate(-50%, -52%) skewY(-6deg);
  border-radius: 12px;
  background: linear-gradient(180deg, #ffffff 0%, #ecf5ff 100%);
  border: 2px solid #d9ecff;
  box-shadow: 0 18px 40px rgba(0, 0, 0, 0.08);
}

.home__screen::after {
  content: '';
  position: absolute;
  inset: 12% 10% 18%;
  border-radius: 8px;
  background: repeating-linear-gradient(
    0deg,
    #e4e7ed,
    #e4e7ed 1px,
    transparent 1px,
    transparent 10px
  );
  opacity: 0.55;
}

.home__figure {
  position: absolute;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #409eff;
  opacity: 0.85;
}

.home__figure--1 {
  left: 30%;
  top: 38%;
}

.home__figure--2 {
  right: 30%;
  bottom: 30%;
  background: #909399;
}

.home__modules {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}

.home__module {
  display: flex;
  gap: 12px;
  align-items: flex-start;
  padding: 14px 14px;
  border-radius: 12px;
  border: 1px dashed #dcdfe6;
  background: #fafafa;
}

.home__module-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  flex-shrink: 0;
  background: radial-gradient(circle at 30% 30%, #ffffff, #409eff);
  border: 1px solid #b3d8ff;
}

.home__module-name {
  font-weight: 600;
  color: #303133;
  font-size: 15px;
}

.home__module-desc {
  margin-top: 4px;
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
}
</style>
