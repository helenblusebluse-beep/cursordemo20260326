package com.example.zhishiku.repository;

import com.example.zhishiku.model.Article;
import com.example.zhishiku.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findBySpaceOrderByUpdatedAtDesc(Space space);
}

