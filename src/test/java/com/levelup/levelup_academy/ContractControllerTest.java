package com.levelup.levelup_academy;

import com.levelup.levelup_academy.Controller.ContractController;
import com.levelup.levelup_academy.DTO.ContractDTO;
import com.levelup.levelup_academy.Model.Contract;
import com.levelup.levelup_academy.Model.Moderator;
import com.levelup.levelup_academy.Model.Pro;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.ContractService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.apache.tomcat.util.net.openssl.OpenSSLStatus.setName;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(ContractController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractService contractService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ContractService contractService() {
            return Mockito.mock(ContractService.class);
        }
    }

    @Test
    public void testGetAllContractsByModeratorId() throws Exception {
        Pro pro = new Pro();
        pro.setId(10);

        Moderator moderator = new Moderator();
        moderator.setId(1);

        Contract mockContract = new Contract();
        mockContract.setId(1);
        mockContract.setTeam("Alpha Team");
        mockContract.setEmail("alpha@team.com");
        mockContract.setCommercialRegister(123456);
        mockContract.setGame("Valorant");
        mockContract.setStartDate(LocalDate.of(2025, 1, 1));
        mockContract.setEndDate(LocalDate.of(2025, 12, 31));
        mockContract.setAmount(2000.0);
        mockContract.setPro(pro);
        mockContract.setModerator(moderator);

        List<Contract> contractList = List.of(mockContract);

        Mockito.when(contractService.getAllContract(1)).thenReturn(contractList);

        mockMvc.perform(get("/api/v1/contract/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].team").value("Alpha Team"))
                .andExpect(jsonPath("$[0].email").value("alpha@team.com"))
                .andExpect(jsonPath("$[0].commercialRegister").value(123456))
                .andExpect(jsonPath("$[0].game").value("Valorant"))
                .andExpect(jsonPath("$[0].amount").value(2000.0));
    }
    @Test
    public void testAddContract() throws Exception {
        // Mock authenticated user (moderator)
        User mockModerator = new User();
        mockModerator.setId(1);  // Set the user ID for the mock moderator

        // Mock the contractService to do nothing (since we're testing the controller)
        Mockito.doNothing().when(contractService).addContract(Mockito.eq(1), Mockito.any(ContractDTO.class));

        // Prepare contract DTO JSON as request body
        String contractJson = """
        {
            "id": 1,
            "team": "Alpha Team",
            "email": "alpha@team.com",
            "commercialRegister": 123456,
            "game": "Valorant",
            "startDate": "2025-01-01",
            "endDate": "2025-12-31",
            "amount": 2000.0,
            "proId": 10
        }
    """;

        // Set the SecurityContext with a mock AuthenticationPrincipal (the moderator)
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(mockModerator, null);
        authenticationToken.setAuthenticated(true);  // Mark the token as authenticated
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Perform the POST request
        mockMvc.perform(post("/api/v1/contract/add/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contractJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Contract added and email sent successfully."));

        // Clear the security context after test (good practice to avoid cross-test pollution)
        SecurityContextHolder.clearContext();
    }



}
