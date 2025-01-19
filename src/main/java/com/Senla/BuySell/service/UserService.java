package com.Senla.BuySell.service;

import com.Senla.BuySell.dto.user.UserDto;
import com.Senla.BuySell.dto.user.UserDtoMapper;
import com.Senla.BuySell.model.Review;
import com.Senla.BuySell.model.Role;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.ReviewRepository;
import com.Senla.BuySell.repository.RoleRepository;
import com.Senla.BuySell.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserDtoMapper userDtoMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private ReviewRepository reviewRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper,
                       BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository,
                       ReviewRepository reviewRepository){
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.reviewRepository = reviewRepository;
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userDtoMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с таким ID не найден."));
    }

    public List<UserDto> getAllUsers() {
        return userDtoMapper.toDtoList(userRepository.findAll());
    }

    @Transactional
    public void updateUser(Long id, UserDto userDto) {
        User user = findUserById(id);
        updateIfNotNullOrEmpty(userDto.getUsername(), user::setUsername);
        updateIfNotNullOrEmpty(userDto.getNickname(), user::setNickname);
        updateIfNotNullOrEmpty(userDto.getPassword(), password -> user.setPassword(passwordEncoder.encode(password)));

        userRepository.save(user);
    }

    @Transactional
    public void addReview(Long senderId,Long receiverId, int rating, String comment) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Получатель не найден!"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Отправитель не найден!"));

        Review review = new Review(sender, receiver, rating, comment);
        reviewRepository.save(review);

        updateUserRating(receiver);
    }

    @Transactional
    public void registerNewUser(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User newUser = new User(userDto.getUsername(), userDto.getNickname(), encodedPassword);

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

    private void updateUserRating(User receiver) {
        List<Review> reviews = reviewRepository.findByReceiver(receiver);
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        receiver.setRating(averageRating);
        userRepository.save(receiver);
    }

    private <T> void updateIfNotNullOrEmpty(T value, Consumer<T> setter) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            setter.accept(value);
        }
    }
}

