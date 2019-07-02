package com.manager.service.Impl;

import com.manager.dto.LoginDTO;
import com.manager.dto.ProfileDTO;
import com.manager.dto.ResetPasswordDTO;
import com.manager.md5.MD5;
import com.manager.model.CheckInOut;
import com.manager.model.PasswordIssuingCode;
import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.PasswordIssuingCodeRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import com.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MD5 md5;
    @Autowired
    private PasswordIssuingCodeRepository passwordIssuingCodeRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @Value("${emailSystem}")
    String emailSystem;
    @Value("${passwordSystem}")
    private String passwordSystem;
    @Value("${cus.host}")
    private String host;
    @Value("${path.avatar}")
    String pathAvatar;

    @Override
    public ResponseEntity<String> login(LoginDTO loginDTO, HttpServletRequest request) {
        User user = userRepository.searchUserByEmail(loginDTO.getEmail());
        if (user != null) {
            Token token = tokenRepository.getTokenById(user.getId());
            if (token == null) {
                int random = new Random().nextInt();
                while (true) {
                    try {
                        String tokenString = md5.convertToMD5(String.valueOf(random));
                        token = new Token(user.getId(), tokenString);
                        token = tokenRepository.save(token);
                        break;
                    } catch (Exception e) {
                        random = new Random().nextInt();
                    }
                }
            }
            if (user.getPassword().equals(md5.convertToMD5(loginDTO.getPassword()))) {
                HttpSession session = request.getSession();
                session.setAttribute("token", token.getToken());
                session.setMaxInactiveInterval(60 * 60 * 24);
                return new ResponseEntity<>(token.getToken(), HttpStatus.OK);

            }
        }

        return new ResponseEntity<>("WRONG_USERNAME_OR_PASSWORD", HttpStatus.OK);
    }


    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<String> forgotPassword(LoginDTO loginDTO, HttpServletRequest request) {
        String path = request.getContextPath();
        String code = "";
        String email = loginDTO.getEmail();
        if (email != null) {
            if (userRepository.findEmail(email) != null) {
                int id = userRepository.findIdByEmail(email);
                PasswordIssuingCode passwordIssuingCode = passwordIssuingCodeRepository.getPasswordIssuingCodeById(id);
                if (passwordIssuingCode == null) {
                    int random = new Random().nextInt();
                    PasswordIssuingCode passwordIssuingCode1 = new PasswordIssuingCode(id, md5.convertToMD5(String.valueOf(random)));
                    passwordIssuingCode = passwordIssuingCodeRepository.save(passwordIssuingCode1);

                    code = passwordIssuingCode.getCode();
                } else {
                    code = passwordIssuingCode.getCode();
                }
                Properties properties = System.getProperties();
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getDefaultInstance(properties);
                MimeMessage message = new MimeMessage(session);
                try {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                    message.setSubject("Forgot password ITLeadPro");
                    message.setContent("<a href='" + host + path + "/reset-password/" + code + "/" + id + "'>Click Here To Active New Password</a>",
                            "text/html");
                    Transport transport = session.getTransport("smtp");
                    transport.connect("smtp.gmail.com", emailSystem, passwordSystem);
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    return new ResponseEntity<String>("COMPLETE_CHECK_MAIL", HttpStatus.OK);
                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ResponseEntity<String>("EMAIL_NOT_EXITS", HttpStatus.OK);

    }


    // reset password in email send by forgot password
    public ResponseEntity<String> resetPassword(ResetPasswordDTO resetPasswordDTO, String code, int id) {
        //get code reset password in db
        PasswordIssuingCode passwordIssuingCode = passwordIssuingCodeRepository.getPasswordIssuingCodeById(id);

        if (passwordIssuingCode != null && resetPasswordDTO != null && passwordIssuingCode.getCode().equals(code)) {
            //id of passwordIssuingCode = id of user
            User user = userRepository.getUserById(id);
            if (user == null) {
                return new ResponseEntity<>("USER_NOT_EXITS", HttpStatus.BAD_REQUEST);
            }
            if (user.getEmail() != null && user.getEmail().equals(resetPasswordDTO.getEmail()) && user.getId() == id) {
                if (resetPasswordDTO.getNewPassword() != null && resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getNewPassword1())) {
                    //update password
                    user.setPassword(md5.convertToMD5(resetPasswordDTO.getNewPassword()));
                    userRepository.save(user);
                    int random = new Random().nextInt();
                    //update new code
                    passwordIssuingCode.setCode(md5.convertToMD5(String.valueOf(random)));
                    passwordIssuingCodeRepository.save(passwordIssuingCode);
                    return new ResponseEntity<>("COMPLETE", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("PASSWORD_INCORRECT", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("LINK_DIE", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<String> logOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.getAttribute("token");
        session.invalidate();
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    // @Override
   /* public ResponseEntity<String> checkIn(CheckInOutDTO checkInOutDTO) {
        User user = userRepository.findById(checkInOutDTO.getUserId()).get();
        System.out.println(user);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(checkInOutDTO.getDateCheckIn());
        CheckInOut checkInOut = new CheckInOut();
        int hourCheckIn = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteCheckIn = calendar.get(Calendar.MINUTE);
        System.out.println("gio: " + hourCheckIn);
        System.out.println("Phut: " + minuteCheckIn);

        Date date = checkInOutRepository.getDate();

        Date date1 = new Date();

        if (checkInOutImpl.compareDate(date, date1)) {
            return new ResponseEntity<>("Checked", HttpStatus.OK);
        } else {
            if (calendar.get(Calendar.HOUR_OF_DAY) < 8 && calendar.get(Calendar.MINUTE) < 30) {
                return new ResponseEntity<String>("chua den gio", HttpStatus.OK);
            } else if (hourCheckIn >= 8 && minuteCheckIn >= 30 && hourCheckIn <= 9) {
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);
                checkInOut.setStartTime(calendar.getTime());
                checkInOut.setDayCheckIn(calendar.getTime());
                checkInOut.setUser(user);
                checkInOutRepository.save(checkInOut);
                return new ResponseEntity<>("CHECKIN_SUCCESS", HttpStatus.OK);
            } else if (hourCheckIn >= 9 && hourCheckIn <= 10 && minuteCheckIn <= 30) {
                checkInOut.setDayCheckIn(calendar.getTime());
                checkInOut.setStartTime(calendar.getTime());
                checkInOut.setUser(user);

                checkInOutRepository.save(checkInOut);
                return new ResponseEntity<>("CHECKIN_SUCCESS", HttpStatus.OK);
            } else if (hourCheckIn >= 10 && minuteCheckIn > 30 && hourCheckIn < 11) {
                calendar.set(Calendar.HOUR_OF_DAY, 13);
                calendar.set(Calendar.MINUTE, 0);
                checkInOut.setDayCheckIn(calendar.getTime());
                checkInOut.setStartTime(calendar.getTime());
                checkInOut.setUser(user);

                checkInOutRepository.save(checkInOut);
                return new ResponseEntity<>("FAIL", HttpStatus.OK);
            } else if (hourCheckIn >= 12 && (hourCheckIn <= 13 && minuteCheckIn == 0)) {
                checkInOut.setUser(user);
                checkInOut.setDayCheckIn(calendar.getTime());
                calendar.set(Calendar.HOUR_OF_DAY, 13);
                calendar.set(Calendar.MINUTE, 0);
                checkInOut.setStartTime(calendar.getTime());
                checkInOutRepository.save(checkInOut);
                return new ResponseEntity<String>("CHECKIN_SUCCESS", HttpStatus.OK);
            } else if (hourCheckIn >= 13 && (hourCheckIn <= 16 && minuteCheckIn == 0)) {
                checkInOut.setDayCheckIn(calendar.getTime());
                checkInOut.setStartTime(calendar.getTime());
                checkInOut.setUser(user);
                return new ResponseEntity<>("CHECKIN_SUCCESS", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("khong tinh ", HttpStatus.OK);
            }
        }

    }*/


    public ResponseEntity<ProfileDTO> profile(HttpServletRequest request) {
        String code = (String) request.getSession().getAttribute("token");
        Token token = tokenRepository.getTokenByCode(code);
        User user = userRepository.getUserById(token.getId());
        System.out.println(user.getPicture());
        ProfileDTO profileDTO = new ProfileDTO();
        if (user != null) {
            profileDTO.setAvatar(user.getPicture());
            System.out.println(profileDTO.getAvatar());
            profileDTO.setEmail(user.getEmail());
            profileDTO.setFullName(user.getName());
            profileDTO.setPhoneNumber(user.getPhoneNumber());
            profileDTO.setDateOfBirth(user.getBirthDay().getTime());
            if(user.getCreatedDate() != null) {
                long startDate = user.getCreatedDate().getTime();
                profileDTO.setStartDate(startDate);
            }
            if (user.getPosition() == 1) {
                profileDTO.setPosition("DEV");
            } else if (user.getPosition() == 2) {
                profileDTO.setPosition("Manager");
            } else if (user.getPosition() == 3) {
                profileDTO.setPosition("Admin");
            } else if (user.getPosition() == 4) {
                profileDTO.setPosition("Accountant");
            } else {
                profileDTO.setPosition("Employee");
            }
        }

        return new ResponseEntity<>(profileDTO, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<User> updateProfile(ProfileDTO profileDTO, HttpServletRequest request) {

        String codeToken = (String) request.getSession().getAttribute("token");
        if (codeToken == null) {
            return new ResponseEntity("NOT_LOGGED_IN", HttpStatus.BAD_REQUEST);
        }

        if (profileDTO.getPhoneNumber().length() > 10) {
            return new ResponseEntity("LENGTH_OF_PHONE_NUMBER_10", HttpStatus.OK);
        }

        try {
            System.out.println(profileDTO.getPhoneNumber().length());
            long number = Integer.parseInt(profileDTO.getPhoneNumber());
        } catch (NumberFormatException e) {
            return new ResponseEntity("NUMBER_FORMAT_EXCEPTION", HttpStatus.OK);
        }

        Token token = tokenRepository.getTokenByCode(codeToken);
        User user = userRepository.findById(token.getId()).get();
        user.setName(profileDTO.getFullName());
        user.setPhoneNumber(profileDTO.getPhoneNumber());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(profileDTO.getDateOfBirth());
        user.setBirthDay(calendar.getTime());
        user.setPicture(profileDTO.getAvatar());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setAvatar(user.getPicture());
        userRepository.save(user);
        return new ResponseEntity(profileDTO, HttpStatus.OK);

    }

    public ResponseEntity changePassword(ResetPasswordDTO resetPasswordDTO, HttpServletRequest request) {

        String codeToken = (String) request.getSession().getAttribute("token");
        if (codeToken == null) {
            return new ResponseEntity("NOT_LOGGED_IN", HttpStatus.BAD_REQUEST);
        }
        Token token = tokenRepository.getTokenByCode(codeToken);
        User user = userRepository.findById(token.getId()).get();
        String convertPassword = md5.convertToMD5(resetPasswordDTO.getOldPassword());
        if (user.getPassword().equals(convertPassword)) {
            if (resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getNewPassword1())) {
                String convertNewPassword = md5.convertToMD5(resetPasswordDTO.getNewPassword());
                user.setPassword(convertNewPassword);
                userRepository.save(user);
                return new ResponseEntity("COMPLETE", HttpStatus.OK);
            } else {
                return new ResponseEntity("PASSWORD INCORRECT", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity("WRONG_EMAIL_OR_PASSWORD", HttpStatus.OK);
        }
    }

    public ResponseEntity uploadFile(MultipartFile multipartFile, HttpServletRequest request) {
        String codeToken = (String) request.getSession().getAttribute("token");
        Token token = tokenRepository.getTokenByCode(codeToken);
        int userId = token.getId();
        User user = userRepository.findById(userId).get();
        System.out.println(user.getEmail());

        //check file exits, if exits then delete
        if(user.getPicture() != null) {
            File oldFile = new File(user.getPicture());
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        try {
            if (multipartFile != null) {
                String img = md5.convertToMD5(String.valueOf(user.getId()));
                int random = new Random().nextInt();
                String filePath = pathAvatar + img + random + "-" + multipartFile.getOriginalFilename();
                System.out.println(filePath);
                File file = new File(filePath);
                try {
                    multipartFile.transferTo(file);
                    user.setPicture(filePath);
                    userRepository.save(user);
                    return new ResponseEntity(filePath, HttpStatus.OK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (MultipartException e) {
            return new ResponseEntity("FILE_ERROR", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("ERROR", HttpStatus.OK);
    }

}
