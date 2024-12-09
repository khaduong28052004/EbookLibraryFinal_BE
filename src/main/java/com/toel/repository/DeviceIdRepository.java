package com.toel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.DeviceId;

public interface DeviceIdRepository extends JpaRepository<DeviceId, Integer> {
    @Query("SELECT d FROM DeviceId d Where d.account.id = ?1")
    List<DeviceId> getListDeviceId(Integer account_id);
}
