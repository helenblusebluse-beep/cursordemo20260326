import axios from 'axios';

export interface Article {
  id: number;
  title: string;
  summary: string | null;
  content: string;
  spaceId?: number;
}

export async function fetchArticles(spaceId: number) {
  const resp = await axios.get<Article[]>('/api/articles', {
    params: { spaceId }
  });
  return resp.data;
}

export async function fetchArticle(id: number) {
  const resp = await axios.get<Article>(`/api/articles/${id}`);
  return resp.data;
}

