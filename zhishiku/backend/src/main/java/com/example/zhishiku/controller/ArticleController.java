package com.example.zhishiku.controller;

import com.example.zhishiku.model.Article;
import com.example.zhishiku.service.ArticleService;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@Validated
public class ArticleController {

    public static class ArticleRequest {
        @NotNull
        private Long spaceId;
        @NotBlank
        private String title;
        private String summary;
        @NotBlank
        private String content;

        public Long getSpaceId() {
            return spaceId;
        }

        public void setSpaceId(Long spaceId) {
            this.spaceId = spaceId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<Article>> listBySpace(@RequestParam("spaceId") Long spaceId) {
        return ResponseEntity.ok(articleService.listBySpace(spaceId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> get(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Article> create(@RequestBody @Validated ArticleRequest request) {
        Article article = articleService.create(
                request.getSpaceId(),
                request.getTitle(),
                request.getSummary(),
                request.getContent()
        );
        return ResponseEntity.ok(article);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody @Validated ArticleRequest request) {
        Article article = articleService.update(
                id,
                request.getTitle(),
                request.getSummary(),
                request.getContent()
        );
        return ResponseEntity.ok(article);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

