import React, { useState } from 'react';
import { Header } from './components/Header';
import { LeftSidebar } from './components/LeftSidebar';
import { ChatArea } from './components/ChatArea';
import { RightPanel } from './components/RightPanel';
import { InputArea } from './components/InputArea';
import { LoginPage } from './components/LoginPage';
import { UserManagement } from './components/UserManagement';
import { ProfileCenter } from './components/ProfileCenter';
import { Settings } from './components/Settings';

// 用户数据类型
interface UserData {
  username: string;
  role: 'admin' | 'user';
}

// 消息类型
interface Message {
  id: string;
  content: string;
  timestamp: string;
  type: 'ai' | 'user';
}

export default function App() {
  const [currentUser, setCurrentUser] = useState<UserData | null>(null);
  const [showProfileCenter, setShowProfileCenter] = useState(false);
  const [showSettings, setShowSettings] = useState(false);

  // 收藏消息数据状态
  const [favoriteMessages, setFavoriteMessages] = useState<Message[]>([
    {
      id: '1',
      content: '人工智能（AI）是计算机科学的一个分支，致力于创建能够执行通常需要人类智能的任务的系统。这些任务包括视觉感知、语音识别、决策制定和语言翻译等。',
      timestamp: '2026-02-15 10:30',
      type: 'ai',
    },
    {
      id: '2',
      content: '可以帮我总结一下机器学习的主要分类吗？',
      timestamp: '2026-02-14 15:20',
      type: 'user',
    },
    {
      id: '3',
      content: '机器学习主要分为三大类：\n1. 监督学习：使用标记数据进行训练\n2. 无监督学习：从未标记数据中发现模式\n3. 强化学习：通过与环境交互学习最优策略',
      timestamp: '2026-02-14 15:21',
      type: 'ai',
    },
  ]);

  // 模拟用户数据库
  const users = [
    { username: 'admin', password: 'admin123', role: 'admin' as const },
    { username: 'user', password: 'user123', role: 'user' as const },
    { username: 'zhangsan', password: '123456', role: 'user' as const },
  ];

  // 登录处理
  const handleLogin = (username: string, password: string, rememberMe: boolean) => {
    const user = users.find(
      (u) => u.username === username && u.password === password
    );

    if (user) {
      setCurrentUser({ username: user.username, role: user.role });
      if (rememberMe) {
        localStorage.setItem('currentUser', JSON.stringify({ username: user.username, role: user.role }));
      }
    } else {
      alert('用户名或密码错误');
    }
  };

  // 退出登录
  const handleLogout = () => {
    setCurrentUser(null);
    localStorage.removeItem('currentUser');
  };

  // 移除收藏的消息
  const handleRemoveFavoriteMessage = (id: string) => {
    setFavoriteMessages(favoriteMessages.filter((msg) => msg.id !== id));
  };

  // 如果未登录，显示登录页面
  if (!currentUser) {
    return <LoginPage onLogin={handleLogin} />;
  }

  // 如果是管理员，显示用户管理页面
  if (currentUser.role === 'admin') {
    return <UserManagement onLogout={handleLogout} currentUser={currentUser.username} />;
  }

  // 普通用户显示聊天界面
  return (
    <div className="h-screen flex flex-col bg-[#F5F7FB]">
      {/* 顶部导航栏 */}
      <Header
        username={currentUser.username}
        onLogout={handleLogout}
        onOpenProfile={() => setShowProfileCenter(true)}
        onOpenSettings={() => setShowSettings(true)}
      />

      {/* 主体区域 */}
      <div className="flex-1 flex overflow-hidden">
        {/* 左侧边栏 */}
        <LeftSidebar />

        {/* 中间主聊天区域 */}
        <div className="flex-1 flex flex-col">
          <ChatArea />
          <InputArea />
        </div>
      </div>

      {/* 右侧功能区 */}
      <RightPanel />

      {/* 个人中心弹窗 */}
      {showProfileCenter && (
        <ProfileCenter
          onClose={() => setShowProfileCenter(false)}
          username={currentUser.username}
          email={`${currentUser.username}@example.com`}
          joinDate="2025-01-01"
          favoriteMessages={favoriteMessages}
          onRemoveFavoriteMessage={handleRemoveFavoriteMessage}
        />
      )}

      {/* 设置弹窗 */}
      {showSettings && (
        <Settings
          onClose={() => setShowSettings(false)}
          username={currentUser.username}
        />
      )}
    </div>
  );
}
