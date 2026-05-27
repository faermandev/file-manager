package com.faermandev.file_manager.repository;

import com.faermandev.file_manager.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByOwner_Id(Long ownerId);

}
