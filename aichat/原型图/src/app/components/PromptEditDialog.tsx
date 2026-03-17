import React, { useState, useEffect } from 'react';
import { X, Plus } from 'lucide-react';

interface Prompt {
  id: string;
  title: string;
  content: string;
  group: string;
}

interface PromptEditDialogProps {
  onClose: () => void;
  onSave: (id: string | null, title: string, content: string, group: string) => void;
  prompt?: Prompt | null;
  existingGroups: string[];
  onAddGroup?: (groupName: string) => void;
}

export function PromptEditDialog({
  onClose,
  onSave,
  prompt = null,
  existingGroups,
  onAddGroup,
}: PromptEditDialogProps) {
  const [title, setTitle] = useState(prompt?.title || '');
  const [content, setContent] = useState(prompt?.content || '');
  const [selectedGroup, setSelectedGroup] = useState(prompt?.group || existingGroups[0] || '工作');
  const [showNewGroupInput, setShowNewGroupInput] = useState(false);
  const [newGroupName, setNewGroupName] = useState('');

  useEffect(() => {
    if (prompt) {
      setTitle(prompt.title);
      setContent(prompt.content);
      setSelectedGroup(prompt.group);
    }
  }, [prompt]);

  const handleSave = () => {
    if (!title.trim()) {
      alert('请输入提示词标题');
      return;
    }
    if (!content.trim()) {
      alert('请输入提示词内容');
      return;
    }

    onSave(prompt?.id || null, title.trim(), content.trim(), selectedGroup);
    onClose();
  };

  const handleAddNewGroup = () => {
    if (newGroupName.trim()) {
      if (existingGroups.includes(newGroupName.trim())) {
        alert('该分组已存在');
        return;
      }
      if (onAddGroup) {
        onAddGroup(newGroupName.trim());
        setSelectedGroup(newGroupName.trim());
      }
      setNewGroupName('');
      setShowNewGroupInput(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30 backdrop-blur-sm">
      <div className="w-[600px] max-h-[90vh] bg-white rounded-[10px] shadow-2xl flex flex-col">
        {/* 头部 */}
        <div className="h-[60px] flex items-center justify-between px-6 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-[#1C1C1E]">
            {prompt ? '编辑提示词' : '新建提示词'}
          </h3>
          <button
            onClick={onClose}
            className="w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors"
          >
            <X className="w-5 h-5 text-gray-600" />
          </button>
        </div>

        {/* 内容 */}
        <div className="flex-1 overflow-y-auto p-6 space-y-4">
          {/* 标题 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              标题 <span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="请输入提示词标题"
              className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF] transition-colors"
            />
          </div>

          {/* 分组 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              分组 <span className="text-red-500">*</span>
            </label>
            <div className="flex items-center gap-2">
              <select
                value={selectedGroup}
                onChange={(e) => setSelectedGroup(e.target.value)}
                className="flex-1 h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF] bg-white"
              >
                {existingGroups.map((group) => (
                  <option key={group} value={group}>
                    {group}
                  </option>
                ))}
              </select>
              <button
                onClick={() => setShowNewGroupInput(!showNewGroupInput)}
                className="h-10 px-3 bg-gray-100 text-gray-700 rounded-[10px] hover:bg-gray-200 transition-colors flex items-center gap-1"
              >
                <Plus className="w-4 h-4" />
                新建
              </button>
            </div>

            {/* 新建分组输入框 */}
            {showNewGroupInput && (
              <div className="mt-2 flex items-center gap-2">
                <input
                  type="text"
                  value={newGroupName}
                  onChange={(e) => setNewGroupName(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') {
                      handleAddNewGroup();
                    }
                  }}
                  placeholder="输入新分组名称"
                  className="flex-1 h-9 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                />
                <button
                  onClick={handleAddNewGroup}
                  className="h-9 px-4 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors text-sm"
                >
                  添加
                </button>
                <button
                  onClick={() => {
                    setShowNewGroupInput(false);
                    setNewGroupName('');
                  }}
                  className="h-9 px-4 bg-gray-100 text-gray-700 rounded-[10px] hover:bg-gray-200 transition-colors text-sm"
                >
                  取消
                </button>
              </div>
            )}
          </div>

          {/* 内容 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              提示词内容 <span className="text-red-500">*</span>
            </label>
            <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="请输入提示词内容，例如：请帮我分析以下代码的性能问题..."
              rows={10}
              className="w-full px-3 py-2 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF] resize-none transition-colors"
            />
            <div className="mt-1 flex items-center justify-between text-xs text-gray-500">
              <span>支持换行和多段文本</span>
              <span>{content.length} 字符</span>
            </div>
          </div>

          {/* 使用提示 */}
          <div className="bg-blue-50 border border-blue-200 rounded-[10px] p-4">
            <h4 className="text-sm font-medium text-blue-900 mb-2">💡 使用提示</h4>
            <ul className="text-xs text-blue-700 space-y-1">
              <li>• 标题应简洁明了，便于快速识别</li>
              <li>• 内容可以包含具体的指令和上下文说明</li>
              <li>• 使用合适的分组来组织管理提示词</li>
              <li>• 可以在聊天时快速调用保存的提示词</li>
            </ul>
          </div>
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
            {prompt ? '保存修改' : '创建提示词'}
          </button>
        </div>
      </div>
    </div>
  );
}
