package com.vladmihalcea.book.hpjp.hibernate.identifier.optimizer.providers;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

/**
 * @author Vlad Mihalcea
 */
public class Table1PostEntityProvider extends PostEntityProvider<Table1PostEntityProvider.Post> {

    public Table1PostEntityProvider() {
        super(Post.class);
    }

    @Override
    public Post newPost() {
        return new Post();
    }

    @Entity(name = "Post")
    @Table(name = "post")
    public static class Post {

        @Id
        @GenericGenerator(name = "table", strategy = "enhanced-table", parameters = {
                @org.hibernate.annotations.Parameter(name = "table_name", value = "sequence_table"),
                @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
                @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled"),
        })
        @GeneratedValue(generator = "table", strategy=GenerationType.TABLE)
        private Long id;
    }
}
