package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.user.UserDto;
import com.Senla.BuySell.dto.user.UserDtoMapper;
import com.Senla.BuySell.model.Role;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.RoleRepository;
import com.Senla.BuySell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper,
                       BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserById(Long id) {
        return userDtoMapper.toDto(findUserById(id,"Пользователь не найден."));
    }

    public List<UserDto> getAllUsers() {
        return userDtoMapper.toDtoList(userRepository.findAll());
    }

    @Transactional
    public void updateUser(UserDto userDto) {
        Long userId = getCurrentUserId();
        User user = findUserById(userId, "Пользователь не найден.");
        updateIfNotNullOrEmpty(userDto.getUsername(), user::setUsername);
        updateIfNotNullOrEmpty(userDto.getNickname(), user::setNickname);
        updateIfNotNullOrEmpty(userDto.getPassword(), password -> user.setPassword(passwordEncoder.encode(password)));
        userRepository.save(user);
    }

    @Transactional
    public void registerNewUser(UserDto userDto, String role) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User newUser = new User(userDto.getUsername(), userDto.getNickname(), encodedPassword);

        Role userRole = getRoleByName(role);
        newUser.setRoleList(Collections.singletonList(userRole));

        userRepository.save(newUser);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Такого пользователя не существует."));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoleList().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }

    public Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"))
                .getId();
    }

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Роль не найдена."));
    }

    public User findUserById(Long id, String errorMessage) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(errorMessage));
    }

    private <T> void updateIfNotNullOrEmpty(T value, Consumer<T> setter) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            setter.accept(value);
        }
    }
}


