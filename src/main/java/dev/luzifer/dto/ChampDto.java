package dev.luzifer.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@Getter
@RequiredArgsConstructor
@ToString
public class ChampDto {

    String name; // UPPERCASE
    String category;
}
