import React, { useState } from 'react';
import { X, User, Mail, Calendar, MessageSquare, Star, Copy, Download, Trash2 } from 'lucide-react';

interface Message {
  id: string;
  content: string;
  timestamp: string;
  type: 'ai' | 'user';
}

interface ProfileCenterProps {
  onClose: () => void;
  username: string;
  email?: string;
  joinDate?: string;
  favoriteMessages?: Message[];
  onRemoveFavoriteMessage?: (id: string) => void;
}

export function ProfileCenter({
  onClose,
  username,
  email = 'user@example.com',
  joinDate = '2025-01-01',
  favoriteMessages = [],
  onRemoveFavoriteMessage,
}: ProfileCenterProps) {
  const [activeTab, setActiveTab] = useState<'messages'>('messages');

  const getAvatarColor = (username: string) => {
    const colors = [
      'bg-blue-500',
      'bg-green-500',
      'bg-purple-500',
      'bg-orange-500',
      'bg-pink-500',
    ];
    const index = username.charCodeAt(0) % colors.length;
    return colors[index];
  };

  const handleCopyMessage = (content: string) => {
    navigator.clipboard.writeText(content);
    alert('已复制到剪贴板');
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30 backdrop-blur-sm">
      <div className="w-[800px] max-h-[90vh] bg-white rounded-[10px] shadow-2xl flex flex-col">
        {/* 头部 */}
        <div className="h-[70px] flex items-center justify-between px-6 border-b border-gray-200">
          <h2 className="text-xl font-semibold text-[#1C1C1E]">个人中心</h2>
          <button
            onClick={onClose}
            className="w-9 h-9 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors"
          >
            <X className="w-5 h-5 text-gray-600" />
          </button>
        </div>

        {/* 用户信息卡片 */}
        <div className="p-6 bg-gradient-to-r from-blue-50 to-purple-50 border-b border-gray-200">
          <div className="flex items-center gap-4">
            <div
              className={`w-20 h-20 rounded-full flex items-center justify-center text-white text-3xl font-bold ${getAvatarColor(
                username
              )}`}
            >
              {username.charAt(0).toUpperCase()}
            </div>
            <div className="flex-1">
              <h3 className="text-2xl font-bold text-[#1C1C1E] mb-2">{username}</h3>
              <div className="flex items-center gap-4 text-sm text-gray-600">
                <div className="flex items-center gap-1">
                  <Mail className="w-4 h-4" />
                  <span>{email}</span>
                </div>
                <div className="flex items-center gap-1">
                  <Calendar className="w-4 h-4" />
                  <span>加入于 {joinDate}</span>
                </div>
              </div>
            </div>
          </div>

          {/* 统计信息 */}
          <div className="grid grid-cols-2 gap-4 mt-4">
            <div className="bg-white rounded-[10px] p-4">
              <div className="text-2xl font-bold text-[#007AFF] mb-1">
                {favoriteMessages.length}
              </div>
              <div className="text-sm text-gray-600">收藏消息</div>
            </div>
            <div className="bg-white rounded-[10px] p-4">
              <div className="text-2xl font-bold text-purple-500 mb-1">
                {favoriteMessages.filter((m) => m.type === 'ai').length}
              </div>
              <div className="text-sm text-gray-600">AI 回复</div>
            </div>
          </div>
        </div>

        {/* 选项卡 */}
        <div className="flex items-center gap-1 px-6 pt-4 border-b border-gray-200">
          <button
            onClick={() => setActiveTab('messages')}
            className={`px-4 py-3 rounded-t-[10px] flex items-center gap-2 transition-colors ${
              activeTab === 'messages'
                ? 'bg-[#007AFF] text-white'
                : 'text-gray-600 hover:bg-gray-100'
            }`}
          >
            <MessageSquare className="w-4 h-4" />
            收藏的消息
            <span
              className={`px-2 py-0.5 rounded-full text-xs ${
                activeTab === 'messages'
                  ? 'bg-white/20 text-white'
                  : 'bg-gray-200 text-gray-600'
              }`}
            >
              {favoriteMessages.length}
            </span>
          </button>
        </div>

        {/* 内容区域 */}
        <div className="flex-1 overflow-y-auto p-6">
          {activeTab === 'messages' && (
            <div className="space-y-3">
              {favoriteMessages.length === 0 ? (
                <div className="text-center py-12 text-gray-400">
                  <Star className="w-12 h-12 mx-auto mb-3 opacity-30" />
                  <p>暂无收藏的消息</p>
                  <p className="text-sm mt-2">在聊天中点击收藏按钮添加您喜欢的内容</p>
                </div>
              ) : (
                favoriteMessages.map((message) => (
                  <div
                    key={message.id}
                    className="bg-[#F5F7FB] rounded-[10px] p-4 hover:bg-gray-100 transition-colors"
                  >
                    <div className="flex items-start justify-between gap-3">
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-2">
                          <span
                            className={`px-2 py-0.5 rounded-full text-xs ${
                              message.type === 'ai'
                                ? 'bg-[#007AFF] text-white'
                                : 'bg-gray-300 text-gray-700'
                            }`}
                          >
                            {message.type === 'ai' ? 'AI' : '我'}
                          </span>
                          <span className="text-xs text-gray-500">
                            {message.timestamp}
                          </span>
                        </div>
                        <p className="text-[#1C1C1E] leading-relaxed">
                          {message.content}
                        </p>
                      </div>
                      <div className="flex items-center gap-1">
                        <button
                          onClick={() => handleCopyMessage(message.content)}
                          className="w-8 h-8 rounded-[10px] flex items-center justify-center text-gray-400 hover:text-[#007AFF] hover:bg-blue-50 transition-colors"
                          title="复制"
                        >
                          <Copy className="w-4 h-4" />
                        </button>
                        {onRemoveFavoriteMessage && (
                          <button
                            onClick={() => onRemoveFavoriteMessage(message.id)}
                            className="w-8 h-8 rounded-[10px] flex items-center justify-center text-gray-400 hover:text-red-500 hover:bg-red-50 transition-colors"
                            title="移除收藏"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                ))
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}