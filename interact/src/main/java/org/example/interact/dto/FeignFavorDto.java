package org.example.interact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.interact.entity.FavorEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeignFavorDto {
    private FavorEntity favorEntity;

    private String token;

}
