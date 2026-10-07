package com.drogueria.bellavista.infrastructure.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    private final AtomicLong now = new AtomicLong(1_000_000L);
    private FilterChain chain;
    private RateLimitFilter filter;

    @BeforeEach
    void setUp() {
        chain = mock(FilterChain.class);
        filter = new RateLimitFilter(Map.of(
                "/auth/login", new RateLimitFilter.Rule(3, 60_000L),
                "/auth/forgot-password", new RateLimitFilter.Rule(1, 900_000L)), now::get);
    }

    private MockHttpServletResponse call(String method, String path, String ip) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, "/api" + path);
        request.setServletPath(path);
        request.setRemoteAddr(ip);
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, chain);
        return response;
    }

    @Test
    @DisplayName("permite hasta el máximo y bloquea el siguiente con 429 + Retry-After")
    void shouldBlockAfterLimit() throws Exception {
        for (int i = 0; i < 3; i++) {
            assertEquals(200, call("POST", "/auth/login", "1.1.1.1").getStatus());
        }

        MockHttpServletResponse blocked = call("POST", "/auth/login", "1.1.1.1");

        assertEquals(429, blocked.getStatus());
        assertEquals("60", blocked.getHeader("Retry-After"));
        assertTrue(blocked.getContentAsString().contains("Demasiados intentos"));
        verify(chain, times(3)).doFilter(any(), any());
    }

    @Test
    @DisplayName("el límite es por IP")
    void shouldLimitPerIp() throws Exception {
        for (int i = 0; i < 3; i++) {
            call("POST", "/auth/login", "1.1.1.1");
        }

        assertEquals(429, call("POST", "/auth/login", "1.1.1.1").getStatus());
        assertEquals(200, call("POST", "/auth/login", "2.2.2.2").getStatus());
    }

    @Test
    @DisplayName("el límite es por ruta")
    void shouldLimitPerPath() throws Exception {
        assertEquals(200, call("POST", "/auth/forgot-password", "1.1.1.1").getStatus());
        assertEquals(429, call("POST", "/auth/forgot-password", "1.1.1.1").getStatus());
        assertEquals(200, call("POST", "/auth/login", "1.1.1.1").getStatus());
    }

    @Test
    @DisplayName("la ventana se libera con el tiempo")
    void shouldAllowAgainAfterWindow() throws Exception {
        for (int i = 0; i < 3; i++) {
            call("POST", "/auth/login", "1.1.1.1");
        }
        assertEquals(429, call("POST", "/auth/login", "1.1.1.1").getStatus());

        now.addAndGet(60_000L);

        assertEquals(200, call("POST", "/auth/login", "1.1.1.1").getStatus());
    }

    @Test
    @DisplayName("Retry-After refleja el tiempo restante de la ventana")
    void shouldReportRemainingTime() throws Exception {
        call("POST", "/auth/forgot-password", "1.1.1.1");
        now.addAndGet(300_000L);

        MockHttpServletResponse blocked = call("POST", "/auth/forgot-password", "1.1.1.1");

        assertEquals(429, blocked.getStatus());
        assertEquals("600", blocked.getHeader("Retry-After"));
    }

    @Test
    @DisplayName("ignora métodos distintos de POST y rutas sin regla")
    void shouldIgnoreOtherRequests() throws Exception {
        for (int i = 0; i < 10; i++) {
            assertEquals(200, call("GET", "/auth/login", "1.1.1.1").getStatus());
            assertEquals(200, call("POST", "/products", "1.1.1.1").getStatus());
        }
        verify(chain, times(20)).doFilter(any(), any());
    }
}
