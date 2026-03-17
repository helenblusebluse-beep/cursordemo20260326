import React, { useState } from 'react';
import { ArrowLeft, Plus, Search, Star, Edit2, Trash2, X } from 'lucide-react';
import { PromptEditor } from './PromptEditor';

interface Prompt {
  id: string;
  title: string;
  content: string;
  group: string;
  createdAt: string;
  isFavorite: boolean;
}

interface PromptManagerProps {
  onClose: () => void;
  prompts: Prompt[];
  groups: string[];
  onSavePrompt: (title: string, content: string, group: string) => void;
  onDeletePrompt: (id: string) => void;
  onToggleFavorite: (id: string) => void;
  onAddGroup: (groupName: string) => void;
  onDeleteGroup: (groupName: string) => void;
}

export function PromptManager({
  onClose,
  prompts: initialPrompts,
  groups: initialGroups,
  onSavePrompt,
  onDeletePrompt,
  onToggleFavorite,
  onAddGroup,
  onDeleteGroup,
}: PromptManagerProps) {
  const [selectedGroup, setSelectedGroup] = useState<string>('全部提示词');
  const [searchText, setSearchText] = useState('');
  const [showPromptEditor, setShowPromptEditor] = useState(false);
  const [showAddGroupDialog, setShowAddGroupDialog] = useState(false);
  const [newGroupName, setNewGroupName] = useState('');
  const [prompts, setPrompts] = useState<Prompt[]>(initialPrompts);
  const [groups, setGroups] = useState<string[]>(initialGroups);

  // 计算每个分组的提示词数量
  const getGroupCount = (group: string) => {
    if (group === '全部提示词') return prompts.length;
    return prompts.filter(p => p.group === group).length;
  };

  // 过滤提示词
  const filteredPrompts = prompts.filter(p => {
    const matchGroup = selectedGroup === '全部提示词' || p.group === selectedGroup;
    const matchSearch = searchText === '' || 
      p.title.toLowerCase().includes(searchText.toLowerCase()) ||
      p.content.toLowerCase().includes(searchText.toLowerCase());
    return matchGroup && matchSearch;
  });

  const handleAddGroup = () => {
    if (newGroupName.trim() && !groups.includes(newGroupName.trim())) {
      const updatedGroups = [...groups, newGroupName.trim()];
      setGroups(updatedGroups);
      onAddGroup(newGroupName.trim());
      setNewGroupName('');
      setShowAddGroupDialog(false);
    }
  };

  const handleToggleFavorite = (id: string) => {
    setPrompts(prompts.map(p => 
      p.id === id ? { ...p, isFavorite: !p.isFavorite } : p
    ));
    onToggleFavorite(id);
  };

  const handleDeletePrompt = (id: string) => {
    setPrompts(prompts.filter(p => p.id !== id));
    onDeletePrompt(id);
  };

  const handleSaveNewPrompt = (title: string, content: string, group: string) => {
    const newPrompt: Prompt = {
      id: String(Date.now()),
      title,
      content,
      group,
      createdAt: new Date().toISOString().split('T')[0],
      isFavorite: false,
    };
    setPrompts([newPrompt, ...prompts]);
    onSavePrompt(title, content, group);
  };

  return (
    <>
      {/* 提示词编辑器 */}
      {showPromptEditor && (
        <PromptEditor
          onClose={() => setShowPromptEditor(false)}
          onSave={handleSaveNewPrompt}
          existingGroups={groups.filter(g => g !== '全部提示词')}
          onAddGroup={onAddGroup}
          onDeleteGroup={onDeleteGroup}
        />
      )}

      <div className="fixed inset-0 bg-[#F5F7FB] z-[60] flex flex-col">
        {/* 顶部导航栏 */}
        <div className="h-[70px] bg-white border-b border-gray-200 flex items-center justify-between px-6">
          <div className="flex items-center gap-3">
            <button
              onClick={onClose}
              className="w-10 h-10 hover:bg-gray-100 rounded-[10px] flex items-center justify-center transition-colors"
            >
              <ArrowLeft className="w-5 h-5 text-gray-600" />
            </button>
            <h1 className="text-xl font-semibold text-[#1C1C1E]">我的提示词</h1>
          </div>
          <button
            onClick={onClose}
            className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center hover:bg-gray-300 transition-colors"
          >
            <X className="w-5 h-5 text-gray-600" />
          </button>
        </div>

        {/* 主内容区 */}
        <div className="flex-1 flex overflow-hidden">
          {/* 左侧分组导航 */}
          <div className="w-[260px] bg-white border-r border-gray-200 flex flex-col p-4">
            <h3 className="text-sm font-semibold text-[#1C1C1E] mb-3 px-2">分组</h3>
            
            {/* 全部提示词 */}
            <button
              onClick={() => setSelectedGroup('全部提示词')}
              className={`w-full flex items-center justify-between px-3 py-2.5 rounded-[10px] mb-1 transition-colors ${
                selectedGroup === '全部提示词'
                  ? 'bg-[#007AFF] text-white'
                  : 'hover:bg-gray-100 text-gray-700'
              }`}
            >
              <div className="flex items-center gap-2">
                <div className={`w-2 h-2 rounded-full ${
                  selectedGroup === '全部提示词' ? 'bg-white' : 'bg-[#007AFF]'
                }`} />
                <span className="text-sm">全部提示词</span>
              </div>
              <span className="text-xs">{getGroupCount('全部提示词')}</span>
            </button>

            {/* 其他分组 */}
            <div className="flex-1 overflow-y-auto space-y-1">
              {groups.filter(g => g !== '全部提示词').map((group) => (
                <button
                  key={group}
                  onClick={() => setSelectedGroup(group)}
                  className={`w-full flex items-center justify-between px-3 py-2.5 rounded-[10px] transition-colors ${
                    selectedGroup === group
                      ? 'bg-[#007AFF] text-white'
                      : 'hover:bg-gray-100 text-gray-700'
                  }`}
                >
                  <div className="flex items-center gap-2">
                    <div className={`w-2 h-2 rounded-full ${
                      selectedGroup === group ? 'bg-white' : 
                      group === '工作' ? 'bg-blue-500' :
                      group === '学习' ? 'bg-orange-500' :
                      group === '生活' ? 'bg-green-500' : 'bg-purple-500'
                    }`} />
                    <span className="text-sm">{group}</span>
                  </div>
                  <span className="text-xs">{getGroupCount(group)}</span>
                </button>
              ))}
            </div>

            {/* 新建分组按钮 */}
            <button
              onClick={() => setShowAddGroupDialog(true)}
              className="w-full flex items-center justify-center gap-1 px-3 py-2.5 mt-3 border border-dashed border-gray-300 rounded-[10px] text-gray-600 hover:border-[#007AFF] hover:text-[#007AFF] transition-colors"
            >
              <Plus className="w-4 h-4" />
              <span className="text-sm">新建分组</span>
            </button>
          </div>

          {/* 右侧提示词列表 */}
          <div className="flex-1 flex flex-col overflow-hidden">
            {/* 搜索和操作栏 */}
            <div className="bg-white border-b border-gray-200 p-4">
              <div className="flex items-center gap-3">
                <div className="flex-1 relative">
                  <input
                    type="text"
                    value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}
                    placeholder="搜索提示词..."
                    className="w-full h-10 pl-10 pr-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                  />
                  <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
                </div>
                <button
                  onClick={() => setShowPromptEditor(true)}
                  className="h-10 px-5 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors flex items-center gap-2"
                >
                  <Plus className="w-4 h-4" />
                  新建提示词
                </button>
              </div>
            </div>

            {/* 提示词列表 */}
            <div className="flex-1 overflow-y-auto p-6">
              <div className="max-w-5xl mx-auto space-y-3">
                {filteredPrompts.length === 0 ? (
                  <div className="text-center py-12">
                    <p className="text-gray-400">暂无提示词</p>
                  </div>
                ) : (
                  filteredPrompts.map((prompt) => (
                    <div
                      key={prompt.id}
                      className={`bg-white border rounded-[10px] p-5 hover:shadow-md transition-all ${
                        prompt.isFavorite ? 'border-[#007AFF]' : 'border-gray-200'
                      }`}
                    >
                      <div className="flex items-start justify-between mb-3">
                        <div className="flex-1">
                          <div className="flex items-center gap-2 mb-2">
                            <h4 className="text-base font-semibold text-[#1C1C1E]">
                              {prompt.title}
                            </h4>
                            <span className={`px-2 py-0.5 rounded text-xs ${
                              prompt.group === '工作' ? 'bg-blue-100 text-blue-700' :
                              prompt.group === '学习' ? 'bg-orange-100 text-orange-700' :
                              prompt.group === '生活' ? 'bg-green-100 text-green-700' :
                              'bg-purple-100 text-purple-700'
                            }`}>
                              {prompt.group}
                            </span>
                          </div>
                          <p className="text-sm text-gray-600 line-clamp-2 mb-2">
                            {prompt.content}
                          </p>
                          <p className="text-xs text-gray-400">
                            创建于 {prompt.createdAt}
                          </p>
                        </div>
                        <div className="flex items-center gap-2 ml-4">
                          <button
                            onClick={() => handleToggleFavorite(prompt.id)}
                            className={`w-8 h-8 rounded-[10px] flex items-center justify-center transition-colors ${
                              prompt.isFavorite
                                ? 'text-yellow-500 hover:bg-yellow-50'
                                : 'text-gray-400 hover:bg-gray-100'
                            }`}
                          >
                            <Star className={`w-4 h-4 ${prompt.isFavorite ? 'fill-yellow-500' : ''}`} />
                          </button>
                          <button className="w-8 h-8 rounded-[10px] flex items-center justify-center text-gray-400 hover:text-[#007AFF] hover:bg-blue-50 transition-colors">
                            <Edit2 className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleDeletePrompt(prompt.id)}
                            className="w-8 h-8 rounded-[10px] flex items-center justify-center text-gray-400 hover:text-red-500 hover:bg-red-50 transition-colors"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </div>
                    </div>
                  ))
                )}
              </div>
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
                disabled={!newGroupName.trim() || groups.includes(newGroupName.trim())}
                className="px-5 py-2 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                创建
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}