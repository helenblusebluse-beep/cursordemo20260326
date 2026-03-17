import React, { useState } from 'react';
import { ArrowLeft, Plus, Trash2, X } from 'lucide-react';

interface PromptEditorProps {
  onClose: () => void;
  onSave: (title: string, content: string, group: string) => void;
  existingGroups: string[];
  onAddGroup: (groupName: string) => void;
  onDeleteGroup: (groupName: string) => void;
}

export function PromptEditor({ onClose, onSave, existingGroups, onAddGroup, onDeleteGroup }: PromptEditorProps) {
  const [promptTitle, setPromptTitle] = useState('');
  const [promptContent, setPromptContent] = useState('');
  const [selectedGroup, setSelectedGroup] = useState(existingGroups[0] || '工作');
  const [showAddGroupDialog, setShowAddGroupDialog] = useState(false);
  const [newGroupName, setNewGroupName] = useState('');

  const handleSave = () => {
    if (!promptTitle.trim() || !promptContent.trim()) {
      return;
    }
    onSave(promptTitle, promptContent, selectedGroup);
    onClose();
  };

  const handleAddGroup = () => {
    if (newGroupName.trim() && !existingGroups.includes(newGroupName.trim())) {
      onAddGroup(newGroupName.trim());
      setSelectedGroup(newGroupName.trim());
      setNewGroupName('');
      setShowAddGroupDialog(false);
    }
  };

  const filteredGroups = existingGroups.filter(g => g !== '全部');

  return (
    <div className="fixed inset-0 bg-[#F5F7FB] z-[70] flex flex-col">
      {/* 顶部导航栏 */}
      <div className="h-[70px] bg-white border-b border-gray-200 flex items-center justify-between px-6">
        <div className="flex items-center gap-3">
          <button
            onClick={onClose}
            className="w-10 h-10 hover:bg-gray-100 rounded-[10px] flex items-center justify-center transition-colors"
          >
            <ArrowLeft className="w-5 h-5 text-gray-600" />
          </button>
          <h1 className="text-xl font-semibold text-[#1C1C1E]">新建提示词</h1>
        </div>
        <div className="flex items-center gap-3">
          <button
            onClick={onClose}
            className="px-5 py-2 bg-gray-100 text-gray-700 rounded-[10px] hover:bg-gray-200 transition-colors"
          >
            取消
          </button>
          <button
            onClick={handleSave}
            disabled={!promptTitle.trim() || !promptContent.trim()}
            className="px-5 py-2 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            保存
          </button>
        </div>
      </div>

      {/* 主内容区 */}
      <div className="flex-1 overflow-y-auto">
        <div className="max-w-4xl mx-auto p-8">
          {/* 提示词信息卡片 */}
          <div className="bg-white rounded-[10px] shadow-sm border border-gray-200 p-6 mb-6">
            <h2 className="text-lg font-semibold text-[#1C1C1E] mb-6">提示词信息</h2>
            
            <div className="space-y-5">
              <div>
                <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                  标题 <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={promptTitle}
                  onChange={(e) => setPromptTitle(e.target.value)}
                  placeholder="请输入提示词标题"
                  className="w-full h-12 px-4 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF] text-[#1C1C1E]"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                  内容 <span className="text-red-500">*</span>
                </label>
                <textarea
                  value={promptContent}
                  onChange={(e) => setPromptContent(e.target.value)}
                  placeholder="请输入提示词内容，可以使用变量占位符如 {variable}"
                  rows={10}
                  className="w-full px-4 py-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF] text-[#1C1C1E] resize-none"
                />
                <p className="text-xs text-gray-500 mt-2">
                  提示：使用 {'{变量名}'} 作为占位符，使用时可动态替换
                </p>
              </div>
            </div>
          </div>

          {/* 分组管理卡片 */}
          <div className="bg-white rounded-[10px] shadow-sm border border-gray-200 p-6">
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-lg font-semibold text-[#1C1C1E]">选择分组</h2>
              <button
                onClick={() => setShowAddGroupDialog(true)}
                className="flex items-center gap-1 px-4 py-2 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors"
              >
                <Plus className="w-4 h-4" />
                新建分组
              </button>
            </div>

            <div className="grid grid-cols-4 gap-3">
              {filteredGroups.map((group) => (
                <div
                  key={group}
                  className={`relative group/item ${
                    selectedGroup === group ? 'ring-2 ring-[#007AFF]' : ''
                  } rounded-[10px] overflow-hidden`}
                >
                  <button
                    onClick={() => setSelectedGroup(group)}
                    className={`w-full px-4 py-3 rounded-[10px] text-sm transition-colors ${
                      selectedGroup === group
                        ? 'bg-[#007AFF] text-white'
                        : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                    }`}
                  >
                    {group}
                  </button>
                  {!['工作', '学习', '生活'].includes(group) && (
                    <button
                      onClick={() => {
                        onDeleteGroup(group);
                        if (selectedGroup === group) {
                          setSelectedGroup(filteredGroups[0] || '工作');
                        }
                      }}
                      className="absolute top-1 right-1 w-6 h-6 bg-red-500 text-white rounded-full items-center justify-center opacity-0 group-hover/item:opacity-100 transition-opacity hidden group-hover/item:flex"
                    >
                      <X className="w-4 h-4" />
                    </button>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* 新建分组弹窗 */}
      {showAddGroupDialog && (
        <div className="fixed inset-0 bg-black/30 backdrop-blur-sm z-[80] flex items-center justify-center p-6">
          <div className="w-[400px] bg-white rounded-[10px] shadow-2xl">
            <div className="h-[60px] flex items-center justify-between px-6 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-[#1C1C1E]">新建分组</h3>
              <button
                onClick={() => setShowAddGroupDialog(false)}
                className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center hover:bg-gray-300 transition-colors"
              >
                <X className="w-5 h-5 text-gray-600" />
              </button>
            </div>

            <div className="p-6">
              <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                分组名称
              </label>
              <input
                type="text"
                value={newGroupName}
                onChange={(e) => setNewGroupName(e.target.value)}
                placeholder="请输入分组名称"
                className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF] text-[#1C1C1E]"
                onKeyDown={(e) => {
                  if (e.key === 'Enter') {
                    handleAddGroup();
                  }
                }}
              />
            </div>

            <div className="px-6 py-4 border-t border-gray-200 flex items-center justify-end gap-3">
              <button
                onClick={() => setShowAddGroupDialog(false)}
                className="px-5 py-2 bg-gray-100 text-gray-700 rounded-[10px] hover:bg-gray-200 transition-colors"
              >
                取消
              </button>
              <button
                onClick={handleAddGroup}
                disabled={!newGroupName.trim() || existingGroups.includes(newGroupName.trim())}
                className="px-5 py-2 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                创建
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}