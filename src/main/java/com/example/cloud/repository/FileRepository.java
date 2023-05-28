package com.example.cloud.repository;

import com.example.cloud.model.CloudFile;
import com.example.cloud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<CloudFile,Long> {

    List<CloudFile> getAllByUser(User user);

    CloudFile getByName(String name);
}
