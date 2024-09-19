package com.fuck_ruangong.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Args {
    private Integer numberOfQuestions = 10;

    private Integer range = 10;

    private String exercisesFileName;

    private String answerFileName;
}
