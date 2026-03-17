import React, { useState } from 'react';
import { Copy, Volume2, Star, Download } from 'lucide-react';

interface Message {
  id: string;
  type: 'user' | 'ai';
  content: string;
  time: string;
}

export function ChatArea() {
  const [messages] = useState<Message[]>([
    {
      id: '1',
      type: 'user',
      content: '请帮我介绍一下如何优化React应用的性能？',
      time: '10:30',
    },
    {
      id: '2',
      type: 'ai',
      content: '当然可以！React应用性能优化是一个重要的话题。这里有几个核心建议：\n\n1. 使用React.memo()和useMemo()来避免不必要的重渲染\n2. 实现虚拟滚动处理大量数据列表\n3. 代码分割和懒加载减少初始包大小\n4. 使用useCallback优化事件处理函数\n5. 避免在渲染函数中创建新对象或数组\n\n这些技术可以显著提升应用的响应速度和用户体验。',
      time: '10:31',
    },
    {
      id: '3',
      type: 'user',
      content: '能详细说说虚拟滚动的实现方式吗？',
      time: '10:32',
    },
    {
      id: '4',
      type: 'ai',
      content: '虚拟滚动(Virtual Scrolling)是一种只渲染可见区域内容的技术。核心原理是：\n\n• 只在DOM中渲染当前视口可见的元素\n• 使用占位元素撑起整体高度\n• 监听滚动事件动态计算应该渲染哪些项\n\n推荐使用react-window或react-virtualized这两个成熟的库，它们提供了现成的虚拟滚动组件。对于包含上千条数据的列表，性能提升非常明显！',
      time: '10:33',
    },
  ]);

  const handleCopy = (content: string) => {
    navigator.clipboard.writeText(content);
  };

  return (
    <div className="flex-1 overflow-y-auto px-6 py-6">
      <div className="max-w-4xl mx-auto space-y-6">
        {messages.map((message) => (
          <div key={message.id} className="flex gap-3">
            {/* 头像 */}
            <img
              src={
                message.type === 'user'
                  ? 'https://api.dicebear.com/7.x/avataaars/svg?seed=User'
                  : 'https://api.dicebear.com/7.x/bottts/svg?seed=AI'
              }
              alt="avatar"
              className="w-10 h-10 rounded-full flex-shrink-0"
            />

            {/* 消息内容 */}
            <div className="flex-1">
              <div
                className={`inline-block max-w-full px-4 py-3 rounded-[10px] ${
                  message.type === 'user'
                    ? 'bg-[#007AFF] text-white'
                    : 'bg-white border border-[#E9E9EB]'
                }`}
              >
                <div
                  className={`whitespace-pre-wrap ${
                    message.type === 'user' ? 'text-white' : 'text-[#1C1C1E]'
                  }`}
                >
                  {message.content}
                </div>
              </div>

              {/* AI消息的功能按钮 */}
              {message.type === 'ai' && (
                <div className="flex items-center gap-3 mt-2">
                  <button
                    onClick={() => handleCopy(message.content)}
                    className="flex items-center gap-1 text-gray-500 hover:text-[#007AFF] transition-colors"
                  >
                    <Copy className="w-4 h-4" />
                    <span className="text-xs">复制</span>
                  </button>
                  <button className="flex items-center gap-1 text-gray-500 hover:text-[#007AFF] transition-colors">
                    <Volume2 className="w-4 h-4" />
                    <span className="text-xs">朗读</span>
                  </button>
                  <button className="flex items-center gap-1 text-gray-500 hover:text-[#007AFF] transition-colors">
                    <Star className="w-4 h-4" />
                    <span className="text-xs">收藏</span>
                  </button>
                  <button className="flex items-center gap-1 text-gray-500 hover:text-[#007AFF] transition-colors">
                    <Download className="w-4 h-4" />
                    <span className="text-xs">下载</span>
                  </button>
                </div>
              )}

              <div className="text-xs text-gray-400 mt-1">{message.time}</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
