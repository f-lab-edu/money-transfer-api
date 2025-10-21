package com.money.transfer.user.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.money.transfer.common.BaseControllerTest;
import com.money.transfer.user.application.UserService;
import com.money.transfer.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
class UserControllerTest extends BaseControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void join_success() throws Exception {
        // given
        final String requestJson = """
                    {"name": "abcd", "email": "abcd@test.com", "password": "Abcdef1!"}
                """;

        final User mockUser = new User();

        when(userService.join(any())).thenReturn(mockUser);

        // when & then
        mockMvc.perform(post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void join_fail_by_blankEmail() throws Exception {
        // given
        final String requestJson = """
                        {"name": "abcd", "password": "Abcdef1!"}
                """;

        // when & then
        mockMvc.perform(post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email")
                        .value("이메일은 필수입니다."));
    }

    @Test
    void join_fail_by_invalidEmail() throws Exception {
        // given
        final String requestJson = """
                        {"name": "abcd", "email": "email", "password": "Abcdef1!"}
                """;

        // when & then
        mockMvc.perform(post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email")
                        .value("유효한 이메일 형식이어야 합니다."));
    }

    @Test
    void join_fail_by_shortPassword() throws Exception {
        // given
        final String shortPassword = "Abcde1!";

        final String requestJson = String.format("""
                    {"name": "abcd", "email": "abcd@test.com", "password": "%s"}
                """, shortPassword);

        // when & then
        mockMvc.perform(post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password")
                        .value("비밀번호는 8자 이상 21자 이하이어야 합니다."));
    }

    @Test
    void join_fail_by_longPassword() throws Exception {
        // given
        final String longPassword = "Abcdefghijklmnopqrst1!";

        final String requestJson = String.format("""
                    {"name": "abcd", "email": "abcd@test.com", "password": "%s"}
                """, longPassword);

        // when & then
        mockMvc.perform(post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password")
                        .value("비밀번호는 8자 이상 21자 이하이어야 합니다."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password1!😂", "password1!", "PASSWORD1!", "PASSWORD!"})
    void join_fail_by_invalidPassword(final String password) throws Exception {
        // given
        final String requestJson = String.format("""
                    {"name": "abcd", "email": "abcd@test.com", "password": "%s"}
                """, password);

        // when & then
        mockMvc.perform(post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password")
                        .value("비밀번호에 허용되지 않은 문자가 포함되어 있거나, 대문자/소문자/숫자를 포함해야 합니다."));
    }

    @Override
    protected Object getController() {
        return userController;
    }
}