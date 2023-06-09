package com.example.cloud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name",
            nullable = false)
    private String name;

    @Column(name = "path",
            nullable = false,
            unique = true)
    private String path;

    @Column(name = "size")
    private long size;

    @JoinColumn(name = "user_id",
            nullable = false)
    @ManyToOne
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloudFile file = (CloudFile) o;
        return size == file.size && Objects.equals(name, file.name) && Objects.equals(path, file.path) && Objects.equals(user, file.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, size, user);
    }
}
