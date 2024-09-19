package kroryi.demo.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;


@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 200, nullable = false)
    private String title;
    @Column(length = 2000, nullable = false)
    private String content;
    @Column(length = 50, nullable = false)
    private String writer;
    @Column(length = 50, nullable = true)
    private String address;

    @Setter
    @OneToMany(mappedBy = "board",
    cascade = {CascadeType.ALL},
    fetch = FetchType.LAZY,
    orphanRemoval = true
    // orphan 고아 Board와 BoardImage 엔티티간의 연결을 끊는것 쿼리는 Delete쿼리 실행
            // BoardImage에 있는 데이터를 자동으로 삭제된다.
    )
    @Builder.Default
    @BatchSize(size = 10) // where 조건에 in(?,? ...) 사용 where a= ? or a= ? 같은 것
    @ToString.Exclude
    private Set<BoardImage> imageSet= new HashSet<>();

//    Hibernate:
//    select
//    is1_0.board_bno,
//    is1_0.uuid,
//    is1_0.file_name,
//    is1_0.ord
//            from
//    board_image is1_0
//    where
//    is1_0.board_bno in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

    public void addImage(String uuid, String fileName){

        System.out.println("addImage 1--->" + uuid);
        System.out.println("addImage 2--->" + fileName);
        BoardImage image = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(image);
    }

    public void clearImages(){
        imageSet.forEach(boardImage -> {
            boardImage.changeBoard(null);
        });
        this.imageSet.clear();
    }


    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
