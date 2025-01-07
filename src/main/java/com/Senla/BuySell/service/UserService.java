package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.user.UserDto;
import com.Senla.BuySell.dto.user.UserMapper;
import com.Senla.BuySell.dto.user.UserProfileUpdateDto;
import com.Senla.BuySell.dto.user.UserRegisterDto;
import com.Senla.BuySell.model.Role;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.RoleRepository;
import com.Senla.BuySell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper,
                       BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с таким ID не найден."));
    }

    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public void updateUser(Long id, UserProfileUpdateDto updateDTO) {
        User user = findUserById(id);
        updateIfNotNullOrEmpty(updateDTO.username(), user::setUsername);
        updateIfNotNullOrEmpty(updateDTO.nickname(), user::setNickname);
        updateIfNotNullOrEmpty(updateDTO.password(), password -> user.setPassword(passwordEncoder.encode(password)));

        userRepository.save(user);
    }

    public void registerNewUser(UserRegisterDto registerRequest) {
        String encodedPassword = passwordEncoder.encode(registerRequest.password());
        User newUser = new User(registerRequest.username(), registerRequest.nickname(), encodedPassword);

        Role userRole = getRoleByName("ROLE_USER");
        newUser.setRoles(Collections.singletonList(userRole));

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
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден."));
    }

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Роль не найдена."));
    }

    private <T> void updateIfNotNullOrEmpty(T value, Consumer<T> setter) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            setter.accept(value);
        }
    }
}

