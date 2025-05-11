package com.levelup.levelup_academy.Service;

import com.levelup.levelup_academy.Api.ApiException;
import com.levelup.levelup_academy.DTO.EmailRequest;
import com.levelup.levelup_academy.DTO.ParentDTO;
import com.levelup.levelup_academy.Model.Parent;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Repository.AuthRepository;
import com.levelup.levelup_academy.Repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;
    private final AuthRepository authRepository;
    private final EmailNotificationService emailNotificationService;

    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    public void registerParent(ParentDTO parentDTO) {
        parentDTO.setRole("PARENTS");
        User user = new User(null, parentDTO.getUsername(), parentDTO.getPassword(), parentDTO.getEmail(), parentDTO.getFirstName(), parentDTO.getLastName(), parentDTO.getRole(), false, null, null,null,null,null,null,null);

        Parent parent = new Parent(null, user,null, null);
        authRepository.save(user);
        parentRepository.save(parent);

        String subject = "Welcome to LevelUp Academy ";
        String message = "<html><body style='font-family: Arial, sans-serif; color: #fff; line-height: 1.6; background-color: #A53A10; padding: 40px 20px;'>" +
                "<div style='max-width: 600px; margin: auto; background: rgba(255, 255, 255, 0.05); border-radius: 12px; padding: 20px; text-align: center;'>" +
                "<img src='https://i.imgur.com/Q6FtCEu.jpeg' alt='LevelUp Academy Logo' style='width:90px; border-radius: 10px; margin-bottom: 20px;'/>" +
                "<h2 style='color: #fff;'>👨‍👩‍👧 Welcome to <span style='color: #FFD700;'>LevelUp Academy</span>, " + parentDTO.getFirstName() + "!</h2>" +
                "<p style='font-size: 16px;'>We're excited to have you as part of our growing community of supportive parents.</p>" +
                "<p style='font-size: 16px;'> Please don’t forget to <b>register your child</b> so they can begin their learning journey with us.</p>" +
                "<p style='font-size: 16px;'> If you need any help, feel free to contact our support team anytime.</p>" +
                "<p style='font-size: 15px;'>With warm regards,<br/><b>The LevelUp Academy Team</b></p>" +
                "</div>" +
                "</body></html>";

        EmailRequest emailRequest = new EmailRequest(parentDTO.getEmail(),message, subject);
        emailNotificationService.sendEmail(emailRequest);

    }



    public void editParent(Integer parentId, ParentDTO parentDTO) {
        Parent parent = parentRepository.findParentById(parentId);
        if (parent == null) {
            throw new ApiException("Parent not found");
        }
        User user = parent.getUser();
        user.setUsername(parentDTO.getUsername());
        user.setEmail(parentDTO.getEmail());
        user.setFirstName(parentDTO.getFirstName());
        user.setLastName(parentDTO.getLastName());
        user.setPassword(parentDTO.getPassword());

        authRepository.save(user);
        parentRepository.save(parent);
    }

    public void deleteParent(Integer parentId) {
        Parent parent = parentRepository.findParentById(parentId);
        if (parent == null) {
            throw new ApiException("Parent not found");
        }
        User user = parent.getUser();
        parentRepository.delete(parent);
        authRepository.delete(user);
    }
}
