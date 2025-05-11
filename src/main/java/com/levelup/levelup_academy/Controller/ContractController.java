package com.levelup.levelup_academy.Controller;

import com.levelup.levelup_academy.DTO.ContractDTO;
import com.levelup.levelup_academy.Service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/contract")
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;

    @GetMapping("/get")
    public ResponseEntity gatAllContract(){
        return ResponseEntity.status(200).body(contractService.getAllContract());
    }

    @PostMapping("/add/{moderatorId}")
    public ResponseEntity<String> addContract(@RequestBody @Valid ContractDTO contractDTO,@PathVariable Integer moderatorId) {
        contractService.addContract(contractDTO,moderatorId);
        return ResponseEntity.ok("Contract added and email sent successfully.");
    }

}
