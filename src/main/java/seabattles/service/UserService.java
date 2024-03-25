package seabattles.service;import jakarta.transaction.Transactional;import org.springframework.data.jpa.repository.Modifying;import seabattles.aspect.exception.UserException;import seabattles.entity.User;import lombok.RequiredArgsConstructor;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import seabattles.repository.UserJpaRepo;import seabattles.service.dto.UserAuthDto;import seabattles.service.dto.UserDto;import seabattles.service.dto.UserRegisterDto;import seabattles.service.mapper.MapperServiceToRepositoryUserDto;import java.util.List;@Service@RequiredArgsConstructorpublic class UserService {    @Autowired    private final UserJpaRepo userRepository;    @Autowired    private final MapperServiceToRepositoryUserDto mapper;    public List<UserDto> getAllUsers() {        List<User> userList = userRepository.findAll();        return mapper.mapUserToDtoList(userList);    }    public UserDto registerUser(UserRegisterDto userRegisterDto) {        User user = mapper.mapUserRegisterDtoToUser(userRegisterDto);        User maybeOldUser = userRepository.findByLogin(user.getLogin());        if (maybeOldUser != null){            throw new UserException("User with this login is already registered.");        } else {            return mapper.mapUserToDto(userRepository.save(user));        }    }    public UserDto loginUser(UserAuthDto userAuthDto) {        User loggedUser = userRepository.findByLoginAndPassword(userAuthDto.getLogin(), userAuthDto.getPassword());        if (loggedUser == null){            throw new UserException("User with this login and password is not registered yet.");        } else {            return mapper.mapUserToDto(loggedUser);        }    }    @Transactional    @Modifying(flushAutomatically = true)    public UserDto updateUserPass(UserAuthDto userAuthDto, String newPassword){        User loggedUser = userRepository.findByLoginAndPassword(userAuthDto.getLogin(), userAuthDto.getPassword());        if (loggedUser == null){            throw new UserException("User with this login and password is not registered yet.");        } else {            loggedUser.setPassword(newPassword);            return mapper.mapUserToDto(loggedUser);        }    }}