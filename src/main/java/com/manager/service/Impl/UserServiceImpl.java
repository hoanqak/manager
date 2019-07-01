package com.manager.service.Impl;

import com.manager.dto.CheckInOutDTO;
import com.manager.dto.LoginDTO;
import com.manager.dto.ProfileDTO;
import com.manager.dto.ResetPasswordDTO;
import com.manager.md5.MD5;
import com.manager.model.CheckInOut;
import com.manager.model.PasswordIssuingCode;
import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.CheckInOutRepository;
import com.manager.repository.PasswordIssuingCodeRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import com.manager.service.UserService;
import com.mysql.cj.x.protobuf.Mysqlx;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MD5 md5;
    @Autowired
    private PasswordIssuingCodeRepository passwordIssuingCodeRepository;
    @Value("${cus.host}")
    private String host;

    @Autowired
    private CheckInOutRepository checkInOutRepository;

    @Autowired
    private CheckInOutImpl checkInOutImpl;

    @Autowired
    private TokenRepository tokenRepository;

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
            boolean checkMail = false;
            for (String s : userRepository.findEmail(email)) {
                if (s.equals(email)) {
                    checkMail = true;
                    break;
                }
            }
            if (checkMail) {
                int id = userRepository.findIdByEmail(email);
                PasswordIssuingCode passwordIssuingCode = passwordIssuingCodeRepository.getPasswordIssuingCodeById(id);
                if (passwordIssuingCode == null) {
                    int random = new Random().nextInt();
                    PasswordIssuingCode passwordIssuingCode1 = new PasswordIssuingCode(id, md5.convertToMD5(String.valueOf(random)));
                    passwordIssuingCode = passwordIssuingCodeRepository.save(passwordIssuingCode1);

                    code = passwordIssuingCode.getCode() + "-" + passwordIssuingCode.getId();
                } else {
                    code = passwordIssuingCode.getCode() + "-" + passwordIssuingCode.getId();
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
                    transport.connect("smtp.gmail.com", "scofjeld@gmail.com", "saranghae123");
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    return new ResponseEntity<String>("COMPLELE_CHECK_MAIL", HttpStatus.OK);
                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ResponseEntity<String>("EMAIL_NOT_EXITS", HttpStatus.NOT_FOUND);

    }


    public ResponseEntity<String> resetPassword(ResetPasswordDTO resetPasswordDTO, String code, int id) {
        PasswordIssuingCode passwordIssuingCode = passwordIssuingCodeRepository.getPasswordIssuingCodeById(id);
        if (passwordIssuingCode != null && resetPasswordDTO != null && passwordIssuingCode.getCode().equals(code)) {
            User user = userRepository.findById(id).get();
            if (user.getEmail().equals(resetPasswordDTO.getEmail()) && user.getId() == id) {
                if (resetPasswordDTO.getPassword() != null && resetPasswordDTO.getPassword().equals(resetPasswordDTO.getRePassword())) {
                    //update password
                    user.setPassword(md5.convertToMD5(resetPasswordDTO.getPassword()));
                    userRepository.save(user);
                    int random = new Random().nextInt();
                    //update new code
                    passwordIssuingCode.setCode(md5.convertToMD5(String.valueOf(random)));
                    passwordIssuingCodeRepository.save(passwordIssuingCode);
                    return new ResponseEntity<String>("COMPLETE", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("PASSWORD_INCORRECT", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<String>("FAILED", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<String> logOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.getAttribute("token");
        session.invalidate();
        return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
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
        User user = userRepository.findById(token.getId()).get();
        ProfileDTO profileDTO = new ProfileDTO();
        if (user != null) {
            profileDTO.setAvatar(user.getPicture());
            profileDTO.setId(user.getId());
            profileDTO.setEmail(user.getEmail());
            profileDTO.setFullName(user.getName());
            profileDTO.setPhoneNumber(user.getPhoneNumber());
            profileDTO.setDateOfBirth(user.getBirthDay().getTime());
            long startDate = user.getCreatedDate().getTime();
            profileDTO.setStartDate(startDate);
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


    public ResponseEntity updateProfile(ProfileDTO profileDTO, int id) {
        return null;
    }

    @Override
    public ResponseEntity<User> editProfile(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity requestADayOff(Date fromDate, Date toDate, String reason) {
        // TODO Auto-generated method stub
        return null;
    }

    public int checkPassword(ResetPasswordDTO resetPasswordDTO) {
        if (resetPasswordDTO != null) {
            if (resetPasswordDTO.getNewPassword() != null && resetPasswordDTO.getNewPassword1() != null &&
                    resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getNewPassword1())) {
                if (resetPasswordDTO.getNewPassword().length() >= 8 && resetPasswordDTO.getNewPassword().length() <= 13) {
                    return 1;
                } else {
                    return -2;
                }
            } else
                return 0;
        }
        return -1;
    }

    public ResponseEntity changePassword(ResetPasswordDTO resetPasswordDTO) {
        if (checkPassword(resetPasswordDTO) == 1) {
            String password = md5.convertToMD5(resetPasswordDTO.getPassword());
            User user = userRepository.getUserByEmailAndPassword(resetPasswordDTO.getEmail(), password);

            if (user != null) {
                if (user.getPassword().equals(resetPasswordDTO.getNewPassword())) {
                    return new ResponseEntity("COMPLETE", HttpStatus.OK);
                }

                String newPassword = md5.convertToMD5(resetPasswordDTO.getNewPassword());
                user.setPassword(newPassword);
                userRepository.save(user);
                return new ResponseEntity("COMPLETE", HttpStatus.OK);
            } else {
                return new ResponseEntity("WRONG_EMAIL_OR_PASSWORD", HttpStatus.OK);
            }

        } else if (checkPassword(resetPasswordDTO) == 0) {
            return new ResponseEntity("PASSWORD_INCORRECT", HttpStatus.OK);
        } else if (checkPassword(resetPasswordDTO) == -2) {
            return new ResponseEntity("MAX_SIZE_PASSWORD_13_MIN_SIZE_PASSWORD_8", HttpStatus.OK);
        }
        return new ResponseEntity("RESET_PASSWORD_NOT_EXITS", HttpStatus.OK);
    }

}
