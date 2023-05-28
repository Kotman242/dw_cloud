package com.example.cloud.service;

import com.example.cloud.dto.request.EditFileNameRequest;
import com.example.cloud.dto.response.FileResponse;
import com.example.cloud.exception.DeleteFileException;
import com.example.cloud.exception.EditFileException;
import com.example.cloud.exception.SaveFileException;
import com.example.cloud.exception.UnauthorizedException;
import com.example.cloud.model.CloudFile;
import com.example.cloud.model.User;
import com.example.cloud.repository.AuthenticationRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudService {

    private FileRepository fileRepository;
    private UserRepository userRepository;

    private AuthenticationRepository authenticationRepository;


    @Value("${file.pathForSave}")
    private String savePath;

    public void saveFile(String token, String filename, MultipartFile file) {
        checkUser(token,"Добавление файла неавторизованным пользователем ");
        User user = getUserByToken(token);
        Path directory = Path.of(createPath(savePath, user.getUsername()));
        String filePath = createPath(directory.toString(), filename);

        CloudFile cloudFile = CloudFile.builder()
                .size(file.getSize())
                .path(filePath)
                .name(filename)
                .user(user)
                .build();
        try {
            if (Files.notExists(directory)) {
                Files.createDirectory(directory);
            }
        } catch (Exception e) {
            log.error("Ошибка во время создании директории пользователя -->  " + e.getMessage());
            throw new SaveFileException("Ошибка во время создании директории пользователя");
        }
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath))) {

            out.write(file.getBytes());
            out.flush();

        } catch (Exception e) {
            log.error("Ошибка во время сохранения файла -->  " + e.getMessage());
            throw new SaveFileException("Ошибка во время сохранения файла");
        }

        fileRepository.save(CloudFile.builder()
                .size(file.getSize())
                .name(filename)
                .user(user)
                .path(filePath)
                .build());
        fileRepository.save(cloudFile);
    }

    public void deleteFile(String token, String filename) {
        checkUser(token,"Удаление файла неавторизованным пользователем ");
        User user = getUserByToken(token);
        String directory = createPath(savePath, user.getUsername());
        Path file = Path.of(createPath(directory, filename));
        if (Files.exists(file)) {
            try {
                Files.delete(file);
                fileRepository.deleteByNameAndUser(filename,user);
                log.debug("Файл --> " + filename + " был удален");
            } catch (IOException e) {
                log.error("Ошибка во время удаления файла -->  " + e.getMessage());
                throw new DeleteFileException("Ошибка во время удаления файла");
            }
        } else {
            log.debug("Файл --> " + filename + " не существует");
        }
    }

    public void editFile(String token, String filename, EditFileNameRequest editFileNameRequest){
        checkUser(token,"Изменение файла неавторизованным пользователем ");

        User user = getUserByToken(token);

        fileRepository.editFileNameByUser(user, filename, editFileNameRequest.getFilename());

        final CloudFile fileWithOldName = fileRepository.findByUserAndName(user, filename);
        if (fileWithOldName != null) {
            log.error("Ошибка во время изменения файла");
            throw new DeleteFileException("Ошибка во время изменения файла");
        }
        log.info("Файл изменен. Пользователь {}", user.getUsername());
    }

    public byte[] getFile(String token, String filename) {
        checkUser(token,"Получение файла неавторизованным пользователем ");
        CloudFile cloudFile = fileRepository.getByName(filename);
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(
                new FileInputStream(cloudFile.getPath()))) {
            if (Files.notExists(Path.of(cloudFile.getPath()))) {
                log.debug("Файл --> " + filename + " не существует");
                return null;
            }

            byte[] result = bufferedInputStream.readAllBytes();
            log.debug("Файл --> " + filename + " найден");
            return result;

        } catch (IOException e) {
            log.error("Ошибка во время получения файла -->  " + e.getMessage());
            throw new DeleteFileException("Ошибка во время получения файла");
        }
    }

    public List<FileResponse> getAllFiles(String token, Integer limit) {
        checkUser(token,"Получение списка файлов неавторизованным пользователем ");
        final User user = getUserByToken(token);

        log.info("Успешное получение списка файлов. Пользователь {}", user.getUsername());
        return fileRepository.findAllByUser(user).stream()
                .map(o -> new FileResponse(o.getName(), o.getSize()))
                .collect(Collectors.toList());
    }
    private String createPath(String path, String name) {
        return path + File.separator + name;
    }

    private User getUserByToken(String token) {
        String userName = authenticationRepository.getUsernameByToken(token);
        return userRepository.getByUsername(userName);
    }

    private void checkUser(String token, String msg){
        final User user = getUserByToken(token);
        if (user == null) {
            log.error(msg);
            throw new UnauthorizedException("Edit file name: Unauthorized");
        }
    }
}
