package kroryi.demo.domain;


import jakarta.persistence.*;
import lombok.*;

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
    fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<BoardImage> imageSet= new HashSet<>();

    public void addImage(String uuid, String fileName){

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
