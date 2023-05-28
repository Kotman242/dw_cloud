package com.example.cloud.repository;

import com.example.cloud.model.CloudFile;
import com.example.cloud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<CloudFile,Long> {

    List<CloudFile> getAllByUser(User user);

    CloudFile getByName(String name);

    void deleteByNameAndUser(String name, User user);

    CloudFile findByUserAndName(User user, String name);

    @Modifying
    @Query("update CloudFile f set f.name = :newName where f.name = :filename and f.user = :user")
    void editFileNameByUser(@Param("user") User user, @Param("filename") String filename, @Param("newName") String newName);

    List<CloudFile> findAllByUser(User user);
}
