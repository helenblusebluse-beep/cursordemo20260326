import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';

interface ConversationEditDialogProps {
  onClose: () => void;
  onSave: (newTitle: string) => void;
  currentTitle: string;
}

export function ConversationEditDialog({
  onClose,
  onSave,
  currentTitle,
}: ConversationEditDialogProps) {
  const [title, setTitle] = useState(currentTitle);

  useEffect(() => {
    setTitle(currentTitle);
  }, [currentTitle]);

  const handleSave = () => {
    if (title.trim()) {
      onSave(title.trim());
      onClose();
    } else {
      alert('对话标题不能为空');
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSave();
    } else if (e.key === 'Escape') {
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30 backdrop-blur-sm">
      <div className="w-[500px] bg-white rounded-[10px] shadow-2xl">
        {/* 头部 */}
        <div className="h-[60px] flex items-center justify-between px-6 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-[#1C1C1E]">重命名对话</h3>
          <button
            onClick={onClose}
            className="w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors"
          >
            <X className="w-5 h-5 text-gray-600" />
          </button>
        </div>

        {/* 内容 */}
        <div className="p-6">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            对话标题
          </label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="请输入对话标题"
            autoFocus
            className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF] transition-colors"
          />
          <p className="mt-2 text-xs text-gray-500">
            按 Enter 保存，按 Esc 取消
          </p>
        </div>

        {/* 底部按钮 */}
        <div className="h-[60px] flex items-center justify-end gap-3 px-6 border-t border-gray-200">
          <button
            onClick={onClose}
            className="px-5 py-2 bg-gray-100 text-gray-700 rounded-[10px] hover:bg-gray-200 transition-colors"
          >
            取消
          </button>
          <button
            onClick={handleSave}
            className="px-5 py-2 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors"
          >
            保存
          </button>
        </div>
      </div>
    </div>
  );
}
