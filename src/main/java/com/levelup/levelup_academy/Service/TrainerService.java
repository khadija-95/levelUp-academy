package com.levelup.levelup_academy.Service;

import com.levelup.levelup_academy.Api.ApiException;
import com.levelup.levelup_academy.DTO.StatisticChildDTO;
import com.levelup.levelup_academy.DTO.StatisticPlayerDTO;
import com.levelup.levelup_academy.DTO.StatisticProDTO;
import com.levelup.levelup_academy.DTO.TrainerDTO;
import com.levelup.levelup_academy.Model.*;
import com.levelup.levelup_academy.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final AuthRepository authRepository;
    private final StatisticChildService statisticChildService;
    private final StatisticPlayerService statisticPlayerService;
    private final StatisticProService statisticProService;
    private final ChildRepository childRepository;
    private final PlayerRepository playerRepository;
    private final ProRepository proRepository;

    //GET
    public List<Trainer> getAllTrainers(){
        return trainerRepository.findAll();
    }
    //Register Trainer
    public void registerTrainer(TrainerDTO trainerDTO, MultipartFile file){
        trainerDTO.setRole("TRAINER");
        String filePath = null;
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get("uploads/cvs/" + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                filePath = path.toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save CV file.");
            }
        }
        User user = new User(null,trainerDTO.getUsername(),trainerDTO.getPassword(),trainerDTO.getEmail(),trainerDTO.getFirstName(),trainerDTO.getLastName(),trainerDTO.getRole(),false,null,null,null,null,null,null,null);
        Trainer trainer = new Trainer(null,filePath,trainerDTO.getIsAvailable(),user,null,null);
        authRepository.save(user);
        trainerRepository.save(trainer);
    }

    //download
    public byte[] downloadTrainerCv(Integer trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        String filePath = trainer.getCvPath();
        if (filePath == null || filePath.isEmpty()) {
            throw new RuntimeException("CV not uploaded for this trainer.");
        }

        try {
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            if (!Files.exists(path) || !Files.isReadable(path)) {
                throw new RuntimeException("CV file not found or not readable.");
            }

            return Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read CV file", e);
        }
    }
    public void updateTrainer(Integer id, TrainerDTO trainerDTO){
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        User user = trainer.getUser();
        user.setUsername(trainerDTO.getUsername());
        user.setPassword(trainerDTO.getPassword());
        user.setEmail(trainerDTO.getEmail());
        user.setFirstName(trainerDTO.getFirstName());
        user.setLastName(trainerDTO.getLastName());

        trainer.setIsAvailable(trainerDTO.getIsAvailable());

        authRepository.save(user);
        trainerRepository.save(trainer);
    }
    public void deleteTrainer(Integer id){
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        authRepository.delete(trainer.getUser());
        trainerRepository.delete(trainer);
    }

    //for child
    public void addStatisticToChild(Integer trainerId, Integer childId, StatisticChildDTO statisticChildDTO){
        Trainer trainer = trainerRepository.findTrainerById(trainerId);
        if(trainer == null){
            throw new ApiException("Trainer is not found");
        }
        Child child = childRepository.findChildById(childId);
        if(child == null){
            throw new ApiException("Child not found");
        }

        statisticChildService.createStatistic(childId,statisticChildDTO);
    }

    //for player
    public void addStatisticToPlayer(Integer trainerId, Integer playerId, StatisticPlayerDTO statisticPlayerDTO){
        Trainer trainer = trainerRepository.findTrainerById(trainerId);
        if(trainer == null){
            throw new ApiException("Trainer not found");
        }
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null){
            throw new ApiException("Player not found");
        }
        statisticPlayerService.createStatistic(playerId,statisticPlayerDTO);

    }

    public void addStatisticToPro(Integer trainerId, Integer proId, StatisticProDTO statisticProDTO){
        Trainer trainer = trainerRepository.findTrainerById(trainerId);
        if(trainer == null){
            throw new ApiException("Trainer not found");
        }
        Pro pro = proRepository.findProById(proId);
        if(pro == null){
            throw new ApiException("Pro not found");
        }
        statisticProService.createStatistic(proId,statisticProDTO);
    }


}
