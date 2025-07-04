package com.example.userservice.controller;

import com.example.userservice.dto.UserRegistDto;
import com.example.userservice.dto.UserVozvratDto;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserVozvratDto> createUser(@RequestBody @Valid UserRegistDto userRegistDto) {
        log.info("Создание пользователя: {}", userRegistDto);
        UserVozvratDto dto = userService.createUser(userRegistDto);
        log.info("Пользователь создан: {}", dto);

        EntityModel<UserVozvratDto> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(UserController.class).createUser(userRegistDto)).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).updateUser(dto.getId(), null)).withRel("Обновить пользователя"));
        model.add(linkTo(methodOn(UserController.class).deleteUser(dto.getId())).withRel("Удалить пользователя"));
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Список всех пользователей"));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserVozvratDto>> findUserById(@PathVariable Long id) {
        log.info("Поиск пользователя по id: {}", id);
        UserVozvratDto dto = userService.findUserById(id);
        log.info("Пользователь найден: {}", dto);

        EntityModel<UserVozvratDto> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(UserController.class).findUserById(id)).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("Обновить пользователя"));
        model.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("Удалить пользователя"));
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Список всех пользователей"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserVozvratDto> deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        UserVozvratDto dto = userService.deleteUser(userId);
        log.info("Пользователь удален: {}", userId);

        EntityModel<UserVozvratDto> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(UserController.class).deleteUser(userId)).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("Создать нового пользователя"));
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Список всех пользователей"));
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserVozvratDto> updateUser(@PathVariable Long userId, @RequestBody @Valid UserRegistDto userRegistDto) {
        log.info("Обновление пользователя с id: {}", userId);
        UserVozvratDto dto = userService.updateUser(userId, userRegistDto);
        log.info("Пользователь обновлен: {}", userId);

        EntityModel<UserVozvratDto> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(UserController.class).updateUser(userId, userRegistDto)).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).findUserById(userId)).withRel("Поиск пользователя"));
        model.add(linkTo(methodOn(UserController.class).deleteUser(userId)).withRel("Удалить пользователя"));
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Список всех пользователей"));
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserVozvratDto>>> getAllUsers() {
        log.info("Запрошен список всех пользователей");
        List<UserVozvratDto> users = userService.getAllUsers();
        log.info("Получены все пользователи: {}", users);

        List<EntityModel<UserVozvratDto>> models = users.stream().map(dto -> {
                    EntityModel<UserVozvratDto> model = EntityModel.of(dto);
                    model.add(linkTo(methodOn(UserController.class).findUserById(dto.getId())).withSelfRel());
                    return model;
                }).toList();

        CollectionModel<EntityModel<UserVozvratDto>> collectionModel = CollectionModel.of(models);
        collectionModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }
}

