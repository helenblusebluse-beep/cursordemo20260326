import React, { useState } from 'react';
import { Eye, EyeOff, ArrowRight, MessageSquare } from 'lucide-react';

interface LoginPageProps {
  onLogin: (username: string, password: string, rememberMe: boolean) => void;
}

export function LoginPage({ onLogin }: LoginPageProps) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (username && password) {
      onLogin(username, password, rememberMe);
    }
  };

  return (
    <div className="min-h-screen bg-[#F5F7FB] flex items-center justify-center p-6">
      <div className="w-full max-w-md">
        {/* Logo 和标题 */}
        <div className="flex flex-col items-center mb-12">
          <div className="w-24 h-24 bg-[#007AFF] rounded-[20px] flex items-center justify-center mb-6 shadow-lg">
            <MessageSquare className="w-12 h-12 text-white" strokeWidth={2.5} />
          </div>
          <h1 className="text-4xl font-bold text-[#1C1C1E] mb-3">知识库 AI</h1>
          <p className="text-gray-500 text-lg">欢迎回来，请登录您的账户</p>
        </div>

        {/* 登录表单 */}
        <div className="bg-white rounded-[10px] shadow-sm border border-gray-200 p-8">
          <form onSubmit={handleSubmit}>
            {/* 用户名 */}
            <div className="mb-6">
              <label className="block text-base font-medium text-[#1C1C1E] mb-3">
                用户名
              </label>
              <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="请输入用户名"
                className="w-full h-14 px-4 bg-[#F5F7FB] border-0 rounded-[10px] focus:outline-none focus:ring-2 focus:ring-[#007AFF] text-[#1C1C1E] placeholder:text-gray-400"
              />
            </div>

            {/* 密码 */}
            <div className="mb-6">
              <label className="block text-base font-medium text-[#1C1C1E] mb-3">
                密码
              </label>
              <div className="relative">
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="请输入密码"
                  className="w-full h-14 px-4 pr-12 bg-[#F5F7FB] border-0 rounded-[10px] focus:outline-none focus:ring-2 focus:ring-[#007AFF] text-[#1C1C1E] placeholder:text-gray-400"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                >
                  {showPassword ? (
                    <EyeOff className="w-5 h-5" />
                  ) : (
                    <Eye className="w-5 h-5" />
                  )}
                </button>
              </div>
            </div>

            {/* 记住我和忘记密码 */}
            <div className="flex items-center justify-between mb-8">
              <label className="flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                  className="w-5 h-5 rounded border-gray-300 text-[#007AFF] focus:ring-[#007AFF]"
                />
                <span className="ml-2 text-gray-600">记住我</span>
              </label>
              <button
                type="button"
                className="text-[#007AFF] hover:text-[#0066DD] font-medium"
              >
                忘记密码?
              </button>
            </div>

            {/* 登录按钮 */}
            <button
              type="submit"
              disabled={!username || !password}
              className="w-full h-14 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 text-lg font-medium"
            >
              <ArrowRight className="w-5 h-5" />
              登录
            </button>
          </form>
        </div>

        {/* 提示信息 */}
        <div className="mt-6 text-center text-sm text-gray-500">
          <p>测试账号：admin / admin123 (管理员)</p>
          <p>测试账号：user / user123 (普通用户)</p>
        </div>
      </div>
    </div>
  );
}
