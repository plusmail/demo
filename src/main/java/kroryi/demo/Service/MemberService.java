package kroryi.demo.Service;

import kroryi.demo.dto.MemberJoinDTO;

public interface MemberService {

    static class MidExistException extends Exception{

    }
    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
}
