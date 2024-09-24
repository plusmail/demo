package kroryi.demo.dto;

import lombok.Data;

@Data
public class MemberJoinDTO {
    private String mid;
    private String name;
    private String password;
    private String email;
    private boolean retirement;
    private String social;
}
