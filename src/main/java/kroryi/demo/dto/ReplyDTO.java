package kroryi.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private Long rno;
    @NotNull
    private Long bno;
    @NotNull
    private String replyText;
    @NotNull
    private String replyer;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}