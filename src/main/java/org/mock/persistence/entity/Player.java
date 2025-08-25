package org.mock.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    private Long id;
    private String name;
    private String team;
    private String position;

}
