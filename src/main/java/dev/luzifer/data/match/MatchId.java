package dev.luzifer.data.match;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@Getter
@RequiredArgsConstructor(staticName = "of")
@ToString
@EqualsAndHashCode
public class MatchId {

    long id;

}
