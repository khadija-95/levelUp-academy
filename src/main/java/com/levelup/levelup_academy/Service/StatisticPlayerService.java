package com.levelup.levelup_academy.Service;

import com.levelup.levelup_academy.Api.ApiException;
import com.levelup.levelup_academy.DTO.StatisticPlayerDTO;
import com.levelup.levelup_academy.Model.Player;
import com.levelup.levelup_academy.Model.StatisticChild;
import com.levelup.levelup_academy.Model.StatisticPlayer;
import com.levelup.levelup_academy.Model.Trainer;
import com.levelup.levelup_academy.Repository.PlayerRepository;
import com.levelup.levelup_academy.Repository.StatisticPlayerRepository;
import com.levelup.levelup_academy.Repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticPlayerService {
    private final StatisticPlayerRepository repository;
    private final PlayerRepository playerRepository;
    private final TrainerRepository trainerRepository;

    public StatisticPlayer getMyStatisticsByPlayerId(Integer playerId) {
        StatisticPlayer stat = repository.findByPlayer_Id(playerId);
        if (stat == null) throw new ApiException("Statistic not found for this player");
        return stat;
    }

    public StatisticPlayer getStatisticsByPlayerId(Integer trainerId,Integer playerId) {
        Trainer trainer = trainerRepository.findTrainerById(trainerId);
        if (trainer == null){
            throw new ApiException("Trainer is not found");
        }
        StatisticPlayer stat = repository.findByPlayer_Id(playerId);
        if (stat == null) throw new ApiException("Statistic not found for this player");
        return stat;
    }

    public List<StatisticPlayer> getAllStatisticsByTrainerId(Integer trainerId) {
        return repository.findByPlayer_Trainer_Id(trainerId);
    }


    public void createStatisticPlayer(Integer trainerId ,Integer playerId, StatisticPlayerDTO dto) {
        Trainer trainer = trainerRepository.findTrainerById(trainerId);
        if(trainer == null){
            throw new ApiException("Trainer is not found");
        }
        Player player = playerRepository.findPlayerById(playerId);
        if(player == null){
            throw new ApiException("Player not fount");
        }

        StatisticPlayer stat = new StatisticPlayer(null, dto.getRate(), dto.getWinGame(), dto.getLossGame(),
                dto.getTrophy(), dto.getField(), dto.getDate(), player);

        repository.save(stat);
    }
    public void updateStatistic(Integer trainerId,Integer statId, StatisticPlayerDTO dto) {
        Trainer trainer = trainerRepository.findTrainerById(trainerId);
        if(trainer == null){
            throw new ApiException("Trainer is not found");
        }
        StatisticPlayer stat = repository.findById(statId)
                .orElseThrow(() -> new ApiException("Statistic not found"));

        stat.setRate(dto.getRate());
        stat.setWinGame(dto.getWinGame());
        stat.setLossGame(dto.getLossGame());
        stat.setTrophy(dto.getTrophy());
        stat.setField(dto.getField());
        stat.setDate(dto.getDate());

        repository.save(stat);
    }
    public void deleteStatistic(Integer trainerId,Integer statId) {
        Trainer trainer = trainerRepository.findTrainerById(trainerId);
        if(trainer == null){
            throw new ApiException("Trainer is not found");
        }
        StatisticPlayer stat = repository.findStatisticPlayerById(statId);
        if (stat==null){
            throw new ApiException("Statistic not found");
        }

        repository.delete(stat);
    }
    public StatisticPlayer getPlayerWithTopTrophy() {
        List<StatisticPlayer> all = repository.findAll();

        return all.stream()
                .filter(player -> player.getTrophy() != null)
                .max(Comparator.comparingInt(player -> getTrophyRank(player.getTrophy())))
                .orElseThrow(() -> new ApiException("No player has a trophy"));
    }

    private int getTrophyRank(String trophy) {
        return switch (trophy.toUpperCase()) {
            case "GOLD" -> 3;
            case "SILVER" -> 2;
            case "BRONZE" -> 1;
            default -> 0;
        };
    }

    public List<StatisticPlayer> getTop5PlayersByGame(Integer winGame) {
        List<StatisticPlayer> all = repository.findStatisticPlayerByWinGame(winGame);
        return all.stream()
                .sorted(Comparator.comparing(StatisticPlayer::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

}
