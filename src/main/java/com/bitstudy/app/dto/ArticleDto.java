package com.bitstudy.app.dto;

import com.bitstudy.app.domain.Article;

import java.time.LocalDateTime;
import java.io.Serializable;

/** record : 자바 16부터 추가된 속성. DTO와 비슷.
 *  DTO를 구현하려면 게터,세터,이퀄즈, 해시코드, 투스트링같은 데이터처리를 수행하기 위해 오버라이드된 메서드를
 *  반복해서 작성해야 한다. 이런 것들을 보일러 플레이트 코드라고 함(여러 곳에서 재사용되는 반복적으로 비슷한 형태를 가진 코드)
 *  롬복으로 어느정도 중복을 줄일 수 있지만 근본적인 한계(안보일 뿐 생성 자체는 되는 것)는 해결 못함
 *  그래서 특정 데이터와 관련있는 필드들만 묶어놓은 자료구조로 record 라는 것이 생김.
 *
 *  *주의 : record는 entity로 쓸 수 없다. DTO만 가능함.
 *      이유 - 쿼리 결과를 매핑할 때 객체를 인스턴스화 할 수 있도록 매개변수가 없는 생성자가 필요하지만,
 *          레코드에서는 매개변수가 없는 생성자(기본생성자)를 제공하지 않는다. 또한 setter도 사용못함
 *          그래서 모든 필드의 값을 입력한 후에 생성할 수 있다.*/

public record ArticleDto( //우선 엔티티가 가진 모든 정보를 dto도 가지고 있게 해서 나중에 응답시 어떤걸 보낼지 선택해서 가공하게 할 것임.
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag,
        LocalDateTime registerDate,
        String createdBy,
        LocalDateTime modifiedDate,
        String modifiedBy) {

    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, String hashtag, LocalDateTime registerDate, String createdBy, LocalDateTime modifiedDate, String modifiedBy) {
        return new ArticleDto(id, userAccountDto, title, content, hashtag, registerDate, createdBy,modifiedDate, modifiedBy);
    }
    /* entity를 매개변수로 입력하면 ArticleDto로 반환해주는 메서드.
    * entity를 받아서 new 한 뒤 인스턴스에다가 entity. 하면서 매핑시켜서 return하는 것. 맵퍼라고 부름.*/
    public static ArticleDto from(Article entity) {
        return new ArticleDto(entity.getId(), UserAccountDto.from(entity.getUserAccount()), entity.getTitle(), entity.getContent(), entity.getHashtag(), entity.getRegisterDate(), entity.getCreatedBy(), entity.getModifiedDate(), entity.getModifiedBy());
    }

    public Article toEntity() {
        return Article.of(this.userAccountDto.toEntity(), this.title, this.content, this.hashtag);
    }
}
