package com.Senla.BuySell.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.Senla.BuySell.dto.user.UserDto;
import com.Senla.BuySell.dto.user.UserDtoMapper;
import com.Senla.BuySell.model.Role;
import com.Senla.BuySell.model.User;
import com.Senla.BuySell.repository.RoleRepository;
import com.Senla.BuySell.repository.UserRepository;
import com.Senla.BuySell.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDtoMapper userDtoMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, userDtoMapper, passwordEncoder, roleRepository);
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        User mockUser = new User("username", "nickname", "encodedPassword");
        mockUser.setId(userId);
        UserDto mockUserDto = new UserDto("nickname","username" , "encodedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userDtoMapper.toDto(mockUser)).thenReturn(mockUserDto);

        UserDto userDto = userService.getUserById(userId);

        assertNotNull(userDto);
        assertEquals("username", userDto.getUsername());
        assertEquals("nickname", userDto.getNickname());
    }

    @Test
    void testGetAllUsers() {
        List<User> mockUsers = Arrays.asList(
                new User("nickname1", "username1", "encodedPassword1"),
                new User("nickname2", "username2", "encodedPassword2")
        );
        when(userRepository.findAll()).thenReturn(mockUsers);
        when(userDtoMapper.toDtoList(mockUsers)).thenReturn(
                Arrays.asList(
                        new UserDto("nickname1", "username1", "encodedPassword1"),
                        new UserDto("nickname2", "username2", "encodedPassword2")
                )
        );

        // Act
        List<UserDto> userDtos = userService.getAllUsers();

        // Assert
        assertNotNull(userDtos);
        assertEquals(2, userDtos.size());
        assertEquals("username1", userDtos.get(0).getUsername());
        assertEquals("nickname2", userDtos.get(1).getNickname());
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        UserDto updatedUserDto = new UserDto("newNickname", "newUsername", "newPassword");
        User existingUser = new User("oldUsername", "oldNickname", "oldPassword");
        existingUser.setId(userId);
        existingUser.setRoleList(Collections.singletonList(new Role("ROLE_USER")));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("oldUsername");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("oldUsername")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updatedUserDto.getPassword())).thenReturn("encodedPassword");

        userService.updateUser(updatedUserDto);

        assertEquals("newUsername", existingUser.getUsername());
        assertEquals("newNickname", existingUser.getNickname());
        assertEquals("encodedPassword", existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }


    @Test
    void testRegisterNewUser() {
        UserDto newUserDto = new UserDto("newUser", "newNickname", "password");
        Role userRole = new Role("USER");

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(newUserDto.getPassword())).thenReturn("encodedPassword");

        // Act
        userService.registerNewUser(newUserDto, "USER");

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }
}

