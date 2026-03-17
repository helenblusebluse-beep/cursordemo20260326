import React, { useState } from 'react';
import { Plus, ChevronLeft, ChevronRight, Edit2, Download, Trash2 } from 'lucide-react';
import { ConversationEditDialog } from './ConversationEditDialog';

interface Conversation {
  id: string;
  title: string;
  time: string;
}

export function LeftSidebar() {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [hoveredId, setHoveredId] = useState<string | null>(null);
  const [editingConversation, setEditingConversation] = useState<Conversation | null>(null);
  const [conversations, setConversations] = useState<Conversation[]>([
    { id: '1', title: '如何使用AI优化工作流程', time: '2小时前' },
    { id: '2', title: '产品设计最佳实践', time: '昨天' },
    { id: '3', title: 'React性能优化技巧', time: '2天前' },
    { id: '4', title: 'UI/UX设计趋势2026', time: '3天前' },
    { id: '5', title: '前端开发工具推荐', time: '1周前' },
  ]);

  const handleDelete = (id: string) => {
    if (confirm('确定要删除这个对话吗？')) {
      setConversations(conversations.filter(conv => conv.id !== id));
    }
  };

  const handleEdit = (conversation: Conversation) => {
    setEditingConversation(conversation);
  };

  const handleSaveEdit = (newTitle: string) => {
    if (editingConversation) {
      setConversations(
        conversations.map((conv) =>
          conv.id === editingConversation.id ? { ...conv, title: newTitle } : conv
        )
      );
    }
  };

  const handleNewConversation = () => {
    const newConversation: Conversation = {
      id: String(Date.now()),
      title: '新对话',
      time: '刚刚',
    };
    setConversations([newConversation, ...conversations]);
  };

  if (isCollapsed) {
    return (
      <div className="w-12 bg-white border-r border-gray-200 flex flex-col items-center pt-6">
        <button
          onClick={() => setIsCollapsed(false)}
          className="w-8 h-8 flex items-center justify-center hover:bg-gray-100 rounded-[10px]"
        >
          <ChevronRight className="w-5 h-5 text-gray-600" />
        </button>
      </div>
    );
  }

  return (
    <div className="w-[300px] bg-white border-r border-gray-200 flex flex-col">
      {/* 顶部新建对话按钮 */}
      <div className="p-4">
        <div className="relative">
          <button
            className="w-full h-12 bg-[#FFF5F0] border-2 border-dashed border-[#FF6B6B] rounded-[10px] flex items-center justify-center gap-2 hover:bg-[#FFE8E0] transition-colors"
            onClick={handleNewConversation}
          >
            <Plus className="w-5 h-5 text-[#FF6B6B]" />
            <span className="text-[#FF6B6B] font-medium">新建对话</span>
          </button>
          <button
            onClick={() => setIsCollapsed(true)}
            className="absolute -right-3 top-1/2 -translate-y-1/2 w-6 h-6 bg-white border border-gray-200 rounded-full flex items-center justify-center hover:bg-gray-50"
          >
            <ChevronLeft className="w-4 h-4 text-gray-600" />
          </button>
        </div>
      </div>

      {/* 对话列表 */}
      <div className="flex-1 overflow-y-auto px-2">
        {conversations.map((conv) => (
          <div
            key={conv.id}
            className="mb-2 px-3 py-3 rounded-[10px] hover:bg-gray-50 cursor-pointer group"
            onMouseEnter={() => setHoveredId(conv.id)}
            onMouseLeave={() => setHoveredId(null)}
          >
            <div className="flex items-start gap-2">
              <div className="w-5 h-5 bg-[#007AFF] rounded flex-shrink-0 mt-0.5"></div>
              <div className="flex-1 min-w-0">
                <div className="text-[#1C1C1E] text-sm line-clamp-2 mb-1">
                  {conv.title}
                </div>
                <div className="flex items-center justify-between">
                  {hoveredId === conv.id ? (
                    <div className="flex items-center gap-3">
                      <button
                        className="hover:text-[#007AFF] transition-colors"
                        onClick={() => handleEdit(conv)}
                      >
                        <Edit2 className="w-4 h-4 text-gray-500" />
                      </button>
                      <button className="hover:text-[#007AFF] transition-colors">
                        <Download className="w-4 h-4 text-gray-500" />
                      </button>
                      <button
                        onClick={() => handleDelete(conv.id)}
                        className="hover:text-red-500 transition-colors"
                      >
                        <Trash2 className="w-4 h-4 text-gray-500" />
                      </button>
                    </div>
                  ) : (
                    <span className="text-xs text-gray-400">{conv.time}</span>
                  )}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* 编辑对话框 */}
      {editingConversation && (
        <ConversationEditDialog
          onClose={() => setEditingConversation(null)}
          onSave={handleSaveEdit}
          currentTitle={editingConversation.title}
        />
      )}
    </div>
  );
}