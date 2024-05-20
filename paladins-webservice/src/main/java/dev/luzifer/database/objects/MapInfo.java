package dev.luzifer.database.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class MapInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  String mapName;
}
