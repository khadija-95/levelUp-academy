package com.levelup.levelup_academy.Service;

import com.levelup.levelup_academy.Api.ApiException;
import com.levelup.levelup_academy.DTO.ParentDTO;
import com.levelup.levelup_academy.Model.Child;
import com.levelup.levelup_academy.Model.Parent;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Repository.AuthRepository;
import com.levelup.levelup_academy.Repository.ChildRepository;
import com.levelup.levelup_academy.Repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;
    private final AuthRepository authRepository;
    private final ChildRepository childRepository;

    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    public void registerParent(ParentDTO parentDTO) {
        parentDTO.setRole("PARENTS");
        User user = new User(null, parentDTO.getUsername(), parentDTO.getPassword(), parentDTO.getEmail(), parentDTO.getFirstName(), parentDTO.getLastName(), parentDTO.getRole(), LocalDate.now(),null,null,null,null,null,null,null,null);

        Parent parent = new Parent(null, user,null, null);
        authRepository.save(user);
        parentRepository.save(parent);

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

    public void addChildToParent( Child child, Integer parentId) {
        Parent parent = parentRepository.findParentById(parentId);
        if(parent == null) throw new ApiException("Parent not found");

        child.setParent(parent);
        childRepository.save(child);
    }

    public void updateChild( Child child, Integer parentId, Integer childId) {
        Parent parent = parentRepository.findParentById(parentId);
        if(parent == null) throw new ApiException("Parent not found");

        Child ch = childRepository.findChildById(childId);
        if(child == null) throw new ApiException("Child not found");

        if (!child.getParent().getId().equals(parentId)) {
            throw new ApiException("Child does not belong to the parent");
        }
        ch.setFirstName(child.getFirstName());
        ch.setLastName(child.getLastName());
        ch.setAge(child.getAge());

        childRepository.save(ch);
    }

    public void deleteChild(Integer parentId, Integer childId) {
        Parent parent = parentRepository.findParentById(parentId);
        if (parent == null) throw new ApiException("Parent not found");


        Child child = childRepository.findChildById(childId);
        if (child == null) throw new ApiException("Child not found");

        if (!child.getParent().getId().equals(parentId)) {
            throw new ApiException("Child does not belong to the parent");
        }

        childRepository.delete(child);
    }
}
