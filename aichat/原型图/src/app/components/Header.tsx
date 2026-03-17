import React, { useState } from 'react';
import { ChevronDown } from 'lucide-react';

interface HeaderProps {
  username?: string;
  onLogout?: () => void;
  onOpenProfile?: () => void;
  onOpenSettings?: () => void;
}

export function Header({ username = '用户名', onLogout, onOpenProfile, onOpenSettings }: HeaderProps) {
  const [showDropdown, setShowDropdown] = useState(false);

  const handleLogout = () => {
    if (onLogout) {
      onLogout();
    }
  };

  const handleOpenProfile = () => {
    setShowDropdown(false);
    if (onOpenProfile) {
      onOpenProfile();
    }
  };

  const handleOpenSettings = () => {
    setShowDropdown(false);
    if (onOpenSettings) {
      onOpenSettings();
    }
  };

  return (
    <header className="h-[70px] bg-white border-b border-gray-200 flex items-center justify-between px-6">
      {/* Logo + 项目名 */}
      <div className="flex items-center gap-3">
        <div className="w-10 h-10 bg-[#007AFF] rounded-[10px] flex items-center justify-center">
          <span className="text-white text-xl font-bold">AI</span>
        </div>
        <span className="text-[#1C1C1E] text-lg font-semibold">知识库AI</span>
      </div>

      {/* 用户信息 */}
      <div className="relative">
        <button
          onClick={() => setShowDropdown(!showDropdown)}
          className="flex items-center gap-2 hover:bg-gray-50 px-3 py-2 rounded-[10px] transition-colors"
        >
          <img
            src="https://api.dicebear.com/7.x/avataaars/svg?seed=User"
            alt="用户头像"
            className="w-9 h-9 rounded-full"
          />
          <span className="text-[#1C1C1E]">{username}</span>
          <ChevronDown className={`w-4 h-4 text-gray-500 transition-transform ${showDropdown ? 'rotate-180' : ''}`} />
        </button>

        {/* 下拉菜单 */}
        {showDropdown && (
          <div className="absolute right-0 top-full mt-2 w-48 bg-white rounded-[10px] shadow-lg border border-gray-200 py-2 z-50">
            <button className="w-full px-4 py-2 text-left hover:bg-gray-50 text-[#1C1C1E]" onClick={handleOpenProfile}>
              个人中心
            </button>
            <button className="w-full px-4 py-2 text-left hover:bg-gray-50 text-[#1C1C1E]" onClick={handleOpenSettings}>
              设置
            </button>
            <div className="border-t border-gray-200 my-2"></div>
            <button className="w-full px-4 py-2 text-left hover:bg-gray-50 text-red-500" onClick={handleLogout}>
              退出登录
            </button>
          </div>
        )}
      </div>
    </header>
  );
}