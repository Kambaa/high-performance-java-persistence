package com.vladmihalcea.book.hpjp.spring.data.base.repository;

import com.vladmihalcea.book.hpjp.hibernate.forum.Post;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Vlad Mihalcea
 */
@Repository
public interface PostRepository extends BaseJpaRepository<Post, Long> {
}
