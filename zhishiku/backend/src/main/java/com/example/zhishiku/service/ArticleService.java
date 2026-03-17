package com.example.zhishiku.service;

import com.example.zhishiku.model.Article;
import com.example.zhishiku.model.Space;
import com.example.zhishiku.repository.ArticleRepository;
import com.example.zhishiku.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SpaceRepository spaceRepository;

    public ArticleService(ArticleRepository articleRepository,
                          SpaceRepository spaceRepository) {
        this.articleRepository = articleRepository;
        this.spaceRepository = spaceRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> listBySpace(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found: " + spaceId));
        return articleRepository.findBySpaceOrderByUpdatedAtDesc(space);
    }

    @Transactional(readOnly = true)
    public Article getById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));
    }

    @Transactional
    public Article create(Long spaceId, String title, String summary, String content) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found: " + spaceId));
        Article article = new Article();
        article.setSpace(space);
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

    @Transactional
    public Article update(Long id, String title, String summary, String content) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);
        article.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

    @Transactional
    public void delete(Long id) {
        if (!articleRepository.existsById(id)) {
            return;
        }
        articleRepository.deleteById(id);
    }
}

