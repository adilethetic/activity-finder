package com.adilet.repositories;

import com.adilet.entities.Activity;
import com.adilet.enums.ActivityCategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity>  findAllByOrderByStartTimeAsc();

    List<Activity> findByCategoryOrderByStartTimeAsc(ActivityCategoryStatus category);
}
