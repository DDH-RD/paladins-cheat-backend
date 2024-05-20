package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.ItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemInfoRepository extends JpaRepository<ItemInfo, Integer> {}
