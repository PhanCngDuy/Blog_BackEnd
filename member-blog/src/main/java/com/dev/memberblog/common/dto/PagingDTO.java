package com.dev.memberblog.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class PagingDTO {
    private Object data;
    private long total;
    private int page;
    private int limit;
}
