package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.RegionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionInfoRepository extends JpaRepository<RegionInfo, Integer> {}
