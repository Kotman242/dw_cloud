package com.example.cloud.service;

import com.example.cloud.exception.SaveFileException;
import com.example.cloud.model.CloudFile;
import com.example.cloud.model.User;
import com.example.cloud.repository.FileRepository;
import com.example.cloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudService {

    private FileRepository fileRepository;
    private UserRepository userRepository;


    @Value("${file.pathForSave}")
    private String savePath;

    void saveFile(String userName, String filename, MultipartFile file){
        User user;
        String directory = createPath(savePath,userName);
        try{
            Path pathDirectory = Path.of(directory);
        if(Files.notExists(pathDirectory)){
            Files.createDirectory(pathDirectory);
        }
        } catch (Exception e) {
            log.error("Ошибка во время создании директории пользователя -->  " + e.getMessage());
            throw new SaveFileException("Ошибка во время создании директории пользователя");
        }
        String newFile = savePath+File.separator+filename;
        try(BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFile))) {

            out.write(file.getBytes());
            out.flush();

        } catch (Exception e) {
            log.error("Ошибка во время сохранения файла -->  "+e.getMessage());
            throw new SaveFileException("Ошибка во время сохранения файла");
        }

        fileRepository.save(CloudFile.builder()
                        .size(file.getSize())
                        .name(filename)
                        .user(userRepository.getByUsername(userName))
                        .path(newFile)
                .build());
    }

    private String createPath(String path, String name){
        return path+File.separator+name;
    }
}
