package com.sachin.blog.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.sachin.blog.domain.entities.Tag;

public interface TagService {
    List<Tag> getTags();
    List<Tag> createTags(Set<String> tagNames);
    void deteleTag (UUID id);
    Tag getTagById(UUID id);
    List<Tag> getTagByIds(Set<UUID> ids);
}
