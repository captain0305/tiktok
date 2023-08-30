package org.example.interact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.interact.entity.CommentEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeignCommentDto {
    private CommentEntity commentEntity;
    private String token;
}
