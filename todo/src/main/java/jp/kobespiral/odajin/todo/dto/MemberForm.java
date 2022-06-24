package jp.kobespiral.odajin.todo.dto;

import jp.kobespiral.odajin.todo.entity.Member;
import lombok.Data;

@Data
public class MemberForm {
    String mid; // メンバーID．
    String name; // 名前

    public Member toEntity() {
        Member m = new Member(mid, name);
        return m;
    }
}